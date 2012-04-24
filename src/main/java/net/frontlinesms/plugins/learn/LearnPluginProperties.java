package net.frontlinesms.plugins.learn;

import net.frontlinesms.resources.UserHomeFilePropertySet;

public class LearnPluginProperties extends UserHomeFilePropertySet {
	private static final String PROP_RESEND_DELAY = "resend.delay";

	protected LearnPluginProperties() {
		super("plugin.learn");
	}

	public void setResendDelay(int delay) {
		setPropertyAsInteger(PROP_RESEND_DELAY, delay);
	}

	public int getResendDelay() {
		return getPropertyAsInt(PROP_RESEND_DELAY, 300);
	}
}
