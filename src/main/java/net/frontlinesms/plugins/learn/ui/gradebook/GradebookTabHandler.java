package net.frontlinesms.plugins.learn.ui.gradebook;

import java.util.List;

import org.springframework.context.ApplicationContext;

import thinlet.Thinlet;

import net.frontlinesms.data.domain.Group;
import net.frontlinesms.data.repository.GroupDao;
import net.frontlinesms.plugins.learn.data.domain.Assessment;
import net.frontlinesms.plugins.learn.data.domain.AssessmentGradebook;
import net.frontlinesms.plugins.learn.data.domain.ClassGradebook;
import net.frontlinesms.plugins.learn.data.domain.QuestionResponse;
import net.frontlinesms.plugins.learn.data.domain.StudentGrades;
import net.frontlinesms.plugins.learn.data.domain.StudentTopicResult;
import net.frontlinesms.plugins.learn.data.domain.Topic;
import net.frontlinesms.plugins.learn.data.repository.AssessmentDao;
import net.frontlinesms.plugins.learn.data.repository.GradebookDao;
import net.frontlinesms.plugins.learn.data.repository.TopicDao;
import net.frontlinesms.ui.FrontlineUI;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.handler.contacts.GroupSelecterDialog;
import net.frontlinesms.ui.handler.contacts.SingleGroupSelecterDialogOwner;

import static net.frontlinesms.ui.i18n.InternationalisationUtils.getI18nString;
import static net.frontlinesms.ui.i18n.InternationalisationUtils.formatDate;

public class GradebookTabHandler implements ThinletUiEventHandler, SingleGroupSelecterDialogOwner {
	private static final String XML_LAYOUT = "/ui/plugins/learn/gradebook/list.xml";

	private final FrontlineUI ui;
	private final Object tab;
	private final GroupDao groupDao;
	private final GradebookDao gradebookDao;
	private final TopicDao topicDao;
	private final AssessmentDao assessmentDao;

	private Group selectedGroup;
	private Topic selectedTopic;

	public GradebookTabHandler(FrontlineUI ui, ApplicationContext ctx) {
		this.ui = ui;
		tab = ui.loadComponentFromFile(XML_LAYOUT, this);
		groupDao = (GroupDao) ctx.getBean("groupDao");
		gradebookDao = (GradebookDao) ctx.getBean("gradebookDao");
		topicDao = (TopicDao) ctx.getBean("topicDao");
		assessmentDao = (AssessmentDao) ctx.getBean("assessmentDao");
		
		// initialise topic chooser combobox
		Object cbTopic = find("cbTopic");
		for(Topic t : topicDao.list()) {
			ui.add(cbTopic, ui.createComboboxChoice(t.getName(), t));
		}
	}

	public Object getTab() {
		return tab;
	}
	
//> UI EVENT METHODS
	public void selectGroup() {
		GroupSelecterDialog dialog = new GroupSelecterDialog(ui, this, groupDao);
		dialog.init(new Group(null, null));
		dialog.show();
	}
	
	public void assessmentSelected() {
		System.out.println("GradebookTabHandler.assessmentSelected() : ENTRY");
		Object cbAssessment = find("cbAssessment");
		Assessment selectedAssessment = ui.getAttachedObject(ui.getSelectedItem(cbAssessment), Assessment.class);
		setSelectedAssessment(selectedAssessment);
		System.out.println("GradebookTabHandler.assessmentSelected() : EXIT");
	}
	
	public void topicSelected() {
		System.out.println("GradebookTabHandler.topicSelected() : ENTRY");
		Object cbTopic = find("cbTopic");
		selectedTopic = ui.getAttachedObject(ui.getSelectedItem(cbTopic), Topic.class);
		System.out.println("selected topic: " + selectedTopic.getName());
		
		refreshAssessments();
		System.out.println("GradebookTabHandler.topicSelected() : EXIT");
	}

//> GROUP SELECTION METHODS
	public void groupSelectionCompleted(Group group) {
		setGroup(group);
	}
	
//> UI HELPER METHODS
	private void refreshAssessments() {
		List<Assessment> assessments = assessmentDao.findAllByGroupAndTopic(selectedGroup, selectedTopic);
		Object cbAssessment = find("cbAssessment");
		ui.removeAll(cbAssessment);
		if(assessments.size() == 0) {
			ui.setText(cbAssessment, getI18nString("plugins.learn.assessment.none"));
			ui.setEnabled(cbAssessment, false);
			updateTable(null);
		} else {
			ui.setEnabled(cbAssessment, true);
			for(Assessment a : assessments) {
				ui.add(cbAssessment, createComboboxChoice(a));
			}

			Assessment selectedAssessment = assessments.get(0);
			ui.setText(cbAssessment, getDescription(selectedAssessment));
			setSelectedAssessment(selectedAssessment);
		}
	}
	
