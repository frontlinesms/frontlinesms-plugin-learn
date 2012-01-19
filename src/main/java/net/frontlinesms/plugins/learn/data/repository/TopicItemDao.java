package net.frontlinesms.plugins.learn.data.repository;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import static org.hibernate.criterion.Restrictions.*;

import net.frontlinesms.data.repository.hibernate.BaseHibernateDao;
import net.frontlinesms.plugins.learn.data.domain.Topic;
import net.frontlinesms.plugins.learn.data.domain.TopicItem;

public class TopicItemDao extends BaseHibernateDao<TopicItem> {
	protected TopicItemDao(Class<TopicItem> clazz) {
		super(TopicItem.class);
	}

	public List<TopicItem> getAllByTopic(Topic t) {
		DetachedCriteria criteria = getCriterion();
		criteria.add(eq("topic", t));
		return super.getList(criteria );
	}
}
