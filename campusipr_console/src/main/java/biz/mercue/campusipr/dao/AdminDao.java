package biz.mercue.campusipr.dao;

import biz.mercue.campusipr.model.Admin;

import java.util.List;

public interface AdminDao {
    Admin getById(String id);

    Admin getByEmail(String email);

    void createAdmin(Admin bean);

    void deleteAdmin(Admin bean);

    List<Admin> getAllAdminList();

    List<Admin> getRoleBusinessAdminList(String roleId, String businessId, int page, int pageSize);

    int getRoleBusinessAdminCount(String roleId, String businessId);

    List<Admin> searchRoleAdminList(String roleId, String businessId, String text, int page, int pageSize);

    int searchRoleAdminListCount(String roleId, String businessId, String text);

    List<Admin> getPlatformAdminList();

    List<Admin> getSchoolAdminList();

    void updateAdminRole(String adminId, String roleId);
}
