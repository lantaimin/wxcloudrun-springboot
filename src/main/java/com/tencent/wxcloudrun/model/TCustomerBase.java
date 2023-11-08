package com.tencent.wxcloudrun.model;

import lombok.Data;

@Data
public class TCustomerBase {
    /**
    * 客户编号
    */
    private String customerId;

    private String customerName;

    /**
    * 1-正常，9-注销
    */
    private String status;

    private Long createTime;

    private String createUser;

    private Long updateTime;

    private String remark;
}