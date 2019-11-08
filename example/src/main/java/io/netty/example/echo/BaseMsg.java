package io.netty.example.echo;

import java.io.Serializable;

/**
 * @author: LeoLee
 * @date: 2019/11/8 17:05
 */
public class BaseMsg implements Serializable {

    private Long msgId;
    private String msgType;
    private String content;

    public Long getMsgId() {
        return msgId;
    }

    public void setMsgId(Long msgId) {
        this.msgId = msgId;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "BaseMsg{" +
                "msgId=" + msgId +
                ", msgType='" + msgType + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
