package net.frontlinesms.plugins.learn.ui.topic;

import static java.util.Arrays.asList;

import java.util.List;

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

	@Override
	protected void fillFieldsExceptTopic() {
		setValidValuesExcept("cbTopic");
	}
	
	/** Set valid values for fields which are irrelevant to the test being set up. */
	protected void setValidValuesExcept(String... invalidFieldNames) {
		List<String> invalid = asList(invalidFieldNames);
		for(String fieldName : getAllFieldNames()) {
			if(!invalid.contains(fieldName)) {
				setValidValue(fieldName);
			}
		}
	}
	
	protected abstract String[] getAllFieldNames();

	/** Set a valid value for a field */
	protected abstract void setValidValue(String fieldName);
	
	protected void setAllFieldsValid() {
		for(String fieldName : getAllFieldNames()) {
			setValidValue(fieldName);
		}
	}
}

 