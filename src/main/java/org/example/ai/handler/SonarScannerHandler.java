package org.example.ai.handler;

import cn.hutool.core.codec.Base64;
import lombok.extern.slf4j.Slf4j;
import org.example.ai.util.HttpUtils;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author 01423171
 * @ClassName SonarScannerHandler
 * @description: TODO
 * @datetime 2025/3/25 15:24
 * @version: 1.0
 */
@Component
@Slf4j
public class SonarScannerHandler {

    /**
     * 执行SonarQube扫描
     * @param projectPath
     * @throws IOException
     * @throws InterruptedException
     */
    public void runScan(String projectPath) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder("D:\\java\\maven\\apache-maven-3.6.3\\bin\\mvn.cmd", "clean", "verify", "-Dmaven.test.skip=true", "sonar:sonar");
        pb.directory(new File(projectPath));
        pb.redirectErrorStream(true);
        Process process = pb.start();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("[MVN-SONAR] " + line);
            }
        }
        process.waitFor();
    }

    /**
     * 使用SonarQube Web API获取结果
     * @param projectKey
     * @return
     */
    public String getSonarIssues(String projectKey, String componentKey) {
        log.info("查看sonar扫描结果, 入参：{}，{}", projectKey, componentKey);
        String url = "http://10.202.218.2:8999/api/issues/search";
        Map<String, String> headers = new LinkedHashMap<>();
        headers.put("Authorization", "Basic " + new String(Base64.encode("admin:admin@123@".getBytes(StandardCharsets.UTF_8))));
        Map<String, String> params = new LinkedHashMap<>();
        params.put("projects", projectKey);
        if(componentKey != null){
            params.put("componentKeys", componentKey);
        }
        params.put("ps", "100");
        params.put("types", "CODE_SMELL,BUG,VULNERABILITY");
        return HttpUtils.sendGetWithHeader(url, headers, params);
    }

    public static void main(String[] args) {
        try {
            //new SonarScannerService().runScan("C://Users//01423171//idea//work//gis-ass-oms-uimp//uimp-manager//uimp-prod-task");

            String result = new SonarScannerHandler().getSonarIssues("com.sf.gis:gis-ass-oms-mrs", "com.sf.gis:gis-ass-oms-mrs:mrs/mrs-service/src/main/java/com/sf/gis/mrs/service/impl/ShareTaskBaseServiceImpl.java");
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
