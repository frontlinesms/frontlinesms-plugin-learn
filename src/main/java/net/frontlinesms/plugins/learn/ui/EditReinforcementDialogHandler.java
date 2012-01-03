package net.frontlinesms.plugins.learn.ui;

import thinlet.Thinlet;
import net.frontlinesms.plugins.learn.data.domain.Reinforcement;
import net.frontlinesms.plugins.learn.data.domain.Topic;
import net.frontlinesms.plugins.learn.data.repository.ReinforcementDao;
import net.frontlinesms.plugins.learn.data.repository.TopicDao;
import net.frontlinesms.ui.FrontlineUI;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

public class EditReinforcementDialogHandler implements ThinletUiEventHandler {
	private static final String LAYOUT_FILE = "/ui/plugins/learn/reinforcement/edit.xml";
	private static final String CB_TOPIC = "cbTopic";
	private static final String TA_TEXT = "taText";
	
	private final FrontlineUI ui;
	private final ReinforcementDao dao;
	private final TopicDao topicDao;
	private final Reinforcement r;
	private final Object dialog;
	
//> INIT METHODS
	public EditReinforcementDialogHandler(FrontlineUI ui, ReinforcementDao dao, TopicDao topicDao, Reinforcement r) {
		this.dao = dao;
		this.topicDao = topicDao;
		this.ui = ui;
		this.r = r;
		dialog = ui.loadComponentFromFile(LAYOUT_FILE, this);
		
		// init list of topics
		Object cbTopic = find(CB_TOPIC);
		for(Topic t : topicDao.list()) {
			ui.add(cbTopic, ui.createComboboxChoice(t.getName(), t));
		}
		
		if(r.getTopic() != null) {
			setTopic(r.getTopic());
		}
		
		validate();
	}
	
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
	public void save() {
		r.setName(ui.getText(find(TA_TEXT)));
		r.setTopic(topicDao.findByName(ui.getText(find(CB_TOPIC))));
		dao.save(r);
		
		close();
	}
	
	public void close() {
		ui.remove(dialog);
	}
	
	public void validate() {
		boolean valid = true;
		
		if(ui.getText(find(TA_TEXT)).length() == 0) {
			valid = false;
		}
		
		String topicName = ui.getText(find(CB_TOPIC));
		if(topicName.equals("i18n.plugins.learn.topic.choose") ||
				topicName.equals(InternationalisationUtils.getI18nString("i18n.plugins.learn.topic.choose"))) {
			valid = false;
		}

		ui.setEnabled(find("btSave"), valid);
	}
	
//> UI HELPER METHODS
	private Object find(String componentName) {
		return Thinlet.find(dialog, componentName);
	}
}
