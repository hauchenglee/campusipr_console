package biz.mercue.campusipr.service;

import biz.mercue.campusipr.model.Admin;
import biz.mercue.campusipr.model.ListQueryForm;

public interface AdminService {
    Admin getById(String id);

    Admin getByEmail(String email);

    int login(String email, String password);

    int logout(String adminId);

    int checkPassword(String adminId, String password);

    int createAdmin(Admin admin);

    int updateAdmin(Admin admin);

    int deleteAdmin(Admin admin);

    int updatePassword(String adminId, String password);

    int forgetPassword(Admin admin);

    int resetPassword(Admin admin);

    ListQueryForm getRoleBusinessAdminList(String roleId, String businessId, int page);

    ListQueryForm getRoleAdminList(String roleId, int page);

    ListQueryForm searchRoleAdminList(String roleId, String businessId, String text, int page);

    void changeAdminRole(String admin1, String admin2);
}
