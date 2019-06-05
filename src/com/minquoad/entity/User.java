package com.minquoad.entity;

import java.time.Instant;

import com.minquoad.service.Deployment;
import com.minquoad.tool.security.SecurityTool;

public class User {

	public static final int NICKNAME_MIN_LENGTH = 6;
	public static final int NICKNAME_MAX_LENGTH = 25;
	public static final int MAIL_ADDRESS_MAX_LENGTH = 50;

	private Long id;
	private String mailAddress;
	private String nickname;
	private String hashedSaltedPassword;
	private Instant registrationInstant;
	private Instant lastActivityInstant;
	private int adminLevel = 0;
	private Instant unblockInstant;
	private Integer defaultColor = 8421504;
	private String language;
	private boolean readabilityImprovementActivated = true;
	private boolean typingAssistanceActivated = true;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getHashedSaltedPassword() {
		return hashedSaltedPassword;
	}

	public void setHashedSaltedPassword(String hashedSaltedPassword) {
		this.hashedSaltedPassword = hashedSaltedPassword;
	}

	public Instant getRegistrationInstant() {
		return registrationInstant;
	}

	public void setRegistrationInstant(Instant registrationInstant) {
		this.registrationInstant = registrationInstant;
	}

	public Instant getLastActivityInstant() {
		return lastActivityInstant;
	}

	public void setLastActivityInstant(Instant lastLogInInstant) {
		this.lastActivityInstant = lastLogInInstant;
	}

	public int getAdminLevel() {
		return adminLevel;
	}

	public void setAdminLevel(int adminLevel) {
		if (adminLevel >= 0) {
			this.adminLevel = adminLevel;
		}
	}

	public boolean isAdmin() {
		return getAdminLevel() != 0;
	}

	public boolean canAdminister(User user) {
		return this.getAdminLevel() > user.getAdminLevel();
	}

	public void setPassword(String password, Deployment deployment) {
		this.setHashedSaltedPassword(toHashedSaltedPassword(password, deployment));
	}

	public boolean isPassword(String password, Deployment deployment) {
		return this.getHashedSaltedPassword().equals(toHashedSaltedPassword(password, deployment));
	}

	public String toHashedSaltedPassword(String password, Deployment deployment) {
		String saltedString = SecurityTool.getDynamicSalt(this) + deployment.getUserPasswordSalt() + password;
		return SecurityTool.getSha512(saltedString);
	}

	public Instant getUnblockInstant() {
		return unblockInstant;
	}

	public void setUnblockInstant(Instant unblockInstant) {
		this.unblockInstant = unblockInstant;
	}

	public boolean isBlocked() {
		if (unblockInstant == null) {
			return false;
		} else {
			return Instant.now().isBefore(unblockInstant);
		}
	}

	public String getMailAddress() {
		return mailAddress;
	}

	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}

	public String getDefaultColorAsHtmlValue() {
		if (getDefaultColor() == null) {
			return null;
		} else {
			String hexString = Integer.toHexString(getDefaultColor());
			while (hexString.length() < 6) {
				hexString = "0" + hexString;
			}
			return "#" + hexString;
		}
	}

	public Integer getDefaultColor() {
		return defaultColor;
	}

	public void setDefaultColor(Integer defaultColor) {
		this.defaultColor = defaultColor;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public boolean isReadabilityImprovementActivated() {
		return readabilityImprovementActivated;
	}

	public void setReadabilityImprovementActivated(boolean readabilityImprovementActivated) {
		this.readabilityImprovementActivated = readabilityImprovementActivated;
	}

	public boolean isTypingAssistanceActivated() {
		return typingAssistanceActivated;
	}

	public void setTypingAssistanceActivated(boolean typingAssistanceActivated) {
		this.typingAssistanceActivated = typingAssistanceActivated;
	}

}
