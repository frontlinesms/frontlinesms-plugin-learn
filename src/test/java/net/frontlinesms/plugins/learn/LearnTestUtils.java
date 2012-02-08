package net.frontlinesms.plugins.learn;

import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.*;
import static java.util.Arrays.asList;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import net.frontlinesms.plugins.learn.data.domain.*;
import net.frontlinesms.plugins.learn.data.repository.*;

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
	
	public static AssessmentMessage assessmentMessageWithTopicItemAndStartDateAndRepeat(
			final TopicItem expectedTopicItem, final long expectedStartDate,
			final Frequency expectedFrequency) {
		return argThat(new ArgumentMatcher<AssessmentMessage>() {
			@Override
			public boolean matches(Object o) {
				AssessmentMessage m = (AssessmentMessage) o;
				
				boolean matches = expectedTopicItem == m.getTopicItem() &&
						expectedStartDate == m.getStartDate() &&
						null == m.getEndDate() &&
						expectedFrequency == m.getFrequency();
				
				if(!matches) {
					System.out.println("LearnTestUtils.assessmentMessageWithTopicItemAndStartDateAndRepeat(...).new ArgumentMatcher() {...}.matches()");
					println(o, "topicItem", expectedTopicItem,
							"startDate", expectedStartDate,
							"endDate", null,
							"frequency", expectedFrequency);
				}
				
				return matches;
			}
		});
	}
	
	public static Assessment assessmentWithTopic(final String expectedTopicName) {
		return argThat(new ArgumentMatcher<Assessment>() {
			@Override
			public boolean matches(Object o) {
				Assessment a = (Assessment) o;
				return expectedTopicName.equals(a.getTopic().getName());
			}
		});
	}
	
	public static Assessment assessmentWithTopicAndGroup(final String expectedTopicName, final String expectedGroupName) {
		return argThat(new ArgumentMatcher<Assessment>() {
			@Override
			public boolean matches(Object o) {
				Assessment a = (Assessment) o;
				return expectedTopicName.equals(a.getTopic().getName()) &&
						expectedGroupName.equals(a.getGroup().getName());
			}
		});
	}
	
	public static Assessment assessmentWithTopicAndGroupAndMessageCount(final String expectedTopicName, final String expectedGroupName, final int expectedMessageCount) {
		return argThat(new ArgumentMatcher<Assessment>() {
			@Override
			public boolean matches(Object o) {
				Assessment a = (Assessment) o;
				boolean matches = expectedTopicName.equals(a.getTopic().getName()) &&
						expectedGroupName.equals(a.getGroup().getName()) &&
						expectedMessageCount == a.getMessages().size();
				
				if(!matches) {
					System.out.println("LearnTestUtils.assessmentWithTopicAndGroupAndMessageCount(...).new ArgumentMatcher() {...}.matches()");
					println(o, "topic.name", expectedTopicName,
							"group.name", expectedGroupName,
							"messages.size()", expectedMessageCount);
				}
				
				return matches;
			}
		});
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
					println("LearnTestUtils.reinforcementWithTextAndTopic(...).new ArgumentMatcher() {...}.matches()");
					println("\tr.id:  " + r.getId());
					println("\tr.name:  " + r.getMessageText());
					println("\tr.topic: " + r.getTopic().getName());
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
					println("LearnTestUtils.questionWithIdAndTextAndTopicAndAnswersAndMessageText(...).new ArgumentMatcher() {...}.matches()");
					println("\tq.id: " + q.getId() + " vs " + expectedId);
					println("\tq.type: " + q.getType() + " vs " + expectedType);
					println("\tq.questionText: " + q.getQuestionText() + " vs " + expectedQuestionText);
					println("\tq.topicName: " + q.getTopic().getName() + " vs " + expectedTopicName);
					println("\tq.answerA: " + q.getAnswers()[0] + " vs " + expectedAnswerA);
					println("\tq.answerB: " + q.getAnswers()[1] + " vs " + expectedAnswerB);
					println("\tq.answerC: " + q.getAnswers()[2] + " vs " + expectedAnswerC);
					println("\tq.messageText: " + q.getMessageText() + " vs " + expectedMessageText);
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
					println("LearnTestUtils.questionWithMessage(...).new ArgumentMatcher() {...}.matches()");
					println("\tq.qText:  " + q.getQuestionText());
					println("\tq.type: " + q.getType());
					println("\tq.mText: " + q.getMessageText());
				}
				return matches;
			}
		});
	}
	
	public static long today9am() {
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		c.set(Calendar.HOUR_OF_DAY, 9);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTimeInMillis();
	}
	
	private static void println(Object o, Object...fieldsAndExpectedValues) {
		for (int i = 0; i < fieldsAndExpectedValues.length; i+=2) {
			println(o, (String) fieldsAndExpectedValues[i], fieldsAndExpectedValues[i+1]);
		}
	}
	
	private static void println(Object o, String fieldName, Object expectedValue) {
		try {
			Object value = o;
			for(String exp : fieldName.split("\\.")) {
				String methodName;
				if(exp.endsWith("()")) {
					methodName = exp.substring(0, exp.length() - 2);
				} else {
					methodName = "get" + exp.substring(0, 1).toUpperCase() + exp.substring(1);
				}
				Method m = value.getClass().getMethod(methodName);
				value = m.invoke(value);
			}
			println(1, "o." + fieldName + ": " + value + " vs " + expectedValue);
		} catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	private static void println(int tabs, String s) {
		println("\t" + s);
	}
	
	public static void println(String s) {
		System.out.println(s);
	}
}
