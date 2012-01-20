package net.frontlinesms.plugins.learn.ui.topic;

import net.frontlinesms.plugins.learn.data.domain.Topic;
import net.frontlinesms.plugins.learn.data.repository.TopicDao;
import net.frontlinesms.plugins.learn.ui.topic.TopicChoosingDialogHandler;
import net.frontlinesms.test.spring.MockBean;
import net.frontlinesms.test.ui.ThinletEventHandlerTest;

import static net.frontlinesms.plugins.learn.LearnTestUtils.*;

public abstract class TopicChoosingDialogHandlerTest<E extends TopicChoosingDialogHandler<?>> extends ThinletEventHandlerTest<E> {
	@MockBean protected TopicDao topicDao;
	protected Topic[] mockedTopics;

//> SETUP METHODS
	@Override
	protected Object getRootComponent() {
		return h.getDialog();
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		mockedTopics = mockTopics(topicDao, "Music", "Psychology");
		initUiForTests();
	}
	
//> TEST METHODS
	public abstract void testTitle();

	public void testSettingTopicExternally() {
		// when
		h.setTopic(mockedTopics[1]);
		
		// then
		assertEquals("Psychology", $("cbTopic").getText());
	}
	
	public void testTopicNotEditableAsText() {
		assertFalse($("cbTopic").isEditable());
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
	
	public void testDialogIsModel() {
		assertTrue($().isModal());
	}
	
	public abstract void testSaveButton();
	
//> HELPER METHODS
	protected abstract void fillFieldsExceptTopic();
}
