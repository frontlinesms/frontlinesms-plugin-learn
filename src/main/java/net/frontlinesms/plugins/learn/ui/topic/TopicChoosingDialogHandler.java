package net.frontlinesms.plugins.learn.ui.topic;

import thinlet.Thinlet;
import net.frontlinesms.plugins.learn.data.domain.HasTopic;
import net.frontlinesms.plugins.learn.data.domain.Topic;
import net.frontlinesms.plugins.learn.data.repository.TopicDao;
import net.frontlinesms.ui.FrontlineUI;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

public abstract class TopicChoosingDialogHandler<E extends HasTopic> implements ThinletUiEventHandler {
	static final String CB_TOPIC = "cbTopic";

	final FrontlineUI ui;
	final TopicDao topicDao;
	private final Object dialog;
	final E editItem;

//> INIT METHODS
	public TopicChoosingDialogHandler(FrontlineUI ui, TopicDao topicDao, E topicItem) {
		this.topicDao = topicDao;
		this.ui = ui;
		this.editItem = topicItem;
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
	
	public abstract String getLayoutFile();
	
//> ACCESSORS
	/** Set the topic that the reinforcement refers to. */
	public void setTopic(Topic topic) {
		Object cbTopics = find(CB_TOPIC);
		ui.setText(cbTopics, topic.getName());
	}

	public Object getDialog() {
		return dialog;
	}
	
	protected Topic getSelectedTopic() {
		return topicDao.findByName(ui.getText(find(CB_TOPIC)));
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
	
	public abstract boolean doValidate();
	
	protected boolean isTopicValid() {
		String topicName = ui.getText(find(CB_TOPIC));
		return !topicName.equals(InternationalisationUtils.getI18nString("plugins.learn.topic.choose"));
	}
}
