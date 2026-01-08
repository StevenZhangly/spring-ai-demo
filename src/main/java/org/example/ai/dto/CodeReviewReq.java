package org.example.ai.dto;

import lombok.Data;

/**
 * @author 01423171
 * @ClassName CodeReviewReq
 * @description: TODO
 * @datetime 2025/3/25 15:17
 * @version: 1.0
 */
@Data
public class CodeReviewReq {

    public static final String SCAN_TYPE_SONAR = "1";

    public static final String SCAN_TYPE_AI = "2";

    public static final String SCAN_TYPE_SONAR_AI = "3";

    /**
     * 代码路径
     */
    private String codePath;

    /**
     * 工程key
     */
    private String projectKey;

    /**
     * 扫描类型(1-sonarqube,2-ai,3-sonarqube+ai)
     */
    private String scanType;
    /**
     * 组件key
     */
    private String componentKey;
}
