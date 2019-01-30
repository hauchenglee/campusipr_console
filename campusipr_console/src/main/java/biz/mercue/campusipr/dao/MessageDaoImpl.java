package biz.mercue.campusipr.dao;


import java.util.Date;
import java.util.List;



import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import biz.mercue.campusipr.model.Message;



@Repository("messageDao")
public class MessageDaoImpl extends AbstractDao<String,  Message> implements MessageDao {
	
	private Logger log = Logger.getLogger(this.getClass().getName());
	
	
	@Override
	public Message getById(String id) {
		return getByKey(id);
	}
	
	
	@Override
	public void addMessage(Message message) {
		persist(message);
	}
	
	@Override
	public List<Message> getAdminMessages(String adminId, String targetId, int page, int pageSize) {
		log.info("page :"+page);

		Criteria criteria =  createEntityCriteria();
		criteria.add(Restrictions.or(
				Restrictions.and(Restrictions.eq("sender_id", adminId),Restrictions.eq("receiver_id", targetId)),
				Restrictions.and(Restrictions.eq("receiver_id", adminId),Restrictions.eq("sender_id", targetId))));
		criteria.addOrder(Order.desc("message_date"));
		criteria.setFirstResult((page-1)*pageSize);
		criteria.setMaxResults(pageSize);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		return criteria.list();
	}
	

	
	@Override
	public List<Message> getMessagesAfterTime(String adminId,String targetId, Date time) {
		Criteria criteria =  createEntityCriteria();
		criteria.add(Restrictions.gt("message_date", time));
		criteria.add(Restrictions.or(
				Restrictions.and(Restrictions.eq("sender_id", adminId),Restrictions.eq("receiver_id", targetId)),
				Restrictions.and(Restrictions.eq("receiver_id", adminId),Restrictions.eq("sender_id", targetId))));
		criteria.addOrder(Order.desc("message_date"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	
	
	@Override
	public int  getCountAdminMessages(String adminId,String targetId) {
		Criteria criteria =  createEntityCriteria();
		criteria.add(Restrictions.or(
				Restrictions.and(Restrictions.gt("sender_id", adminId),Restrictions.eq("receiver_id", targetId)),
				Restrictions.and(Restrictions.gt("receiver_id", adminId),Restrictions.eq("sender_id", targetId))));
		criteria.setProjection(Projections.rowCount());
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		long count = (long)criteria.uniqueResult();
		return (int)count;
	}
	

	
	
	
	
	

}
