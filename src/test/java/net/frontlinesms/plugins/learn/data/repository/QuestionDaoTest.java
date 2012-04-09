package net.frontlinesms.plugins.learn.data.repository;

import java.util.List;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.junit.BaseTestCase;
import net.frontlinesms.junit.HibernateTestCase;
import net.frontlinesms.plugins.learn.data.domain.*;

import org.springframework.beans.factory.annotation.Autowired;

import static net.frontlinesms.junit.BaseTestCase.*;

public class QuestionDaoTest extends HibernateTestCase {
	/** dao under test */
	@Autowired QuestionDao dao;

	public void testSave() {
		// given
		Question q = new Question();
		assertEquals(0, dao.count());
		
		// when
		dao.save(q);
		
		// then
		assertEquals(1, dao.count());
	}

	public void testDelete() {
		// given
		Question q = new Question();
		dao.save(q);
		assertEquals(1, dao.count());
		
		// when
		dao.delete(q);
		
		// then
		assertEquals(0, dao.count());
	}

	public void testUpdate() {
		// given
		Question q = new Question();
		dao.save(q);
		assertEquals(1, dao.count());
		
		// when
		dao.update(q);
		
		// then
		assertEquals(1, dao.count());
	}
}
