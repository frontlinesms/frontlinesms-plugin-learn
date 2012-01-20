package net.frontlinesms.plugins.learn.ui.topic;

import net.frontlinesms.plugins.learn.ui.topic.TopicChoosingDialogHandler;

public abstract class NewTopicChoosingDialogHandlerTest<E extends TopicChoosingDialogHandler<?>> extends TopicChoosingDialogHandlerTest<E> {
	public void testTopicNotInitiallySet() {
		// when topic has not been set
		// then topic is "no topic set"
		assertEquals("plugins.learn.topic.choose", $("cbTopic").getText());
	}
	
	public void testTopicValidation() {
		// given
		fillFieldsExceptTopic();
		
		// when no topic is selected		
		// then save is disabled
		assertFalse($("btSave").isEnabled());
		
		// when topic is selected and text is entered
		$("cbTopic").setSelected("Psychology");
		
		// then save is enabled
		assertTrue($("btSave").isEnabled());
	}
}

