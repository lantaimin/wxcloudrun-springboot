package com.tencent.wxcloudrun.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.tencent.wxcloudrun.dao.TWxMessageLogMapper;
import com.tencent.wxcloudrun.dto.BaseWxMsgReq;
import com.tencent.wxcloudrun.dto.ReplyTextResp;
import com.tencent.wxcloudrun.dto.TextMsgReq;
import com.tencent.wxcloudrun.enums.WxMsgType;
import com.tencent.wxcloudrun.model.TWxMessageLog;
import com.tencent.wxcloudrun.xfxh.component.XfService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author: lantaimin
 * @version: 1.0
 * @date: 2023-11-08 14:39:16
 **/
@Service
public class WxMessageService {

    private static Logger LOG = LoggerFactory.getLogger(WxMessageService.class);

    @Autowired
    private TWxMessageLogMapper wxMessageLogMapper;

    @Autowired
    private XfService xfService;


    public String receiveMsg(String reqData) {
        LOG.info("接收到公众号的推送消息：{}", reqData);
        JSONObject reqJsonObj = JSONUtil.parseObj(reqData);
        if (BaseWxMsgReq.CHECK_CONTAINER_PATH.equals(reqJsonObj.getStr("action"))) {
            return "success";
        }
        int logId = this.saveMessageLog(reqData, null);
        String msgType = reqJsonObj.getStr("MsgType");
        if(WxMsgType.TYPE_TEXT.getType().equals(msgType)){
            TextMsgReq textMsgReq = JSONUtil.toBean(reqData, TextMsgReq.class);
            ReplyTextResp textResp = new ReplyTextResp();
            textResp.setToUserName(textMsgReq.getFromUserName());
            textResp.setFromUserName(textMsgReq.getToUserName());
            textResp.setCreateTime(System.currentTimeMillis()/1000);
            textResp.setMsgType(WxMsgType.TYPE_TEXT.getType());

            String answer = null;
            try {
                answer = xfService.question(textMsgReq.getFromUserName(), textMsgReq.getContent());
            } catch (InterruptedException e) {
                LOG.error("content={},调用讯飞大模型异常：{}", textMsgReq.getContent(), e);
            }
            if (StringUtils.isBlank(answer)) {
                answer = "哦豁~系统异常啦，快去找我大哥兰傲天修一下bug叭~";
            }
            textResp.setContent("周霸天：" + answer);
            this.updateRespData(logId, JSONUtil.toJsonStr(textResp));
            return JSONUtil.toJsonStr(textResp);
        }
        return "success";
    }

    public int saveMessageLog(String reqData, String respData) {
        TWxMessageLog wxMessageLog = new TWxMessageLog();
        wxMessageLog.setCreateTime(System.currentTimeMillis());
        wxMessageLog.setReqData(reqData);
        wxMessageLog.setRespData(respData);
        wxMessageLogMapper.insertSelective(wxMessageLog);
        return wxMessageLog.getId();
    }

    public void updateRespData(int id, String respData) {
        TWxMessageLog wxMessageLog = new TWxMessageLog();
        wxMessageLog.setId(id);
        wxMessageLog.setRespData(respData);
        wxMessageLog.setUpdateTime(System.currentTimeMillis());
        wxMessageLogMapper.updateByPrimaryKeySelective(wxMessageLog);
    }

    //https://developers.weixin.qq.com/miniprogram/dev/wxcloudrun/src/development/weixin/callback.html

}
