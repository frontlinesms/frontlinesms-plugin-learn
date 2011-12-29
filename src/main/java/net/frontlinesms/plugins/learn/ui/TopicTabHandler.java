package net.frontlinesms.plugins.learn.ui;

import net.frontlinesms.plugins.learn.data.repository.TopicDao;
import net.frontlinesms.ui.FrontlineUI;
import net.frontlinesms.ui.ThinletUiEventHandler;

public class TopicTabHandler implements ThinletUiEventHandler {
	private static final String TAB_LAYOUT = "/ui/plugins/learn/topic/list.xml";

	private final TopicDao dao;
	private final FrontlineUI ui;
	private final Object tab;


	public TopicTabHandler(FrontlineUI ui, TopicDao dao) {
		this.dao = dao;
		this.ui = ui;
		this.tab = ui.loadComponentFromFile(TAB_LAYOUT, this);
	}

	public Object getTab() {
		return tab;
	}

//> UI EVENT HANDLERS
	public void newTopic() {
		ui.add(new TopicEditDialogHandler(ui, dao).getDialog());
	}
}
