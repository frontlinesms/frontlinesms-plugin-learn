package net.frontlinesms.plugins.learn.ui.settings;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
	
//> INITIALISATION
	public LearnSettingsHandler(FrontlineUI ui) {
		this.ui = ui;
	}
	
	public void init(LearnPluginProperties properties, EventBus eventBus) {
		this.properties = properties;
		panel = ui.loadComponentFromFile("/ui/plugins/learn/settings/pnMain.xml", this);
		this.eventBus = eventBus;
		
		// init fields
		originalValues = new HashMap<String, Object>();

		init("tfResendDelay", properties.getResendDelay());
		init("taCorrectResponse", properties.getCorrectResponse());
	}
	
	private void init(String componentName, Integer value) {
		init(componentName, Integer.toString(properties.getResendDelay()));
	}
	
	private void init(String componentName, String value) {
		ui.setText(find(componentName), value);
		originalValues.put(componentName, value);
	}

//> ACCESSORS
	public Object getPanel() {
		return panel;
	}

	public void deinit() {}

	public void save() {
		properties.setResendDelay(getResendDelay());
		properties.setCorrectResponse(getText("taCorrectResponse"));
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
	private String getText(String componentName) {
		return ui.getText(find(componentName));
	}
	
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
