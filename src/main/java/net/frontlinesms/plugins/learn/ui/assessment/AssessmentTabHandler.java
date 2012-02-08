package net.frontlinesms.plugins.learn.ui.assessment;

import org.springframework.context.ApplicationContext;

import thinlet.Thinlet;

import net.frontlinesms.data.repository.GroupDao;
import net.frontlinesms.plugins.learn.data.domain.Assessment;
import net.frontlinesms.plugins.learn.data.domain.Topic;
import net.frontlinesms.plugins.learn.data.repository.*;
import net.frontlinesms.ui.FrontlineUI;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

public class AssessmentTabHandler implements ThinletUiEventHandler {
	private static final String XML_LAYOUT = "/ui/plugins/learn/assessment/list.xml";
	
	private final FrontlineUI ui;
	private final Object tab;
	private final TopicDao topicDao;
	private final GroupDao groupDao;
	private final TopicItemDao topicItemDao;
	private final AssessmentDao assessmentDao;
	private final AssessmentMessageDao assessmentMessageDao;
	
	public AssessmentTabHandler(FrontlineUI ui, ApplicationContext ctx) {
		this.ui = ui;
		topicDao = (TopicDao) ctx.getBean("topicDao");
		groupDao = (GroupDao) ctx.getBean("groupDao");
		topicItemDao = (TopicItemDao) ctx.getBean("topicItemDao");
		assessmentDao = (AssessmentDao) ctx.getBean("assessmentDao");
		assessmentMessageDao = (AssessmentMessageDao) ctx.getBean("assessmentMessageDao");

		tab = ui.loadComponentFromFile(XML_LAYOUT, this);
		
		Object cbTopic = find("cbTopic");
		System.out.println("AssessmentTabHandler.AssessmentTabHandler()");
		for(Topic t : topicDao.list()) {
			System.out.println("Adding topic: " + t.getName());
			ui.add(cbTopic, ui.createComboboxChoice(t.getName(), t));
		}
	}
	
	public Object getTab() {
		return tab;
	}
	
//> UI EVENT METHODS
	public void newAssessment() {
		ui.add(new NewAssessmentDialogHandler(ui, assessmentDao, assessmentMessageDao, groupDao, topicDao, topicItemDao).getDialog());
	}
	
	public void topicChanged(Object cbTopic) {
		System.out.println("AssessmentTabHandler.topicChanged()");
		Object selectedItem = ui.getSelectedItem(cbTopic);
		
		Object assessmentsTable = find("tblAssessments");
		ui.removeAll(assessmentsTable);
		
		Topic t = ui.getAttachedObject(selectedItem, Topic.class);
		System.out.println("AssessmentTabHandler.topicChanged() : Topic: " + t);
		if(t != null) {
			System.out.println("AssessmentTabHandler.topicChanged() : assessments: " + assessmentDao.findAllByTopic(t).size());
			for(Assessment a : assessmentDao.findAllByTopic(t)) {
				ui.add(assessmentsTable, createRow(a));
			}
		}
	}

	private Object createRow(Assessment a) {
		String start = InternationalisationUtils.formatDate(a.getStartDate());
		String end = InternationalisationUtils.formatDate(a.getEndDate());
		Object row = ui.createTableRow(a, a.getGroup().getName(), start, end);
		return row;
	}

	//> UI HELPER METHODS
	private Object find(String componentName) {
		return Thinlet.find(tab, componentName);
	}
}
