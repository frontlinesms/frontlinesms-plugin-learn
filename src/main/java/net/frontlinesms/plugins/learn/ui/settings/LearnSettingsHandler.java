package net.frontlinesms.plugins.learn.ui.settings;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import thinlet.Thinlet;

import net.frontlinesms.events.EventBus;
import net.frontlinesms.plugins.learn.LearnPluginProperties;
import net.frontlinesms.settings.FrontlineValidationMessage;
import net.frontlinesms.ui.FrontlineUI;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.settings.SettingsChangedEventNotification;
import net.frontlinesms.ui.settings.UiSettingsSectionHandler;

public class LearnSettingsHandler implements UiSettingsSectionHandler, ThinletUiEventHandler {
	private LearnPluginProperties properties;
	private FrontlineUI ui;
	private Object panel;
	private EventBus eventBus;
	protected Map<String, Object> originalValues;
	
	public LearnSettingsHandler(FrontlineUI ui) {
		this.ui = ui;
	}
	
	public void init(ApplicationContext ctx, LearnPluginProperties properties) {
		this.properties = properties;
		panel = ui.loadComponentFromFile("/ui/plugins/learn/settings/pnMain.xml", this);
		eventBus = (EventBus) ctx.getBean("eventBus");
		
		// init fields
		originalValues = new HashMap<String, Object>();
		
		ui.setText(find("tfResendDelay"), Integer.toString(properties.getResendDelay()));
		originalValues.put("tfResendDelay", Integer.toString(properties.getResendDelay()));
	}

	public Object getPanel() {
		return panel;
	}

	public void deinit() {}

	public void save() {
		properties.setResendDelay(Integer.parseInt(ui.getText(find("tfResendDelay"))));
		properties.saveToDisk();
	}

	public List<FrontlineValidationMessage> validateFields() {
		LinkedList<FrontlineValidationMessage> failures = new LinkedList<FrontlineValidationMessage>();
		if(getResendDelay() == null) failures.add(new FrontlineValidationMessage("plugins.learn.resend.delay.invalid", null, null));
		return failures;
	}

	public String getTitle() {
		return "Learn settings";
	}

	public Object getSectionNode() { return null; }
	
//> UI EVENT METHODS
	public void settingChanged(String key, String newValue) {
		Object oldValue = this.originalValues.get(key);
		eventBus.notifyObservers(new SettingsChangedEventNotification(key,
				newValue == null && oldValue == null || newValue.equals(oldValue)));
	}

	
//> HELPER METHODS
	private Object find(String componentName) {
		return Thinlet.find(panel, componentName);
	}
	
	private Integer getResendDelay() {
		try {
			return Integer.parseInt(ui.getText(find("tfResendDelay")));
		} catch(NumberFormatException ex) {
			return null;
		}
	}
}
