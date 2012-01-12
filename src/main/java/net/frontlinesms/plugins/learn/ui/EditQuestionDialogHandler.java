package net.frontlinesms.plugins.learn.ui;

import net.frontlinesms.plugins.learn.data.domain.Question;
import net.frontlinesms.plugins.learn.data.repository.QuestionDao;
import net.frontlinesms.plugins.learn.data.repository.TopicDao;
import net.frontlinesms.ui.FrontlineUI;

public class EditQuestionDialogHandler extends TopicItemDialogHandler<Question> {
	private static final String LAYOUT_FILE = "/ui/plugins/learn/question/edit.xml";
	
	private final QuestionDao dao;
	
//> INIT METHODS
	public EditQuestionDialogHandler(FrontlineUI ui, QuestionDao dao, TopicDao topicDao, Question q) {
		super(ui, topicDao, q);
		this.dao = dao;
		validate();
	}
	
	public String getLayoutFile() {
		return LAYOUT_FILE;
	}
	
//> UI EVENT METHODS
	public void save() {
		// TODO update item!
		
		dao.save(topicItem);
		
		close();
	}
	
//> UI HELPER METHODS
	boolean doValidate() {
		if(ui.getText(find("tfQuestion")).length() == 0) {
			return false;
		}
		
		return true;
	}
}
