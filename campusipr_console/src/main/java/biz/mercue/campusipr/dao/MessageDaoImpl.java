package biz.mercue.campusipr.dao;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
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
	public List<Message> getAccountMessages(String botId, String accountId, int page, int range) {
		log.info("page :"+page);
		
		log.info("range :"+range);
		Criteria criteria =  createEntityCriteria();
		criteria.add(Restrictions.eq("account_id", accountId));
		criteria.add(Restrictions.eq("bot_id", botId));
		criteria.addOrder(Order.desc("message_date"));
		criteria.setFetchMode("template", FetchMode.SELECT);
		criteria.setFirstResult((page-1)*range);
		criteria.setMaxResults(range);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		return criteria.list();
	}
	
	@Override
	public List<Message> searchAccountMessages(String botId, String accountId, String keyword, int page, int range) {
		Criteria criteria =  createEntityCriteria();
		criteria.add(Restrictions.eq("account_id", accountId));
		criteria.add(Restrictions.eq("bot_id", botId));
		criteria.add(Restrictions.like("message_text", keyword, MatchMode.ANYWHERE));
		criteria.addOrder(Order.desc("message_date"));
		criteria.setFetchMode("template", FetchMode.SELECT);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		criteria.setFirstResult((page-1)*range);
		criteria.setMaxResults(range);			
		 
		return criteria.list();
	}
	
	@Override
	public Message getLatestMessage(String botId, String accountId) {
		Criteria criteria =  createEntityCriteria();
		criteria.add(Restrictions.eq("account_id", accountId));
		criteria.add(Restrictions.eq("bot_id", botId));
		criteria.addOrder(Order.desc("message_date"));
		criteria.setMaxResults(1);
		Object message = criteria.uniqueResult();
		if (message != null) {
			return (Message) message;
		} else {
			return null;
		}
	}
	
	
	
	@Override
	public List<Message> getAccountsLastestMessage(List<String> adminIdList){
		String strSQL = "SELECT m1.* FROM message as m1 JOIN "+
		"(SELECT MAX(message_date) AS message_date ,account_id,bot_id  FROM message GROUP BY account_id,bot_id) m2 ON m1.message_date=m2.message_date and m1.account_id = m2.account_id and m1.bot_id = m2.bot_id " +
		"WHERE m1.admin_id in (:idList)  "+
		"ORDER BY m1.message_date desc ,m1.account_id";
		Query query = getSession().createSQLQuery(strSQL).addEntity(Message.class);
		query.setParameterList("idList", adminIdList);	
		return query.list();
	}
	
	@Override
	public List<Message> getAccountsLastestMessage(String adminId){
		String strSQL = "SELECT m1.* FROM message as m1 JOIN "+
		"(SELECT MAX(message_date) AS message_date ,account_id,bot_id  FROM message GROUP BY account_id,bot_id) m2 ON m1.message_date=m2.message_date and m1.account_id = m2.account_id and m1.bot_id = m2.bot_id " +
		"WHERE m1.admin_id =:adminId  "+
		"ORDER BY m1.message_date desc ,m1.account_id";
		Query query = getSession().createSQLQuery(strSQL).addEntity(Message.class);
		query.setParameter("adminId", adminId);	
		return query.list();
	}
	
	@Override
	public List<Message> getAdminMessagesAfterTime(String id, Date time) {
		Criteria criteria =  createEntityCriteria();
		//System.out.println(time);
		criteria.add(Restrictions.and(
				Restrictions.gt("message_date", time),
				Restrictions.eq("admin_id", id)
		));
		criteria.setMaxResults(50);
		
		criteria.addOrder(Order.asc("send_user_id"))
		.addOrder(Order.desc("message_date"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}
	
	@Override
	public void addMessage(Message message) {
		persist(message);
	}
	
	@Override
	public void deleteMessage(Message message) {
		delete(message);
	}
	
	//TODO for demo
	@Override
	public int deleteByBusiess(String businessId){
		String hql = "delete from Message where business_id = :business_id  ";
		Session session = getSession();
		Query query  = session.createQuery(hql); 
		query.setString("business_id", businessId);
		int result = query.executeUpdate();
		
		
		
		List<String> listBot = new ArrayList<String>();
		listBot.add("1f9d09f1976f0e1286206f3d7d7045fa");
		listBot.add("20651ce3edeca7c4122be667ebbb11c1");
		
		String hql2 = "delete from Message where bot_id in (:botlist)  ";
		Query query2  = session.createQuery(hql2); 
		query2.setParameterList("botlist", listBot);
		result = query2.executeUpdate();
		
		
		return result; 
	}
	
	
	@Override
	public List<Message> getOfflineMessage(String businessId, int page, int range){
		
		String sql ="SELECT * FROM message as m1 "
				+ "where admin_id is null and business_id = :business_id "
				+ "and m1.message_id = ( select m2.message_id from message m2 where m2.account_id = m1.account_id order by  m2.message_date desc limit 1) "
				+ "LIMIT :range OFFSET :offset";
		Session session = getSession();
		Query query  = session.createSQLQuery(sql).addEntity(Message.class);
		
		query.setParameter("business_id", businessId);
		query.setParameter("range", range);
		query.setParameter("offset", (page -1) * range);
		return query.list();
	}
	
	
	@Override
	public List<Message> getOfflineMessageByAccount(String botId, String accountId){
		Criteria criteria =  createEntityCriteria();
		criteria.add(Restrictions.eq("account_id", accountId));
		criteria.add(Restrictions.eq("bot_id", botId));
		criteria.add(Restrictions.isNull("admin_id"));
		criteria.addOrder(Order.desc("message_date"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		return criteria.list();
	}
	@Override
	public List<Message> getHistoryMessage(String busienssId, int page, int range){
		Criteria criteria =  createEntityCriteria();
		criteria.add(Restrictions.eq("business_id", busienssId));
		criteria.addOrder(Order.desc("message_date"));
		criteria.setFetchMode("template", FetchMode.SELECT);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setFirstResult((page-1)*range);
		criteria.setMaxResults(range);
		return criteria.list();
	}
	
	@Override
	public Message getByIdAndAccountId(String id,String accountId){
		Criteria criteria =  createEntityCriteria();
		criteria.add(Restrictions.eq("account_id", accountId));
		criteria.add(Restrictions.eq("message_id", id));
		return (Message) criteria.uniqueResult();
	}
	
	@Override
	public int getUnreadMessagesNum(String botId, String id) {
		Criteria criteria =  createEntityCriteria();
		criteria.add(Restrictions.eq("account_id", id));
		criteria.add(Restrictions.eq("bot_id", botId));
		criteria.add(Restrictions.eq("is_read", false));
		criteria.setProjection(Projections.rowCount());
		long result = (long) criteria.uniqueResult();
		int num = (int) result;
		return num;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Message> getUnreadMessages(String botId, String id) {
		Criteria criteria =  createEntityCriteria();
		criteria.add(Restrictions.eq("account_id", id));
		criteria.add(Restrictions.eq("bot_id", botId));
		criteria.add(Restrictions.eq("is_read", false));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}
	
	
	@Override
	public List<Message> getNotifyMessages(String botId, String id) {
		Criteria criteria =  createEntityCriteria();
		criteria.add(Restrictions.eq("account_id", id));
		criteria.add(Restrictions.eq("bot_id", botId));
		criteria.add(Restrictions.eq("is_update_notfy", true));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}
	

	
	@Override
	public Map<String,BigInteger> countPushMessages(String accountId, String botId){
		String sql ="SELECT count(*), DATE(message_date) FROM chatingplus.message\n" + 
				"WHERE account_id= :account_id and bot_id= :bot_id\n" +
				"and admin_id = '40w9dse0277455f634fw40439sd' group by DATE(message_date)\n" + 
				"order by DATE(message_date) asc";
		Session session = getSession();
		Query query  = session.createSQLQuery(sql);
		
		query.setParameter("account_id", accountId);
		query.setParameter("bot_id", botId);
		
		Map<String,BigInteger> map = new HashMap<>();
		List<Object[]> results = (List<Object[]>)query.list();
		for (Object[] result:results) {
			BigInteger groupNum = (BigInteger)result[0];
			Date groupDate = (Date)result[1];
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			String outputString = formatter.format(groupDate);
			map.put(outputString, groupNum);
		}
		
		return map;
	}
	
	@Override
	public Map<String,BigInteger> countSendMessages(String accountId, String botId){
		String sql ="SELECT count(*), DATE(message_date) FROM chatingplus.message\n" + 
				"WHERE account_id= :account_id and bot_id= :bot_id\n" +
				"and admin_id != '40w9dse0277455f634fw40439sd' and message_send_receive = 'send' group by DATE(message_date)\n" + 
				"order by DATE(message_date) asc";
		Session session = getSession();
		Query query  = session.createSQLQuery(sql);
		
		query.setParameter("account_id", accountId);
		query.setParameter("bot_id", botId);
		
		Map<String,BigInteger> map = new HashMap<>();
		List<Object[]> results = (List<Object[]>)query.list();
		for (Object[] result:results) {
			BigInteger groupNum = (BigInteger)result[0];
			Date groupDate = (Date)result[1];
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			String outputString = formatter.format(groupDate);
			map.put(outputString, groupNum);
		}
		
		return map;
	}
	
	@Override
	public Map<String,BigInteger> countReceiveMessages(String accountId, String botId){
		String sql ="SELECT count(*), DATE(message_date) FROM chatingplus.message\n" + 
				"WHERE account_id= :account_id and bot_id= :bot_id\n" +
				"and admin_id != '40w9dse0277455f634fw40439sd' and message_send_receive = 'receive' group by DATE(message_date)\n"+
				"order by DATE(message_date) asc";
		Session session = getSession();
		Query query  = session.createSQLQuery(sql);
		
		query.setParameter("account_id", accountId);
		query.setParameter("bot_id", botId);
		
		Map<String,BigInteger> map = new HashMap<>();
		List<Object[]> results = (List<Object[]>)query.list();
		for (Object[] result:results) {
			BigInteger groupNum = (BigInteger)result[0];
			Date groupDate = (Date)result[1];
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			String outputString = formatter.format(groupDate);
			map.put(outputString, groupNum);
		}
		
		return map;
	}

	@Override
	public List<Message> getAdminUnreadMessage(String adminId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int countMessages(String accountId, String botId) {
		// TODO Auto-generated method stub
		return 0;
	}


}
