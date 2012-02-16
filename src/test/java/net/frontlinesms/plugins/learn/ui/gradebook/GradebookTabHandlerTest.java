package net.frontlinesms.plugins.learn.ui.gradebook;

import java.util.ArrayList;
import java.util.List;

import net.frontlinesms.data.domain.Contact;
import net.frontlinesms.data.domain.Group;
import net.frontlinesms.data.repository.*;
import net.frontlinesms.plugins.learn.data.domain.*;
import net.frontlinesms.plugins.learn.data.repository.*;
import net.frontlinesms.test.spring.MockBean;
import net.frontlinesms.test.ui.ThinletComponent;
import net.frontlinesms.test.ui.ThinletEventHandlerTest;

import static net.frontlinesms.plugins.learn.LearnTestUtils.*;

import static org.mockito.Mockito.*;

public class GradebookTabHandlerTest extends ThinletEventHandlerTest<GradebookTabHandler> {
	@MockBean private GroupMembershipDao groupMembershipDao;
	@MockBean private GradebookDao gradebookDao;
	@SuppressWarnings("unused")
	@MockBean private GroupDao groupDao;
	@MockBean private TopicDao topicDao;

//> SETUP METHODS
	@Override
	protected GradebookTabHandler initHandler() {
		return new GradebookTabHandler(ui, ctx);
	}

	@Override
	protected Object getRootComponent() {
		return h.getTab();
	}

//> TEST METHODS
	public void testClassGroupSelecterInitialisedEmpty() {
		assertEquals("", $("tfClass").getText());
	}
	
	public void testClassGroupTextfieldNotEditable() {
		assertFalse($("tfClass").isEditable());
	}
	
	public void testTopicSelecterInitialisedToAllTopics() {
		assertEquals("plugins.learn.topic.all", $("cbTopic").getText());
	}
	
	public void testTopicSelecterDisabledByDefault() {
		assertFalse($("cbTopic").isEnabled());
	}
	
	public void testTopicSelecterNotEditable() {
		assertFalse($("cbTopic").isEditable());
	}
	
	public void testTopicSelecterOptionsCorrespondToTopics() {
		// given
		mockTopics(topicDao, "topic1", "topic2", "topic3");
		
		// when
		initUiForTests();
		
		// then
		assertEquals("topic selecter options",
				array("plugins.learn.topic.all", "topic1", "topic2", "topic3"),
				$("cbTopic").getOptions());
	}
	
	public void testSettingGroupEnablesTopicSelecter() {
		// when
		Group mockGroup = mockEmptyGradebookAndClass();
		h.groupSelectionCompleted(mockGroup);
		
		// then
		assertTrue($("cbTopic").isEnabled());
	}
	
	public void testChangingToAnotherGroupResetsTopicSelecter() {
		// given
		mockTopics(topicDao, "topic1", "topic2");
		initUiForTests();
		
		Group[] groups = mockEmptyGradebooksAndClasses(2);
		h.groupSelectionCompleted(groups[0]);
		
		$("cbTopic").setSelected("topic1");
		
		// when
		h.groupSelectionCompleted(groups[1]);
		
		// then
		assertEquals("plugins.learn.topic.all", $("cbTopic").getText());
	}
	
	public void testSelectingATopicUpdatesTableColumnTitles() {
		// given
		Group misfits = mockTopicAndClass("topic1", "misfits", "Alfred", "Bernadette");
		initUiForTests();
		h.groupSelectionCompleted(misfits);
		
		// when
		$("cbTopic").setSelected("topic1");
		
		// then
		assertEquals("Grade table headers",
				array("plugins.learn.student", "Q1", "Q2", "Q3", "plugins.learn.gradebook.score"),
				$("tbGrades").getColumnTitles());
	}
	
	public void testSelectingATopicAddsAverageRowAtBottomOfTable() {
		// given
		Group misfits = mockTopicAndClass("topic1", "misfits", "Alfred", "Bernadette");
		initUiForTests();
		h.groupSelectionCompleted(misfits);
		
		// when
		$("cbTopic").setSelected("topic1");
		
		// then
		assertEquals("Grade table averages",
				array("plugins.learn.gradebook.average", "12%", "36%", "0%", "16%"),
				$("tbGrades").getRowText(2));
	}
	
	public void testSelectingATopicUpdatesTableContentsWithStudentsAnswers() {
		// given
		Group misfits = mockTopicAndClass("topic1", "misfits", "Alfred", "Bernadette");
		initUiForTests();
		h.groupSelectionCompleted(misfits);
		
		// when
		$("cbTopic").setSelected("topic1");
		
		// then
		assertEquals("Alfred's results",
				array("Alfred", "", "1", "2", "2%"),
				$("tbGrades").getRowText(0));
		assertEquals("Bernadette's results",
				array("Bernadette", "4", "5", "6", "6%"),
				$("tbGrades").getRowText(1));
	}
	
	public void testGradeTableInitialisedEmpty() {
		assertEquals(0, $("tbGrades").getRowCount());
	}
	
	public void testGradeTableColumnsInitialised() {
		assertEquals("Grade table column names",
				array("plugins.learn.gradebook.student"),
				$("tbGrades").getColumnTitles());
	}
	
	public void testSelectClassButtonShowsGroupSelecterDialog() {
		// when
		$("btSelectClass").click();
		
		// then
		assertTrue($("dgGroupSelecter").isVisible());
	}
	
