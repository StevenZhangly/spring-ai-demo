package org.example.ai.service;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;
import org.example.ai.dto.CodeIssueDTO;
import org.example.ai.dto.CodeReviewReq;
import org.example.ai.factory.ReportEngineFactory;
import org.example.ai.handler.AiScannerHandler;
import org.example.ai.handler.CodePreprocessor;
import org.example.ai.handler.SonarScannerHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author 01423171
 * @ClassName CodeScannerService
 * @description: TODO
 * @datetime 2025/3/25 15:20
 * @version: 1.0
 */
@Service
public class CodeScannerService {

    @Autowired
    private SonarScannerHandler sonarScannerHandler;

    @Autowired
    private AiScannerHandler aiScannerHandler;

    public void runSonarScan(String projectPath){
        try {
            sonarScannerHandler.runScan(projectPath);
        } catch (Exception e) {
            throw new RuntimeException("执行sonar扫描异常", e);
        }
    }

    public List<CodeIssueDTO> queryScanResult(CodeReviewReq req) {
        String sonarScanResult = null;
        String aiScanResult = null;
        Map<String, Integer> anchorMapping = null;
        if(CodeReviewReq.SCAN_TYPE_SONAR.equals(req.getScanType())){
            sonarScanResult = sonarScannerHandler.getSonarIssues(req.getProjectKey(), req.getComponentKey());
        }
        if(CodeReviewReq.SCAN_TYPE_AI.equals(req.getScanType())){
            CodePreprocessor.ProcessResult processResult = codePreprocessor(req.getCodePath());
            anchorMapping = processResult.anchorMapping();
            aiScanResult = aiScannerHandler.runScan(JSON.toJSONString(processResult.processedCode()));
        }
        if(CodeReviewReq.SCAN_TYPE_SONAR_AI.equals(req.getScanType())){
            sonarScanResult = sonarScannerHandler.getSonarIssues(req.getProjectKey(), req.getComponentKey());
            CodePreprocessor.ProcessResult processResult = codePreprocessor(req.getCodePath());
            anchorMapping = processResult.anchorMapping();
            aiScanResult = aiScannerHandler.runScan(JSON.toJSONString(processResult.processedCode()));
        }
        // 合并结果
        return ReportEngineFactory.mergeResults(sonarScanResult, aiScanResult, anchorMapping);
    }

    public CodePreprocessor.ProcessResult codePreprocessor(String codeFilePath){
        try {
            return new CodePreprocessor().process(new File(codeFilePath));
        } catch (IOException e) {
            throw new RuntimeException("文件预处理异常", e);
        }
    }
}
