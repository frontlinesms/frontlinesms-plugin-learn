package net.frontlinesms.plugins.learn.ui.assessment.message;

import net.frontlinesms.plugins.learn.data.domain.AssessmentMessage;
import net.frontlinesms.plugins.learn.data.domain.TopicItem;
import net.frontlinesms.ui.FrontlineUI;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

public class NewAssessmentMessageDialogHandler extends EditAssessmentMessageDialogHandler {
	public NewAssessmentMessageDialogHandler(FrontlineUI ui, EditAssessmentMessageDialogOwner dialogOwner, TopicItem topicItem) {
		super(ui, dialogOwner, extracted(topicItem));
		ui.setText(getDialog(), InternationalisationUtils.getI18nString("plugins.learn.message.new"));
	}

	private static AssessmentMessage extracted(TopicItem topicItem) {
		AssessmentMessage assessmentMessage = new AssessmentMessage(topicItem);
		assessmentMessage.setStartDate(System.currentTimeMillis());
		return assessmentMessage;
	}
}
