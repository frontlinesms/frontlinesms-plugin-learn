package net.frontlinesms.plugins.learn.ui.topic;

import org.mockito.Mock;

import net.frontlinesms.plugins.learn.data.repository.TopicDao;
import net.frontlinesms.plugins.learn.ui.topic.EditTopicDialogHandler;
import net.frontlinesms.test.ui.ThinletComponent;
import net.frontlinesms.test.ui.ThinletEventHandlerTest;

import static org.mockito.Mockito.*;

import static net.frontlinesms.plugins.learn.LearnTestUtils.topicWithName;

public class NewTopicDialogHandlerTest extends ThinletEventHandlerTest<EditTopicDialogHandler> {
	@Mock private TopicDao dao;
	
//> SETUP
	@Override
	protected EditTopicDialogHandler initHandler() {
		return new EditTopicDialogHandler(ui, dao);
	}

	@Override
	protected Object getRootComponent() {
		return h.getDialog();
	}
	
//> TESTS
	public void testDialogTitleIsCorrect() throws Exception {
		assertEquals("plugins.learn.topic.new", $().getText());
	}
	
	public void testNameIsBlank() throws Exception {
		assertEquals("", $("tfName").getText());
	}
	
	public void testSaveIsDisabledWhenNameIsBlank() throws Exception {
		// given
		ThinletComponent tf = $("tfName");
		assertEquals("", tf.getText());
		ThinletComponent btSave = $("btSave");
		assertFalse(btSave.isEnabled());
		
		// when
		tf.setText("something");
		
		// then
		assertTrue(btSave.isEnabled());
		
		// when
		tf.setText("");
		
		// then
		assertFalse(btSave.isEnabled());
	}
	
	public void testSaveWorks() throws Exception {
		// when
		$("tfName").setText("Maths + English");
		$("btSave").click();
		
		// then
		verify(dao).save(topicWithName("Maths + English"));

		// and
		assertFalse($().isVisible());
	}
	
	public void testCloseWorks() throws Exception {
		// given
		assertTrue($().isVisible());
		
		// when
		$().close();
		
		// then
		assertFalse($().isVisible());
	}
	
	public void testCancelWorks() throws Exception {
		// when
		$("btCancel").click();
		
		// then
		assertFalse($().isVisible());
	}
}
