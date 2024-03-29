package biz.mercue.campusipr.dao;

import biz.mercue.campusipr.model.Admin;
import biz.mercue.campusipr.util.Constants;
import biz.mercue.campusipr.util.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("adminDao")
public class AdminDaoImpl extends AbstractDao<String, Admin> implements AdminDao {

    @Override
    public Admin getById(String id) {
        return getByKey(id);
    }

    @Override
    public List<Admin> getRoleBusinessAdminList(String roleId, String businessId, int page, int pageSize) {
        Criteria crit = createEntityCriteria();
        crit.createAlias("role", "role");
        crit.add(Restrictions.eq("role.role_id", roleId));
        if (!StringUtils.isNULL(businessId)) {
            crit.createAlias("business", "business");
            crit.add(Restrictions.eq("business.business_id", businessId));
        }
        crit.setFirstResult((page - 1) * pageSize);
        crit.setMaxResults(pageSize);
        return crit.list();
    }

    @Override
    public int getRoleBusinessAdminCount(String roleId, String businessId) {
        Criteria crit = createEntityCriteria();
        crit.createAlias("role", "role");
        crit.add(Restrictions.eq("role.role_id", roleId));
        if (!StringUtils.isNULL(businessId)) {
            crit.createAlias("business", "business");
            crit.add(Restrictions.eq("business.business_id", businessId));
        }
        crit.setProjection(Projections.rowCount());
        long count = (long) crit.uniqueResult();
        return (int) count;
    }

    @Override
    public List<Admin> searchRoleAdminList(String roleId, String businessId, String text, int page, int pageSize) {
        Criteria crit = createEntityCriteria();
        crit.createAlias("role", "role");
        crit.add(Restrictions.eq("role.role_id", roleId));
        if (!StringUtils.isNULL(businessId)) {
            crit.createAlias("business", "business");
            crit.add(Restrictions.eq("business.business_id", businessId));
        }
        Criterion field1 = Restrictions.like("admin_name", "%" + text + "%");
        Criterion field2 = Restrictions.like("admin_email", "%" + text + "%");
        crit.add(Restrictions.or(field1, field2));
        crit.setFirstResult((page - 1) * pageSize);
        crit.setMaxResults(pageSize);
        return crit.list();
    }

    @Override
    public int searchRoleAdminListCount(String roleId, String businessId, String text) {
        Criteria crit = createEntityCriteria();
        crit.createAlias("role", "role");
        crit.add(Restrictions.eq("role.role_id", roleId));
        if (!StringUtils.isNULL(businessId)) {
            crit.createAlias("business", "business");
            crit.add(Restrictions.eq("business.business_id", businessId));
        }

        Criterion field1 = Restrictions.like("admin_name", "%" + text + "%");
        Criterion field2 = Restrictions.like("admin_email", "%" + text + "%");
        crit.add(Restrictions.or(field1, field2));
        crit.setProjection(Projections.rowCount());
        long count = (long) crit.uniqueResult();
        return (int) count;
    }

    @Override
    public Admin getByEmail(String email) {
        Criteria criteria = createEntityCriteria();
        criteria.add(Restrictions.eq("admin_email", email));
        return (Admin) criteria.uniqueResult();
    }

    @Override
    public void createAdmin(Admin bean) {
        persist(bean);
    }

    @Override
    public void deleteAdmin(Admin bean) {
        delete(bean);
    }

    @Override
    public List<Admin> getAllAdminList() {
        Criteria criteria = createEntityCriteria();
        return criteria.list();
    }

    @Override
    public List<Admin> getPlatformAdminList() {
        String hql = "select adm from Admin adm where adm.role.role_id = :ROLE_PLATFORM_MANAGER or adm.role.role_id = :ROLE_PLATFORM_PATENT";
        Session session = getSession();
        Query query = session.createQuery(hql);
        query.setParameter("ROLE_PLATFORM_MANAGER", Constants.ROLE_PLATFORM_MANAGER);
        query.setParameter("ROLE_PLATFORM_PATENT", Constants.ROLE_PLATFORM_PATENT);
        return query.list();
    }

    @Override
    public List<Admin> getSchoolAdminList() {
        String hql = "select adm from Admin adm where adm.role.role_id = :ROLE_BUSINESS_MANAGER" +
                " or adm.role.role_id = :ROLE_BUSINESS_PATENT";
        Session session = getSession();
        Query query = session.createQuery(hql);
        query.setParameter("ROLE_BUSINESS_MANAGER", Constants.ROLE_BUSINESS_MANAGER);
        query.setParameter("ROLE_BUSINESS_PATENT", Constants.ROLE_BUSINESS_PATENT);
        return query.list();
    }

    @Override
    public void updateAdminRole(String adminId, String roleId) {
        String hql = "update Admin as admin " +
                "set role_id = :roleId " +
                "where admin_id = :adminId";
        Session session = getSession();
        Query query = session.createQuery(hql);
        query.setParameter("adminId", adminId);
        query.setParameter("roleId", roleId);
        query.executeUpdate();
    }
}
