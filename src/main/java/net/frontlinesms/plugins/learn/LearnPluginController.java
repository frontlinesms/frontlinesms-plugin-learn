package net.frontlinesms.plugins.learn;

import org.springframework.context.ApplicationContext;

import net.frontlinesms.BuildProperties;
import net.frontlinesms.FrontlineSMS;
import net.frontlinesms.plugins.BasePluginController;
import net.frontlinesms.plugins.PluginControllerProperties;
import net.frontlinesms.plugins.PluginInitialisationException;
import net.frontlinesms.plugins.learn.ui.LearnDebugMenuController;
import net.frontlinesms.plugins.learn.ui.topic.LearnPluginTabHandler;
import net.frontlinesms.ui.UiGeneratorController;

@PluginControllerProperties(name="Learn (beta)",
		i18nKey="plugins.learn.name", iconPath="/icons/plugins/learn/logo_small.png",
		hibernateConfigPath="classpath:net/frontlinesms/plugins/learn/hibernate.cfg.xml",
		springConfigLocation="classpath:net/frontlinesms/plugins/learn/spring.xml")
public class LearnPluginController extends BasePluginController {
	private ApplicationContext ctx;
	private ScheduleHandler scheduleHandler;
	private LearnIncomingMessageProcessor messageProcessor;

	public void init(FrontlineSMS frontlineController, ApplicationContext applicationContext) throws PluginInitialisationException {
		ctx = applicationContext;
		scheduleHandler = new ScheduleHandler(frontlineController, ctx);
		messageProcessor = (LearnIncomingMessageProcessor) ctx.getBean("learnIncomingMessageProcessor");
	}

	public void deinit() {
		scheduleHandler.shutdown();
		messageProcessor.shutdown();
	}

	@Override
	protected Object initThinletTab(UiGeneratorController ui) {
		scheduleHandler.start();
		messageProcessor.start();
		
		LearnPluginTabHandler tabHandler = new LearnPluginTabHandler();
		tabHandler.init(ui, ctx);
		
		if(BuildProperties.getInstance().isSnapshot()) {
			new LearnDebugMenuController().init(ui, scheduleHandler);
		}
		
		return tabHandler.getTab();
	}
}
