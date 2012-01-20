package net.frontlinesms.plugins.learn.ui.assessment;

import org.springframework.context.ApplicationContext;

import net.frontlinesms.test.ui.TestFrontlineUI;
import net.frontlinesms.ui.FrontlineUI;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;

public class AssessmentTabHandler implements ThinletUiEventHandler {
	private static final String XML_LAYOUT = "/ui/plugins/learn/assessment/list.xml";
	
	private final FrontlineUI ui;
	private final Object tab;
	
	public AssessmentTabHandler(FrontlineUI ui, ApplicationContext ctx) {
		this.ui = ui;
		tab = ui.loadComponentFromFile(XML_LAYOUT);
	}
	
	public Object getTab() {
		return tab;
	}
}
