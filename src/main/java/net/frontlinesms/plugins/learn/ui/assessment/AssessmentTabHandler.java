package net.frontlinesms.plugins.learn.ui.assessment;

import org.springframework.context.ApplicationContext;

import thinlet.Thinlet;

import net.frontlinesms.data.domain.Group;
import net.frontlinesms.data.repository.GroupDao;
import net.frontlinesms.plugins.learn.data.domain.Assessment;
import net.frontlinesms.plugins.learn.data.domain.Topic;
import net.frontlinesms.plugins.learn.data.repository.*;
import net.frontlinesms.ui.FrontlineUI;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.handler.contacts.GroupSelecterDialog;
import net.frontlinesms.ui.handler.contacts.SingleGroupSelecterDialogOwner;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

public class AssessmentTabHandler implements ThinletUiEventHandler, SingleGroupSelecterDialogOwner {
	private static final String XML_LAYOUT = "/ui/plugins/learn/assessment/list.xml";

	private static final String[] TOPIC_COLUMN_TITLES = {
		"plugins.learn.assessment.class",
		"plugins.learn.assessment.start",
		"plugins.learn.assessment.end"
	};
	private static final String[] CLASS_COLUMN_TITLES = {
		"plugins.learn.topic",
		"plugins.learn.assessment.start",
		"plugins.learn.assessment.end"
	};
	
	private final FrontlineUI ui;
	private final Object tab;
	private final TopicDao topicDao;
	private final GroupDao groupDao;
	private final TopicItemDao topicItemDao;
	private final AssessmentDao assessmentDao;
	
	public AssessmentTabHandler(FrontlineUI ui, ApplicationContext ctx) {
		this.ui = ui;
		topicDao = (TopicDao) ctx.getBean("topicDao");
		groupDao = (GroupDao) ctx.getBean("groupDao");
		topicItemDao = (TopicItemDao) ctx.getBean("topicItemDao");
		assessmentDao = (AssessmentDao) ctx.getBean("assessmentDao");

		tab = ui.loadComponentFromFile(XML_LAYOUT, this);
		
		setViewBy("topic");
	}
	
	public Object getTab() {
		return tab;
	}
	
//> UI EVENT METHODS
	public void newAssessment() {
		ui.add(new NewAssessmentDialogHandler(ui, assessmentDao, groupDao, topicDao, topicItemDao).getDialog());
	}
	
	public void editAssessment() {
		ui.add(new EditAssessmentDialogHandler(ui, assessmentDao, groupDao, topicDao, topicItemDao, getSelectedAssessment()).getDialog());
	}
	
	public void assessmentSelectionChanged() {
		ui.setEnabled(find("btEditAssessment"), getSelectedAssessment()!=null);
	}
	
	public void topicChanged(Object cbTopic) {
		Object selectedItem = ui.getSelectedItem(cbTopic);
		Topic t = ui.getAttachedObject(selectedItem, Topic.class);
		
		Object assessmentsTable = clearAssessmentTable();
		if(t != null) {
			for(Assessment a : assessmentDao.findAllByTopic(t)) {
				ui.add(assessmentsTable, createRow(a, true));
			}
		}
	}
	
	public void setViewBy(Object cbViewBy) {
		String viewBy = ui.getName(cbViewBy).substring("cbViewBy_".length());
		setViewBy(viewBy);
	}
	
	public void selectGroup() {
		GroupSelecterDialog selecter = new GroupSelecterDialog(ui, this, groupDao);
		selecter.init(new Group(null, null));
		selecter.show();
	}

//> UI HELPER METHODS
	private Assessment getSelectedAssessment() {
		return ui.getAttachedObject(ui.getSelectedItem(find("tblAssessments")), Assessment.class);
	}
	
	private void setViewBy(String viewBy) {
		boolean topic = viewBy.equals("topic");
		ui.setVisible(find("cbTopic"), topic);
		ui.setVisible(find("pnClass"), !topic);
		if(topic) {
			Object cbTopic = find("cbTopic");
			ui.removeAll(cbTopic);
			for(Topic t : topicDao.list()) {
				ui.add(cbTopic, ui.createComboboxChoice(t.getName(), t));
			}
		}
		
		Object assessmentsTable = clearAssessmentTable();
		
		Object tableHeader = Thinlet.get(assessmentsTable, Thinlet.HEADER);
		ui.removeAll(tableHeader);
		for(String columnTitle : topic? TOPIC_COLUMN_TITLES: CLASS_COLUMN_TITLES) {
			ui.add(tableHeader, ui.createColumn(
					InternationalisationUtils.getI18nString(columnTitle),
					null));
		}
	}
	
	private Object find(String componentName) {
		return Thinlet.find(tab, componentName);
	}

	private Object createRow(Assessment a, boolean topic) {
		String start = a.getStartDate() == null? "?":
				InternationalisationUtils.formatDate(a.getStartDate());
		String end = a.getEndDate() == null? "?":
				InternationalisationUtils.formatDate(a.getEndDate());
		Object row = ui.createTableRow(a,
				topic? a.getGroup().getName(): a.getTopic().getName(),
				start, end);
		return row;
	}
	
	private Object clearAssessmentTable() {
		Object table = find("tblAssessments");
		ui.removeAll(table);
		assessmentSelectionChanged();
		return table;
	}
	
//> GROUP SELECTION METHODS
	public void groupSelectionCompleted(Group group) {
		Object assessmentsTable = clearAssessmentTable();
		if(group != null) {
			for(Assessment a : assessmentDao.findAllByGroup(group)) {
				ui.add(assessmentsTable, createRow(a, false));
			}
		}
	}
}
