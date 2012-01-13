package net.frontlinesms.plugins.learn;

import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import net.frontlinesms.plugins.learn.data.domain.Question;
import net.frontlinesms.plugins.learn.data.domain.Reinforcement;
import net.frontlinesms.plugins.learn.data.domain.Topic;
import net.frontlinesms.plugins.learn.data.repository.TopicDao;

import org.mockito.ArgumentMatcher;

public class LearnTestUtils {
//> TEST HELPER METHODS
	public static Topic[] mockTopics(TopicDao topicDao, String... names) {
		ArrayList<Topic> topics = new ArrayList<Topic>();
		for(String name : names) {
			Topic t = mock(Topic.class);
			when(t.getName()).thenReturn(name);
			when(topicDao.findByName(name)).thenReturn(t);
			topics.add(t);
		}
		when(topicDao.list()).thenReturn(topics);
		
		return topics.toArray(new Topic[topics.size()]);
	}
	
	public static Topic topicWithName(final String expectedName) {
		return argThat(new ArgumentMatcher<Topic>() {
			@Override
			public boolean matches(Object a) {
				Topic t = (Topic) a;
				return t.getName().equals(expectedName);
			}
		});
	}
	
	public static Reinforcement reinforcementWithTextAndTopic(final String expectedReinforementText, final String expectedTopicName) {
		return argThat(new ArgumentMatcher<Reinforcement>() {
			@Override
			public boolean matches(Object o) {
				Reinforcement r = (Reinforcement) o;
				final boolean matches = expectedReinforementText.equals(r.getMessageText()) &&
						expectedTopicName.equals(r.getTopic().getName());
				if(!matches) {
					System.out.println("LearnTestUtils.reinforcementWithTextAndTopic(...).new ArgumentMatcher() {...}.matches()");
					System.out.println("\tr.name:  " + r.getMessageText());
					System.out.println("\tr.topic: " + r.getTopic().getName());
				}
				return matches;
			}
		});
	}
	
	public static Question questionWithMessage(final String expectedMessageText) {
		return argThat(new ArgumentMatcher<Question>() {
			@Override
			public boolean matches(Object o) {
				Question q = (Question) o;
				final boolean matches = expectedMessageText.equals(q.getMessageText());
				if(!matches) {
					System.out.println("LearnTestUtils.questionWithMessage(...).new ArgumentMatcher() {...}.matches()");
					System.out.println("\tq.qText:  " + q.getQuestionText());
					System.out.println("\tq.type: " + q.getType());
					System.out.println("\tq.mText: " + q.getMessageText());
				}
				return matches;
			}
		});
	}
}
