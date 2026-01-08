package org.example.ai.handler;

import com.alibaba.cloud.ai.dashscope.api.DashScopeResponseFormat;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 01423171
 * @ClassName AiScannerHandler
 * @description: TODO
 * @datetime 2025/3/25 15:26
 * @version: 1.0
 */
@Component
@Slf4j
public class AiScannerHandler {

    @Autowired
    private ChatClient chatClient;

    String CODE_REVIEW_PROMPT = """
            你是一个专业的代码审查助手，从代码缺陷、数据安全、性能问题等几个方面对代码进行扫描，最后生成严格符合JSON语法规范的结果，json结构示例：
              {
                "issues": [{
                  "type": "SECURITY|PERFORMANCE|CODE_SMELL",
                  "severity": "BLOCKER|CRITICAL|MAJOR|MINOR|INFO",
                  "line": "代码所在行数",
                  "hashCode": "/*ANC:d3b7a2*/",
                  "code": "该行代码",
                  "description": "未处理的空指针异常",
                  "suggestion": "添加空值检查逻辑"
                }]
              }
            """;

    public String runScan(String code){
        log.info("执行AI代码扫描");
        return chatClient.prompt(CODE_REVIEW_PROMPT)
                .user("请对这段代码进行审查：" + code)
                .options(DashScopeChatOptions.builder()
                        .withResponseFormat(DashScopeResponseFormat.builder().type(DashScopeResponseFormat.Type.JSON_OBJECT).build())
                        .build())
                .call()
                .content();
    }
}
