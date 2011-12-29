package net.frontlinesms.plugins.learn.ui;

import thinlet.Thinlet;

import net.frontlinesms.plugins.learn.data.repository.TopicDao;
import net.frontlinesms.ui.FrontlineUI;
import net.frontlinesms.ui.ThinletUiEventHandler;

public class LearnPluginTabHandler implements ThinletUiEventHandler {
	private TopicDao topicDao;
	
	private Object tab;
	
	public void init(FrontlineUI ui) {
		tab = Thinlet.create("tab");
		ui.setName(tab, "plugin-learn");
		ui.setText(tab, "Learn (beta)");
		ui.setIcon(tab, "/icons/plugins/learn/logo_large.png");
		Object tabbedPane = Thinlet.create(Thinlet.TABBEDPANE);
		ui.add(tab, tabbedPane);
		ui.add(tabbedPane, new TopicTabHandler(ui, topicDao).getTab());
	}

	public Object getTab() {
		return tab;
	}
	
	public void setTopicDao(TopicDao topicDao) {
		this.topicDao = topicDao;
	}
}
