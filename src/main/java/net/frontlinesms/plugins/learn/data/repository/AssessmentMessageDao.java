package net.frontlinesms.plugins.learn.data.repository;

import net.frontlinesms.data.repository.hibernate.BaseHibernateDao;
import net.frontlinesms.plugins.learn.data.domain.AssessmentMessage;

public class AssessmentMessageDao extends BaseHibernateDao<AssessmentMessage> {
	public AssessmentMessageDao() {
		super(AssessmentMessage.class);
	}
	
	public void save(AssessmentMessage m) {
		super.saveWithoutDuplicateHandling(m);
	}
	
	public int count() {
		return super.countAll();
	}
}
