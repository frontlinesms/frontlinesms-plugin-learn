package net.frontlinesms.plugins.learn.ui.topic;

import net.frontlinesms.plugins.learn.data.domain.Question;
import net.frontlinesms.plugins.learn.data.repository.QuestionDao;
import net.frontlinesms.plugins.learn.data.repository.TopicDao;
import net.frontlinesms.ui.FrontlineUI;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

public class NewQuestionDialogHandler extends EditQuestionDialogHandler {
	public NewQuestionDialogHandler(FrontlineUI ui, QuestionDao dao, TopicDao topicDao) {
		super(ui, dao, topicDao, new Question());
		ui.setText(getDialog(), InternationalisationUtils.getI18nString("plugins.learn.question.new"));
	}
}
