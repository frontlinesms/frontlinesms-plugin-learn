package net.frontlinesms.plugins.learn.ui.settings;

import java.util.List;

import net.frontlinesms.events.EventBus;
import net.frontlinesms.plugins.learn.LearnPluginProperties;
import net.frontlinesms.settings.FrontlineValidationMessage;
import net.frontlinesms.test.spring.MockBean;
import net.frontlinesms.test.ui.ThinletEventHandlerTest;
import net.frontlinesms.ui.settings.SettingsChangedEventNotification;

import static org.mockito.Mockito.*;

public class LearnSettingsHandlerTest extends ThinletEventHandlerTest<LearnSettingsHandler> {
	@MockBean LearnPluginProperties properties;
	@MockBean EventBus eventBus;
	
//> SETUP METHODS
	@Override
	protected LearnSettingsHandler initHandler() {
		when(properties.getResendDelay()).thenReturn(360);
		
		LearnSettingsHandler handler = new LearnSettingsHandler(ui);
		handler.init(ctx, properties);
		return handler;
	}
	
	@Override
	protected Object getRootComponent() {
		return h.getPanel();
	}

//> TEST METHODS
	public void testFieldForResendDelayIsPresent() {
		$("tfResendDelay").exists();
	}
	
	public void testFieldForResendDelayIsInitialised() {
		assertEquals("360", $("tfResendDelay").getText());
	}
	
	public void testChangingFieldForResendDelayEnablesSave() {
		// when
		$("tfResendDelay").setText("400");
		waitForUiEvents();
		
		// then
		verify(eventBus, atLeastOnce()).notifyObservers(any(SettingsChangedEventNotification.class));
	}
	
	public void testInvalidResendDelayValueGivesValidationError() {
		// given
		$("tfResendDelay").setText("");
		
		// when
		List<FrontlineValidationMessage> validation = h.validateFields();
		
		// then
		assertEquals(1, validation.size());
	}
	
	public void testSavingPropogatesChangeToResendDelay() {
		// given
		$("tfResendDelay").setText("100");
		
		// when
		h.save();
		
		// then
		verify(properties).setResendDelay(100);
		verify(properties).saveToDisk();
	}

	public void testFieldForCorrectResponseIsPresent() { TODO(); }
	public void testFieldForCorrectResponseIsInitialised() { TODO(); }
	public void testChangingFieldForCorrectResponseEnablesSave() { TODO(); }
	public void testSavingPropogatesChangeToCorrectResponse() { TODO(); }

	public void testFieldForIncorrectResponseIsPresent() { TODO(); }
	public void testFieldForIncorrectResponseIsInitialised() { TODO(); }
	public void testChangingFieldForIncorrectResponseEnablesSave() { TODO(); }
	public void testSavingPropogatesChangeToIncorrectResponse() { TODO(); }
}
