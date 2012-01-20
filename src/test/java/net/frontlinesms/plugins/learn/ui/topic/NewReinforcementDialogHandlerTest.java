package net.frontlinesms.plugins.learn.ui.topic;

import net.frontlinesms.plugins.learn.data.repository.ReinforcementDao;
import net.frontlinesms.plugins.learn.ui.topic.NewReinforcementDialogHandler;
import net.frontlinesms.test.spring.MockBean;

import static org.mockito.Mockito.*;

import static net.frontlinesms.plugins.learn.LearnTestUtils.*;

public class NewReinforcementDialogHandlerTest extends NewTopicChoosingDialogHandlerTest<NewReinforcementDialogHandler> {
	@MockBean private ReinforcementDao dao;
	
//> SETUP METHODS
	@Override
	protected NewReinforcementDialogHandler initHandler() {
		return new NewReinforcementDialogHandler(ui, dao, topicDao);
	}
	
	@Override
	protected String[] getAllFieldNames() {
		return new String[]{"cbTopic", "taText"};
	}
	
	@Override
	protected void setValidValue(String fieldName) {
		if(fieldName.equals("cbTopic")) {
			$("cbTopic").setSelected("Psychology");
		} else if(fieldName.equals("taText")) {
			$("taText").setText("Negative reinforcement is the best!");
		}
	}
	
//> TEST METHODS
	public void testTitle() {
		assertEquals("plugins.learn.reinforcement.new", $().getText());
	}
	
	public void testTextValidation() {
		// given
		setValidValuesExcept("taText");
		
		// when no text is entered		
		// then save is disabled
		assertFalse($("btSave").isEnabled());
		
		// when text is entered
		setValidValue("taText");

		// then save is enabled
		assertTrue($("btSave").isEnabled());
	}
	
	public void testSaveButton() {
		// given
		setAllFieldsValid();
		
		// when
		$("btSave").click();
		
		// then
		assertFalse($("dgEditReinforcement").isVisible());
		verify(dao).save(reinforcementWithTextAndTopic(
				"Negative reinforcement is the best!",
				"Psychology"));
	}
}
