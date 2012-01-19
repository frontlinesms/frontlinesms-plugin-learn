package net.frontlinesms.plugins.learn.ui;

import java.util.List;

import org.springframework.context.ApplicationContext;

import thinlet.Thinlet;

import net.frontlinesms.data.events.DatabaseEntityNotification;
import net.frontlinesms.data.events.EntityDeleteWarning;
import net.frontlinesms.events.EventBus;
import net.frontlinesms.events.EventObserver;
import net.frontlinesms.events.FrontlineEventNotification;
import net.frontlinesms.plugins.learn.data.domain.*;
import net.frontlinesms.plugins.learn.data.repository.*;
import net.frontlinesms.ui.FrontlineUI;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.events.FrontlineUiUpdateJob;

public class TopicTabHandler implements ThinletUiEventHandler, EventObserver {
	private static final String TAB_LAYOUT = "/ui/plugins/learn/topic/list.xml";

	private final TopicDao dao;
	private final TopicItemDao topicItemDao;
	private final ReinforcementDao reinforcementDao;
	private final QuestionDao questionDao;
	private final FrontlineUI ui;
	private final Object tab;

	public TopicTabHandler(FrontlineUI ui, ApplicationContext ctx) {
		this.dao = (TopicDao) ctx.getBean("topicDao");
		this.topicItemDao = (TopicItemDao) ctx.getBean("topicItemDao");
		this.reinforcementDao = (ReinforcementDao) ctx.getBean("reinforcementDao");
		this.questionDao = (QuestionDao) ctx.getBean("questionDao");
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
		
		boolean enableTopicItemCreation = topics.size() > 0;
		ui.setEnabled(find("btNewReinforcement"), enableTopicItemCreation);
		ui.setEnabled(find("btNewQuestion"), enableTopicItemCreation);
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
		ui.add(new NewReinforcementDialogHandler(ui, reinforcementDao, dao).getDialog());
	}
	
	public void newQuestion() {
		ui.add(new NewQuestionDialogHandler(ui, questionDao, dao).getDialog());
	}
	
	public void trTopics_expand(Object tree) {
		Object topicComponent = ui.getSelectedItem(tree);
		Topic selectedTopic = ui.getAttachedObject(topicComponent, Topic.class);
		ui.removeAll(topicComponent);
		for(TopicItem t : topicItemDao.getAllByTopic(selectedTopic)) {
			ui.add(topicComponent, createNode(t));
		}
	}
	
	public void trTopics_perform(Object tree) {
		Object selectedItem = ui.getSelectedItem(tree);
		if(selectedItem != null) {
			Object attached = ui.getAttachedObject(selectedItem);
			if(attached instanceof Reinforcement) {
				ui.add(new EditReinforcementDialogHandler(ui, reinforcementDao, dao, (Reinforcement) attached).getDialog());
			} else throw new RuntimeException("Don't know how to handle: " + attached.getClass());
		}
	}
	
//> UI HELPER METHODS
	private Object find(String componentName) {
		return Thinlet.find(tab, componentName);
	}
	
	private Object createNode(Topic t) {
		Object n = ui.createNode(t.getName(), t);
		ui.add(n, ui.createNode("dummy-node", null));
		ui.setExpanded(n, false);
		return n;
	}
	
	private Object createNode(TopicItem t) {
		Object n = ui.createNode(t.getMessageText(), t);
		return n;
	}
	
//> INTERNAL EVENT HANDLING
	public void notify(final FrontlineEventNotification notification) {
		if(isDbNotification(Topic.class, notification) ||
				isDbNotification(TopicItem.class, notification)) {
			threadSafeRefresh();
		}
	}
	
	private boolean isDbNotification(Class<?> c, FrontlineEventNotification n) {
		return n instanceof DatabaseEntityNotification<?> &&
				!(n instanceof EntityDeleteWarning<?>) &&
				c.isInstance(((DatabaseEntityNotification<?>) n).getDatabaseEntity());
	}
}
