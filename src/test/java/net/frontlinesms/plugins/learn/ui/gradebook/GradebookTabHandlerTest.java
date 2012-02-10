package net.frontlinesms.plugins.learn.ui.gradebook;

import java.util.List;

import net.frontlinesms.data.domain.Contact;
import net.frontlinesms.data.domain.Group;
import net.frontlinesms.data.repository.*;
import net.frontlinesms.plugins.learn.data.domain.*;
import net.frontlinesms.plugins.learn.data.repository.*;
import net.frontlinesms.test.spring.MockBean;
import net.frontlinesms.test.ui.ThinletEventHandlerTest;

import static net.frontlinesms.plugins.learn.LearnTestUtils.*;

import static org.mockito.Mockito.*;

public class GradebookTabHandlerTest extends ThinletEventHandlerTest<GradebookTabHandler> {
	@MockBean private GroupMembershipDao groupMembershipDao;
	@MockBean private GradebookDao gradebookDao;
	@SuppressWarnings("unused")
	@MockBean private GroupDao groupDao;

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
		Group g = mockGroup("empty class");
		Gradebook gb = mock(Gradebook.class);
		when(gradebookDao.getForClass(g)).thenReturn(gb);
		return g;
	}

	private Group mockGradeBookAndClass() {
		Group g = mockGroup("rd");
		Contact[] students = mockContacts("Angela", "Beatrix", "Clare", "Dave", "Edwina");
		when(groupMembershipDao.getActiveMembers(g)).thenReturn(asList(students));
		
		Gradebook gb = mock(Gradebook.class);
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
}
