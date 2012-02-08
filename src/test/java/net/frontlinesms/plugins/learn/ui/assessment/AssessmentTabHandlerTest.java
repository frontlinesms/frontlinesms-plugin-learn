package net.frontlinesms.plugins.learn.ui.assessment;

import java.util.List;

import net.frontlinesms.plugins.learn.data.domain.Assessment;
import net.frontlinesms.plugins.learn.data.domain.Topic;
import net.frontlinesms.plugins.learn.data.repository.*;
import net.frontlinesms.test.spring.MockBean;
import net.frontlinesms.test.ui.ThinletEventHandlerTest;

import static org.mockito.Mockito.*;

import static net.frontlinesms.plugins.learn.LearnTestUtils.*;

import static java.util.Arrays.asList;

public class AssessmentTabHandlerTest extends ThinletEventHandlerTest<AssessmentTabHandler> {
	@SuppressWarnings("unused")
	@MockBean private TopicDao topicDao;
	@MockBean private AssessmentDao assessmentDao;

//> SETUP METHODS
	@Override
	protected AssessmentTabHandler initHandler() {
		mockTopics(topicDao, "Cookery", "Music", "Philately");
		
		return new AssessmentTabHandler(ui, ctx);
	}
	
	@Override
	protected Object getRootComponent() {
		return h.getTab();
	}
	
//> TEST METHODS
	public void testNewAssessmentButton() {		
		// when
		$("btNewAssessment").click();
		
		// then
		assertEquals("plugins.learn.assessment.new", $("dgEditAssessment").getText());
	}
	
	public void testListByTopicTableHeaders() {
		// given we are already viewing by topic
		
		// then
		assertEquals("Column titles",
				new String[]{ "plugins.learn.group",
						"plugins.learn.assessment.start",
						"plugins.learn.assessment.end" },
				$("tblAssessments").getColumnText());
	}
	
	public void testListByTopicTableContents() {
		// given we are already viewing by topic
		List<Assessment> assessments = asList(mockAssessment("Space mutants", "20/12/11", "13/12/12"),
				mockAssessment("Biker mice", "3/4/12", "14/4/12"));
		when(assessmentDao.findAllByTopic(any(Topic.class))).thenReturn(assessments);
		
		// when
		$("cbTopic").setSelected("Music");
		
		// then
		assertEquals("First row contents.",
				new String[]{ "Space mutants", "20/12/11", "13/12/12" },
				$("tblAssessments").getRowText(0));
		assertEquals("Second row contents.",
				new String[]{ "Biker mice", "3/4/12", "14/4/12" },
				$("tblAssessments").getRowText(1));
	}
}
