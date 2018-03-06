package marc.com.maildemo.model;

/**
 * Version: V1.0
 * Description:
 * Date: 2018/2/27
 * Created by wangcd
 */

public class SimpleEmail {
    private String subject;

    private String sendTime;

    private boolean isNew;

    private String from;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
