package net.frontlinesms.plugins.learn.ui.topic;

import net.frontlinesms.plugins.learn.data.domain.Question;
import net.frontlinesms.plugins.learn.data.domain.Question.Type;
import net.frontlinesms.plugins.learn.data.repository.QuestionDao;
import net.frontlinesms.plugins.learn.data.repository.TopicDao;
import net.frontlinesms.ui.FrontlineUI;

public class EditQuestionDialogHandler extends TopicChoosingDialogHandler<Question> {
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
		
		validate(null);
		
		if(q.getId() > 0)
			ui.setText(find("taMessage"), q.getMessageText());
	}
	
	public String getLayoutFile() {
		return LAYOUT_FILE;
	}
	
//> UI EVENT METHODS
	public void save() {
		editItem.setQuestionText(getText("tfQuestion"));
		editItem.setType(isBinary()? Type.BINARY: Type.MULTIPLE_CHOICE);
		editItem.setAnswers(getText("tfMultichoice1"), getText("tfMultichoice2"), getText("tfMultichoice3"));
		editItem.setTopic(getSelectedTopic());
		editItem.setMessageText(generateMessageText());
		editItem.setCorrectAnswer(getCorrectAnswer());
		
		if(editItem.getId() > 0)
			dao.update(editItem);
		else dao.save(editItem);
		
		close();
	}
	
//> UI HELPER METHODS
	private String generateMessageText() {
		return getText("tfQuestion") + "\n" +
				(isMultichoice()? "A) " + getText("tfMultichoice1") + "\n" +
						"B) " + getText("tfMultichoice2") + "\n" +
						"C) " + getText("tfMultichoice3") + "\n": "") +
				"Reply " + (isMultichoice()? "${id}A, ${id}B or ${id}C": "${id}T or ${id}F");
	}
	
	private String getText(String componentName) {
		return ui.getText(find(componentName));
	}
	
	private boolean isMultichoice() {
		return !isBinary();
	}
	
	private boolean isBinary() {
		return ui.isSelected(find("rbType_binary"));
	}
	
	private int getCorrectAnswer() {
		if(isBinary()) {
			return ui.isSelected(find("rbBinaryCorrect_true"))? 0: 1;
		} else {
			if(ui.isSelected(find("rbMultichoiceCorrect_1"))) return 0;
			else if(ui.isSelected(find("rbMultichoiceCorrect_2"))) return 1;
			else if(ui.isSelected(find("rbMultichoiceCorrect_3"))) return 2;
			else throw new RuntimeException("Unknown correct answer for multichoice!");
		}
	}
	
	@Override
	public boolean doValidate(Object component) {
		ui.setText(find("taMessage"), generateMessageText().replace("${id}", "1"));

		ui.setVisible(find("pnBinary"), !isMultichoice());
		ui.setVisible(find("pnMultichoice"), isMultichoice());
		
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
