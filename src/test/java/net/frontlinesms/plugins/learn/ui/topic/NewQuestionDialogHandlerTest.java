package net.frontlinesms.plugins.learn.ui.topic;

import net.frontlinesms.plugins.learn.data.repository.QuestionDao;
import net.frontlinesms.plugins.learn.ui.topic.NewQuestionDialogHandler;
import net.frontlinesms.test.spring.MockBean;
import net.frontlinesms.test.ui.ThinletComponent;

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
	
	public void testIncorrectResponseInitialisedEmpty() {
		assertEquals("", $("tfIncorrectResponse").getText());
	}
	
	public void testTypeBinaryValidation() {
		// given
		setValidValuesExcept("rbType");
		
		// when
		$("rbType_binary").select();
		
		// then
		assertTrue($("btSave").isEnabled());
	}
	
	public void testBinaryPanelVisibleByDefault() {
		assertTrue($("pnBinary").isVisible());
	}
	
	public void testBinaryPanelHiddenWhenTypeChangedToMultichoice() {
		// when
		$("rbType_multichoice").select();
		
		// then
		assertFalse($("pnBinary").isVisible());
	}
	
	public void testBinaryPanelReshownWhenTypeChangedBackToBinary() {
		// given
		$("rbType_multichoice").select();
		assertFalse($("pnBinary").isVisible());
		
		// when
		$("rbType_binary").select();
		
		// then
		assertTrue($("pnBinary").isVisible());
	}
	
	public void testBinaryPanelContainsCorrectAnswerRadios() {
		$("pnBinary").find("rbBinaryCorrect_true").exists();
		$("pnBinary").find("rbBinaryCorrect_false").exists();
	}
	
	public void testBinaryCorrectTrueByDefault() {
		assertTrue($("rbBinaryCorrect_true").isChecked());
	}
	
	public void testMultichoicePanelHiddenByDefault() {
		assertFalse($("pnMultichoice").isVisible());
	}
	
	public void testMultichoicePanelVisibleWhenTypeChangedToMultichoice() {
		// when
		$("rbType_multichoice").select();
		
		// then
		assertTrue($("pnMultichoice").isVisible());
	}
	
	public void testMultichoicePanelRehiddenWhenTypeChangedToBinary() {
		// given
		$("rbType_multichoice").select();
		assertTrue($("pnMultichoice").isVisible());

		// when
		$("rbType_binary").select();
		
		// then
		assertFalse($("pnMultichoice").isVisible());
	}
	
	public void testMultichoicePanelContainsCorrectAnswerRadios() {
		$("pnMultichoice").find("rbMultichoiceCorrect_1").exists();
		$("pnMultichoice").find("rbMultichoiceCorrect_2").exists();
		$("pnMultichoice").find("rbMultichoiceCorrect_3").exists();
	}
	
	public void testMultichoiceCorrectAByDefault() {
		assertTrue($("rbMultichoiceCorrect_1").isChecked());
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
				"Reply 1T or 1F",
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
		$("rbMultichoiceCorrect_2").click();
		
		// when 
		$("btSave").click();
		
		// then
		assertFalse($().isVisible());
		verify(dao).save(questionWithMessageAndAnswer("What is your fave colour?\n" +
						"A) Puce\n" +
						"B) Green\n" +
						"C) All of the above\n" +
						"Reply ${id}A, ${id}B or ${id}C",
				1));
	}
}
