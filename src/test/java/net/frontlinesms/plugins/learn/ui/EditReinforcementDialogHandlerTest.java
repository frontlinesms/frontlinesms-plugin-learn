package net.frontlinesms.plugins.learn.ui;

import net.frontlinesms.plugins.learn.data.domain.Reinforcement;
import net.frontlinesms.plugins.learn.data.domain.Topic;
import net.frontlinesms.plugins.learn.data.repository.ReinforcementDao;
import net.frontlinesms.test.spring.MockBean;

public class EditReinforcementDialogHandlerTest extends TopicItemDialogHandlerTest<EditReinforcementDialogHandler> {
	@MockBean private ReinforcementDao dao;
	
//> SETUP METHODS
	@Override
	protected EditReinforcementDialogHandler initHandler() {
		Reinforcement r = new Reinforcement();
		Topic t = new Topic();
		t.setName("Science & Nature");
		r.setTopic(t);
		r.setName("The African Pied Wagtail should not be confused with the British Pied Wagtail, which is a subspecies of the White Wagtail.");
		return new EditReinforcementDialogHandler(ui, dao, topicDao, r);
	}
	
//> TEST METHODS
	public void testTitle() {
		assertEquals("i18n.plugins.learn.reinforcement.edit", $().getText());
	}
	
	public void testTopicInitialisation() {
		assertEquals("Science & Nature", $("cbTopic").getText());
	}
	
	public void testQuestionInitialisation() {
		assertEquals("The African Pied Wagtail should not be confused with the British Pied Wagtail, which is a subspecies of the White Wagtail.",
				$("taText").getText());
	}
	
	@Override
	protected void fillFieldsExceptTopic() {
		// Nothing is done here because fields should be filled in the init method.
	}
	
	@Override
	public void testSaveButton() {
		// TODO Auto-generated method stub
		
	}
}
