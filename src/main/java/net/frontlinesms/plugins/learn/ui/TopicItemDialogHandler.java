package net.frontlinesms.plugins.learn.ui;

import thinlet.Thinlet;
import net.frontlinesms.plugins.learn.data.domain.Topic;
import net.frontlinesms.plugins.learn.data.domain.TopicItem;
import net.frontlinesms.plugins.learn.data.repository.TopicDao;
import net.frontlinesms.ui.FrontlineUI;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

public abstract class TopicItemDialogHandler<E extends TopicItem> implements ThinletUiEventHandler {
	static final String CB_TOPIC = "cbTopic";

	final FrontlineUI ui;
	final TopicDao topicDao;
	private final Object dialog;
	final E topicItem;

//> INIT METHODS
	public TopicItemDialogHandler(FrontlineUI ui, TopicDao topicDao, E topicItem) {
		this.topicDao = topicDao;
		this.ui = ui;
		this.topicItem = topicItem;
		dialog = ui.loadComponentFromFile(getLayoutFile(), this);
		
		// init list of topics
		Object cbTopic = find(CB_TOPIC);
		for(Topic t : topicDao.list()) {
			ui.add(cbTopic, ui.createComboboxChoice(t.getName(), t));
		}
		
		if(topicItem.getTopic() != null) {
			setTopic(topicItem.getTopic());
		}
	}
	
	abstract String getLayoutFile();
	
//> ACCESSORS
	/** Set the topic that the reinforcement refers to. */
	public void setTopic(Topic topic) {
		Object cbTopics = find(CB_TOPIC);
		ui.setText(cbTopics, topic.getName());
	}

	public Object getDialog() {
		return dialog;
	}
	
//> UI EVENT METHODS
	public abstract void save();
	
	public void close() {
		ui.remove(dialog);
	}
	
	public void validate() {
		ui.setEnabled(find("btSave"), doValidate());
	}
	
//> UI HELPER METHODS
	protected Object find(String componentName) {
		return Thinlet.find(dialog, componentName);
	}
	
	abstract boolean doValidate();
	
	protected boolean isTopicValid() {
		String topicName = ui.getText(find(CB_TOPIC));
		return !topicName.equals("i18n.plugins.learn.topic.choose") &&
				!topicName.equals(InternationalisationUtils.getI18nString("i18n.plugins.learn.topic.choose"));
	}
}
