package net.frontlinesms.plugins.learn;

import org.springframework.context.ApplicationContext;

import net.frontlinesms.FrontlineSMS;
import net.frontlinesms.plugins.BasePluginController;
import net.frontlinesms.plugins.PluginControllerProperties;
import net.frontlinesms.plugins.PluginInitialisationException;
import net.frontlinesms.ui.UiGeneratorController;

@PluginControllerProperties(name="Learn (beta)",
		i18nKey="plugins.learn.name", iconPath="icons/learn/logo.png",
		hibernateConfigPath=PluginControllerProperties.NO_VALUE,
		springConfigLocation=PluginControllerProperties.NO_VALUE)
public class LearnPluginController extends BasePluginController {
	public void init(FrontlineSMS frontlineController, ApplicationContext applicationContext) throws PluginInitialisationException {}

	public void deinit() {}

	@Override
	protected Object initThinletTab(UiGeneratorController uiController) {
		return null;
	}
}
