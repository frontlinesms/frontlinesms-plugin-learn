package net.frontlinesms.plugins.learn.data.repository;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import net.frontlinesms.data.repository.hibernate.BaseHibernateDao;
import net.frontlinesms.plugins.learn.data.domain.AssessmentMessage;

public class AssessmentMessageDao extends BaseHibernateDao<AssessmentMessage> {
	public AssessmentMessageDao() {
		super(AssessmentMessage.class);
	}
	
	public AssessmentMessage get(long id) {
		DetachedCriteria c = getCriterion();
		c.add(Restrictions.eq("id", id));
		return getUnique(c);
	}
}
