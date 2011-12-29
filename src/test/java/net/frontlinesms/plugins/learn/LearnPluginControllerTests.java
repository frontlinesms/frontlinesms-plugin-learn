package net.frontlinesms.plugins.learn;

import net.frontlinesms.plugins.BasePluginControllerTests;
import net.frontlinesms.plugins.learn.ui.LearnPluginTabHandler;
import net.frontlinesms.ui.UiGeneratorController;

import static org.mockito.Mockito.*;

public class LearnPluginControllerTests extends BasePluginControllerTests<LearnPluginController> {
	public Class<LearnPluginController> getControllerClass() {
		return LearnPluginController.class;
	}
	
	public void testTabAvailable() {
		UiGeneratorController ui = mock(UiGeneratorController.class);
		assertNotNull(controller.getTab(ui));
	}
}

