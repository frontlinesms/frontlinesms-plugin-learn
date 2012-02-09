package net.frontlinesms.plugins.learn.ui.assessment.message;

import java.util.Calendar;

import net.frontlinesms.plugins.learn.data.domain.AssessmentMessage;
import net.frontlinesms.plugins.learn.data.domain.Frequency;
import net.frontlinesms.ui.FrontlineUI;
import net.frontlinesms.ui.ThinletUiEventHandler;

import static thinlet.Thinlet.find;

public class EditAssessmentMessageDialogHandler implements ThinletUiEventHandler {
	private final FrontlineUI ui;
	private final Object dialog;
	private final EditAssessmentMessageDialogOwner dialogOwner;
	private final AssessmentMessage assessmentMessage;

	public EditAssessmentMessageDialogHandler(FrontlineUI ui, EditAssessmentMessageDialogOwner dialogOwner, AssessmentMessage am) {
		this.ui = ui;
		dialog = ui.loadComponentFromFile("/ui/plugins/learn/assessment/message/edit.xml", this);
		assessmentMessage = am;
		this.dialogOwner = dialogOwner;
		
		// init fields
		ui.setText(find(dialog, "taSummary"), am.getTopicItem().getMessageText());
		ui.setEnabledRecursively(find(dialog, "pnEndDate"), assessmentMessage.getFrequency() != Frequency.ONCE);
	}

	public Object getDialog() {
		return dialog;
	}
	
//> ACCESSORS METHODS
	public void setStartDate(long date) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(date);
		setText("tfStartYear", c.get(Calendar.YEAR));
		setText("tfStartMonth", c.get(Calendar.MONTH));
		setText("tfStartDayOfMonth", c.get(Calendar.DAY_OF_MONTH));
		setText("tfStartHour", c.get(Calendar.HOUR_OF_DAY));
		setText("tfStartMinute", c.get(Calendar.MINUTE));
	}
	
//> UI EVENT METHODS
	public void save() {
		Calendar c = Calendar.getInstance();
		c.set(getTextAsInteger("tfStartYear"),
				getTextAsInteger("tfStartMonth"),
				getTextAsInteger("tfStartDayOfMonth"),
				getTextAsInteger("tfStartHour"),
				getTextAsInteger("tfStartMinute"), 0);
		c.set(Calendar.MILLISECOND, 0);
		assessmentMessage.setStartDate(c.getTimeInMillis());
		
		close();
		
		dialogOwner.notifyAssessmentMessageSaved(assessmentMessage);
	}
	
	public void close() {
		ui.remove(dialog);
	}
	
//> UI HELPER METHODS
	private void setText(String componentName, Object value) {
		ui.setText(find(dialog, componentName), value.toString());
	}
	
	private int getTextAsInteger(String componentName) {
		return Integer.parseInt(ui.getText(find(dialog, componentName)));
	}
}
