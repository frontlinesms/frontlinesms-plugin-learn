package net.frontlinesms.plugins.learn.data.repository;

import net.frontlinesms.junit.HibernateTestCase;

import net.frontlinesms.plugins.learn.data.domain.*;

import org.springframework.beans.factory.annotation.Autowired;

class TopicDaoTests extends HibernateTestCase {
	/** dao under test */
	@Autowired TopicDao topicDao;

	public void testSave() {
		// given a topic exists in memory
		Topic t = new Topic();
		assertEquals(0, t.getId());
		assertEquals(0, topicDao.count());

		// when the topic is saved
		topicDao.save(t);

		// then there are topics in the database and the ID is set
		assertTrue(t.getId() > 0);
		assertEquals(1, topicDao.count());
	}

	public void testDelete() {
		// given a topic is already saved
		Topic t = new Topic();
		assertEquals(0, topicDao.count());
		topicDao.save(t);
		assertEquals(1, topicDao.count());

		// when the topic is deleted
		topicDao.delete(t);

		// then it is no longer in the database
		assertEquals(0, topicDao.count());
	}
}
