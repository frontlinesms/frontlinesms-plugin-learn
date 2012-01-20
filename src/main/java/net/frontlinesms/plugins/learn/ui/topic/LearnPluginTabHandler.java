package net.frontlinesms.plugins.learn.ui.topic;

import org.springframework.context.ApplicationContext;

import thinlet.Thinlet;

import net.frontlinesms.plugins.learn.ui.assessment.AssessmentTabHandler;
import net.frontlinesms.ui.FrontlineUI;
import net.frontlinesms.ui.ThinletUiEventHandler;

public class LearnPluginTabHandler implements ThinletUiEventHandler {
	private Object tab;
	
	public void init(FrontlineUI ui, ApplicationContext ctx) {
		tab = Thinlet.create("tab");
		ui.setName(tab, "plugin-learn");
		ui.setText(tab, "Learn (beta)");
		ui.setIcon(tab, "/icons/plugins/learn/logo_large.png");
		Object tabbedPane = Thinlet.create(Thinlet.TABBEDPANE);
		ui.add(tab, tabbedPane);
		ui.add(tabbedPane, new TopicTabHandler(ui, ctx).getTab());
		ui.add(tabbedPane, new AssessmentTabHandler(ui, ctx).getTab());
	}

	public Object getTab() {
		return tab;
	}
}
