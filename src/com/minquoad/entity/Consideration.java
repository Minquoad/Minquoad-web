package com.minquoad.entity;

public class Consideration {

	public static final int STATUS_FIEND = 0;
	public static final int STATUS_BLOCKED = 1;

	private Long id;
	private User consideringUser;
	private User consideredUser;
	private Integer status = STATUS_FIEND;
	private Integer perceptionColor;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public User getConsideringUser() {
		return consideringUser;
	}
	public void setConsideringUser(User consideringUser) {
		this.consideringUser = consideringUser;
	}
	public User getConsideredUser() {
		return consideredUser;
	}
	public void setConsideredUser(User consideredUser) {
		this.consideredUser = consideredUser;
	}
	public Integer getPerceptionColor() {
		return perceptionColor;
	}
	public void setPerceptionColor(Integer perceptionColor) {
		this.perceptionColor = perceptionColor;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}

}
