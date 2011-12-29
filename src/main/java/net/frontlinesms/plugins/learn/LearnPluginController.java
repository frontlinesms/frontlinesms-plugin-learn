package net.frontlinesms.plugins.learn;

import org.springframework.context.ApplicationContext;

import net.frontlinesms.FrontlineSMS;
import net.frontlinesms.plugins.BasePluginController;
import net.frontlinesms.plugins.PluginControllerProperties;
import net.frontlinesms.plugins.PluginInitialisationException;
import net.frontlinesms.plugins.learn.data.repository.TopicDao;
import net.frontlinesms.plugins.learn.ui.LearnPluginTabHandler;
import net.frontlinesms.ui.UiGeneratorController;

@PluginControllerProperties(name="Learn (beta)",
		i18nKey="plugins.learn.name", iconPath="/icons/plugins/learn/logo_small.png",
		hibernateConfigPath="classpath:net/frontlinesms/plugins/learn/hibernate.cfg.xml",
		springConfigLocation="classpath:net/frontlinesms/plugins/learn/spring.xml")
public class LearnPluginController extends BasePluginController {
	private TopicDao topicDao;

	public void init(FrontlineSMS frontlineController, ApplicationContext applicationContext) throws PluginInitialisationException {
		this.topicDao = (TopicDao) applicationContext.getBean("topicDao");
	}

	public void deinit() {}

	@Override
	protected Object initThinletTab(UiGeneratorController ui) {
		LearnPluginTabHandler tabHandler = new LearnPluginTabHandler();
		tabHandler.setTopicDao(topicDao);
		tabHandler.init(ui);
		return tabHandler.getTab();
	}
}
