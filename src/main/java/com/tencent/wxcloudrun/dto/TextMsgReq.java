package com.tencent.wxcloudrun.dto;

import lombok.Data;

/**
 * @author: lantaimin
 * @version: 1.0
 * @date: 2023-11-08 17:06:49
 **/
@Data
public class TextMsgReq extends BaseWxMsgReq{
    private String Content;
}
