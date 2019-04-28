package com.minquoad.tool;

import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;

public abstract class InternationalizationTool {

	public final static String[] supportedLanguageCodes = { "en", "fr" };

	public static String getText(String key, String bundleBaseName, Locale locale) {
		return ResourceBundle.getBundle(bundleBaseName, locale).getString(key);
	}

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
