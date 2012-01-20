package net.frontlinesms.plugins.learn.ui.assessment.message;

import net.frontlinesms.plugins.learn.data.domain.AssessmentMessage;
import net.frontlinesms.plugins.learn.data.domain.TopicItem;
import net.frontlinesms.ui.FrontlineUI;

public class NewAssessmentMessageDialogHandler extends EditAssessmentMessageDialogHandler {
	public NewAssessmentMessageDialogHandler(FrontlineUI ui, TopicItem topicItem) {
		super(ui, new AssessmentMessage());
	}
}
