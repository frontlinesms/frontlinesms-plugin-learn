package net.frontlinesms.plugins.learn.ui.assessment.message;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;

import net.frontlinesms.plugins.learn.data.domain.AssessmentMessage;
import net.frontlinesms.plugins.learn.data.domain.Frequency;
import net.frontlinesms.ui.FrontlineUI;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.handler.core.DateSelecter;

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
		Object repeatCombobox = find(dialog, "cbRepeat");
		for(Frequency f : Frequency.values()) {
			ui.add(repeatCombobox, ui.createComboboxChoice(getI18nString(f), f));
		}
		
		ui.setText(find(dialog, "taSummary"), am.getTopicItem().getMessageText());
		ui.setEnabledRecursively(find(dialog, "pnEndDate"), assessmentMessage.getFrequency() != Frequency.ONCE);
	}

	public Object getDialog() {
		return dialog;
	}
	
//> ACCESSORS METHODS
	public void setStartDate(long date) {
		setDate("Start", date);
	}

	public void setEndDate(long date) {
		setDate("End", date);
	}
	
//> UI EVENT METHODS
	public void repeatFrequencyChanged() {
		Frequency f = ui.getAttachedObject(ui.getSelectedItem(find(dialog, "cbRepeat")), Frequency.class);
		ui.setEnabledRecursively(find(dialog, "pnEndDate"), !f.equals(Frequency.ONCE));
	}
	
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
	
	public void showDateSelecter(Object dateFieldToUpdate) throws IOException {
		new DateSelecter(ui, dateFieldToUpdate).showSelecter();
	}
	
//> UI HELPER METHODS
	private void setDate(String name, long date) {
		String formattedDate = formatDate(date);
		String formattedTime = formatTime(date);
		setText("tf" + name + "Date", formattedDate);
		setText("tf" + name + "Time", formattedTime);
	}
	
	private void setText(String componentName, Object value) {
		ui.setText(find(dialog, componentName), value.toString());
	}
	
	private String getText(String componentName) {
		return ui.getText(find(dialog, componentName));
	}
}
