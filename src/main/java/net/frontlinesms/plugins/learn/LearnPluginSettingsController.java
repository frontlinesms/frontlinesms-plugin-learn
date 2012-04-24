package net.frontlinesms.plugins.learn;

import net.frontlinesms.plugins.PluginSettingsController;
import net.frontlinesms.plugins.learn.ui.settings.LearnSettingsHandler;
import net.frontlinesms.ui.FrontlineUI;
import net.frontlinesms.ui.settings.UiSettingsSectionHandler;

public class LearnPluginSettingsController implements PluginSettingsController {
	private Object rootNode;
	private UiSettingsSectionHandler rootPanelHandler;
	
	public LearnPluginSettingsController(FrontlineUI ui) {
		rootPanelHandler = new LearnSettingsHandler(ui);
	}
	
	public void addSubSettingsNodes(Object rootSettingsNode) {}
	public UiSettingsSectionHandler getHandlerForSection(String section) {
		return null;
	}
	public UiSettingsSectionHandler getRootPanelHandler() {
		return rootPanelHandler;
	}
	public String getTitle() {
		return "Learn, alright!";
	}
	public Object getRootNode() {
		return rootNode;
	}
}
