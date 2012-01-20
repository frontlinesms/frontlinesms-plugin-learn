package net.frontlinesms.plugins.learn.ui.assessment;

import net.frontlinesms.plugins.learn.data.domain.Assessment;
import net.frontlinesms.plugins.learn.data.repository.AssessmentDao;
import net.frontlinesms.plugins.learn.data.repository.AssessmentMessageDao;
import net.frontlinesms.plugins.learn.data.repository.TopicDao;
import net.frontlinesms.plugins.learn.data.repository.TopicItemDao;
import net.frontlinesms.ui.FrontlineUI;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

public class NewAssessmentDialogHandler extends EditAssessmentDialogHandler {
	public NewAssessmentDialogHandler(FrontlineUI ui, AssessmentDao assessmentDao, TopicDao topicDao, TopicItemDao topicItemDao) {
		super(ui, assessmentDao, topicDao, topicItemDao, new Assessment());
		ui.setText(getDialog(), InternationalisationUtils.getI18nString("plugins.learn.assessment.new"));
	}
}
