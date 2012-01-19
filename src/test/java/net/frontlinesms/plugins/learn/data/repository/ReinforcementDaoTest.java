package net.frontlinesms.plugins.learn.data.repository;

import net.frontlinesms.junit.HibernateTestCase;
import net.frontlinesms.plugins.learn.data.domain.*;

import org.springframework.beans.factory.annotation.Autowired;

public class ReinforcementDaoTest extends HibernateTestCase {
	/** dao under test */
	@Autowired ReinforcementDao dao;

	public void testSave() throws Exception {
		// given a reinforcement exists in memory
		Reinforcement r = new Reinforcement();
		r.setMessageText("Test Reinforcement 1");
		assertEquals(0, r.getId());
		assertEquals(0, dao.count());

		// when the topic is saved
		dao.save(r);

		// then there are topics in the database and the ID is set
		assertTrue(r.getId() > 0);
		assertEquals(1, dao.count());
	}

	public void testDelete() throws Exception {
		// given a topic is already saved
		Reinforcement t = new Reinforcement();
		t.setMessageText("Test topic 2");
		assertEquals(0, dao.count());
		dao.save(t);
		assertEquals(1, dao.count());

		// when the topic is deleted
		dao.delete(t);

		// then it is no longer in the database
		assertEquals(0, dao.count());
	}
}
