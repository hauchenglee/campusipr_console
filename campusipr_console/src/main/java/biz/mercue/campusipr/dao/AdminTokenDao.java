package biz.mercue.campusipr.dao;


import java.util.List;

import biz.mercue.campusipr.model.AdminToken;


public interface AdminTokenDao {
	
	int addToken(AdminToken token);
	AdminToken getById(String token);
	List<AdminToken> getByUserId(String userId);
	List<AdminToken> getByAvailableUserId(String userId);

}
