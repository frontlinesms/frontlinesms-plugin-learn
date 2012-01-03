package net.frontlinesms.plugins.learn.data.repository;

import java.util.List;

import net.frontlinesms.data.repository.hibernate.BaseHibernateDao;
import net.frontlinesms.plugins.learn.data.domain.*;

public class TopicDao extends BaseHibernateDao<Topic> {
	protected TopicDao() {
		super(Topic.class);
	}

	public int count() {
		return super.countAll();
	}

	public void save(Topic t) {
		super.saveWithoutDuplicateHandling(t);
	}

	public void delete(Topic t) {
		super.delete(t);
	}
	
	public List<Topic> list() {
		return super.getAll();
	}
}

