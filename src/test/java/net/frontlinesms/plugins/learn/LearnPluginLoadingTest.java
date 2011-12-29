package net.frontlinesms.plugins.learn;

import net.frontlinesms.plugins.BasePluginControllerTests;
import net.frontlinesms.ui.UiGeneratorController;

import static org.mockito.Mockito.*;

public class LearnPluginLoadingTest extends BasePluginControllerTests<LearnPluginController> {
	public Class<LearnPluginController> getControllerClass() {
		return LearnPluginController.class;
	}
	
	public void testTabHandlerAvailable() {
		Object tab = controller.getTab(mock(UiGeneratorController.class));
		assertNotNull(tab);
	}
}

