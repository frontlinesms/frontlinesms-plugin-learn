package net.frontlinesms.plugins.learn.ui.topic;

import net.frontlinesms.plugins.learn.data.domain.Question;
import net.frontlinesms.plugins.learn.data.domain.Question.Type;
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

		ui.setText(find("tfQuestion"), q.getQuestionText());
		boolean isMultichoice = q.getType()==Type.MULTIPLE_CHOICE;
		ui.setSelected(find("rbType_binary"), !isMultichoice);
		ui.setSelected(find("rbType_multichoice"), isMultichoice);
		if(isMultichoice) {
			String[] a = q.getAnswers();
			ui.setText(find("tfMultichoice1"), a[0]);
			ui.setText(find("tfMultichoice2"), a[1]);
			ui.setText(find("tfMultichoice3"), a[2]);
		}
		
		validate();
		
		if(q.getId() > 0)
			ui.setText(find("taMessage"), q.getMessageText());
	}
	
	public String getLayoutFile() {
		return LAYOUT_FILE;
	}
	
//> UI EVENT METHODS
	public void save() {
		topicItem.setQuestionText(getText("tfQuestion"));
		topicItem.setType(ui.isSelected(find("rbType_binary"))? Type.BINARY: Type.MULTIPLE_CHOICE);
		topicItem.setAnswers(getText("tfMultichoice1"), getText("tfMultichoice2"), getText("tfMultichoice3"));
		topicItem.setTopic(getSelectedTopic());
		topicItem.setMessageText(generateMessageText());
		
		if(topicItem.getId() > 0)
			dao.update(topicItem);
		else dao.save(topicItem);
		
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
	
	@Override
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
