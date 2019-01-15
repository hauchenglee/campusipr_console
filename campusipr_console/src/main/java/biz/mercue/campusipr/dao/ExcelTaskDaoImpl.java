package biz.mercue.campusipr.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import biz.mercue.campusipr.model.ExcelTask;




@Repository("excelTaskDao")
public class ExcelTaskDaoImpl extends AbstractDao<String,  ExcelTask> implements ExcelTaskDao {

	private Logger log = Logger.getLogger(this.getClass().getName());
		
	
	
	
	@Override
	public ExcelTask getById(String id){
		return getByKey(id);
	}
	
	
	@Override
	public void create(ExcelTask bean) {
		persist(bean);
	}
	
	@Override
	public void delete(String id) {
		ExcelTask dbBean = getByKey(id);
		if(dbBean!=null){
			delete(dbBean);
		}
	}
	
	
	@Override
	public ExcelTask getByBusinessId(String businessId,String id) {
		Criteria criteria =  createEntityCriteria();
		criteria.add(Restrictions.eq("excel_task_id", id));
		criteria.createAlias("business","bs");
		criteria.add(Restrictions.eq("bs.business_id", businessId));
		return (ExcelTask) criteria.uniqueResult();
	}
	
	
	@Override
	public List<ExcelTask> getByBusiness(String businessId){
		Criteria criteria =  createEntityCriteria();
		criteria.createAlias("business","bs");
		criteria.add(Restrictions.eq("bs.business_id", businessId));
		criteria.addOrder(Order.desc("create_date"));
		return criteria.list();
	}
	
	@Override
	public List<ExcelTask> getByAdmin(String adminId){
		Criteria criteria =  createEntityCriteria();
		criteria.createAlias("admin","admin");
		criteria.add(Restrictions.eq("admin.admin_id", adminId));
		criteria.addOrder(Order.desc("create_date"));
		return criteria.list();
	}
	
	@Override
	public List<ExcelTask> getNotFinishByAdmin(String adminId){
		Criteria criteria =  createEntityCriteria();
		criteria.createAlias("admin","admin");
		criteria.add(Restrictions.eq("admin.admin_id", adminId));
		criteria.add(Restrictions.eq("is_finish", false));
		criteria.addOrder(Order.desc("create_date"));
		return criteria.list();
	}
	
	
	@Override
	public List<ExcelTask> getNotInformByAdmin(String adminId){
		Criteria criteria =  createEntityCriteria();
		criteria.createAlias("admin","admin");
		criteria.add(Restrictions.eq("admin.admin_id", adminId));
		criteria.add(Restrictions.eq("is_finish", true));
		criteria.add(Restrictions.eq("is_inform", false));
		criteria.addOrder(Order.desc("create_date"));
		return criteria.list();
	}
	
	
//	@Override
//	public ExcelTask getByFileName(String name){
//		Criteria criteria =  createEntityCriteria();
//		criteria.createAlias("admin","admin");
//		criteria.add(Restrictions.eq("admin.admin_id", adminId));
//		criteria.addOrder(Order.desc("create_date"));
//		return criteria.list();
//	}

	
	
	

	
	
	

	

}
