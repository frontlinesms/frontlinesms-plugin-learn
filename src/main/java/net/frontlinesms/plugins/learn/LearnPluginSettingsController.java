package net.frontlinesms.plugins.learn;

import net.frontlinesms.events.EventBus;
import net.frontlinesms.plugins.PluginSettingsController;
import net.frontlinesms.plugins.learn.ui.settings.LearnSettingsHandler;
import net.frontlinesms.ui.FrontlineUI;
import net.frontlinesms.ui.settings.UiSettingsSectionHandler;

public class LearnPluginSettingsController implements PluginSettingsController {
	private Object rootNode;
	private UiSettingsSectionHandler rootPanelHandler;
	
	public LearnPluginSettingsController(FrontlineUI ui, EventBus eventBus) {
		LearnSettingsHandler learnSettingsHandler = new LearnSettingsHandler(ui);
		learnSettingsHandler.init(LearnPluginProperties.getInstance(), eventBus);
		rootPanelHandler = learnSettingsHandler;
		
		rootNode = ui.createNode("Learn", rootPanelHandler);
		ui.setIcon(rootNode, "/icons/plugins/learn/logo_small.png");
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
