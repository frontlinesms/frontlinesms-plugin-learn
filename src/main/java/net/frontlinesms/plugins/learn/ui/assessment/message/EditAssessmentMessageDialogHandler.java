package net.frontlinesms.plugins.learn.ui.assessment.message;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

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
		Frequency[] frequencies = Frequency.values();
		for(int i=0; i<frequencies.length; ++i) {
			Frequency f = frequencies[i];
			Object choice = ui.createComboboxChoice(getI18nString(f), f);
			ui.add(repeatCombobox, choice);
			if(f == assessmentMessage.getFrequency()) {
				ui.setSelectedIndex(repeatCombobox, i);
			}
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
	private Frequency getSelectedFrequency() {
		Frequency frequency = ui.getAttachedObject(ui.getSelectedItem(find(dialog, "cbRepeat")), Frequency.class);
		return frequency;
	}
	
	private long getEnteredStartDate() throws ParseException {
		String startTime = ui.getText(find(dialog, "tfStartTime"));
		Date startDate = parseDate(ui.getText(find(dialog, "tfStartDate")));
		return parseTime(startDate, startTime).getTime();
	}
	
	private long getEnteredEndDate() throws ParseException {
		String startTime = ui.getText(find(dialog, "tfStartTime"));
		String endDateAsText = ui.getText(find(dialog, "tfEndDate"));
		Date endDate = parseDate(endDateAsText);
		Date endDateTime = parseTime(endDate, startTime);
		return endDateTime.getTime();
	}
	
	public void validate() {
		boolean valid = true;
		
		// check start time & date
		try {
			getEnteredStartDate();
		} catch(ParseException ex) {
			valid = false;
		}

		// check frequency
		Frequency f = getSelectedFrequency();
		ui.setEnabledRecursively(find(dialog, "pnEndDate"), f != Frequency.ONCE);
		
		// check end date
		if(f != Frequency.ONCE) {
			try {
				getEnteredEndDate();
			} catch(ParseException ex) {
				valid = false;
			}
		}
		
		ui.setEnabled(find(dialog, "btSave"), valid);
	}
	
	public void save() {
		try {
			assessmentMessage.setStartDate(getEnteredStartDate());
			assessmentMessage.setFrequency(getSelectedFrequency());
			assessmentMessage.setEndDate(getEnteredEndDate());

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
		Object timeField = find(dialog, "tf" + name + "Time");
		if(timeField != null) ui.setText(timeField, formattedTime);
	}
	
	private void setText(String componentName, Object value) {
		ui.setText(find(dialog, componentName), value.toString());
	}
	
	private String getText(String componentName) {
		return ui.getText(find(dialog, componentName));
	}
}
