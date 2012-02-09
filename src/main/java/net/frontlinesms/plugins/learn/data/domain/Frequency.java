package net.frontlinesms.plugins.learn.data.domain;

import net.frontlinesms.ui.i18n.Internationalised;

public enum Frequency implements Internationalised {
	ONCE("plugins.learn.frequency.once"),
	DAILY("plugins.learn.frequency.daily"),
	WEEKLY("plugins.learn.frequency.weekly"),
	MONTHLY("plugins.learn.frequency.monthly");
	
	private Frequency(String i18nKey) {
		this.i18nKey = i18nKey;
	}
	
	private final String i18nKey;

	public String getI18nKey() {
		return i18nKey;
	}
}
