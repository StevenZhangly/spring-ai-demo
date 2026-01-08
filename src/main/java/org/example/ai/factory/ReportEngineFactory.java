package org.example.ai.factory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.example.ai.dto.CodeIssueDTO;
import org.example.ai.util.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 01423171
 * @ClassName ReportEngine
 * @description: TODO
 * @datetime 2025/3/25 14:34
 * @version: 1.0
 */
@Component
@Slf4j
public class ReportEngineFactory {

    private static final Pattern ANCHOR_PATTERN = Pattern.compile("/\\*ANC:([a-f0-9]{6})\\*/");

    public static List<CodeIssueDTO> mergeResults(String sonarData, String aiData, Map<String, Integer> anchorMapping) {
        log.info("sonarScanData:{}", sonarData);
        log.info("aiScanData:{}", aiData);
        List<CodeIssueDTO> issues = new ArrayList<>();
        // 使用复合键分组：行号+类型+描述哈希（避免不同来源描述差异导致过度合并）
        Map<String, CodeIssueDTO> issueMap = new ConcurrentHashMap<>();
        // 解析SonarQube结果
        if(StringUtils.isNotBlank(sonarData)){
            JSONArray sonarIssues = JSONObject.parseObject(sonarData).getJSONArray("issues");
            if (sonarIssues != null) {
                for (int i = 0; i < sonarIssues.size(); i++) {
                    JSONObject item = sonarIssues.getJSONObject(i);
                    String type = item.getString("type");
                    int line = item.getIntValue("line");
                    String desc = item.getString("message");
                    String severity = item.getString("severity");
                    CodeIssueDTO issue;
                    String compositeKey = type + "|" + line;
                    if(issueMap.containsKey(compositeKey)){
                        issue = issueMap.get(compositeKey);
                        issue.setSource("SONARQUBE");
                        issue.setDescription((StringUtils.isNotBlank(issue.getDescription()) ? issue.getDescription() + "，" : "") + desc);
                    } else {
                        issue = new CodeIssueDTO();
                        issue.setSource("SONARQUBE");
                        issue.setType(type);
                        issue.setLine(line);
                        issue.setDescription(desc);
                        issue.setSeverity(severity);
                        issueMap.put(compositeKey, issue);
                    }
                    issueMap.put(compositeKey, issue);
                }
            }
        }
        if(StringUtils.isNotBlank(aiData)){
            // 解析AI结果
            JSONArray aiIssues = JSONObject.parseObject(aiData).getJSONArray("issues");
            if (aiIssues != null) {
                for (int i = 0; i < aiIssues.size(); i++) {
                    JSONObject item = aiIssues.getJSONObject(i);
                    String type = item.getString("type");
                    int line = item.getIntValue("line");
                    String desc = item.getString("description");
                    String suggestion = item.getString("suggestion");
                    String severity = item.getString("severity");
                    String hashCode = item.getString("hashCode");
                    String originCode = item.getString("code");
                    CodeIssueDTO issue;
                    // 提取锚点
                    Matcher m = ANCHOR_PATTERN.matcher(hashCode);
                    while (m.find()) {
                        String anchor = m.group(1);
                        if (anchorMapping.containsKey(anchor)) {
                            line = anchorMapping.get(anchor);
                        }
                    }
                    String compositeKey = type + "|" + line;
                    if(issueMap.containsKey(compositeKey)){
                        issue = issueMap.get(compositeKey);
                        issue.setSource("AI");
                        issue.setDescription((StringUtils.isNotBlank(issue.getDescription()) ? issue.getDescription() + "，" : "") + desc);
                        issue.setSuggestions(Collections.singletonList(suggestion));
                    } else {
                        issue = new CodeIssueDTO();
                        issue.setSource("AI");
                        issue.setSuggestions(Collections.singletonList(suggestion));
                        issue.setType(type);
                        issue.setLine(line);
                        issue.setDescription(desc);
                        issue.setSeverity(severity);
                        issue.setCode(originCode);
                        issueMap.put(compositeKey, issue);
                    }
                }
            }
        }
        return new ArrayList<>(issueMap.values());
    }
}
