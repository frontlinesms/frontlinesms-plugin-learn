package net.frontlinesms.plugins.learn.ui.assessment.message;

import net.frontlinesms.plugins.learn.data.domain.AssessmentMessage;

public interface EditAssessmentMessageDialogOwner {
	void notifyAssessmentMessageSaved(AssessmentMessage assessmentMessage);
}
