package net.frontlinesms.plugins.learn.data.repository;

import net.frontlinesms.data.repository.hibernate.BaseHibernateDao;
import net.frontlinesms.plugins.learn.data.domain.Question;

public class QuestionDao extends BaseHibernateDao<Question> {
	protected QuestionDao() {
		super(Question.class);
	}
	
	public void save(Question q) {
		super.saveWithoutDuplicateHandling(q);
	}
	
	public void update(Question q) {
		super.updateWithoutDuplicateHandling(q);
	}
	
	public void delete(Question q) {
		super.delete(q);
	}
	
	public int count() {
		return super.getCount(getCriterion());
	}
}
