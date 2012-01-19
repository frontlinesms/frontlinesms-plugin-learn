package net.frontlinesms.plugins.learn;

import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.*;
import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import net.frontlinesms.plugins.learn.data.domain.*;
import net.frontlinesms.plugins.learn.data.repository.*;

import org.hibernate.hql.ast.tree.ExpectedTypeAwareNode;
import org.mockito.ArgumentMatcher;

public class LearnTestUtils {
//> TEST HELPER METHODS
	public static Reinforcement mockReinforcement(TopicDao topicDao, String topicName, TopicItemDao topicItemDao, String messageText) {
		Topic t = mockTopics(topicDao, topicName)[0];
		
		Reinforcement r = mock(Reinforcement.class);
		when(r.getMessageText()).thenReturn(messageText);
		when(r.getTopic()).thenReturn(t);
		
		List<TopicItem> items = asList(new TopicItem[] {r});
		when(topicItemDao.getAllByTopic(t)).thenReturn(items);
		
		return r;
	}
	
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
				return expectedName.equals(t.getName());
			}
		});
	}
	
	public static Reinforcement reinforcementWithTextAndTopic(final String expectedReinforementText, final String expectedTopicName) {
		return reinforcementWithIdAndTextAndTopic(0, expectedReinforementText, expectedTopicName);
	}
	
	public static Reinforcement reinforcementWithIdAndTextAndTopic(final long expectedId, final String expectedReinforementText, final String expectedTopicName) {
		return argThat(new ArgumentMatcher<Reinforcement>() {
			@Override
			public boolean matches(Object o) {
				Reinforcement r = (Reinforcement) o;
				final boolean matches =
						expectedId == r.getId() &&
						expectedReinforementText.equals(r.getMessageText()) &&
						expectedTopicName.equals(r.getTopic().getName());
				if(!matches) {
					System.out.println("LearnTestUtils.reinforcementWithTextAndTopic(...).new ArgumentMatcher() {...}.matches()");
					System.out.println("\tr.id:  " + r.getId());
					System.out.println("\tr.name:  " + r.getMessageText());
					System.out.println("\tr.topic: " + r.getTopic().getName());
				}
				return matches;
			}
		});
	}
	
	public static Question questionWithIdAndTextAndTypeAndTopicAndAnswersAndMessageText(final long expectedId,
			final String expectedQuestionText, final Question.Type expectedType, final String expectedTopicName,
			final String expectedAnswerA, final String expectedAnswerB, final String expectedAnswerC,
			final String expectedMessageText) {
		return argThat(new ArgumentMatcher<Question>() {
			@Override
			public boolean matches(Object o) {
				Question q = (Question) o;
				final boolean matches = expectedId == q.getId() &&
						expectedQuestionText.equals(q.getQuestionText()) &&
						expectedTopicName.equals(q.getTopic().getName()) &&
						expectedAnswerA.equals(q.getAnswers()[0]) &&
						expectedAnswerB.equals(q.getAnswers()[1]) &&
						expectedAnswerC.equals(q.getAnswers()[2]) &&
						expectedMessageText.equals(q.getMessageText());
				if(!matches) {
					System.out.println("LearnTestUtils.questionWithIdAndTextAndTopicAndAnswersAndMessageText(...).new ArgumentMatcher() {...}.matches()");
					System.out.println("\tq.id: " + q.getId() + " vs " + expectedId);
					System.out.println("\tq.type: " + q.getType() + " vs " + expectedType);
					System.out.println("\tq.questionText: " + q.getQuestionText() + " vs " + expectedQuestionText);
					System.out.println("\tq.topicName: " + q.getTopic().getName() + " vs " + expectedTopicName);
					System.out.println("\tq.answerA: " + q.getAnswers()[0] + " vs " + expectedAnswerA);
					System.out.println("\tq.answerB: " + q.getAnswers()[1] + " vs " + expectedAnswerB);
					System.out.println("\tq.answerC: " + q.getAnswers()[2] + " vs " + expectedAnswerC);
					System.out.println("\tq.messageText: " + q.getMessageText() + " vs " + expectedMessageText);
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
