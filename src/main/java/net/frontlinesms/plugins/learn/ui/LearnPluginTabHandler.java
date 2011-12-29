package net.frontlinesms.plugins.learn.ui;

import thinlet.Thinlet;
import net.frontlinesms.ui.FrontlineUI;
import net.frontlinesms.ui.ThinletUiEventHandler;

public class LearnPluginTabHandler implements ThinletUiEventHandler {
	private Object tab;
	
	public LearnPluginTabHandler(FrontlineUI ui) {
		tab = Thinlet.create("tab");
		ui.setName(tab, "plugin-learn");
		Object tabbedPane = Thinlet.create(Thinlet.TABBEDPANE);
		ui.add(tab, tabbedPane);
		ui.add(tabbedPane, new TopicTabHandler(ui).getTab());
	}

	public Object getTab() {
		return tab;
	}
}