	private void setSelectedAssessment(Assessment assessment) {
		AssessmentGradebook gradebook = gradebookDao.getForAssessment(assessment);
		updateTable(gradebook);
	}

	private void updateTable(AssessmentGradebook gradebook) {
		// update grade table
		Object table = find("tbGrades");
		// update table headers
		Object header = Thinlet.get(table, Thinlet.HEADER);
		ui.removeAll(header);
		if(gradebook != null) {
			ui.add(header, ui.createColumn(getI18nString("plugins.learn.student"), null));
			for(int i=0; i<gradebook.getQuestionCount(); ++i) {
				ui.add(header, ui.createColumn("Q" + (1+i), null));
			}
			ui.add(header, ui.createColumn(getI18nString("plugins.learn.gradebook.score"), null));
		}
		// update table content
		ui.removeAll(table);
		if(gradebook != null) {
			// add result entries
			for(StudentTopicResult r : gradebook.getResults()) {
				ui.add(table, createRow(r));
			}
			// add average entry
			ui.add(table, createAverageRow(gradebook));
		}
	}
	
	private Object find(String componentName) {
		return Thinlet.find(tab, componentName);
	}
	
	private void setGroup(Group g) {
		selectedGroup = g;
		
		// enable and reset topic selecter
		Object cbTopic = find("cbTopic");
		ui.setEnabled(cbTopic, true);
		ui.setText(cbTopic, "plugins.learn.topic.all");
		
		// Update group selecter
		ui.setText(find("tfClass"), g.getName());
		ClassGradebook gradebook = gradebookDao.getForClass(g);
		
		// Update table
		Object table = find("tbGrades");

		// Update table header
		Object header = Thinlet.get(table, Thinlet.HEADER);
		Object[] columns = ui.getItems(header);
		for (int i = 1; i < columns.length; i++) {
			ui.remove(columns[i]);
		}
		
		if(gradebook != null) {
			for(Topic t : gradebook.getTopics()) {
				ui.add(header, ui.createColumn(t.getName(), null));
			}
			
			// Update table contents
			for(StudentGrades r : gradebook.getResults()) {
				ui.add(table, createRow(r));
			}
		}
	}

	private Object createRow(StudentGrades r) {
		Integer[] grades = r.getGrades();
		String[] cellText = new String[grades.length + 1];
		cellText[0] = r.getStudent().getName();
		for (int i = 0; i < grades.length; i++) {
			String grade = grades[i] == null
					? getI18nString("plugins.learn.gradebook.result.none")
					: grades[i] + "%";
			cellText[i + 1] = grade;
		}
		return ui.createTableRow(null, cellText);
	}
	
	private Object createAverageRow(AssessmentGradebook gradebook) {
		String[] text = new String[2 + gradebook.getQuestionCount()];
		text[0] = getI18nString("plugins.learn.gradebook.average");
		
		int[] averages = gradebook.getAverages();
		for (int i = 0; i < averages.length; i++) {
			text[i+1] = averages[i] + "%";
		}
		return ui.createTableRow(null, text);
	}

	private Object createRow(StudentTopicResult r) {
		QuestionResponse[] responses = r.getResponses();
		String[] text = new String[responses.length + 2];
		text[0] = r.getContact().getName();
		for (int i = 0; i < responses.length; i++) {
			Integer value = responses[i].getValue();
			text[i + 1] = value==null? "": ""+(char)('A' + value);
		}
		text[text.length - 1] = r.getScore() + "%";
		return ui.createTableRow(null, text);
	}
	
	private Object createComboboxChoice(Assessment a) {
		return ui.createComboboxChoice(getDescription(a), a);
	}

	private String getDescription(Assessment selectedAssessment) {
		return formatDate(selectedAssessment.getStartDate()) + " - " +
				formatDate(selectedAssessment.getEndDate());
	}
}
