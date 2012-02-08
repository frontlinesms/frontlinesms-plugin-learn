package net.frontlinesms.plugins.learn.ui.assessment.message;

import net.frontlinesms.plugins.learn.data.domain.AssessmentMessage;
import net.frontlinesms.plugins.learn.data.domain.Frequency;
import net.frontlinesms.plugins.learn.data.repository.AssessmentMessageDao;
import net.frontlinesms.ui.FrontlineUI;
import net.frontlinesms.ui.ThinletUiEventHandler;

import static thinlet.Thinlet.find;

public class EditAssessmentMessageDialogHandler implements ThinletUiEventHandler {
	private final FrontlineUI ui;
	private final Object dialog;
	private final AssessmentMessageDao dao;
	private final AssessmentMessage assessmentMessage;

	public EditAssessmentMessageDialogHandler(FrontlineUI ui, EditAssessmentMessageDialogOwner dialogOwner, AssessmentMessageDao dao, AssessmentMessage am) {
		this.ui = ui;
		dialog = ui.loadComponentFromFile("/ui/plugins/learn/assessment/message/edit.xml", this);
		this.dao = dao;
		assessmentMessage = am;
		
		// init fields
		ui.setText(find(dialog, "taSummary"), am.getTopicItem().getMessageText());
		ui.setEnabledRecursively(find(dialog, "pnEndDate"), assessmentMessage.getFrequency() != Frequency.ONCE);
	}

	public Object getDialog() {
		return dialog;
	}
	
//> ACCESSORS METHODS
	public long getStartDate() { return 0; }
	public void setStartDate(long date) {}
	
//> UI EVENT METHODS
	public void save() {
		dao.save(assessmentMessage);
	}
	
	public void close() {
		ui.remove(dialog);
	}
}
