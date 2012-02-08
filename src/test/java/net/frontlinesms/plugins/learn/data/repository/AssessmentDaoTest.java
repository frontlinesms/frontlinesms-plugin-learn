package net.frontlinesms.plugins.learn.data.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.junit.HibernateTestCase;
import net.frontlinesms.plugins.learn.data.domain.Assessment;
import net.frontlinesms.plugins.learn.data.domain.Topic;

import static java.util.Arrays.asList;

public class AssessmentDaoTest extends HibernateTestCase {
	/** dao under test */
	@Autowired AssessmentDao dao;
	@Autowired TopicDao topicDao;
	
	public void testSave() {
		// given
		Assessment a = new Assessment();
		assertEquals(0, dao.count());
		
		// when
		dao.save(a);
		
		// then
		assertEquals(1, dao.count());
	}
	
	public void testFindAllByTopic_none() throws Exception {
		// given
		Topic t = createTopics("Random")[0];
		
		// when
		List<Assessment> a = dao.findAllByTopic(t);
		
		// then
		assertEquals(0, a.size());
	}
	
	public void testFindAllByTopic() throws Exception {
		// given
		Topic[] topics = createTopics("Zero", "One", "Two");
		Assessment a1 = createAssessment(topics[1]);
		Assessment a1a = createAssessment(topics[1]);
		Assessment a2 = createAssessment(topics[2]);
		
		{
			// when
			List<Assessment> a = dao.findAllByTopic(topics[0]);
			// then
			assertEquals(0, a.size());
		}
		
		{
			// when
			List<Assessment> a = dao.findAllByTopic(topics[1]);
			// then
			assertEquals(2, a.size());
			assertEquals(asList(a1, a1a), a);
		}
		
		{
			// when
			List<Assessment> a = dao.findAllByTopic(topics[2]);
			// then
			assertEquals(1, a.size());
			assertEquals(asList(a2), a);
		}
	}
	
	private Topic[] createTopics(String... names) throws Exception {
		Topic[] topics = new Topic[names.length];
		for (int i = 0; i < topics.length; i++) {
			topics[i] = new Topic();
			topics[i].setName(names[i]);
			topicDao.save(topics[i]);
		}
		return topics;
	}
	
	private Assessment createAssessment(Topic t) {
		Assessment a = new Assessment();
		a.setTopic(t);
		dao.save(a);
		return a;
	}
}
