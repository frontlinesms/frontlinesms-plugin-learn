package net.frontlinesms.plugins.learn.data.domain;

import javax.persistence.Entity;

@Entity
public class Question extends TopicItem {
	private String messageText;
	
//> ACCESSORS
	public String getMessageText() {
		return messageText;
	}
	
	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}

	public String getQuestionText() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}
}