package net.frontlinesms.plugins.learn.data.repository;

import org.springframework.beans.factory.annotation.Autowired;

import net.frontlinesms.junit.HibernateTestCase;
import net.frontlinesms.plugins.learn.data.domain.AssessmentMessage;

public class AssessmentMessageDaoTest extends HibernateTestCase {
	/** dao under test */
	@Autowired AssessmentMessageDao dao;
	
	public void testSave() {
		// given
		AssessmentMessage m = new AssessmentMessage();
		assertEquals(0, dao.count());
		
		// when
		dao.save(m);
		
		// then
		assertEquals(1, dao.count());
	}
}
