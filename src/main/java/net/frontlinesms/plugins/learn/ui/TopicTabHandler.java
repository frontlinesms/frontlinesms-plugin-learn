package net.frontlinesms.plugins.learn.ui;

import org.springframework.context.ApplicationContext;

import net.frontlinesms.events.EventBus;
import net.frontlinesms.events.EventObserver;
import net.frontlinesms.events.FrontlineEventNotification;
import net.frontlinesms.plugins.learn.data.repository.TopicDao;
import net.frontlinesms.ui.FrontlineUI;
import net.frontlinesms.ui.ThinletUiEventHandler;

public class TopicTabHandler implements ThinletUiEventHandler, EventObserver {
	private static final String TAB_LAYOUT = "/ui/plugins/learn/topic/list.xml";

	private final TopicDao dao;
	private final FrontlineUI ui;
	private final Object tab;


	public TopicTabHandler(FrontlineUI ui, ApplicationContext ctx) {
		this.dao = (TopicDao) ctx.getBean("topicDao");
		this.ui = ui;
		this.tab = ui.loadComponentFromFile(TAB_LAYOUT, this);

		((EventBus) ctx.getBean("eventBus")).registerObserver(this);
	}

	public Object getTab() {
		return tab;
	}

//> UI EVENT HANDLERS
	public void newTopic() {
		ui.add(new TopicEditDialogHandler(ui, dao).getDialog());
	}
	
//> INTERNAL EVENT HANDLING
	public void notify(FrontlineEventNotification notification) {}
}
