package net.frontlinesms.plugins.learn;

import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.*;
import static java.util.Arrays.asList;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import net.frontlinesms.data.domain.Contact;
import net.frontlinesms.data.domain.FrontlineMessage;
import net.frontlinesms.data.domain.Group;
import net.frontlinesms.data.events.EntityDeletedNotification;
import net.frontlinesms.data.events.EntitySavedNotification;
import net.frontlinesms.data.events.EntityUpdatedNotification;
import net.frontlinesms.data.repository.ContactDao;
import net.frontlinesms.events.EventObserver;
import net.frontlinesms.events.FrontlineEventNotification;
import net.frontlinesms.plugins.learn.data.domain.*;
import net.frontlinesms.plugins.learn.data.gradebook.StudentGrades;
import net.frontlinesms.plugins.learn.data.repository.*;

import org.mockito.ArgumentMatcher;

public class LearnTestUtils {
	public static final Long YESTERDAY, TODAY, TOMORROW;
	
	static {
		final long ONE_DAY = 1000L * 60 * 60 * 24;
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		c.set(Calendar.HOUR_OF_DAY, 9);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		TODAY = c.getTimeInMillis();
		
		c.setTimeInMillis(TODAY - ONE_DAY);
		c.set(Calendar.HOUR_OF_DAY, 9);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		YESTERDAY = c.getTimeInMillis();
		
		c.setTimeInMillis(TODAY + ONE_DAY);
		c.set(Calendar.HOUR_OF_DAY, 9);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		TOMORROW = c.getTimeInMillis();
	}
	
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("d/M/yyyy HH:mm");
	
//> TEST HELPER METHODS
	public static StudentGrades mockStudentClassResults(Contact student, Integer... grades) {
		StudentGrades r = mock(StudentGrades.class);
		when(r.getStudent()).thenReturn(student);
		when(r.getGrades()).thenReturn(grades);
		when(r.getAverage()).thenReturn(calculateMean(grades));
		return r;
	}
	
	private static int calculateMean(Integer... grades) {
		long total = 0;
		for(Integer g : grades) if(g != null) total += g;
		return (int) (total/grades.length);
	}
	
	public static Contact mockContact(ContactDao dao, String number) {
		Contact c = mock(Contact.class);
		when(dao.getFromMsisdn(number)).thenReturn(c);
		return c;
	}
	
	public static Contact[] mockContacts(String... names) {
		Contact[] c = new Contact[names.length];
		for (int i = 0; i < c.length; i++) {
			c[i] = mock(Contact.class);
			when(c[i].getName()).thenReturn(names[i]);
		}
		return c;
	}
	
	public static AssessmentMessage mockMessageWithTopicItem(TopicItem i, Frequency f, String startDate, String endDate) {
		AssessmentMessage m = mock(AssessmentMessage.class);
		when(m.getTopicItem()).thenReturn(i);
		when(m.getFrequency()).thenReturn(f);
		when(m.getStartDate()).thenReturn(parseDate(startDate));
		when(m.getEndDate()).thenReturn(parseDate(endDate==null? startDate: endDate));
		return m;
	}
	
	public static TopicItem[] mockTopicItems(int count) {
		TopicItem[] items = new TopicItem[count];
		for (int i = 0; i < items.length; i++) {
			items[i] = mock(TopicItem.class);
			when(items[i].getId()).thenReturn((long) i);
			when(items[i].getMessageText()).thenReturn("mock topic item: " + i);
		}
		return items;
	}
	
	public static Assessment mockAssessmentWithGroup(long id, String groupName, String startDate, String endDate) {
		Assessment a = mockAssessmentWithGroup(groupName, startDate, endDate);
		when(a.getId()).thenReturn(id);
		return a;
	}
	
	public static Assessment mockAssessmentWithGroup(String groupName, String startDate, String endDate) {
		Assessment a = mock(Assessment.class);
		Group g = mockGroup(groupName);
		when(a.getGroup()).thenReturn(g);
		when(a.getStartDate()).thenReturn(parseDate(startDate));
		when(a.getEndDate()).thenReturn(parseDate(endDate));
		return a;
	}
	
	public static Assessment mockAssessmentWithId(long id) {
		Assessment a = mock(Assessment.class);
		when(a.getId()).thenReturn(id);
		return a;
	}
	
