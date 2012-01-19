package net.frontlinesms.plugins.learn.data.repository;

import java.util.Arrays;
import java.util.List;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.junit.BaseTestCase;
import net.frontlinesms.junit.HibernateTestCase;
import net.frontlinesms.plugins.learn.data.domain.*;

import org.springframework.beans.factory.annotation.Autowired;

public class TopicItemDaoTest extends HibernateTestCase {
	/** dao under test */
	@Autowired TopicItemDao dao;
	@Autowired TopicDao topicDao;
	@Autowired ReinforcementDao reinforcementDao;
	@Autowired QuestionDao questionDao;

	public void testGetAllByTopic() throws Exception {
		// given
		Topic[] topics = createTopics("Topic 0", "Topic 1");
		Reinforcement[] reinforcements = createReinforcements("r0", topics[0],
				"r1", topics[1]);
		Question[] questions = createQuestions("People are not wearing enough hats.", topics[0],
				"The owl and the pussycat went to sea.", topics[0],
				"Never before had a boy asked for more.", topics[1]);
		
		{
			// when
			List<TopicItem> items = dao.getAllByTopic(topics[0]);
			// then
			assertEquals(3, items.size());
			assertEquals(items, reinforcements[0], questions[0], questions[1]);
		}
		
		{
			// when
			List<TopicItem> items = dao.getAllByTopic(topics[1]);
			// then
			assertEquals(2, items.size());
			assertEquals(items, reinforcements[1], questions[2]);
		}
	}
	
	private static void assertEquals(List<TopicItem> expected, TopicItem... actual) {
		long[] expectedIds = getIds(expected);
		long[] actualIds = getIds(actual);
		
		BaseTestCase.assertEquals("Unexpected ID array", expectedIds, actualIds);
	}
	
	private static long[] getIds(List<TopicItem> items) {
		return getIds(items.toArray(new TopicItem[0]));
	}
	
	private static long[] getIds(TopicItem[] items) {
		long[] ids = new long[items.length];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = items[i].getId();
		}
		return ids;
	}
	
	private Topic[] createTopics(String... names) throws DuplicateKeyException {
		Topic[] topics = new Topic[names.length];
		for (int i = 0; i < names.length; i++) {
			String name = names[i];
			Topic t = new Topic();
			t.setName(name);
			topicDao.save(t);
			topics[i] = t;
		}
		return topics;
	}
	
	private Reinforcement[] createReinforcements(Object... attribs) {
		Reinforcement[] reinforcements = new Reinforcement[attribs.length/2];
		for (int i = 0; i < attribs.length; i+=2) {
			Reinforcement r = new Reinforcement();
			r.setMessageText((String) attribs[i]);
			r.setTopic((Topic) attribs[i + 1]);
			reinforcementDao.save(r);
			reinforcements[i/2] = r;
		}
		return reinforcements;
	}
	
	private Question[] createQuestions(Object... attribs) {
		Question[] questions = new Question[attribs.length/2];
		for (int i = 0; i < attribs.length; i+=2) {
			Question q = new Question();
			q.setMessageText((String) attribs[i]);
			q.setTopic((Topic) attribs[i + 1]);
			questionDao.save(q);
			questions[i/2] = q;
		}
		return questions;
	}
}
