package net.frontlinesms.plugins.learn.ui.assessment.message;

import net.frontlinesms.plugins.learn.data.domain.AssessmentMessage;
import net.frontlinesms.ui.FrontlineUI;
import net.frontlinesms.ui.ThinletUiEventHandler;

public class EditAssessmentMessageDialogHandler implements ThinletUiEventHandler {
	private final FrontlineUI ui;
	private final Object dialog;

	public EditAssessmentMessageDialogHandler(FrontlineUI ui, AssessmentMessage am) {
		this.ui = ui;
		dialog = ui.loadComponentFromFile("/ui/plugins/learn/assessment/message/edit.xml", this);
	}

	public Object getDialog() {
		return dialog;
	}
	
//> UI EVENT METHODS
	public void close() {
		ui.remove(dialog);
	}
}
