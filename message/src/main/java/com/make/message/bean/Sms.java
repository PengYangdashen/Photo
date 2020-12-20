package com.make.message.bean;

public class Sms {

	private String id;
	
	private String sendNo;
	
	private String sendName;
	
	private String receptNo;
	
	private String receptName;
	
	private String content;
	/**
	 * 类型 收信，还是发信
	 */
	private String type;
	/**
	 * 发送时间
	 */
	private long sendTimeStamp;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSendNo() {
		return sendNo;
	}

	public void setSendNo(String sendNo) {
		this.sendNo = sendNo;
	}

	public String getSendName() {
		return sendName;
	}

	public void setSendName(String sendName) {
		this.sendName = sendName;
	}

	public String getReceptNo() {
		return receptNo;
	}

	public void setReceptNo(String receptNo) {
		this.receptNo = receptNo;
	}

	public String getReceptName() {
		return receptName;
	}

	public void setReceptName(String receptName) {
		this.receptName = receptName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getSendTimeStamp() {
		return sendTimeStamp;
	}

	public void setSendTimeStamp(long sendTimeStamp) {
		this.sendTimeStamp = sendTimeStamp;
	}
	
	
	
}
