package net.frontlinesms.plugins.learn.data.repository;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.junit.HibernateTestCase;
import net.frontlinesms.plugins.learn.data.domain.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

public class TopicDaoTest extends HibernateTestCase {
	/** dao under test */
	@Autowired TopicDao dao;

	public void setTopicDao(TopicDao topicDao) {
		this.dao = topicDao;
	}

	public void testSave() throws Exception {
		// given a topic exists in memory
		Topic t = new Topic();
		t.setName("Test Topic 1");
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
		t.setName("Test topic 2");
		assertEquals(0, dao.count());
		dao.save(t);
		assertEquals(1, dao.count());

		// when the topic is deleted
		dao.delete(t);

		// then it is no longer in the database
		assertEquals(0, dao.count());
	}
	
	public void testTopicNameNotNull() throws Exception {
		// given
		Topic t = new Topic();
		
		try {
			// when
			dao.save(t);
			fail("Exception should be thrown.");
		} catch(DataIntegrityViolationException ex) {
			// then this exception was expected, so ignore it
		}
	}
	
	public void testTopicNameUnique() throws Exception {
		// given
		Topic t1 = new Topic();
		Topic t2 = new Topic();
		t1.setName("the only name");
		t2.setName("the only name");
		
		// when
		dao.save(t1);
		// then everything is ok
		
		try {
			// when
			dao.save(t2);
			fail("Exception should have been thrown.");
		} catch(DuplicateKeyException ex) {
			// then this exception was expected, so ignore it
		}
	}
	
	public void testFindByName() throws Exception {
		// given that there are a few topics saved
		createTopics("Arithmetic", "Biology", "Cartography");
		
		// then
		assertNull(dao.findByName("Dermetology"));
		assertEquals("Arithmetic", dao.findByName("Arithmetic").getName());
		assertEquals("Biology", dao.findByName("Biology").getName());
		assertEquals("Cartography", dao.findByName("Cartography").getName());
	}
	
	private void createTopics(String... names) throws Exception {
		for(String name : names) {
			Topic t = new Topic();
			t.setName(name);
			dao.save(t);
		}
	}
}
