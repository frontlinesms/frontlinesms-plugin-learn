package net.frontlinesms.plugins.learn.ui.assessment;

import org.springframework.context.ApplicationContext;

import net.frontlinesms.plugins.learn.data.repository.AssessmentDao;
import net.frontlinesms.plugins.learn.data.repository.TopicDao;
import net.frontlinesms.plugins.learn.ui.topic.NewReinforcementDialogHandler;
import net.frontlinesms.test.ui.TestFrontlineUI;
import net.frontlinesms.ui.FrontlineUI;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;

public class AssessmentTabHandler implements ThinletUiEventHandler {
	private static final String XML_LAYOUT = "/ui/plugins/learn/assessment/list.xml";
	
	private final FrontlineUI ui;
	private final Object tab;
	private final TopicDao topicDao;
	private final AssessmentDao assessmentDao;
	
	public AssessmentTabHandler(FrontlineUI ui, ApplicationContext ctx) {
		this.ui = ui;
		tab = ui.loadComponentFromFile(XML_LAYOUT, this);
		topicDao = (TopicDao) ctx.getBean("topicDao");
		assessmentDao = (AssessmentDao) ctx.getBean("assessmentDao");
	}
	
	public Object getTab() {
		return tab;
	}
	
//> UI EVENT METHODS
	public void newAssessment() {
		ui.add(new NewAssessmentDialogHandler(ui, assessmentDao, topicDao).getDialog());
	}
}
