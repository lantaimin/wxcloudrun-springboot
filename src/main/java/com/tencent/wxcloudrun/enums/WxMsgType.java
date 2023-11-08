package com.tencent.wxcloudrun.enums;

public enum WxMsgType {
    /** 消息类型 **/
    TYPE_TEXT("text","文本消息"),
    TYPE_IMAGE("image","图片消息"),
    TYPE_VOICE("voice","语音消息"),
    TYPE_VIDEO("video","视频消息"),
    TYPE_MUSIC("music","音乐消息"),
    TYPE_NEWS("news","图文消息"),
    ;
    private String type;
    private String desc;

    WxMsgType(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public String getType() {
        return type;
    }


    public String getDesc() {
        return desc;
    }
}
