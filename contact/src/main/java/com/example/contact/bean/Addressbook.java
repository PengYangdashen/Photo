package com.example.contact.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Addressbook implements Serializable {

	/**
	 * 通讯录 人员单个主键
	 */
	private String id;
	/**
	 * 姓氏
	 */
	private String lastame;
	/**
	 * 名字
	 */
	private String name;
	/**
	 * 公司
	 */
	private String company;
	/**
	 * 职务
	 */
	private String position;

	/**
	 * 电话本  例如 电话 186-xxxxxx
	 *         住宅电话    8406-xxxx
	 *      工作   8406-xxxx
	 */
	private Map<String, String> phoneBook=new HashMap<>();
	/**
	 *电子邮箱
	 */
	private Map<String, String> emailBook =new HashMap<>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLastame() {
		return lastame;
	}

	public void setLastame(String lastame) {
		this.lastame = lastame;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public Map<String, String> getPhoneBook() {
		return phoneBook;
	}

	public void setPhoneBook(Map<String, String> phoneBook) {
		this.phoneBook = phoneBook;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public Map<String, String> getEmailBook() {
		return emailBook;
	}

	public void setEmailBook(Map<String, String> emailBook) {
		this.emailBook = emailBook;
	}

}
