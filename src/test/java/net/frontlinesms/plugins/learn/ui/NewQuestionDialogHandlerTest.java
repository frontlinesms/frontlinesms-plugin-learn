package net.frontlinesms.plugins.learn.ui;

import java.util.Collection;
import java.util.List;

import net.frontlinesms.plugins.learn.data.repository.QuestionDao;
import net.frontlinesms.test.spring.MockBean;
import net.frontlinesms.test.ui.ThinletComponent;

import static java.util.Arrays.asList;
import static net.frontlinesms.plugins.learn.LearnTestUtils.*;
import static org.mockito.Mockito.verify;

public class NewQuestionDialogHandlerTest extends TopicItemHandlerTest<NewQuestionDialogHandler> {
//> STATIC CONSTANTS
	private static final List<String> ALL_FIELD_NAMES = asList(new String[]{"tfQuestion", "rbType"});
	
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
	
	/** Set valid values for fields which are irrelevant to the test being set up. */
	private void setValidValuesExcept(String... invalidFieldNames) {
		List<String> invalid = asList(invalidFieldNames);
		for(String fieldName : ALL_FIELD_NAMES) {
			if(!invalid.contains(fieldName)) {
				setValidValue(fieldName);
			}
		}
	}

	/** Set a valid value for a field */
	private void setValidValue(String fieldName) {
		TODO("Implement.");
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
		assertEquals("i18n.plugins.learn.question.new", $().getText());
	}
	
	public void testQuestionTextDefaultsEmpty() {
		assertEquals("", $("tfQuestion").getText());
	}
	
	public void testQuestionTextValidation() {
		// given
		// TODO setValidValues(all other fields);
		
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
	
	public void testTypeMultichoiceValidation_mustEnterThreeAnswers() {
		// given
		ThinletComponent a = $("tfMultichoice1");
		ThinletComponent b = $("tfMultichoice2");
		ThinletComponent c = $("tfMultichoice3");
		
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
		setValidValues(ALL_FIELD_NAMES);
		
		// when 
		$("btSave").click();
		
		// then
		assertFalse($().isVisible());
		verify(dao).save(questionWithMessage("What is your fave colour?\n" +
				"A) Puce\n",
				"B) Green\n" +
				"C) All of the above\n" +
				"Reply ${id}A, ${id}B, ${id}C"));
	}
	
	@Override
	protected void fillFieldsExceptTopic() {}
}
