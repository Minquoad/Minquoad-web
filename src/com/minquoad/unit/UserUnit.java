package com.minquoad.unit;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;

import com.minquoad.dao.interfaces.UserDao;
import com.minquoad.entity.User;
import com.minquoad.tool.InternationalizationTool;
import com.minquoad.tool.form.ImprovedForm;

public class UserUnit extends Unit {

	public UserUnit(UnitFactory unitFactory) {
		super(unitFactory);
	}

	public User createNewUser(String mailAddress, String nickname, String password, String language) {

		UserDao userDao = getDaoFactory().getUserDao();

		User user = new User();
		user.setMailAddress(mailAddress);
		user.setNickname(nickname);
		user.setRegistrationInstant(Instant.now());
		user.setLastActivityInstant(Instant.now());
		user.setLanguage(language);
		userDao.persist(user);
		user.setPassword(password, getDeployment());
		userDao.persist(user);
		getUnitFactory().getConversationUnit().initUserConversations(user);

		return user;
	}

	public static String formatNickanameCase(String nickname) {
		if (nickname.isEmpty()) {
			return nickname;
		}
		return nickname.substring(0, 1).toUpperCase() + nickname.substring(1).toLowerCase();
	}

	public static Collection<String> getNicknameProblems(String nickname, Locale locale) {
		Collection<String> problems = new ArrayList<String>();

		if (nickname.length() < User.NICKNAME_MIN_LENGTH) {
			problems.add(InternationalizationTool.getText(
					"TooShortNickName",
					ImprovedForm.formResourceBundleName,
					locale,
					User.NICKNAME_MIN_LENGTH));
			return problems;
		}

		if (nickname.length() > User.NICKNAME_MAX_LENGTH) {
			problems.add(InternationalizationTool.getText(
					"TooLongNickname",
					ImprovedForm.formResourceBundleName,
					locale,
					User.NICKNAME_MAX_LENGTH));
		}

		Collection<Character> nicknameImpossibleChar = getNotNormalChars(nickname);
		if (nicknameImpossibleChar.size() != 0) {
			String impossibleCharsText = "";
			for (Character character : nicknameImpossibleChar) {
				impossibleCharsText += character;
			}
			problems.add(InternationalizationTool.getText(
					"UnauthorisedChars",
					ImprovedForm.formResourceBundleName,
					locale,
					impossibleCharsText));
		}

		int dashOccurences = 0;
		for (int i = 0; i < nickname.length(); i++) {
			if (nickname.charAt(i) == '-') {
				dashOccurences++;
			}
		}

		if (dashOccurences > 1) {
			problems.add(InternationalizationTool.getText(
					"TooMuchHyphen",
					ImprovedForm.formResourceBundleName,
					locale));
		}

		if (nickname.charAt(0) == '-') {
			problems.add(InternationalizationTool.getText(
					"HyphenWrongPotitionBeginning",
					ImprovedForm.formResourceBundleName,
					locale));
		}

		if (nickname.charAt(nickname.length() - 1) == '-') {
			problems.add(InternationalizationTool.getText(
					"HyphenWrongPotitionEnd",
					ImprovedForm.formResourceBundleName,
					locale));
		}

		return problems;
	}

	public static Collection<Character> getNotNormalChars(String nickname) {
		Collection<Character> impossibleChars = new HashSet<Character>();

		for (int i = 0; i < nickname.length(); i++) {
			char currentChar = nickname.charAt(i);
			if (!isCharNormal(currentChar)) {
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

	public static Collection<String> getPasswordProblems(String password, Locale locale) {
		Collection<String> problems = new ArrayList<String>();

		Collection<Character> passwordNotNormalChars = getNotNormalChars(password);
		if (passwordNotNormalChars.size() == 0) {
			problems.add(InternationalizationTool.getText(
					"NospecialChar",
					ImprovedForm.formResourceBundleName,
					locale));
		}

		if (password.length() < 6) {
			problems.add(InternationalizationTool.getText(
					"TooShortPassword",
					ImprovedForm.formResourceBundleName,
					locale));
		}

		return problems;
	}

}
