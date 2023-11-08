package com.tencent.wxcloudrun.dto;

import lombok.Data;

/**
 * 基础消息
 *
 * @author: lantaimin
 * @version: 1.0
 * @date: 2023-11-08 17:02:52
 **/
@Data
public class BaseWxMsgReq {
    public static final String CHECK_CONTAINER_PATH = "CheckContainerPath";

    private String action;
    private String ToUserName;
    private String FromUserName;
    private String CreateTime;
    private String MsgType;
    private String MsgId;
}
