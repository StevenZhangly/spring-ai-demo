package org.example.ai.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.example.ai.dto.CodeIssueDTO;
import org.example.ai.dto.CodeReviewReq;
import org.example.ai.service.CodeScannerService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * @author 01423171
 * @ClassName ChatAiController
 * @description: TODO
 * @datetime 2025/3/20 15:24
 * @version: 1.0
 */
@RestController
@RequestMapping("/ai")
public class ChatAiController {

    @Autowired
    private ChatClient chatClient;

    @Autowired
    private CodeScannerService codeScannerService;

    @GetMapping("/stream/chat")
    public Flux<String> streamChat(@RequestParam(value = "query", defaultValue = "你好，很高兴认识你，能简单介绍一下自己吗？")String query, HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        return chatClient.prompt(query).stream().content();
    }

    @PostMapping("/sonar/scan")
    public void runSonarScan(@RequestParam("projectPath") String projectPath){
        codeScannerService.runSonarScan(projectPath);
    }

    @PostMapping("/code/review")
    public List<CodeIssueDTO> codeReview(@RequestBody CodeReviewReq req){
        return codeScannerService.queryScanResult(req);
    }
}
