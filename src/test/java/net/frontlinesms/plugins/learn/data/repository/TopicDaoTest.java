package net.frontlinesms.plugins.learn.data.repository;

import net.frontlinesms.junit.HibernateTestCase;
import net.frontlinesms.plugins.learn.data.domain.*;

import org.springframework.beans.factory.annotation.Autowired;

public class TopicDaoTest extends HibernateTestCase {
	/** dao under test */
	@Autowired TopicDao dao;

	public void setTopicDao(TopicDao topicDao) {
		this.dao = topicDao;
	}

	public void testSave() throws Exception {
		// given a topic exists in memory
		Topic t = new Topic();
		assertEquals(0, t.getId());
		assertEquals(0, dao.count());

		// when the topic is saved
		dao.save(t);

		// then there are topics in the database and the ID is set
		assertTrue(t.getId() > 0);
		assertEquals(1, dao.count());
	}

	public void testDelete() throws Exception {
		// given a topic is already saved
		Topic t = new Topic();
		assertEquals(0, dao.count());
		dao.save(t);
		assertEquals(1, dao.count());

		// when the topic is deleted
		dao.delete(t);

		// then it is no longer in the database
		assertEquals(0, dao.count());
	}
}
