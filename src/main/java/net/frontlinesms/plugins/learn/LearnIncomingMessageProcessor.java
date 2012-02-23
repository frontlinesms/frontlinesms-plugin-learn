package net.frontlinesms.plugins.learn;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;

import net.frontlinesms.data.domain.Contact;
import net.frontlinesms.data.domain.FrontlineMessage;
import net.frontlinesms.data.events.EntitySavedNotification;
import net.frontlinesms.data.repository.ContactDao;
import net.frontlinesms.events.EventBus;
import net.frontlinesms.events.EventObserver;
import net.frontlinesms.events.FrontlineEventNotification;
import net.frontlinesms.plugins.learn.data.domain.AssessmentMessage;
import net.frontlinesms.plugins.learn.data.domain.AssessmentMessageResponse;
import net.frontlinesms.plugins.learn.data.domain.Question;
import net.frontlinesms.plugins.learn.data.domain.TopicItem;
import net.frontlinesms.plugins.learn.data.repository.AssessmentMessageDao;
import net.frontlinesms.plugins.learn.data.repository.AssessmentMessageResponseDao;

public class LearnIncomingMessageProcessor implements EventObserver {
	private static final String ALPHABET = "abc";
	private static final Pattern ANSWER_PATTERN = Pattern.compile("\\s*(\\d+)\\s*(?:(true|false)|([a-z]))\\s*");
	
	@Autowired private EventBus eventBus;
	@Autowired private ContactDao contactDao;
	@Autowired private AssessmentMessageDao assessmentMessageDao;
	@Autowired private AssessmentMessageResponseDao responseDao;

	public void start() {
		eventBus.registerObserver(this);
	}
	
	public void shutdown() {
		eventBus.unregisterObserver(this);
	}

	public void notify(FrontlineEventNotification n) {
		if(!(n instanceof EntitySavedNotification<?>)) return;
		Object entity = ((EntitySavedNotification<?>) n).getDatabaseEntity();
		if(!(entity instanceof FrontlineMessage)) return;
		FrontlineMessage m = (FrontlineMessage) entity;
		processMessage(m);
	}

//> MESSAGE PROCESSING
	void processMessage(FrontlineMessage m) {
		AssessmentMessage am = getAssessmentMessage(m);
		if(am == null) return;
		Question q = getQuestion(am);
		if(q == null) return;
		Contact c = getContact(m);
		if(c == null) return;
		
		AssessmentMessageResponse r = new AssessmentMessageResponse();
		r.setAssessmentMessage(am);
		r.setStudent(c);
		
		int answer = getAnswer(m);
		r.setAnswer(answer);
		r.setCorrect(answer == q.getCorrectAnswer());
		
		responseDao.save(r);
	}

	int getAnswer(FrontlineMessage m) {
		Matcher matcher = ANSWER_PATTERN.matcher(m.getTextContent().toLowerCase());
		if(matcher.find()) {
			String trueOrFalse = matcher.group(2);
			if(trueOrFalse != null) {
				if(trueOrFalse.equals("true")) return 0;
				if(trueOrFalse.equals("false")) return 1;
			}
			try {
				return ALPHABET.indexOf(matcher.group(3));
			} catch(NumberFormatException ex) {}
		}
		return -1;
	}

	private Contact getContact(FrontlineMessage m) {
		return contactDao.getFromMsisdn(m.getSenderMsisdn());
	}

	private Question getQuestion(AssessmentMessage am) {
		TopicItem topicItem = am.getTopicItem();
		if(topicItem instanceof Question) return (Question) topicItem;
		else return null;
	}

	AssessmentMessage getAssessmentMessage(FrontlineMessage m) {
		Matcher matcher = ANSWER_PATTERN.matcher(m.getTextContent().toLowerCase());
		if(matcher.find()) {
			String id = matcher.group(1);
			try {
				return assessmentMessageDao.get(Long.parseLong(id));
			} catch(NumberFormatException ex) {}
		}
		return null;
	}
}
