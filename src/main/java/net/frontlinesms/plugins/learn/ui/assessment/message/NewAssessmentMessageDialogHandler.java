package net.frontlinesms.plugins.learn.ui.assessment.message;

import net.frontlinesms.plugins.learn.data.domain.AssessmentMessage;
import net.frontlinesms.plugins.learn.data.domain.TopicItem;
import net.frontlinesms.plugins.learn.data.repository.AssessmentMessageDao;
import net.frontlinesms.ui.FrontlineUI;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

public class NewAssessmentMessageDialogHandler extends EditAssessmentMessageDialogHandler {
	public NewAssessmentMessageDialogHandler(FrontlineUI ui, EditAssessmentMessageDialogOwner dialogOwner, AssessmentMessageDao dao, TopicItem topicItem) {
		super(ui, dialogOwner, dao, new AssessmentMessage(topicItem));
		ui.setText(getDialog(), InternationalisationUtils.getI18nString("plugins.learn.assessment.message.new"));
	}
}
