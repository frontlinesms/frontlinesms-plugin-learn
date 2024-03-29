package net.frontlinesms.plugins.learn;

import net.frontlinesms.resources.UserHomeFilePropertySet;

public class LearnPluginProperties extends UserHomeFilePropertySet {
	private static final String PROP_RESEND_DELAY = "resend.delay";
	private static final String PROP_CORRECT_RESPONSE = "response.correct";
	
	private static LearnPluginProperties instance;

	private LearnPluginProperties() {
		super("plugin.learn");
	}

	public void setResendDelay(int delay) {
		setPropertyAsInteger(PROP_RESEND_DELAY, delay);
	}

	public int getResendDelay() {
		return getPropertyAsInt(PROP_RESEND_DELAY, 0);
	}

	public boolean isResendEnabled() {
		return getResendDelay() > 0;
	}

	public String getCorrectResponse() {
		return getProperty(PROP_CORRECT_RESPONSE);
	}

	public void setCorrectResponse(String response) {
		setProperty(PROP_CORRECT_RESPONSE, response);
	}
	
	/**
	 * Lazy getter for {@link #instance}
	 * @return The singleton instance of this class
	 */
	public static synchronized LearnPluginProperties getInstance() {
		if(instance == null) {
			instance = new LearnPluginProperties();
		}
		return instance;
	}
}
