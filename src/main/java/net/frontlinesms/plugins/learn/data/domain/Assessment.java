package net.frontlinesms.plugins.learn.data.domain;

import net.frontlinesms.data.domain.Group;

public class Assessment implements HasTopic {
	private Topic topic;
	private Group group;
	
	public Topic getTopic() {
		return topic;
	}
	
	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	public void setGroup(Group g) {
		group = g;
	}

	public Object getGroup() {
		return group;
	}
}