package net.frontlinesms.plugins.learn.ui;

import static net.frontlinesms.plugins.learn.LearnTestUtils.*;
import static org.mockito.Mockito.*;

import net.frontlinesms.plugins.learn.data.domain.Reinforcement;
import net.frontlinesms.plugins.learn.data.domain.Topic;
import net.frontlinesms.plugins.learn.data.repository.ReinforcementDao;
import net.frontlinesms.test.spring.MockBean;

public class EditReinforcementDialogHandlerTest extends TopicItemDialogHandlerTest<EditReinforcementDialogHandler> {
	@MockBean private ReinforcementDao dao;
	
//> SETUP METHODS
	@Override
	protected EditReinforcementDialogHandler initHandler() {
		Topic[] topics = mockTopics(topicDao, "Science & Nature", "Ancient History");

		Reinforcement r = new Reinforcement();
		r.setId(77);
		r.setTopic(topics[0]);
		r.setMessageText("The African Pied Wagtail should not be confused with the British Pied Wagtail, which is a subspecies of the White Wagtail.");
		return new EditReinforcementDialogHandler(ui, dao, topicDao, r);
	}
	
//> TEST METHODS
	public void testTitle() {
		assertEquals("i18n.plugins.learn.reinforcement.edit", $().getText());
	}
	
	public void testTopicInitialisation() {
		assertEquals("Science & Nature", $("cbTopic").getText());
	}
	
	public void testMessageTextInitialisation() {
		assertEquals("The African Pied Wagtail should not be confused with the British Pied Wagtail, which is a subspecies of the White Wagtail.",
				$("taText").getText());
	}
	
	@Override
	protected void fillFieldsExceptTopic() {
		// Nothing is done here because fields should be filled in the init method.
	}
	
	@Override
	public void testSaveButton() {
		// given
		$("taText").setText("This is not about birds.");
		$("cbTopic").setSelected("Ancient History");
		
		// when
		$("btSave").click();
		
		// then
		verify(dao).update(reinforcementWithIdAndTextAndTopic(77,
						"This is not about birds.",
						"Ancient History"));
	}
}
