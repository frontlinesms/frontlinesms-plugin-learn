package net.frontlinesms.plugins.learn;

import net.frontlinesms.plugins.BasePluginControllerTests;
import net.frontlinesms.plugins.learn.ui.LearnPluginTabHandler;
import net.frontlinesms.ui.UiGeneratorController;

import static org.mockito.Mockito.*;

public class LearnPluginLoadingTest extends BasePluginControllerTests<LearnPluginController> {
	public Class<LearnPluginController> getControllerClass() {
		return LearnPluginController.class;
	}
	
	public void testTabAvailable() {
		Object expectedTab = new Object();
		UiGeneratorController ui = mock(UiGeneratorController.class);
		when(ui.loadComponentFromFile(anyString(), any(LearnPluginTabHandler.class))).thenReturn(expectedTab);
		Object actualTab = controller.getTab(ui);
		assertEquals(expectedTab, actualTab);
	}
}

