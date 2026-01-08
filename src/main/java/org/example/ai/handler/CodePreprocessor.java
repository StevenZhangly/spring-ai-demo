package org.example.ai.handler;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author 01423171
 * @ClassName CodePreprocessor
 * @description: TODO
 * @datetime 2025/3/26 17:17
 * @version: 1.0
 */
public class CodePreprocessor {

    private static final String ANCHOR_PREFIX = "/*ANC:%s*/";

    private static final Pattern LINE_PATTERN = Pattern.compile("^\\s*//.*|^\\s*$");

    public ProcessResult process(File sourceFile) throws IOException {
        List<String> originalLines = Files.readAllLines(sourceFile.toPath());
        Map<String, Integer> anchorMap = new LinkedHashMap<>();
        List<String> processedLines = new ArrayList<>();

        int effectiveLine = 1;
        for (int i = 0; i < originalLines.size(); i++) {
            String line = originalLines.get(i);

            // 跳过空行和注释
            if (LINE_PATTERN.matcher(line).matches()) {
                effectiveLine++;
                processedLines.add(line);
                continue;
            }

            // 生成锚点
            String hash = generateAnchor(line, i+1);
            String anchoredLine = String.format(ANCHOR_PREFIX, hash) + line;
            processedLines.add(anchoredLine);

            anchorMap.put(hash, effectiveLine);
            effectiveLine++;
        }

        return new ProcessResult(processedLines, anchorMap);
    }

    private String generateAnchor(String content, int lineNumber) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(
                    (lineNumber + ":" + content).getBytes(StandardCharsets.UTF_8)
            );
            return HexFormat.of().formatHex(hashBytes).substring(0, 6);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Anchor generation failed", e);
        }
    }

    public record ProcessResult(
            List<String> processedCode,
            Map<String, Integer> anchorMapping
    ) {}
}
