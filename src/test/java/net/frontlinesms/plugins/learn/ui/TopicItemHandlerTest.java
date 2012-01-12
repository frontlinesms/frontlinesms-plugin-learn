package net.frontlinesms.plugins.learn.ui;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import net.frontlinesms.plugins.learn.data.domain.Topic;
import net.frontlinesms.plugins.learn.data.repository.TopicDao;
import net.frontlinesms.test.spring.MockBean;
import net.frontlinesms.test.ui.ThinletEventHandlerTest;
import net.frontlinesms.ui.ThinletUiEventHandler;

// TODO rename TopicItemDialogHandlerTest
public abstract class TopicItemHandlerTest<E extends TopicItemDialogHandler> extends ThinletEventHandlerTest<E> {
	@MockBean protected TopicDao topicDao;

//> SETUP METHODS
	@Override
	protected Object getRootComponent() {
		return h.getDialog();
	}
	
//> TEST METHODS
	public abstract void testTitle();
	
	public void testTopicNotInitiallySet() {
		// when topic has not been set
		// then topic is "no topic set"
		assertEquals("i18n.plugins.learn.topic.choose", $("cbTopic").getText());
	}

	public void testSettingTopicExternally() {
		// given
		Topic[] t = mockTopics("Fascinating Topic 1", "Fascinating Topic 2");
		
		// when
		h.setTopic(t[0]);
		
		// then
		assertEquals("Fascinating Topic 1", $("cbTopic").getText());
	}
	
	public void testTopicNotEditable() {
		assertFalse($("cbTopic").isEditable());
	}
	
	public void testTopicValidation() {
		// given
		mockTopics("Psychology", "Physiognomy");
		fillFieldsExceptTopic();
		
		// when no topic is selected		
		// then save is disabled
		assertFalse($("btSave").isEnabled());
		
		// when topic is selected and text is entered
		$("cbTopic").setSelected("Physiognomy");
		
		// then save is enabled
		assertTrue($("btSave").isEnabled());
	}
	
	public void testCloseDialog() {
		// given the dialog is visible
		assertTrue($().isVisible());
		
		// when
		$().close();
		
		// then
		assertFalse($().isVisible());
	}
	
	public void testCancelButton() {
		// given
		assertTrue($().isVisible());
		
		// when
		$("btCancel").click();
		
		// then
		assertFalse($().isVisible());
	}
	
	public abstract void testSaveButton();
	
//> HELPER METHODS
	protected abstract void fillFieldsExceptTopic();
	
	protected Topic[] mockTopics(String... names) {
		ArrayList<Topic> topics = new ArrayList<Topic>();
		for(String name : names) {
			Topic t = mock(Topic.class);
			when(t.getName()).thenReturn(name);
			when(topicDao.findByName(name)).thenReturn(t);
			topics.add(t);
		}
		when(topicDao.list()).thenReturn(topics);
		
		initUiForTests();
		
		return topics.toArray(new Topic[topics.size()]);
	}
}
