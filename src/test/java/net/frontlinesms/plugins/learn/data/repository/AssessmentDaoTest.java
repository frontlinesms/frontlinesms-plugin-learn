package net.frontlinesms.plugins.learn.data.repository;

import org.springframework.beans.factory.annotation.Autowired;

import net.frontlinesms.junit.HibernateTestCase;
import net.frontlinesms.plugins.learn.data.domain.Assessment;

public class AssessmentDaoTest extends HibernateTestCase {
	/** dao under test */
	@Autowired AssessmentDao dao;
	
	public void testSave() {
		// given
		Assessment a = new Assessment();
		assertEquals(0, dao.count());
		
		// when
		dao.save(a);
		
		// then
		assertEquals(1, dao.count());
	}
}
