package net.frontlinesms.plugins.learn.data.gradebook;

import net.frontlinesms.data.domain.Contact;
import net.frontlinesms.junit.BaseTestCase;

import static org.mockito.Mockito.*;

public class StudentGradesTest extends BaseTestCase {
	public void testGetAverage() {
		final Object[] TEST_DATA = {
				0, array(0, 0, null),
				100, objectArray(100, 100),
				50, array(0, 100, null, 100),
				75, objectArray(100, 100, 0, 100),
				67, array(null, 100, 100, 67),
		};
		
		for (int i = 0; i < TEST_DATA.length; i+=2) {
			// given
			int expectedAverage = (Integer) TEST_DATA[i];
			Integer[] results = (Integer[]) TEST_DATA[i + 1];
			
			// when
			int actualAverage = new StudentGrades(mock(Contact.class), results).getAverage();
			
			// then
			assertEquals(expectedAverage, actualAverage);
		}
	}
}
