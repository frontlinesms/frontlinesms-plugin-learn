package net.frontlinesms.plugins.learn.data.domain;

import javax.persistence.Entity;

@Entity
public class Reinforcement extends TopicItem {
	private String messageText;
	
	public String getMessageText() {
		return messageText;
	}
	
	public void setName(String name) {
		this.messageText = name;
	}
}