package net.frontlinesms.plugins.learn.data.domain;

import javax.persistence.Entity;

@Entity
public class Reinforcement extends TopicItem {
	private String name;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}