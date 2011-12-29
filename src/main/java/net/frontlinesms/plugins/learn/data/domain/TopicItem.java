package net.frontlinesms.plugins.learn.data.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public abstract class TopicItem {
	/** Unique id for this entity.  This is for hibernate usage. */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true,nullable=false,updatable=false)
	private long id;
}

