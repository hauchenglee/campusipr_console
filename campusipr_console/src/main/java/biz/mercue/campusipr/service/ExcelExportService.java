package biz.mercue.campusipr.service;

import biz.mercue.campusipr.model.Patent;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public interface ExcelExportService {
    ByteArrayInputStream PatentToExcel(List<String> field_ids, List<Patent> list, String businessId) throws IOException;
}
