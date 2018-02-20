package marc.com.maildemo.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by chengda
 * Date: 2018/2/18
 * Time: 20:43
 * Version: 1.0
 * Description:
 * Email:wangchengda1990@gmail.com
 **/
public class Email implements Serializable {
	private static final long serialVersionUID = 1L;
	private String messageID;
	private String from;
	private String to;
	private String cc;
	private String bcc;
	private String subject;
	private String sentdata;
	private String content;
	private boolean replysign;
	private boolean html;
	private boolean news;
	private ArrayList<String> attachments;
	private String charset;

	public String getMessageID() {
		return messageID;
	}

	public void setMessageID(String messageID) {
		this.messageID = messageID;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public String getBcc() {
		return bcc;
	}

	public void setBcc(String bcc) {
		this.bcc = bcc;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getSentdata() {
		return sentdata;
	}

	public void setSentdata(String sentdata) {
		this.sentdata = sentdata;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isReplysign() {
		return replysign;
	}

	public void setReplysign(boolean replysign) {
		this.replysign = replysign;
	}

	public boolean isHtml() {
		return html;
	}

	public void setHtml(boolean html) {
		this.html = html;
	}

	public boolean isNews() {
		return news;
	}

	public void setNews(boolean news) {
		this.news = news;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public ArrayList<String> getAttachments() {
		return attachments;
	}

	public void setAttachments(ArrayList<String> attachments) {
		this.attachments = attachments;
	}
}
