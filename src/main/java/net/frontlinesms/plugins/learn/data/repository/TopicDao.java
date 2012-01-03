package net.frontlinesms.plugins.learn.data.repository;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.data.repository.hibernate.BaseHibernateDao;
import net.frontlinesms.plugins.learn.data.domain.*;

public class TopicDao extends BaseHibernateDao<Topic> {
	protected TopicDao() {
		super(Topic.class);
	}

	public int count() {
		return super.countAll();
	}

	public void save(Topic t) throws DuplicateKeyException {
		super.save(t);
	}

	public void delete(Topic t) {
		super.delete(t);
	}
	
	public List<Topic> list() {
		return super.getAll();
	}

	public Topic findByName(String text) {
		DetachedCriteria c = getCriterion();
		c.add(Restrictions.eq("name", text));
		return getUnique(c);
	}
}

