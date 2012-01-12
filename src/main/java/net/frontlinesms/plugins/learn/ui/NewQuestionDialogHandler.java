package net.frontlinesms.plugins.learn.ui;

import net.frontlinesms.plugins.learn.data.domain.Question;
import net.frontlinesms.plugins.learn.data.repository.QuestionDao;
import net.frontlinesms.plugins.learn.data.repository.TopicDao;
import net.frontlinesms.ui.FrontlineUI;

public class NewQuestionDialogHandler extends EditQuestionDialogHandler {
	public NewQuestionDialogHandler(FrontlineUI ui, QuestionDao dao, TopicDao topicDao) {
		super(ui, dao, topicDao, new Question());
		ui.setText(getDialog(), "i18n.plugins.learn.question.new");
	}
}
