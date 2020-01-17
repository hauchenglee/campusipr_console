package biz.mercue.campusipr.service;

import biz.mercue.campusipr.dao.AdminDao;
import biz.mercue.campusipr.dao.AdminTokenDao;
import biz.mercue.campusipr.dao.TempTokenDao;
import biz.mercue.campusipr.model.*;
import biz.mercue.campusipr.util.Constants;
import biz.mercue.campusipr.util.KeyGeneratorUtils;
import biz.mercue.campusipr.util.MailSender;
import biz.mercue.campusipr.util.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service("adminService")
@Transactional
public class AdminServiceImpl implements AdminService {
    private Logger log = Logger.getLogger(this.getClass().getName());

    @Autowired
    private AdminDao dao;

    @Autowired
    private AdminTokenDao tokenDao;

    @Autowired
    private TempTokenDao tempTokenDao;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public Admin getById(String id) {
        return dao.getById(id);
    }

    @Override
    public int login(String email, String password) {
        Admin dbBean = dao.getByEmail(email);
        if (dbBean == null) {
            return Constants.INT_CANNOT_FIND_DATA;
        }
        if (!encoder.matches(password, dbBean.getAdmin_password())) {
            log.info("login fail, password error");
            return Constants.INT_PASSWORD_ERROR;
        }
        Role roleBean = dbBean.getRole();
        if (roleBean == null) {
            return Constants.INT_NO_PERMISSION;
        }
        List<Permission> list = roleBean.getPermissionList();
        if (!list.isEmpty() && dbBean.isAvailable()) {
            return Constants.INT_SUCCESS;
        } else {
            return Constants.INT_NO_PERMISSION;
        }
    }

    @Override
    public int logout(String adminId) {
        List<AdminToken> list = tokenDao.getByAvailableUserId(adminId);
        list.forEach(bean -> {
            bean.setAvailable(false);
        });
        return Constants.INT_SUCCESS;
    }


    @Override
    public int checkPassword(String adminId, String password) {
        Admin dbBean = dao.getById(adminId);
        if (dbBean == null) {
            return Constants.INT_CANNOT_FIND_DATA;
        }

        if (encoder.matches(password, dbBean.getAdmin_password())) {
            return Constants.INT_SUCCESS;
        } else {
            log.info("login fail, password error");
            return Constants.INT_PASSWORD_ERROR;
        }
    }

    @Override
    public ListQueryForm getRoleBusinessAdminList(String roleId, String businessId, int page) {
        int count = dao.getRoleBusinessAdminCount(roleId, businessId);
        List list = dao.getRoleBusinessAdminList(roleId, businessId, page, Constants.SYSTEM_PAGE_SIZE);
        return new ListQueryForm(count, Constants.SYSTEM_PAGE_SIZE, list);
    }

    @Override
    public ListQueryForm getRoleAdminList(String roleId, int page) {
        int count = dao.getRoleBusinessAdminCount(roleId, null);
        List list = dao.getRoleBusinessAdminList(roleId, null, page, Constants.SYSTEM_PAGE_SIZE);
        return new ListQueryForm(count, Constants.SYSTEM_PAGE_SIZE, list);
    }

    @Override
    public ListQueryForm searchRoleAdminList(String roleId, String businessId, String text, int page) {
        int count = dao.searchRoleAdminListCount(roleId, businessId, text);
        List list = dao.searchRoleAdminList(roleId, businessId, text, page, Constants.SYSTEM_PAGE_SIZE);
        return new ListQueryForm(count, Constants.SYSTEM_PAGE_SIZE, list);
    }