	public static Assessment mockAssessmentWithTopic(String topicName, String startDate, String endDate) {
		Assessment a = mock(Assessment.class);
		Topic t = mockTopic(topicName);
		when(a.getTopic()).thenReturn(t);
		when(a.getStartDate()).thenReturn(parseDate(startDate));
		when(a.getEndDate()).thenReturn(parseDate(endDate));
		return a;
	}
	
	public static Assessment mockAssessmentWithTopicAndId(long id, String topicName, String startDate, String endDate) {
		Assessment a = mockAssessmentWithTopic(topicName, startDate, endDate);
		when(a.getId()).thenReturn(id);
		return a;
	}
	
	public static AssessmentMessage mockAssessmentMessage(long startDate, long endDate) {
		AssessmentMessage m = mock(AssessmentMessage.class);
		when(m.getStartDate()).thenReturn(startDate);
		when(m.getEndDate()).thenReturn(endDate);
		return m;
	}
	
	public static AssessmentMessage mockAssessmentMessage(TopicItem item) {
		AssessmentMessage m = mock(AssessmentMessage.class);
		when(m.getTopicItem()).thenReturn(item);
		return m;
	}
	
	public static Long parseDate(String date) {
		if(date == null) return null;
		try {
			return DATE_FORMAT.parse(date).getTime();
		} catch (ParseException ex) {
			// try the next format
		}
		try {
			return new SimpleDateFormat("d/M/y").parse(date).getTime();
		} catch (ParseException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public static Group mockGroup(String name) {
		Group g = mock(Group.class);
		when(g.getName()).thenReturn(name);
		return g;
	}
	
	public static Topic mockTopic(String name) {
		Topic t = mock(Topic.class);
		when(t.getName()).thenReturn(name);
		return t;
	}
	
	public static Reinforcement mockReinforcement(TopicDao topicDao, String topicName, TopicItemDao topicItemDao, String messageText) {
		Topic t = mockTopics(topicDao, topicName)[0];
		
		Reinforcement r = mock(Reinforcement.class);
		when(r.getMessageText()).thenReturn(messageText);
		when(r.getTopic()).thenReturn(t);
		
		List<TopicItem> items = asList(new TopicItem[] {r});
		when(topicItemDao.getAllByTopic(t)).thenReturn(items);
		
		return r;
	}
	
	public static Question mockQuestion(TopicDao topicDao, String topicName,
			TopicItemDao topicItemDao, String questionText, String... answers) {
		Topic t = mockTopics(topicDao, topicName)[0];
		
		Question q = mock(Question.class);
		when(q.getQuestionText()).thenReturn(questionText);
		when(q.getAnswers()).thenReturn(answers);
		when(q.getTopic()).thenReturn(t);
		
		List<TopicItem> items = asList(new TopicItem[] {q});
		when(topicItemDao.getAllByTopic(t)).thenReturn(items);
		
		return q;
	}
	
	public static FrontlineMessage mockIncomingMessage(String messageText) {
		FrontlineMessage m = mock(FrontlineMessage.class);
		when(m.getType()).thenReturn(FrontlineMessage.Type.RECEIVED);
		when(m.getTextContent()).thenReturn(messageText);
		return m;
	}
	
	public static FrontlineMessage mockIncomingMessage(String from, String messageText) {
		FrontlineMessage m = mockIncomingMessage(messageText);
		when(m.getSenderMsisdn()).thenReturn(from);
		return m;
	}
	
	public static FrontlineMessage mockOutgoingMessage(String messageText) {
		FrontlineMessage m = mock(FrontlineMessage.class);
		when(m.getType()).thenReturn(FrontlineMessage.Type.OUTBOUND);
		when(m.getTextContent()).thenReturn(messageText);
		return m;
	}
	
	public static FrontlineMessage mockOutgoingMessage(String from, String messageText) {
		FrontlineMessage m = mockOutgoingMessage(messageText);
		when(m.getSenderMsisdn()).thenReturn(from);
		return m;
	}
	
	public static List<StudentGrades> mockStudentGrades(Integer[]... grades) {
		ArrayList<StudentGrades> sg = new ArrayList<StudentGrades>();
		for(Integer[] grade : grades) {
			StudentGrades s = mock(StudentGrades.class);
			when(s.getGrades()).thenReturn(grade);
			sg.add(s);
		}
		return sg;
	}
	
	public static Topic[] mockTopics(String... names) {
		ArrayList<Topic> topics = new ArrayList<Topic>();
		for(String name : names) {
			Topic t = mock(Topic.class);
			when(t.getName()).thenReturn(name);
			topics.add(t);
		}
		return topics.toArray(new Topic[topics.size()]);
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
			final TopicItem expectedTopicItem, final String expectedStartDate,
			final Frequency expectedFrequency) {
		return assessmentMessageWithTopicItemAndStartDateAndRepeat(expectedTopicItem, parseDate(expectedStartDate), expectedFrequency);
	}
	
	public static AssessmentMessage assessmentMessageWithTopicItemAndStartDateAndRepeat(
			final TopicItem expectedTopicItem, final long expectedStartDate,
			final Frequency expectedFrequency) {
		return assessmentMessageWithTopicItemAndStartDateAndRepeatAndEndDate(expectedTopicItem,
				expectedStartDate, expectedFrequency, expectedStartDate);
	}
	
	public static AssessmentMessage assessmentMessageWithTopicItemAndStartDateAndRepeatAndEndDate(
			final TopicItem expectedTopicItem, final long expectedStartDate,
			final Frequency expectedFrequency, final String expectedEndDate) {
		return assessmentMessageWithTopicItemAndStartDateAndRepeatAndEndDate(expectedTopicItem, expectedStartDate, expectedFrequency, parseDate(expectedEndDate));
	}
	
	public static AssessmentMessage assessmentMessageWithTopicItemAndStartDateAndRepeatAndEndDate(
			final TopicItem expectedTopicItem, final String expectedStartDate,
			final Frequency expectedFrequency, final String expectedEndDate) {
		return assessmentMessageWithTopicItemAndStartDateAndRepeatAndEndDate(
				expectedTopicItem,
				parseDate(expectedStartDate),
				expectedFrequency,
				parseDate(expectedEndDate));
	}
	
	public static AssessmentMessage assessmentMessageWithTopicItemAndStartDateAndRepeatAndEndDate(
			final TopicItem expectedTopicItem, final long expectedStartDate,
			final Frequency expectedFrequency, final long expectedEndDate) {
		return argThat(new ArgumentMatcher<AssessmentMessage>() {
			@Override
			public boolean matches(Object o) {
				return match("assessmentMessageWithTopicItemAndStartDateAndRepeatAndEndDate", o,
						"topicItem", expectedTopicItem,
						"startDate", expectedStartDate,
						"endDate", expectedEndDate,
						"frequency", expectedFrequency);
			}
		});
	}
	
	public static EventObserver eventObserversOfClass(final Class<?>... classes) {
		// seems to check twice for each of these!
		final ArrayList<Class<?>> classesStillExpected1 = new ArrayList<Class<?>>(Arrays.asList(classes));
		final ArrayList<Class<?>> classesStillExpected2 = new ArrayList<Class<?>>(Arrays.asList(classes));
		return argThat(new ArgumentMatcher<EventObserver>() {
			@Override
			public boolean matches(Object o) {
				return classesStillExpected1.remove(o.getClass()) || classesStillExpected2.remove(o.getClass());
			}
		});
	}
	
	public static Assessment assessmentWithTopic(final String expectedTopicName) {
		return argThat(new ArgumentMatcher<Assessment>() {
			@Override
			public boolean matches(Object o) {
				return match("assessmentWithTopic", o,
						"topic.name", expectedTopicName);
			}
		});
	}
	
	public static Assessment assessmentWithTopicAndGroup(final String expectedTopicName, final String expectedGroupName) {
		return full_match("assessmentWithTopicAndGroup",
						"topic.name", expectedTopicName,
						"group.name", expectedGroupName);
	}
	
	public static Assessment assessmentWithTopicAndGroupAndMessageCount(final String expectedTopicName, final String expectedGroupName, final int expectedMessageCount) {
		return argThat(new ArgumentMatcher<Assessment>() {
			@Override
			public boolean matches(Object o) {
				return match("assessmentWithTopicAndGroupAndMessageCount", o,
						"topic.name", expectedTopicName,
						"group.name", expectedGroupName,
						"messages.size()", expectedMessageCount);
			}
		});
	}
	
	public static AssessmentMessageResponse assessmentMessageResponseWithMessageAndStudentAndAnswerAndCorrect(final AssessmentMessage expectedAssessmentMessage, final Contact expectedStudent, final int expectedAnswer, final boolean expectedCorrect) {
		return full_match("assessmentMessageResponseWithMessageAndStudentAndAnswerAndCorrect",
				"assessmentMessage", expectedAssessmentMessage,
				"student", expectedStudent,
				"answer", expectedAnswer,
				"correct", expectedCorrect);
	}
	
	public static Topic topicWithName(final String expectedName) {
		return argThat(new ArgumentMatcher<Topic>() {
			@Override
			public boolean matches(Object a) {
				return match("topicWithName", a,
						"name", expectedName);
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
				return match("reinforcementWithTextAndTopic", o,
						"id", expectedId,
						"messageText", expectedReinforementText,
						"topic.name", expectedTopicName);
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
		return full_match("questionWithMessage",
				"questionText", expectedMessageText);
	}
	
	public static Question questionWithMessageAndAnswer(String expectedMessageText, int expectedAnswer) {
		return full_match("questionWithMessage",
				"messageText", expectedMessageText,
				"correctAnswer", expectedAnswer);
	}
	
	public static Question questionWithMessageAndCorrectResponse(final String expectedMessageText, int correctResponse) {
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
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 9);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTimeInMillis();
	}
	
	public static long firstOfTheMonth9am() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 9);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTimeInMillis();
	}

	private static <T> T full_match(final String methodName, final Object... comparisons) {
		return argThat(new ArgumentMatcher<T>() {
			@Override
			public boolean matches(Object o) {
				return match(methodName, o, comparisons);
			}
		});
	}
	
	private static boolean match(String methodName, Object o, Object... fieldNamesAndExpectedValues) {
		boolean matches = true;

		for(int i=0; matches && i<fieldNamesAndExpectedValues.length; i+=2) {
			String fieldName = (String) fieldNamesAndExpectedValues[i];
			Object expectedValue = fieldNamesAndExpectedValues[i + 1];
			Object actualValue = getValue(o, fieldName);
			if(expectedValue == null) {
				matches = (actualValue == null);
			} else {
				matches = actualValue != null &&
						actualValue.equals(expectedValue);
			}
		}
		
		if(!matches) {
			println("LearnTestUtils." + methodName + "(...).new ArgumentMatcher() {...}.matches()");
			println(1, "<fieldName>: <actualValue> vs <expectedValue>");
			for(int i=0; i<fieldNamesAndExpectedValues.length; i+=2) {
				String fieldName = (String) fieldNamesAndExpectedValues[i];
				Object expectedValue = fieldNamesAndExpectedValues[i + 1];
				println(o, fieldName, expectedValue);
			}
		}
		
		return matches;
	}
	
	private static void println(Object o, String fieldName, Object expectedValue) {
		Object value = getValue(o, fieldName);
		println(1, "o." + fieldName + ": " + value + " vs " + expectedValue + " (" + getAlternateFormat(value) + " vs " + getAlternateFormat(expectedValue) + ")");
	}
	
	private static Object getValue(Object o, String fieldName) {
		try {
			Object value = o;
			for(String exp : fieldName.split("\\.")) {
				Method m;
				if(exp.endsWith("()")) {
					m = value.getClass().getMethod(exp.substring(0, exp.length() - 2));
				} else {
					String methodNameBody = exp.substring(0, 1).toUpperCase() + exp.substring(1);
					try {
						m = value.getClass().getMethod("get" + methodNameBody);
					} catch(NoSuchMethodException ex) {
						m = value.getClass().getMethod("is" + methodNameBody);
					}
				}
				value = m.invoke(value);
			}
			return value;
		} catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	private static String getAlternateFormat(Object value) {
		if(value == null) return null;
		String formatted = null;
		if(value instanceof Long) formatted = DATE_FORMAT.format(new Date((Long) value));
		return formatted == null? "": formatted;
	}
	
	private static void println(int tabs, String s) {
		println("\t" + s);
	}
	
	public static void println(String s) {
		System.out.println(s);
	}

	public static <T> FrontlineEventNotification mockEntitySavedNotification(Class<T> clazz) {
		return new EntitySavedNotification<T>(mock(clazz));
	}

	public static <T> FrontlineEventNotification mockEntityUpdatedNotification(Class<T> clazz) {
		return new EntityUpdatedNotification<T>(mock(clazz));
	}

	public static <T> FrontlineEventNotification mockEntityDeletedNotification(Class<T> clazz) {
		return new EntityDeletedNotification<T>(mock(clazz));
	}

	public static <T> FrontlineEventNotification mockEntitySavedNotification(T instance) {
		return new EntitySavedNotification<T>(instance);
	}

	public static <T> FrontlineEventNotification mockEntityUpdatedNotification(T instance) {
		return new EntityUpdatedNotification<T>(instance);
	}

	public static <T> FrontlineEventNotification mockEntityDeletedNotification(T instance) {
		return new EntityDeletedNotification<T>(instance);
	}
}
