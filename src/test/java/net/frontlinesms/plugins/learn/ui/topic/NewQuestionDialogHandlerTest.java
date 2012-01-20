package net.frontlinesms.plugins.learn.ui.topic;

import java.util.Collection;
import java.util.List;

import net.frontlinesms.plugins.learn.data.repository.QuestionDao;
import net.frontlinesms.plugins.learn.ui.topic.NewQuestionDialogHandler;
import net.frontlinesms.test.spring.MockBean;
import net.frontlinesms.test.ui.ThinletComponent;

import static java.util.Arrays.asList;
import static net.frontlinesms.plugins.learn.LearnTestUtils.*;
import static org.mockito.Mockito.verify;

public class NewQuestionDialogHandlerTest extends NewTopicChoosingDialogHandlerTest<NewQuestionDialogHandler> {
//> STATIC CONSTANTS
	
//> INSTANCE VARIABLES
	@MockBean private QuestionDao dao;
	
//> SETUP METHODS
	@Override
	protected NewQuestionDialogHandler initHandler() {
		return new NewQuestionDialogHandler(ui, dao, topicDao);
	}

	@Override
	protected Object getRootComponent() {
		return h.getDialog();
	}
	
	protected String[] getAllFieldNames() {
		return new String[]{"tfQuestion", "rbType", "cbTopic"};
	}

	/** Set a valid value for a field */
	protected void setValidValue(String fieldName) {
		if(fieldName.equals("tfQuestion")) {
			$("tfQuestion").setText("What is your fave colour?");
		} else if(fieldName.equals("rbType")) {
			if($("rbType_binary").isChecked()) {
				// already set to valid value
			} else {
				$("tfMultichoice1").setText("Puce");
				$("tfMultichoice2").setText("Green");
				$("tfMultichoice3").setText("All of the above");
			}
		} else if(fieldName.equals("cbTopic")) {
			$("cbTopic").setSelected("Music");
		} else TODO("Implement valid value setting for: " + fieldName);
	}
	
	private void setValidValues(Collection<String> validFieldNames) {
		setValidValues(validFieldNames.toArray(new String[0]));
	}
	
	private void setValidValues(String... validFieldNames) {
		for(String fieldName : validFieldNames) {
			setValidValue(fieldName);
		}
	}
	
//> TEST METHODS
	public void testTitle() {
		assertEquals("plugins.learn.question.new", $().getText());
	}
	
	public void testQuestionTextDefaultsEmpty() {
		assertEquals("", $("tfQuestion").getText());
	}
	
	public void testQuestionTextValidation() {
		// given
		setValidValuesExcept("tfQuestion");
		
		// then
		assertFalse($("btSave").isEnabled());
	}
	
	public void testTypeDefaultsToBinary() {
		assertTrue($("rbType_binary").isChecked());
		assertFalse($("rbType_multichoice").isChecked());
	}
	
	public void testTypeBinaryValidation() {
		// given
		setValidValuesExcept("rbType");
		
		// when
		$("rbType_binary").select();
		
		// then
		assertTrue($("btSave").isEnabled());
	}
	
	public void testMultichoiceTextfieldsOnlyEnabledWhenMultichoiceSelected() {
		// when
		$("rbType_binary").select();
		
		// then
		assertFalse($("tfMultichoice1").isEditable());
		assertFalse($("tfMultichoice2").isEditable());
		assertFalse($("tfMultichoice3").isEditable());
		
		// when
		$("rbType_multichoice").select();
		
		// then
		assertTrue($("tfMultichoice1").isEditable());
		assertTrue($("tfMultichoice2").isEditable());
		assertTrue($("tfMultichoice3").isEditable());
	}
	
	public void testTypeMultichoiceValidation_mustEnterThreeAnswers() {
		// given
		setValidValuesExcept("rbType");
		ThinletComponent a = $("tfMultichoice1");
		ThinletComponent b = $("tfMultichoice2");
		ThinletComponent c = $("tfMultichoice3");
		$("rbType_multichoice").select();
		
		// then
		assertFalse($("btSave").isEnabled());
		
		// when
		a.setText("something...");
		
		// then
		assertFalse($("btSave").isEnabled());
		
		// when
		b.setText("...another thing...");
		
		// then
		assertFalse($("btSave").isEnabled());
		
		// when
		c.setText("...and finally!");
		
		// then
		assertTrue($("btSave").isEnabled());
		
		// when
		a.setText("");
		
		// then
		assertFalse($("btSave").isEnabled());
	}
	
	public void testTypeBinaryMessageText() {
		// when
		$("tfQuestion").setText("African swallows are faster than European swallows.");
		
		// then
		assertEquals("African swallows are faster than European swallows.\n" +
				"Reply 1TRUE or 1FALSE",
				$("taMessage").getText());
	}
	
	public void testTypeMultichoiceMessageText() {
		// when
		$("tfQuestion").setText("What's your favourite pint?");
		$("rbType_multichoice").select();
		$("tfMultichoice1").setText("Bitter");
		$("tfMultichoice2").setText("Bitburger");
		$("tfMultichoice3").setText("Bovril");
		
		// then
		assertEquals("What's your favourite pint?\n" +
				"A) Bitter\n" +
				"B) Bitburger\n" +
				"C) Bovril\n" +
				"Reply 1A, 1B or 1C",
				$("taMessage").getText());
	}
	
	public void testMessageTextNotEditable() {
		assertFalse($("taMessage").isEditable());
	}
	
	public void testSaveButton() {
		// given
		$("rbType_multichoice").select();
		assertTrue($("rbType_multichoice").isChecked());
		setAllFieldsValid();
		
		// when 
		$("btSave").click();
		
		// then
		assertFalse($().isVisible());
		verify(dao).save(questionWithMessage("What is your fave colour?\n" +
				"A) Puce\n" +
				"B) Green\n" +
				"C) All of the above\n" +
				"Reply ${id}A, ${id}B or ${id}C"));
	}
}
