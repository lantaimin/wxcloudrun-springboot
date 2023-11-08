package com.tencent.wxcloudrun.model;

import lombok.Data;

@Data
public class TWxMessageLog {
    /**
    * 注解
    */
    private Integer id;

    /**
    * 请求信息
    */
    private String reqData;

    /**
    * 响应信息
    */
    private String respData;

    private Long createTime;

    private Long updateTime;

    private String remark;
}