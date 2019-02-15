package biz.mercue.campusipr.dao;

import java.util.List;

import biz.mercue.campusipr.model.TempToken;



public interface TempTokenDao {
	
	TempToken getById(String id);
	
	TempToken getByIdAndAdmin(String tokenId,String adminId);
	
	TempToken getAvailableByAdmin(String adminId);
	
	void createToken(TempToken bean);

	void deleteToken(TempToken bean);
	
	

}
