package com.minquoad.tool;

import java.util.Enumeration;
import java.util.Locale;

public abstract class InternationalizationTool {

	public final static String[] supportedLanguageCodes = { "en", "fr" };

	public static Locale getClosestValidLocale(Enumeration<Locale> locales) {

		String country = null;

		while (locales.hasMoreElements()) {
			Locale locale = (Locale) locales.nextElement();

			if (isLanguageValid(locale.getLanguage())) {
				return locale;
			}

			if (country == null && !"".equals(locale.getCountry())) {
				country = locale.getCountry();
			}
		}

		if (country == null) {
			country = Locale.getDefault().getCountry();
		}
		return new Locale(supportedLanguageCodes[0], country);
	}

	public static boolean isLanguageValid(String language) {
		for (String supportedLanguageCode : supportedLanguageCodes) {
			if (supportedLanguageCode.equals(language)) {
				return true;
			}
		}
		return false;
	}

}
