package net.frontlinesms.plugins.learn.ui.topic;

import net.frontlinesms.plugins.learn.data.domain.Reinforcement;
import net.frontlinesms.plugins.learn.data.repository.ReinforcementDao;
import net.frontlinesms.plugins.learn.data.repository.TopicDao;
import net.frontlinesms.ui.FrontlineUI;

public class EditReinforcementDialogHandler extends TopicItemDialogHandler<Reinforcement> {
	private static final String LAYOUT_FILE = "/ui/plugins/learn/reinforcement/edit.xml";
	private static final String TA_TEXT = "taText";
	
	private final ReinforcementDao dao;
	
//> INIT METHODS
	public EditReinforcementDialogHandler(FrontlineUI ui, ReinforcementDao dao, TopicDao topicDao, Reinforcement r) {
		super(ui, topicDao, r);
		this.dao = dao;
		
		ui.setText(find("taText"), r.getMessageText());
		
		validate();
	}
	
	public String getLayoutFile() {
		return LAYOUT_FILE;
	}
	
//> UI EVENT METHODS
	public void save() {
		topicItem.setMessageText(ui.getText(find(TA_TEXT)));
		topicItem.setTopic(getSelectedTopic());
		if(topicItem.getId() == 0)
			dao.save(topicItem);
		else dao.update(topicItem);
		
		close();
	}
	
//> UI HELPER METHODS
	boolean doValidate() {
		if(ui.getText(find(TA_TEXT)).length() == 0) {
			return false;
		}
		
		if(!isTopicValid()) return false;

		return true;
	}
}
