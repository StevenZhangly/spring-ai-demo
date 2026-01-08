package org.example.ai.config;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 01423171
 * @ClassName SpringAiConfig
 * @description: TODO
 * @datetime 2025/3/20 15:33
 * @version: 1.0
 */
@Configuration
public class SpringAiConfig {

    private static final String DEFAULT_PROMPT = "你是一个博学的智能聊天助手，请根据用户提问回答！";

    String CODE_REVIEW_PROMPT = """
            你是一个专业的代码审查助手，从代码缺陷、代码缺陷、性能问题等几个方面对代码进行扫描，并以严格的JSON格式返回代码审查结果，json结构示例：
              {
                "issues": [{
                  "type": "SECURITY|PERFORMANCE|CODE_SMELL",
                  "severity": "BLOCKER|CRITICAL|MAJOR|MINOR|INFO",
                  "line": 15,
                  "description": "未处理的空指针异常",
                  "suggestion": "添加空值检查逻辑"
                }]
              }，
              需要审查的代码：
            """;

    @Bean
    public ChatClient buildChatClient(ChatClient.Builder chatClientBuilder){
        return chatClientBuilder
                .defaultSystem(DEFAULT_PROMPT)
                // 实现 Chat Memory 的 Advisor
                // 在使用 Chat Memory 时，需要指定对话 ID，以便 Spring AI 处理上下文。
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(new InMemoryChatMemory())
                )
                // 实现 Logger 的 Advisor
                .defaultAdvisors(
                        new SimpleLoggerAdvisor()
                )
                // 设置 ChatClient 中 ChatModel 的 Options 参数
                .defaultOptions(
                        DashScopeChatOptions.builder()
                                .withTopP(0.7)
                                .build()
                )
                .build();
    }
}
