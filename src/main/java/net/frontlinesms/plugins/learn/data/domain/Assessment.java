package net.frontlinesms.plugins.learn.data.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import net.frontlinesms.data.domain.Group;

@Entity
public class Assessment implements HasTopic {
	/** Unique id for this entity.  This is for hibernate usage. */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true,nullable=false,updatable=false)
	private long id;
	
	@ManyToOne
	private Topic topic;
	
	@ManyToOne
	private Group group;
	
	@OneToMany @OrderBy("startDate")
	private List<AssessmentMessage> messages = new ArrayList<AssessmentMessage>();
	
	public Topic getTopic() {
		return topic;
	}
	
	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	public void setGroup(Group g) {
		group = g;
	}

	public Group getGroup() {
		return group;
	}
	
	public List<AssessmentMessage> getMessages() {
		return Collections.unmodifiableList(messages);
	}

	public void setMessages(List<AssessmentMessage> messages) {
		this.messages = messages;
	}
	
	public long getStartDate() {
		return 0;
	}
	
	public long getEndDate() {
		return 0;
	}
}