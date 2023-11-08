package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.service.WxMessageLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 微信云托管消息
 * @author: lantaimin
 * @version: 1.0
 * @date: 2023-11-08 14:55:26
 **/
@RestController
public class WxMsgController {
    private static Logger LOG = LoggerFactory.getLogger(WxMsgController.class);

    @Autowired
    private WxMessageLogService wxMessageLogService;

    @PostMapping(value = "/wx/message")
    ApiResponse queryCustomerInfo(@RequestParam String reqData) {
        LOG.info("接收到公众号的推送消息：{}", reqData);
        wxMessageLogService.saveMessageLog(reqData, "success");
        return ApiResponse.ok("success");
    }
}
