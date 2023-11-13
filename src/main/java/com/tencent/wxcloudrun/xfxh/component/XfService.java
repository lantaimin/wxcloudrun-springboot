package com.tencent.wxcloudrun.xfxh.component;

import cn.hutool.core.util.StrUtil;
import com.tencent.wxcloudrun.xfxh.config.XfXhConfig;
import com.tencent.wxcloudrun.xfxh.dto.InteractMsg;
import com.tencent.wxcloudrun.xfxh.dto.MsgDTO;
import com.tencent.wxcloudrun.xfxh.listener.XfXhWebSocketListener;
import okhttp3.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

/**
 * @author: lantaimin
 * @version: 1.0
 * @date: 2023-11-13 17:36:24
 **/
@Component
public class XfService {
    private static Logger LOG = LoggerFactory.getLogger(XfService.class);
    @Resource
    private XfXhConfig xfXhConfig;

    @Resource
    private XfXhStreamClient xfXhStreamClient;

    @Resource
    private MemoryUserRecordSpace memoryUserRecordSpace;

    // 使用 id 作为唯一用户的标识（区分不同用户）
    public String question(String id, String question) throws InterruptedException {
        LOG.info("讯飞大模型question方法请求参数：id={},question={}", id, question);
        if (StrUtil.isBlank(question)) {
            return "无效问题，请重新输入";
        }

        // 尝试锁定用户
        if (!memoryUserRecordSpace.tryLock(id)) {
            LOG.info("id={},尝试锁定用户失败", id);
            return "正在处理上次问题，请稍后再试~";
        }

        // 获取连接令牌
        if (!xfXhStreamClient.operateToken(XfXhStreamClient.GET_TOKEN_STATUS)) {
            // 释放锁
            memoryUserRecordSpace.unLock(id);
            LOG.info("id={},获取连接令牌失败", id);
            return "忙不过来啦，请稍后再试~";
        }

        MsgDTO msgDTO = MsgDTO.createUserMsg(question);
        XfXhWebSocketListener listener = new XfXhWebSocketListener();
        // 组装上下文内容发送
        List<MsgDTO> msgList = memoryUserRecordSpace.getAllInteractMsg(id);

        msgList.add(msgDTO);
        WebSocket webSocket = xfXhStreamClient.sendMsg(UUID.randomUUID().toString().substring(0, 10), msgList, listener);
        if (webSocket == null) {
            // 归还令牌
            xfXhStreamClient.operateToken(XfXhStreamClient.BACK_TOKEN_STATUS);
            // 释放锁
            memoryUserRecordSpace.unLock(id);
            return "系统内部错误，请联系我大哥兰傲天~";
        }
        try {
            int count = 0;
            // 为了避免死循环，设置循环次数来定义超时时长
            int maxCount = xfXhConfig.getMaxResponseTime() * 5;
            while (count <= maxCount) {
                Thread.sleep(200);
                if (listener.isWsCloseFlag()) {
                    break;
                }
                count++;
            }
            if (count > maxCount) {
                return "大模型响应超时，请联系我大哥兰傲天";
            }
            // 将记录添加到 memoryUserRecordSpace
            String answer = listener.getAnswer().toString();
            memoryUserRecordSpace.storeInteractMsg(id, new InteractMsg(MsgDTO.createUserMsg(question), MsgDTO.createAssistantMsg(answer)));
            LOG.info("讯飞大模型question方法返回参数：id={},answer={}", id, answer);
            return answer;
        } finally {
            // 关闭连接
            webSocket.close(1000, "");
            // 释放锁
            memoryUserRecordSpace.unLock(id);
            // 归还令牌
            xfXhStreamClient.operateToken(XfXhStreamClient.BACK_TOKEN_STATUS);
        }
    }
}
