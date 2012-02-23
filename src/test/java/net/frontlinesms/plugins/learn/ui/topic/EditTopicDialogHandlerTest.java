package net.frontlinesms.plugins.learn.ui.topic;

import net.frontlinesms.plugins.learn.data.domain.Topic;
import net.frontlinesms.plugins.learn.data.repository.TopicDao;
import net.frontlinesms.test.spring.MockBean;
import net.frontlinesms.test.ui.ThinletEventHandlerTest;

import static net.frontlinesms.plugins.learn.LearnTestUtils.*;

import static org.mockito.Mockito.*;

public class EditTopicDialogHandlerTest extends ThinletEventHandlerTest<EditTopicDialogHandler> {
	@MockBean private TopicDao dao;
	
	private Topic topic;

//> SETUP METHODS
	@Override
	protected EditTopicDialogHandler initHandler() {
		topic = new Topic();
		topic.setName("test-topic");
		inject(topic, "id", 77);
		return new EditTopicDialogHandler(ui, dao, topic);
	}

	@Override
	protected Object getRootComponent() {
		return h.getDialog();
	}

//> TEST METHODS
	public void testDialogTitle() {
		assertEquals("plugins.learn.topic.edit", $().getText());
	}
	
	public void testNameInitialisation() {
		assertEquals("test-topic", $("tfName").getText());
	}

	public void testCancelButton() throws Exception {
		// when
		$("btCancel").click();
		
		// then
		assertFalse($().isVisible());
		verify(dao, never()).save(any(Topic.class));
	}

	public void testDialogClose() throws Exception {
		// when
		$().close();
		
		// then
		assertFalse($().isVisible());
		verify(dao, never()).save(any(Topic.class));
	}

	public void testFilledNameDoesValidate() {
		assertTrue($("btSave").isEnabled());
	}

	public void testEmptyNameDoesntValidate() {
		// when
		$("tfName").setText("");
		
		// then
		assertFalse($("btSave").isEnabled());
	}

	public void testRefilledNameDoesValidate() {
		// given
		$("tfName").setText("");
		
		// when
		$("tfName").setText("new name");

		// then
		assertTrue($("btSave").isEnabled());
	}

	public void testSaveButton() throws Exception {
		// given
		$("tfName").setText("new name");
		
		// when
		$("btSave").click();

		// then
		assertFalse($().isVisible());
		verify(dao).save(topicWithName("new name"));
	}
}
