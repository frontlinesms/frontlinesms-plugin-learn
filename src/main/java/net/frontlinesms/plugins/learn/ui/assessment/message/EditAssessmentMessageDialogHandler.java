package net.frontlinesms.plugins.learn.ui.assessment.message;

import java.text.ParseException;
import java.util.Calendar;

import net.frontlinesms.plugins.learn.data.domain.AssessmentMessage;
import net.frontlinesms.plugins.learn.data.domain.Frequency;
import net.frontlinesms.ui.FrontlineUI;
import net.frontlinesms.ui.ThinletUiEventHandler;

import static thinlet.Thinlet.find;
import static net.frontlinesms.ui.i18n.InternationalisationUtils.*;

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
		String formattedDate = formatDate(date);
		String formattedTime = formatTime(date);
		System.out.println("EditAssessmentMessageDialogHandler.setStartDate() : " + formattedDate + " " + formattedTime);
		setText("tfStartDate", formattedDate);
		setText("tfStartTime", formattedTime);
	}
	
//> UI EVENT METHODS
	public void save() {
		try {
			Calendar c = Calendar.getInstance();
			c.setTime(parseTime(parseDate(getText("tfStartDate")), getText("tfStartTime")));
			
			assessmentMessage.setStartDate(c.getTimeInMillis());
			
			close();
			
			dialogOwner.notifyAssessmentMessageSaved(assessmentMessage);
		} catch(ParseException ex) {
			ui.alert(getI18nString("message.wrong.format.date"));
		}
	}
	
	public void close() {
		ui.remove(dialog);
	}
	
//> UI HELPER METHODS
	private void setText(String componentName, Object value) {
		ui.setText(find(dialog, componentName), value.toString());
	}
	
	private String getText(String componentName) {
		return ui.getText(find(dialog, componentName));
	}
}
