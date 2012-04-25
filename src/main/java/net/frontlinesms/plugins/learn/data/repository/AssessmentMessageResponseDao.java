package net.frontlinesms.plugins.learn.data.repository;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import net.frontlinesms.data.domain.Contact;
import net.frontlinesms.data.repository.hibernate.BaseHibernateDao;
import net.frontlinesms.plugins.learn.data.domain.Assessment;
import net.frontlinesms.plugins.learn.data.domain.AssessmentMessage;
import net.frontlinesms.plugins.learn.data.domain.AssessmentMessageResponse;

public class AssessmentMessageResponseDao extends BaseHibernateDao<AssessmentMessageResponse> {
	public AssessmentMessageResponseDao() {
		super(AssessmentMessageResponse.class);
	}
	
	public void save(AssessmentMessageResponse r) {
		super.saveWithoutDuplicateHandling(r);
	}

	public List<AssessmentMessageResponse> findAllByStudentAndAssessment(
			Contact student, Assessment a) {
		DetachedCriteria criteria = super.getCriterion();
		criteria.add(Restrictions.and(
				Restrictions.eq("student", student),
				Restrictions.in("assessmentMessage", a.getMessages())));
		return super.getList(criteria);
	}

	public boolean hasResponded(Contact student, AssessmentMessage m) {
		DetachedCriteria criteria = super.getCriterion();
		criteria.add(Restrictions.and(
				Restrictions.eq("student", student),
				Restrictions.eq("assessmentMessage", m)));
		return super.getCount(criteria) > 0;		
	}
}
