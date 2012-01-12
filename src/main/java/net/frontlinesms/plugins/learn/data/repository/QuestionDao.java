package net.frontlinesms.plugins.learn.data.repository;

import net.frontlinesms.data.repository.hibernate.BaseHibernateDao;
import net.frontlinesms.plugins.learn.data.domain.Question;

public class QuestionDao extends BaseHibernateDao<Question> {
	protected QuestionDao() {
		super(Question.class);
	}
	
	public void save(Question r) {
		super.saveWithoutDuplicateHandling(r);
	}
	
	public void delete(Question r) {
		super.delete(r);
	}
	
	public int count() {
		return super.getCount(getCriterion());
	}
}
