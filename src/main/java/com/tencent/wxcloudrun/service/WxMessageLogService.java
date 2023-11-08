package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.dao.TWxMessageLogMapper;
import com.tencent.wxcloudrun.model.TWxMessageLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: lantaimin
 * @version: 1.0
 * @date: 2023-11-08 14:39:16
 **/
@Service
public class WxMessageLogService {
    @Autowired
    private TWxMessageLogMapper wxMessageLogMapper;

    public void saveMessageLog(String reqData, String respData) {
        TWxMessageLog wxMessageLog = new TWxMessageLog();
        wxMessageLog.setCreateTime(System.currentTimeMillis());
        wxMessageLog.setReqData(reqData);
        wxMessageLog.setRespData(respData);
        wxMessageLogMapper.insertSelective(wxMessageLog);
    }
}
