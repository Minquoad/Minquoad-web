package com.minquoad.entity;

import java.time.Instant;

public class RequestLog {

	private Long id;
	private Instant instant;
	private String url;
	private User user;
	private String error;
	private Integer serviceDuration;
	private String servletName;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Instant getInstant() {
		return instant;
	}
	public void setInstant(Instant instant) {
		this.instant = instant;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public Integer getServiceDuration() {
		return serviceDuration;
	}
	public void setServiceDuration(Integer serviceDuration) {
		this.serviceDuration = serviceDuration;
	}
	public String getServletName() {
		return servletName;
	}
	public void setServletName(String servletName) {
		this.servletName = servletName;
	}

}
