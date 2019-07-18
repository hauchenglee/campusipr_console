package biz.mercue.campusipr.dao;


import java.util.Date;
import java.util.List;


import biz.mercue.campusipr.model.Admin;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import biz.mercue.campusipr.model.Message;

import javax.persistence.TemporalType;


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
	public List<Message> getMessagesList(String senderId, String receiverId) {
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.lt("message_date", new Date()));
		criteria.add(Restrictions.or(
				Restrictions.and(Restrictions.eq("sender_id", senderId), Restrictions.eq("receiver_id", receiverId)),
				Restrictions.and(Restrictions.eq("receiver_id", senderId), Restrictions.eq("sender_id", receiverId))));
		criteria.addOrder(Order.asc("message_date"));
		criteria.setMaxResults(10);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public List<Message> getMessagesBeforeTime(String senderId, String receiverId, Date timeStamp) {
		Criteria criteria =  createEntityCriteria();
		criteria.add(Restrictions.lt("message_date", timeStamp));
		criteria.add(Restrictions.or(
				Restrictions.and(Restrictions.eq("sender_id", senderId),Restrictions.eq("receiver_id", receiverId)),
				Restrictions.and(Restrictions.eq("receiver_id", senderId),Restrictions.eq("sender_id", receiverId))));
		criteria.addOrder(Order.asc("message_date"));
		criteria.setMaxResults(20);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public List<Message> getMessagesBeforeAndEqualTime(String senderId, String receiverId, Date timeStamp) {
		Criteria criteria =  createEntityCriteria();
		criteria.add(Restrictions.le("message_date", timeStamp));
		criteria.add(Restrictions.or(
				Restrictions.and(Restrictions.eq("sender_id", senderId),Restrictions.eq("receiver_id", receiverId)),
				Restrictions.and(Restrictions.eq("receiver_id", senderId),Restrictions.eq("sender_id", receiverId))));
		criteria.addOrder(Order.asc("message_date"));
		criteria.setMaxResults(20);
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
		criteria.addOrder(Order.asc("message_date"));
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
		return (int) count;
	}

	@Override
	public List<Message> searchText(String senderId, String receiverId, String text) {
		String hql = "select m from Message m where m.sender_id = :senderId and m.receiver_id = :receiverId and m.message_text like :text";
		Session session = getSession();
		Query query = session.createQuery(hql);
		query.setParameter("senderId", senderId);
		query.setParameter("receiverId", receiverId);
		query.setParameter("text", "%" + text + "%");
		return query.list();
	}

	@Override
	public Message getNewestMessage(String senderId) {
		String hql = "select m from Message m where m.sender_id = :senderId order by m.message_date desc";
		Session session = getSession();
		Query query = session.createQuery(hql);
		query.setParameter("senderId", senderId);
		query.setMaxResults(1);
		return (Message) query.uniqueResult();
	}

	@Override
	public void readMessage(String senderId, String receiverId) {
		String hql = "update Message m set m.is_read = 1 where m.sender_id = :senderId and m.receiver_id = :receiverId";
		Session session = getSession();
		Query query = session.createQuery(hql);
		query.setParameter("senderId", senderId);
		query.setParameter("receiverId", receiverId);
		query.executeUpdate();
	}
}
