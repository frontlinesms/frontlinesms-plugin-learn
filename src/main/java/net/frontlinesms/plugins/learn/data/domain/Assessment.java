package net.frontlinesms.plugins.learn.data.domain;

public class Assessment implements HasTopic {
	private Topic topic;
	
	public Topic getTopic() {
		return topic;
	}
	
	public void setTopic(Topic topic) {
		this.topic = topic;
	}
}