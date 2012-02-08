package net.frontlinesms.plugins.learn.data.repository;

import java.util.List;

import net.frontlinesms.data.repository.hibernate.BaseHibernateDao;
import net.frontlinesms.plugins.learn.data.domain.Assessment;
import net.frontlinesms.plugins.learn.data.domain.Topic;

public class AssessmentDao extends BaseHibernateDao<Assessment> {
	protected AssessmentDao() {
		super(Assessment.class);
	}
	
	public void save(Assessment a) {
		super.saveWithoutDuplicateHandling(a);
	}
	
	public int count() {
		return super.countAll();
	}
	
	public List<Assessment> list() {
		return super.getAll();
	}

	public List<Assessment> findAllByTopic(Topic t) {
		return null;
	}
}