package biz.mercue.campusipr.dao;


import java.util.Date;
import java.util.List;

import biz.mercue.campusipr.model.Applicant;
import biz.mercue.campusipr.model.IPCClass;
import biz.mercue.campusipr.model.PatentStatus;
import biz.mercue.campusipr.model.Status;


public interface IPCClassDao {
	
	IPCClass getByIdAndVersion(String id);
	
	void create(IPCClass ps);
}
