package net.frontlinesms.plugins.learn.data.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Topic {
	/** Unique id for this entity.  This is for hibernate usage. */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true,nullable=false,updatable=false)
	private long id;
	private String name;
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	private List<TopicItem> items = new ArrayList<TopicItem>();
	
	public long getId() {
		return id;
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
