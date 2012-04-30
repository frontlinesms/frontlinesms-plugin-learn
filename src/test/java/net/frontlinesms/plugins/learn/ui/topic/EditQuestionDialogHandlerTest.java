package net.frontlinesms.plugins.learn.ui.topic;

import static net.frontlinesms.plugins.learn.LearnTestUtils.*;
import static org.mockito.Mockito.*;

import net.frontlinesms.plugins.learn.data.domain.Question;
import net.frontlinesms.plugins.learn.data.domain.Question.Type;
import net.frontlinesms.plugins.learn.data.domain.Topic;
import net.frontlinesms.plugins.learn.data.repository.QuestionDao;
import net.frontlinesms.plugins.learn.ui.topic.EditQuestionDialogHandler;
import net.frontlinesms.test.spring.MockBean;

public class EditQuestionDialogHandlerTest extends TopicChoosingDialogHandlerTest<EditQuestionDialogHandler> {
	private Question q;
	@MockBean private QuestionDao dao;
	
//> SETUP METHODS
	@Override
	protected void setUp() throws Exception {
		q = new Question();
		q.setId(64);
		q.setType(Type.BINARY);
		q.setQuestionText("Twas brillig...");
		q.setMessageText("HAHA you don't know what the question is.");
		q.setAnswers("Slithy tothes were gimbling", "Slithy tothes were gyreing", "Vorpled corpuscles were muscling");
		q.setIncorrectResponse("TOTAL WRONGNESS!!!");
		
		super.setUp();
	}
	
	@Override
	protected EditQuestionDialogHandler initHandler() {
		Topic[] topics = mockTopics(topicDao, "Sense", "Nonsense");
		q.setTopic(topics[1]);
		return new EditQuestionDialogHandler(ui, dao, topicDao, q);
	}
	
//> TEST METHODS
	public void testTitle() {
		assertEquals("plugins.learn.question.edit", $().getText());
	}
	
	public void testTopicInitialisation() {
		assertEquals("Nonsense", $("cbTopic").getText());
	}
	
	public void testQuestionInitialisation() {
		assertEquals("Twas brillig...", $("tfQuestion").getText());
	}
	
	public void testMessageTextInitialisation() {
		assertEquals("HAHA you don't know what the question is.", $("taMessage").getText());
	}
	
	public void testIncorrectResponseTextInitialisation() {
		assertEquals("TOTAL WRONGNESS!!!", $("tfIncorrectResponse").getText());
	}
	
	public void testCorrectResponseInitialisation_binary_true() {
		// when
		initUiForTests();
		
		// then
		assertTrue($("rbBinaryCorrect_true").isChecked());
		assertFalse($("rbBinaryCorrect_false").isChecked());
	}
	
	public void testCorrectResponseInitialisation_binary_false() {
		// given
		q.setCorrectAnswer(1);
		
		// when
		initUiForTests();
		
		// then
		assertFalse($("rbBinaryCorrect_true").isChecked());
		assertTrue($("rbBinaryCorrect_false").isChecked());
	}
	
	public void testCorrectResponseInitialisation_multichoice_1() {
		// given
		q.setType(Type.MULTIPLE_CHOICE);
		
		// when
		initUiForTests();
		
		// then
		assertTrue($("rbMultichoiceCorrect_1").isChecked());
		assertFalse($("rbMultichoiceCorrect_2").isChecked());
		assertFalse($("rbMultichoiceCorrect_3").isChecked());
	}
	
	public void testCorrectResponseInitialisation_multichoice_2() {
		// given
		q.setType(Type.MULTIPLE_CHOICE);
		q.setCorrectAnswer(1);
		
		// when
		initUiForTests();
		
		// then
		assertTrue($("rbMultichoiceCorrect_2").isChecked());
		assertFalse($("rbMultichoiceCorrect_1").isChecked());
		assertFalse($("rbMultichoiceCorrect_3").isChecked());
	}
	
	public void testCorrectResponseInitialisation_multichoice_3() {
		// given
		q.setType(Type.MULTIPLE_CHOICE);
		q.setCorrectAnswer(2);
		
		// when
		initUiForTests();
		
		// then
		assertTrue($("rbMultichoiceCorrect_3").isChecked());
		assertFalse($("rbMultichoiceCorrect_1").isChecked());
		assertFalse($("rbMultichoiceCorrect_2").isChecked());
	}
	
	public void testQuestionTypeInitialisation() {
		assertTrue($("rbType_binary").isChecked());
		assertFalse($("rbType_multichoice").isChecked());
		assertEquals("", $("tfMultichoice1").getText());
		assertEquals("", $("tfMultichoice2").getText());
		assertEquals("", $("tfMultichoice3").getText());
		
		// when
		q.setType(Type.MULTIPLE_CHOICE);
		initUiForTests();
		// then
		assertFalse($("rbType_binary").isChecked());
		assertTrue($("rbType_multichoice").isChecked());
		assertEquals("Slithy tothes were gimbling", $("tfMultichoice1").getText());
		assertEquals("Slithy tothes were gyreing", $("tfMultichoice2").getText());
		assertEquals("Vorpled corpuscles were muscling", $("tfMultichoice3").getText());
	}
	
	@Override
	protected void fillFieldsExceptTopic() {
		// Nothing is done here because fields should be filled in the init method.
	}
	
	@Override
	public void testSaveButton() {
		// given
		$("tfQuestion").setText("How were the borogroves?");
		$("cbTopic").setSelected("Sense");
		$("rbType_multichoice").select();
		$("tfMultichoice1").setText("Mimsy");
		$("tfMultichoice2").setText("Outgrabscious");
		$("tfMultichoice3").setText("Callayed en route");
		$("tfIncorrectResponse").setText("wrong");
		
		// when
		$("btSave").click();
		
		// then
		verify(dao).update(questionWithIdAndTextAndTypeAndTopicAndAnswersAndMessageTextAndIncorrectResponse(64,
						"How were the borogroves?",
						Type.MULTIPLE_CHOICE,
						"Sense",
						"Mimsy", "Outgrabscious", "Callayed en route",
						"How were the borogroves?\nA) Mimsy\nB) Outgrabscious\nC) Callayed en route\nReply ${id}A, ${id}B or ${id}C",
						"wrong"));
	}
}
