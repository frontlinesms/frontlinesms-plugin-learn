package net.frontlinesms.plugins.learn.ui.assessment;

import org.springframework.context.ApplicationContext;

import net.frontlinesms.data.repository.GroupDao;
import net.frontlinesms.plugins.learn.data.repository.*;
import net.frontlinesms.ui.FrontlineUI;
import net.frontlinesms.ui.ThinletUiEventHandler;

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
		tab = ui.loadComponentFromFile(XML_LAYOUT, this);
		topicDao = (TopicDao) ctx.getBean("topicDao");
		groupDao = (GroupDao) ctx.getBean("groupDao");
		topicItemDao = (TopicItemDao) ctx.getBean("topicItemDao");
		assessmentDao = (AssessmentDao) ctx.getBean("assessmentDao");
		assessmentMessageDao = (AssessmentMessageDao) ctx.getBean("assessmentMessageDao");
	}
	
	public Object getTab() {
		return tab;
	}
	
//> UI EVENT METHODS
	public void newAssessment() {
		ui.add(new NewAssessmentDialogHandler(ui, assessmentDao, assessmentMessageDao, groupDao, topicDao, topicItemDao).getDialog());
	}
}