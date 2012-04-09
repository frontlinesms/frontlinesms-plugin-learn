package net.frontlinesms.plugins.learn.data.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import net.frontlinesms.junit.HibernateTestCase;
import net.frontlinesms.plugins.learn.data.domain.Assessment;
import net.frontlinesms.plugins.learn.data.domain.AssessmentMessage;
import net.frontlinesms.plugins.learn.data.domain.Frequency;
import net.frontlinesms.plugins.learn.data.domain.Reinforcement;
import net.frontlinesms.plugins.learn.data.domain.Topic;
import net.frontlinesms.plugins.learn.data.domain.TopicItem;

public class AssessmentMessageDaoTest extends HibernateTestCase {
	/** dao under test */
	@Autowired AssessmentMessageDao dao;
	@Autowired AssessmentDao assessmentDao;
	@Autowired ReinforcementDao reinforcementDao;
	@Autowired TopicDao topicDao;
	@Autowired TopicItemDao topicItemDao;

//> TEST METHODS
	public void testGet() throws Exception {
		// given
		Topic t = createTopic("random");
		TopicItem[] items = createItems(t, 3);
		Assessment a = new Assessment();
		a.setTopic(t);
		AssessmentMessage[] messages = createMessages(a);
		assessmentDao.save(a);
		
		// when
		AssessmentMessage m = dao.get(messages[1].getId());
		
		// then
		assertEquals(messages[1].getId(), m.getId());
		assertEquals(Frequency.DAILY, m.getFrequency());
		assertEquals(items[1], m.getTopicItem());
	}

	private AssessmentMessage[] createMessages(Assessment a) {
		List<AssessmentMessage> messages = new ArrayList<AssessmentMessage>();
		List<TopicItem> topicItems = topicItemDao.getAllByTopic(a.getTopic());
		for(int i=0; i<topicItems.size(); ++i) {
			AssessmentMessage m = new AssessmentMessage(topicItems.get(i));
			m.setFrequency(Frequency.values()[i]);
			messages.add(m);
		}
		a.setMessages(messages);
		return messages.toArray(new AssessmentMessage[0]);
	}

	private TopicItem[] createItems(Topic t, int count) {
		List<TopicItem> items = new ArrayList<TopicItem>();
		for (int i = 0; i < count; i++) {
			Reinforcement r = new Reinforcement();
			r.setTopic(t);
			reinforcementDao.save(r);
			items.add(r);
		}
		return items.toArray(new TopicItem[0]);
	}

	private Topic createTopic(String name) throws Exception {
		Topic t = new Topic();
		t.setName(name);
		topicDao.save(t);
		return t;
	}
}
