package net.frontlinesms.plugins.learn.data.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Topic {
	private String name;
	private List<TopicItem> items = new ArrayList<TopicItem>();
	
	public long getId() {
		return 0;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<TopicItem> getItems() {
		return Collections.unmodifiableList(items);
	}

	public void addItem(TopicItem i) {
		items.add(i);
	}
}
