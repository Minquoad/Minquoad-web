package Entities;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import frameworks.daos.Entity;
import utilities.Deployment;

public class User extends Entity {

	private String nickname;
	private String hashedSaltedPassword;
	private String pictureName;
	private Date registrationDate;
	private Date lastActivityDate;
	private int adminLevel;

	public static final int nicknameMaxlength = 25;

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

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public Date getLastActivityDate() {
		return lastActivityDate;
	}

	public void setLastActivityDate(Date lastLogInDate) {
		this.lastActivityDate = lastLogInDate;
	}

	public int getAdminLevel() {
		return adminLevel;
	}

	public void setAdminLevel(int adminLevel) {
		this.adminLevel = adminLevel;
	}

	public String getPictureName() {
		return pictureName;
	}

	public void setPictureName(String picture) {
		this.pictureName = picture;
	}

	public void setPassword(String password) {
		this.setHashedSaltedPassword(toHashedSaltedPassword(password));
	}

	public boolean isPassword(String password) {
		return this.getHashedSaltedPassword().equals(toHashedSaltedPassword(password));
	}

	public String toHashedSaltedPassword(String string) {
		try {

			String saltedString = Deployment.passwordSalt + string + Deployment.getDynamicSalt(this);

			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(saltedString.getBytes("UTF-8"));
			StringBuffer hexString = new StringBuffer();

			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}

			return hexString.toString();

		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public static String formatNickanameCase(String nickname) {
		return nickname.substring(0, 1).toUpperCase() + nickname.substring(1).toLowerCase();
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

}
