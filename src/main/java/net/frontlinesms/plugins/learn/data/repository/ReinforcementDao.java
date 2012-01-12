package net.frontlinesms.plugins.learn.data.repository;

import net.frontlinesms.data.repository.hibernate.BaseHibernateDao;
import net.frontlinesms.plugins.learn.data.domain.Reinforcement;

public class ReinforcementDao extends BaseHibernateDao<Reinforcement> {
	protected ReinforcementDao() {
		super(Reinforcement.class);
	}
	
	public void save(Reinforcement r) {
		super.saveWithoutDuplicateHandling(r);
	}
	
	public void delete(Reinforcement r) {
		super.delete(r);
	}
	
	public int count() {
		return super.getCount(getCriterion());
	}
}
