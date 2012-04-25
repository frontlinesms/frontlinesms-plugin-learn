package net.frontlinesms.plugins.learn.ui.topic;

import net.frontlinesms.plugins.learn.data.domain.Question;
import net.frontlinesms.plugins.learn.data.domain.Question.Type;
import net.frontlinesms.plugins.learn.data.repository.QuestionDao;
import net.frontlinesms.plugins.learn.data.repository.TopicDao;
import net.frontlinesms.ui.FrontlineUI;

public class EditQuestionDialogHandler extends TopicChoosingDialogHandler<Question> {
	private static final String LAYOUT_FILE = "/ui/plugins/learn/question/edit.xml";
	private static final String TICK = "/icons/tick.png";
	private static final String CROSS = "/icons/cross.png";
	
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
			ui.setSelected(find("rbMultichoiceCorrect_1"), q.getCorrectAnswer() == 0);
			ui.setSelected(find("rbMultichoiceCorrect_2"), q.getCorrectAnswer() == 1);
			ui.setSelected(find("rbMultichoiceCorrect_3"), q.getCorrectAnswer() == 2);
		} else {
			boolean answerTrue = q.getCorrectAnswer()==0;
			ui.setSelected(find("rbBinaryCorrect_true"), answerTrue);
			ui.setSelected(find("rbBinaryCorrect_false"), !answerTrue);
		}
		updateBinaryTicksAndCrosses();
		updateMultichoiceTicksAndCrosses();
		
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
	
	public void correctAnswerChanged() {
		if(isBinary()) updateBinaryTicksAndCrosses();
		else updateMultichoiceTicksAndCrosses();
	}

//> UI HELPER METHODS
	private void updateMultichoiceTicksAndCrosses() {
		int correctAnswerMultichoice = getCorrectMultichoiceAnswer();
		setIcon("rbMultichoiceCorrect_1", correctAnswerMultichoice==0);
		setIcon("rbMultichoiceCorrect_2", correctAnswerMultichoice==1);
		setIcon("rbMultichoiceCorrect_3", correctAnswerMultichoice==2);
	}

	private void updateBinaryTicksAndCrosses() {
		boolean correctAnswerBinary = ui.isSelected(find("rbBinaryCorrect_true"));
		setIcon("rbBinaryCorrect_true", correctAnswerBinary);
		setIcon("rbBinaryCorrect_false", !correctAnswerBinary);
	}
	
	private void setIcon(String componentName, boolean tick) {
		ui.setIcon(find(componentName), tick? TICK: CROSS);
	}
	
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
			return getCorrectMultichoiceAnswer();
		}
	}
	
	private int getCorrectMultichoiceAnswer() {
		if(ui.isSelected(find("rbMultichoiceCorrect_1"))) return 0;
		else if(ui.isSelected(find("rbMultichoiceCorrect_2"))) return 1;
		else if(ui.isSelected(find("rbMultichoiceCorrect_3"))) return 2;
		else throw new RuntimeException("Unknown correct answer for multichoice!");
	}
	
	@Override
	public boolean doValidate(Object component) {
		ui.setText(find("taMessage"), generateMessageText().replace("${id}", "1"));

		boolean multichoice = isMultichoice();
		ui.setVisible(find("lbBinaryCorrect"), !multichoice);
		ui.setVisible(find("pnBinary"), !multichoice);
		ui.setVisible(find("lbMultichoiceOptions"), multichoice);
		ui.setVisible(find("pnMultichoice"), multichoice);
		
		if(!isTopicValid()) return false;
		
		if(getText("tfQuestion").length() == 0) {
			return false;
		}
		
		if(multichoice &&
				(getText("tfMultichoice1").length() == 0 ||
						getText("tfMultichoice2").length() == 0 ||
						getText("tfMultichoice3").length() == 0)) {
			return false;
		}
		
		return true;
	}
}
