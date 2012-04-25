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
		when(properties.getCorrectResponse()).thenReturn("CORRECT!");
		when(properties.getIncorrectResponse()).thenReturn("WRONG!");
		
		LearnSettingsHandler handler = new LearnSettingsHandler(ui);
		handler.init(properties, eventBus);
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

	public void testFieldForCorrectResponseIsPresent() {
		$("taCorrectResponse").exists();
	}
	
	public void testFieldForCorrectResponseIsInitialised() {
		assertEquals("CORRECT!", $("taCorrectResponse").getText());
	}
	
	public void testChangingFieldForCorrectResponseEnablesSave() {
		// when
		$("taCorrectResponse").setText("HAHA WRONG");
		waitForUiEvents();
		
		// then
		verify(eventBus, atLeastOnce()).notifyObservers(any(SettingsChangedEventNotification.class));
	}

	public void testSavingPropogatesChangeToCorrectResponse() {
		// given
		$("taCorrectResponse").setText("U GOT IT!");
		
		// when
		h.save();
		
		// then
		verify(properties).setCorrectResponse("U GOT IT!");
		verify(properties).saveToDisk();
	}

	public void testFieldForIncorrectResponseIsPresent() {
		$("taIncorrectResponse").exists();
	}
	
	public void testFieldForIncorrectResponseIsInitialised() {
		assertEquals("WRONG!", $("taIncorrectResponse").getText());
	}
	
	public void testChangingFieldForIncorrectResponseEnablesSave() {
		// when
		$("taIncorrectResponse").setText("RIGHT?");
		waitForUiEvents();
		
		// then
		verify(eventBus, atLeastOnce()).notifyObservers(any(SettingsChangedEventNotification.class));
	}

	public void testSavingPropogatesChangeToIncorrectResponse() {
		// given
		$("taIncorrectResponse").setText("NOPE!");
		
		// when
		h.save();
		
		// then
		verify(properties).setIncorrectResponse("NOPE!");
		verify(properties).saveToDisk();
	}
}
