package biz.mercue.campusipr.service;

import biz.mercue.campusipr.dao.TechnologyDao;
import biz.mercue.campusipr.model.Technology;
import biz.mercue.campusipr.model.TechnologyExtension;
import biz.mercue.campusipr.model.TechnologyInventor;
import biz.mercue.campusipr.model.TechnologyStatus;
import biz.mercue.campusipr.util.Constants;
import biz.mercue.campusipr.util.KeyGeneratorUtils;
import biz.mercue.campusipr.util.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("technologyService")
@Transactional
public class TechnologyServiceImpl implements TechnologyService {
    private Logger log = Logger.getLogger(this.getClass().getName());

    @Autowired
    TechnologyDao technologyDao;

    @Override
    public Technology getById(String id) {
        Technology dbTechnology = technologyDao.getById(id);

        if (dbTechnology != null) {
            dbTechnology.getListInventor().size();
            dbTechnology.getListStatus().size();
            dbTechnology.getListExtension().size();
        }

        return dbTechnology;
    }

    @Override
    public int addTechnology(Technology technology) {
        try {
            log.info("add technology");
            if (StringUtils.isNULL(technology.getTechnology_id())) {
                technology.setTechnology_id(KeyGeneratorUtils.generateRandomString());
            }

//            if (technology.getBusiness() == null) {
//                log.error("no business data");
//                return Constants.INT_DATA_ERROR;
//            }

            List<TechnologyInventor> inventorList = technology.getListInventor();
            if (inventorList != null || !inventorList.isEmpty()) {
                for (TechnologyInventor inventor : inventorList) {
                    inventor.setInventor_id(KeyGeneratorUtils.generateRandomString());
                    inventor.setTechnology(technology);
                }
            }

            List<TechnologyStatus> statusList = technology.getListStatus();
            if (statusList != null || !statusList.isEmpty()) {
                for (TechnologyStatus status : statusList) {
                    status.setStatus_id(KeyGeneratorUtils.generateRandomString());
                    status.setTechnology(technology);
                }
            }

            List<TechnologyExtension> extensionList = technology.getListExtension();
            if (extensionList != null || !extensionList.isEmpty()) {
                for (TechnologyExtension extension : extensionList) {
                    extension.setExtension_id(KeyGeneratorUtils.generateRandomString());
                    extension.setTechnology(technology);
                }
            }

            technologyDao.create(technology);
            return Constants.INT_SUCCESS;
        } catch (Exception e) {
            log.error(e.getMessage());
            return Constants.INT_SYSTEM_PROBLEM;
        }
    }
}
