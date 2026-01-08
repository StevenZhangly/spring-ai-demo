package org.example.ai.dto;

import lombok.Data;

import java.util.List;

/**
 * @author 01423171
 * @ClassName CodeIssueDTO
 * @description: TODO
 * @datetime 2025/3/25 14:02
 * @version: 1.0
 */
@Data
public class CodeIssueDTO {

    /**
     * 数据来源（SONARQUBE、DEEPSEEK）
     */
    private String source;

    /**
     * 类型（BUG/VULNERABILITY/CODE_SMELL/ANTI_PATTERN）
     */
    private String type;

    /**
     * 行号
     */
    private Integer line;

    /**
     * 问题描述
     */
    private String description;

    /**
     * 严重程度（BLOCKER/CRITICAL/MAJOR/MINOR/INFO）
     */
    private String severity;

    /**
     * 解决方案建议
     */
    private List<String> suggestions;

    /**
     * 具体代码
     */
    private String code;
}
