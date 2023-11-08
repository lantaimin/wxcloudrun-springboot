package com.tencent.wxcloudrun.dto;

import lombok.Data;

/**
 * @author: lantaimin
 * @version: 1.0
 * @date: 2023-11-08 16:35:49
 **/
@Data
public class ReplyTextResp {
    //{
    //  "ToUserName": "用户OPENID",
    //  "FromUserName": "公众号/小程序原始ID",
    //  "CreateTime": "发送时间", // 整型，例如：1648014186
    //  "MsgType": "text",
    //  "Content": "文本消息"
    //}

    /**
     * 用户OPENID
     */
    private String ToUserName;

    /**
     * 公众号/小程序原始ID
     */
    private String FromUserName;

    /**
     * 发送时间
     */
    private Long CreateTime;

    /**
     * 消息类型
     */
    private String MsgType;

    /**
     * 消息内容
     */
    private String Content;
}
