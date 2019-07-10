package biz.mercue.campusipr.dao;



import biz.mercue.campusipr.model.PatentFamily;

import java.util.List;

public interface PatentFamilyDao {

	PatentFamily getById(String id);

    PatentFamily getByPatentIdAndBusinessId(String patentId, String businessId);

    List<String> getPatentIds(String familyId);

    void create(PatentFamily family);
	
	void delete(String id);
	
	

	
	
	

}
