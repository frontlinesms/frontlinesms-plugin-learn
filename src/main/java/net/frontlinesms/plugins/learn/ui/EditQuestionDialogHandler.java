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
		topicItem.setMessageText(generateMessageText());
		
		dao.save(topicItem);
		
		close();
	}
	
//> UI HELPER METHODS
	private String generateMessageText() {
		return getText("tfQuestion") + "\n" +
				(isMultichoice()? "A) " + getText("tfMultichoice1") + "\n" +
						"B) " + getText("tfMultichoice2") + "\n" +
						"C) " + getText("tfMultichoice3") + "\n": "") +
				"Reply " + (isMultichoice()? "${id}A, ${id}B or ${id}C": "1TRUE or 1FALSE");
	}
	
	private String getText(String componentName) {
		return ui.getText(find(componentName));
	}
	
	private boolean isMultichoice() {
		return ui.isSelected(find("rbType_multichoice"));
	}
	
	boolean doValidate() {
		ui.setText(find("taMessage"), generateMessageText().replace("${id}", "1"));
		
		ui.setChildrenEditable(find("pnMultichoice"), isMultichoice());
		
		if(!isTopicValid()) return false;
		
		if(getText("tfQuestion").length() == 0) {
			return false;
		}
		
		if(isMultichoice() &&
				(getText("tfMultichoice1").length() == 0 ||
						getText("tfMultichoice2").length() == 0 ||
						getText("tfMultichoice3").length() == 0)) {
			return false;
		}
		
		return true;
	}
}
