package net.frontlinesms.plugins.learn.ui;

import net.frontlinesms.plugins.learn.data.domain.Reinforcement;
import net.frontlinesms.plugins.learn.data.repository.ReinforcementDao;
import net.frontlinesms.plugins.learn.data.repository.TopicDao;
import net.frontlinesms.ui.FrontlineUI;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

public class EditReinforcementDialogHandler extends TopicItemDialogHandler<Reinforcement> {
	private static final String LAYOUT_FILE = "/ui/plugins/learn/reinforcement/edit.xml";
	private static final String TA_TEXT = "taText";
	
	private final ReinforcementDao dao;
	
//> INIT METHODS
	public EditReinforcementDialogHandler(FrontlineUI ui, ReinforcementDao dao, TopicDao topicDao, Reinforcement r) {
		super(ui, topicDao, r);
		this.dao = dao;
		validate();
	}
	
	public String getLayoutFile() {
		return LAYOUT_FILE;
	}
	
//> UI EVENT METHODS
	public void save() {
		topicItem.setName(ui.getText(find(TA_TEXT)));
		topicItem.setTopic(topicDao.findByName(ui.getText(find(CB_TOPIC))));
		dao.save(topicItem);
		
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
