package net.frontlinesms.plugins.learn.data.repository;

import net.frontlinesms.data.repository.hibernate.BaseHibernateDao;
import net.frontlinesms.plugins.learn.data.domain.Assessment;

public class AssessmentDao extends BaseHibernateDao<Assessment> {
	protected AssessmentDao() {
		super(Assessment.class);
	}
}