package net.frontlinesms.plugins.learn.ui.topic;

import net.frontlinesms.plugins.learn.data.repository.ReinforcementDao;
import net.frontlinesms.plugins.learn.ui.topic.NewReinforcementDialogHandler;
import net.frontlinesms.test.spring.MockBean;

import static org.mockito.Mockito.*;

import static net.frontlinesms.plugins.learn.LearnTestUtils.*;

public class NewReinforcementDialogHandlerTest extends NewTopicItemDialogHandlerTest<NewReinforcementDialogHandler> {
	@MockBean private ReinforcementDao dao;
	
//> SETUP METHODS
	@Override
	protected NewReinforcementDialogHandler initHandler() {
		return new NewReinforcementDialogHandler(ui, dao, topicDao);
	}
	
//> TEST METHODS
	public void testTitle() {
		assertEquals("plugins.learn.reinforcement.new", $().getText());
	}
	
	public void testTextValidation() {
		// given
		$("cbTopic").setSelected("Psychology");
		
		// when no text is entered		
		// then save is disabled
		assertFalse($("btSave").isEnabled());
		
		// when text is entered
		$("taText").setText("Negative reinforcement is the best!");

		// then save is enabled
		assertTrue($("btSave").isEnabled());
	}
	
	public void testSaveButton() {
		// when
		$("cbTopic").setSelected("Music");
		$("taText").setText("Remember: music can soothe, but it can also excite!");
		$("btSave").click();
		
		// then
		assertFalse($("dgEditReinforcement").isVisible());
		verify(dao).save(reinforcementWithTextAndTopic(
				"Remember: music can soothe, but it can also excite!",
				"Music"));
	}
	
	@Override
	protected void fillFieldsExceptTopic() {
		$("taText").setText("Negative reinforcement is the best!");
	}
}
