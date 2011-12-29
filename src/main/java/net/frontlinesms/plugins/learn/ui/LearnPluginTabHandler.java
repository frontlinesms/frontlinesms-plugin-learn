package net.frontlinesms.plugins.learn.ui;

import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;

public class LearnPluginTabHandler implements ThinletUiEventHandler {
	private static final String LAYOUT_FILE = "/ui/plugins/learn/tbMain.xml";
	
	private Object tab;
	
	public LearnPluginTabHandler(UiGeneratorController ui) {
		tab = ui.loadComponentFromFile(LAYOUT_FILE, this);
	}
	
	public Object getTab() {
		return tab;
	}
}
