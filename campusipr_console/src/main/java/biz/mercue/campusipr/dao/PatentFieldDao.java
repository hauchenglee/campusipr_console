package biz.mercue.campusipr.dao;

import biz.mercue.campusipr.model.PatentField;

import java.util.List;

public interface PatentFieldDao {
    /**
     * excel專利匯出所有欄位
     * @return result list
     */
    List<PatentField> getAllExcelExportFields();
}