    @Override
    public int createAdmin(Admin admin) {
        Admin dbBean = dao.getByEmail(admin.getAdmin_email());
        if (dbBean == null) {
            admin.setAdmin_id(KeyGeneratorUtils.generateRandomString());
            if (!StringUtils.isNULL(admin.getAdmin_password())) {
                admin.setAdmin_password(encoder.encode(admin.getAdmin_password()));
            }
            admin.setCreate_date(new Date());
            admin.setUpdate_date(new Date());
            dao.createAdmin(admin);
            createTempToken(admin);
            new MailSender().sendActiveAccount(admin);
            return Constants.INT_SUCCESS;
        } else {
            log.info("e-mail:" + dbBean.getAdmin_email() + ",business_id:" + dbBean.getBusiness().getBusiness_id());
            return Constants.INT_USER_DUPLICATE;
        }
    }

    @Override
    public int updateAdmin(Admin admin) {
        Admin dbBean = dao.getById(admin.getAdmin_id());
        if (dbBean == null) {
            return Constants.INT_CANNOT_FIND_DATA;
        }
        dbBean.setAdmin_email(admin.getAdmin_email());
        dbBean.setAdmin_name(admin.getAdmin_name());
        dbBean.setAdmin_unit_name(admin.getAdmin_unit_name());
        dbBean.setRole(admin.getRole());
        dbBean.setUpdate_date(new Date());
        dbBean.setAvailable(admin.isAvailable());
        if (!StringUtils.isNULL(admin.getAdmin_password())) {
            dbBean.setAdmin_password(encoder.encode(admin.getAdmin_password()));
        }
        return Constants.INT_SUCCESS;
    }

    @Override
    public int updatePassword(String adminId, String password) {
        Admin dbBean = dao.getById(adminId);
        if (dbBean == null) {
            return Constants.INT_CANNOT_FIND_DATA;
        }
        dbBean.setAdmin_password(encoder.encode(password));
        return Constants.INT_SUCCESS;
    }

    @Override
    public int deleteAdmin(Admin admin) {
        Admin dbBean = dao.getById(admin.getAdmin_id());
        if (dbBean == null) {
            return Constants.INT_CANNOT_FIND_DATA;
        }
        dao.deleteAdmin(dbBean);
        return Constants.INT_SUCCESS;
    }

    @Override
    public Admin getByEmail(String email) {
        Admin admin = dao.getByEmail(email);
        if (admin != null) {
            Business business = admin.getBusiness();
        }
        return admin;
    }

    @Override
    public int forgetPassword(Admin admin) {
        Admin dbAdmin = dao.getByEmail(admin.getAdmin_email());
        if (dbAdmin == null) {
            return Constants.INT_CANNOT_FIND_DATA;
        }
        log.info("dbAdmin :" + dbAdmin.getAdmin_email());
        createTempToken(dbAdmin);
        new MailSender().sendForgetPassword(dbAdmin);
        return Constants.INT_SUCCESS;
    }

    @Override
    public int resetPassword(Admin admin) {
        TempToken tempBean = tempTokenDao.getById(admin.getToken());
        if (tempBean != null && tempBean.getAdmin() != null) {
            if (tempBean.getExpire_date().after(new Date()) && tempBean.isAvailable()) {
                tempBean.setAvailable(false);
                Admin dbAdmin = tempBean.getAdmin();
                return updatePassword(dbAdmin.getAdmin_id(), admin.getAdmin_password());
            } else {
                return Constants.INT_ACCESS_TOKEN_ERROR;
            }
        } else {
            return Constants.INT_CANNOT_FIND_DATA;
        }
    }

    @Override
    public void changeAdminRole(String adminId1, String adminId2) {
        Admin admin1 = dao.getById(adminId1);
        Admin admin2 = dao.getById(adminId2);
        dao.updateAdminRole(adminId1, admin2.getRole().getRole_id());
        dao.updateAdminRole(adminId2, admin1.getRole().getRole_id());
    }

    private void createTempToken(Admin admin) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 2);
        TempToken token = new TempToken();
        token.setToken_id(KeyGeneratorUtils.generateRandomString());
        token.setAdmin(admin);
        token.setAvailable(true);
        token.setCreate_date(new Date());
        token.setExpire_date(calendar.getTime());
        tempTokenDao.createToken(token);
    }
}
