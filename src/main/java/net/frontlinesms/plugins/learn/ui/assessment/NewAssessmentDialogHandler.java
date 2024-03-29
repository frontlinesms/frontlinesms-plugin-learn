package net.frontlinesms.plugins.learn.ui.assessment;

import net.frontlinesms.data.repository.GroupDao;
import net.frontlinesms.plugins.learn.data.domain.Assessment;
import net.frontlinesms.plugins.learn.data.repository.*;
import net.frontlinesms.ui.FrontlineUI;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

public class NewAssessmentDialogHandler extends EditAssessmentDialogHandler {
	public NewAssessmentDialogHandler(FrontlineUI ui, AssessmentDao assessmentDao, GroupDao groupDao, TopicDao topicDao, TopicItemDao topicItemDao) {
		super(ui, assessmentDao, groupDao, topicDao, topicItemDao, new Assessment());
		ui.setText(getDialog(), InternationalisationUtils.getI18nString("plugins.learn.assessment.new"));
	}
}
