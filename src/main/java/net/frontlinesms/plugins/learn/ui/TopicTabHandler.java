package net.frontlinesms.plugins.learn.ui;

import java.util.List;

import org.springframework.context.ApplicationContext;

import thinlet.Thinlet;

import net.frontlinesms.data.events.DatabaseEntityNotification;
import net.frontlinesms.data.events.EntityDeleteWarning;
import net.frontlinesms.events.EventBus;
import net.frontlinesms.events.EventObserver;
import net.frontlinesms.events.FrontlineEventNotification;
import net.frontlinesms.plugins.learn.data.domain.Topic;
import net.frontlinesms.plugins.learn.data.repository.ReinforcementDao;
import net.frontlinesms.plugins.learn.data.repository.TopicDao;
import net.frontlinesms.ui.FrontlineUI;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.events.FrontlineUiUpdateJob;

public class TopicTabHandler implements ThinletUiEventHandler, EventObserver {
	private static final String TAB_LAYOUT = "/ui/plugins/learn/topic/list.xml";

	private final TopicDao dao;
	private final ReinforcementDao reinforcementDao;
	private final FrontlineUI ui;
	private final Object tab;

	public TopicTabHandler(FrontlineUI ui, ApplicationContext ctx) {
		this.dao = (TopicDao) ctx.getBean("topicDao");
		this.reinforcementDao = (ReinforcementDao) ctx.getBean("reinforcementDao");
		this.ui = ui;
		this.tab = ui.loadComponentFromFile(TAB_LAYOUT, this);
		
		refresh();

		((EventBus) ctx.getBean("eventBus")).registerObserver(this);
	}

	public Object getTab() {
		return tab;
	}
	
	private void refresh() {
		Object tree = find("trTopics");
		ui.removeAll(tree);
		
		List<Topic> topics = dao.list();
		for(Topic t : topics) {
			ui.add(tree, createNode(t));
		}
	}
	
	private void threadSafeRefresh() {
		new FrontlineUiUpdateJob() {
			public void run() { refresh(); }
		}.execute();
	}

//> UI EVENT HANDLERS
	public void newTopic() {
		ui.add(new TopicEditDialogHandler(ui, dao).getDialog());
	}
	
	public void newReinforcement() {
		ui.add(new NewReinforcementDialogHandler(ui, reinforcementDao).getDialog());
	}
	
//> UI HELPER METHODS
	private Object find(String componentName) {
		return Thinlet.find(tab, componentName);
	}
	
	private Object createNode(Topic t) {
		Object n = ui.createNode(t.getName(), t);
		return n;
	}
	
//> INTERNAL EVENT HANDLING
	public void notify(FrontlineEventNotification notification) {
		if(isTopicNotification(notification)) {
			threadSafeRefresh();
		}
	}
	
	private boolean isTopicNotification(FrontlineEventNotification n) {
		return n instanceof DatabaseEntityNotification<?> &&
				!(n instanceof EntityDeleteWarning<?>) &&
				((DatabaseEntityNotification<?>) n).getDatabaseEntity() instanceof Topic;
	}
}
