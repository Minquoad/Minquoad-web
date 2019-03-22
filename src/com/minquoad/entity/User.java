package com.minquoad.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.minquoad.framework.dao.Entity;
import com.minquoad.service.Deployment;
import com.minquoad.tool.SecurityTool;

public class User extends Entity {

	private String mailAddress;
	private String nickname;
	private String hashedSaltedPassword;
	private Instant registrationInstant;
	private Instant lastActivityInstant;
	private int adminLevel;
	private Instant unblockInstant;

	public static final int nicknameMaxlength = 25;
	public static final int mailAddressMaxlength = 50;

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
		if (adminLevel < 0) {
			new Exception("adminLevel must be >= 0").printStackTrace();
		} else {
			this.adminLevel = adminLevel;
		}
	}

	public boolean isAdmin() {
		return this.adminLevel != 0;
	}

	public boolean hasAdminLvl(int adminLevel) {
		return this.adminLevel >= adminLevel;
	}

	public boolean canAdminister(User user) {
		return hasAdminLvl(user.getAdminLevel() + 1);
	}

	public void setPassword(String password) {
		this.setHashedSaltedPassword(toHashedSaltedPassword(password));
	}

	public boolean isPassword(String password) {
		return this.getHashedSaltedPassword().equals(toHashedSaltedPassword(password));
	}

	public String toHashedSaltedPassword(String string) {
		try {

			String saltedString = Deployment.getDynamicSalt(this) + Deployment.passwordSalt + string;
			return SecurityTool.toSha512(saltedString);

		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public static String formatNickanameCase(String nickname) {
		return nickname.substring(0, 1).toUpperCase() + nickname.substring(1).toLowerCase();
	}

	public static String formatMailAddressCase(String mailAddress) {
		return mailAddress.toLowerCase();
	}

	public static List<String> getNicknameProblems(String nickname) {
		List<String> problems = new ArrayList<String>();

		if (nickname.length() < 6) {
			problems.add("The nickname must contain at least 6 characters.");
			return problems;
		}

		if (nickname.length() > nicknameMaxlength) {
			problems.add("The nickname must contain at maximum 25 characters.");
		}

		List<Character> nicknameImpossibleChar = getNotNormalChars(nickname);
		if (nicknameImpossibleChar.size() != 0) {
			String impossibleCharsText = "The folowing characters are not allowed in a nickname: ";
			boolean firstLoop = true;
			for (Character character : nicknameImpossibleChar) {
				if (!firstLoop) {
					impossibleCharsText += ", ";
				}
				impossibleCharsText += character;

				firstLoop = false;
			}
			problems.add(impossibleCharsText);
		}

		int dashOccurences = 0;
		for (int i = 0; i < nickname.length(); i++) {
			if (nickname.charAt(i) == '-') {
				dashOccurences++;
			}
		}

		if (dashOccurences > 1) {
			problems.add("Only one \"-\" is allowed in a nickname.");
		}

		if (nickname.charAt(0) == '-') {
			problems.add("A \"-\" is not allowed at nickname beginning.");
		}

		if (nickname.charAt(nickname.length() - 1) == '-') {
			problems.add("A \"-\" is not allowed at nickname end.");
		}

		return problems;
	}

	public static List<Character> getNotNormalChars(String nickname) {
		List<Character> impossibleChars = new ArrayList<Character>();

		for (int i = 0; i < nickname.length(); i++) {
			char currentChar = nickname.charAt(i);
			if (!isCharNormal(currentChar) && !impossibleChars.contains(currentChar)) {
				impossibleChars.add(currentChar);
			}
		}

		return impossibleChars;
	}

	public static boolean isCharNormal(char character) {
		for (char possibleChar : getNormalChars()) {
			if (possibleChar == character) {
				return true;
			}
		}
		return false;
	}

	public static char[] getNormalChars() {
		char[] possibleChars = {
				'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
				'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
				'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'À', 'Á', 'Â', 'Ã', 'Ä', 'Å', 'Æ', 'Ç', 'È', 'É', 'Ê',
				'Ë', 'Ì', 'Í', 'Î', 'Ï', 'Ð', 'Ñ', 'Ò', 'Ó', 'Ô', 'Õ', 'Ö', 'Ø', 'Ù', 'Ú', 'Û', 'Ü', 'Ý', 'Þ', 'ß', 'à',
				'á', 'â', 'ã', 'ä', 'å', 'æ', 'ç', 'è', 'é', 'ê', 'ë', 'ì', 'í', 'î', 'ï', 'ð', 'ñ', 'ò', 'ó', 'ô', 'õ',
				'ö', 'ù', 'ú', 'û', 'ü', 'ý', 'þ', 'ÿ', '-'
		};
		return possibleChars;
	}

	public static List<String> getPasswordProblems(String password) {
		List<String> problems = new ArrayList<String>();

		List<Character> passwordNotNormalChars = getNotNormalChars(password);
		if (passwordNotNormalChars.size() == 0) {
			problems.add("Please add a number and/or a special character.");
		}

		if (password.length() < 6) {
			problems.add("The password must contain at least 6 characters.");
		}

		return problems;
	}

	public static List<String> getMailAddressProblems(String mailAddress) {
		List<String> problems = new ArrayList<String>();

		// TODO

		return problems;
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
			Instant dayBeforeUnblockInstant = Instant.ofEpochMilli(unblockInstant.toEpochMilli() - (1000l * 60l * 60l * 24l));
			return Instant.now().isBefore(dayBeforeUnblockInstant);
		}
	}

	public String getMailAddress() {
		return mailAddress;
	}

	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}

}