	public void testSelectingClassUpdatesClassTextfield() {
		// given
		Group g = mockEmptyGradebookAndClass();
		
		// when
		h.groupSelectionCompleted(g);
		
		// then
		assertEquals("empty class", $("tfClass").getText());
	}
	
	public void testSelectingClassUpdatesGradeTableColumnNames() {
		// given
		Group g = mockGradeBookAndClass();
		
		// when
		h.groupSelectionCompleted(g);
		
		// then
		assertEquals("Grade table column names",
				array("plugins.learn.gradebook.student", "Health & Safety", "Maths + English", "Electronics"),
				$("tbGrades").getColumnTitles());
	}
	
	public void testSelectingClassUpdatesGradeTableStudents() {
		// given
		Group g = mockGradeBookAndClass();
		
		// when
		h.groupSelectionCompleted(g);
		
		// then
		assertEquals("Student column values",
				array("Angela", "Beatrix", "Clare", "Dave", "Edwina"),
				$("tbGrades").getColumnText(0));
	}
	
	public void testSelectingClassUpdatesGradeTableResults() {
		// given
		Group g = mockGradeBookAndClass();
		
		// when
		h.groupSelectionCompleted(g);
		
		// then
		assertEquals("Results (Health & Safety)",
				array("100%", "47%", "82%", "100%", "4%"),
				$("tbGrades").getColumnText(1));
		assertEquals("Results (Maths + English)",
				array("plugins.learn.gradebook.result.none",
						"plugins.learn.gradebook.result.none",
						"plugins.learn.gradebook.result.none",
						"plugins.learn.gradebook.result.none",
						"plugins.learn.gradebook.result.none"),
				$("tbGrades").getColumnText(2));
		assertEquals("Results (Electronics)",
				array("17%", "52%", "76%", "100%", "100%"),
				$("tbGrades").getColumnText(3));
	}
	
	private Group mockEmptyGradebookAndClass() {
		return mockEmptyGradebookAndClass("empty class");
	}
	
	private Group mockEmptyGradebookAndClass(String name) {
		Group g = mockGroup(name);
		ClassGradebook gb = mock(ClassGradebook.class);
		when(gradebookDao.getForClass(g)).thenReturn(gb);
		return g;
	}
	
	private Group[] mockEmptyGradebooksAndClasses(int count) {
		Group[] groups = new Group[count];
		for (int i = 0; i < count; i++) {
			groups[i] = mockEmptyGradebookAndClass("group-" + i);
		}
		return groups;
	}

	private Group mockGradeBookAndClass() {
		Group g = mockGroup("rd");
		Contact[] students = mockContacts("Angela", "Beatrix", "Clare", "Dave", "Edwina");
		when(groupMembershipDao.getActiveMembers(g)).thenReturn(asList(students));
		
		ClassGradebook gb = mock(ClassGradebook.class);
		Topic[] mockTopics = mockTopics("Health & Safety", "Maths + English", "Electronics");
		when(gb.getTopics()).thenReturn(asList(mockTopics));
		List<StudentGrades> results = asList(
				mockStudentClassResults(students[0], 100, null, 17),
				mockStudentClassResults(students[1], 47, null, 52),
				mockStudentClassResults(students[2], 82, null, 76),
				mockStudentClassResults(students[3], 100, null, 100),
				mockStudentClassResults(students[4], 4, null, 100));
		when(gb.getResults()).thenReturn(results);
		when(gradebookDao.getForClass(g)).thenReturn(gb);
		
		return g;
	}
	
	private Group mockTopicAndClass(String topicName, String groupName, String... studentNames) {
		Topic t = mockTopics(topicDao, topicName)[0];
		
		Group g = mockGroup(groupName);
		
		// create students
		Contact[] students = mockContacts(studentNames);
		when(groupMembershipDao.getActiveMembers(g)).thenReturn(asList(students));
		
		// create gradebook
		ClassTopicGradebook gb = mock(ClassTopicGradebook.class);
		when(gb.getQuestionCount()).thenReturn(3);
		StudentTopicResult[] results = mockStudentTopicResults(students, 3);
		when(gb.getAverages()).thenReturn(new int[]{12, 36, 0, 16});
		when(gb.getResults()).thenReturn(results);
		
		when(gradebookDao.getForClassAndTopic(g, t)).thenReturn(gb);
		
		return g;
	}

	private StudentTopicResult[] mockStudentTopicResults(Contact[] students,
			int questionCount) {
		int counter = -1;
		ArrayList<StudentTopicResult> results = new ArrayList<StudentTopicResult>();
		for(Contact s : students) {
			StudentTopicResult r = mock(StudentTopicResult.class);
			when(r.getContact()).thenReturn(s);
			IndividualQuestionResponse[] individualResponses = new IndividualQuestionResponse[questionCount];
			for (int i = 0; i < individualResponses.length; i++) {
				IndividualQuestionResponse iqr = mock(IndividualQuestionResponse.class);
				Integer value = counter < 0? null: counter;
				when(iqr.isCorrect()).thenReturn(value != null && (counter % 1) == 0);
				when(iqr.getValue()).thenReturn(value);
				individualResponses[i] = iqr;
				++counter;
			}
			when(r.getResponses()).thenReturn(individualResponses);
			when(r.getScore()).thenReturn(counter++);
			results.add(r);
		}
		return results.toArray(new StudentTopicResult[results.size()]);
	}
}
