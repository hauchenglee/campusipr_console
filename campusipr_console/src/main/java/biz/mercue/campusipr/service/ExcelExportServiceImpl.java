package biz.mercue.campusipr.service;

import biz.mercue.campusipr.dao.CountryDao;
import biz.mercue.campusipr.dao.PatentFieldDao;
import biz.mercue.campusipr.model.*;
import biz.mercue.campusipr.util.Constants;
import biz.mercue.campusipr.util.DateUtils;
import biz.mercue.campusipr.util.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ExcelExportServiceImpl implements ExcelExportService {
    private Logger log = Logger.getLogger(this.getClass().getName());

    @Autowired
    private PatentFieldDao fieldDao;

    @Autowired
    private CountryDao countryDao;

    @Override
    public ByteArrayInputStream PatentToExcel(List<String> fieldIds, List<Patent> list, String businessId) throws IOException {
        // Create a Workbook
        // new HSSFWorkbook() for generating `.xls` file
        Workbook workbook = new HSSFWorkbook();

        // CreationHelper helps us create instances of various things like DataFormat,
        // Hyperlink, RichTextString etc, in a format (HSSF, XSSF) independent way */
        CreationHelper createHelper = workbook.getCreationHelper();

        // Create a Sheet
        Sheet sheet = workbook.createSheet("Patent");

        // Create a Font for styling header cells
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 10);
        headerFont.setColor(IndexedColors.GREEN.getIndex());

        // Create a CellStyle with the font
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        // Create a Row
        Row headerRow = sheet.createRow(0);

        // Create Cell Style for formatting Date
        CellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd"));
        dateCellStyle.setAlignment(HorizontalAlignment.LEFT);

        // excel匯出時使用者所勾選的欄位標題field list
        List<String> titleList = new ArrayList<>();
        List<PatentField> fieldList = fieldDao.getAllExcelExportFields();
        for (String fieldId : fieldIds) {
            for (PatentField dbField : fieldList) {
                String dbFieldId = dbField.getField_id(); // database patent_field primary key
                String dbFieldName = dbField.getField_name(); // database patent_field field_name
                if (dbFieldId.equals(fieldId)) {
                    titleList.add(dbFieldName);
                }
            }
        }

        // 設定要匯出的excel title標題欄位樣式
        int columnTitleCount = 0;
        for (String title : titleList) {
            Cell cell = headerRow.createCell(columnTitleCount);
            cell.setCellValue(title);
            cell.setCellStyle(headerCellStyle);
            columnTitleCount++;
        }
        log.info("row number 1, title count: " + (columnTitleCount - 1));

        // 取得所有country
        List<Country> countryList = countryDao.getAll();

        // Create Other rows and cells with employees data
        int rowNum = 1;
        for (Patent patent : list) {
            int columnCount = 0;
            Row row = sheet.createRow(rowNum++);
            log.info("row number " + rowNum + ", patent id: " + patent.getPatent_id());
            for (String fieldId : fieldIds) {
                switch (fieldId) {
                    case Constants.PATENT_NAME_FIELD:
                        log.info("columnCount: " + columnCount + ", PATENT_NAME_FIELD: " + Constants.PATENT_NAME_FIELD);
                        row.createCell(columnCount).setCellValue(patent.getPatent_name());
                        columnCount++;
                        break;
                    case Constants.PATENT_NAME_EN_FIELD:
                        log.info("columnCount: " + columnCount + ", PATENT_NAME_EN_FIELD: " + Constants.PATENT_NAME_EN_FIELD);
                        row.createCell(columnCount).setCellValue(patent.getPatent_name_en());
                        columnCount++;
                        break;
                    case Constants.PATENT_COUNTRY_FIELD:
                        log.info("columnCount: " + columnCount + ", PATENT_COUNTRY_FIELD: " + Constants.PATENT_COUNTRY_FIELD);
                        String countryEN = patent.getPatent_appl_country();
                        String finalCountryName = countryEN;
                        for (Country country : countryList) {
                            String countryName = country.getCountry_name();
                            String aliasName = country.getCountry_alias_name();
                            if (aliasName.contains(countryEN)) {
                                finalCountryName = countryName;
                            }
                        }
                        row.createCell(columnCount).setCellValue(finalCountryName);
                        columnCount++;
                        break;
                    case Constants.PATENT_STATUS_FIELD:
                        log.info("columnCount: " + columnCount + ", PATENT_STATUS_FIELD: " + Constants.PATENT_STATUS_FIELD);
                        if (patent.getListPatentStatus() != null) {
                            String statusStr = "";
                            int index = 0;
                            for (PatentStatus patentStatus : patent.getListPatentStatus()) {
                                Status status = patentStatus.getStatus();
                                if (!StringUtils.isNULL(status.getStatus_desc())
                                        && (businessId.equals(patentStatus.getBusiness_id())) || patentStatus.getBusiness_id() == null) {
                                    statusStr += status.getStatus_desc();
                                }
                                if (patentStatus.getCreate_date() != null
                                        && (businessId.equals(patentStatus.getBusiness_id())) || patentStatus.getBusiness_id() == null) {
                                    statusStr += "_" + DateUtils.getDashFormatDate(patentStatus.getCreate_date());
                                }
                                if (index < patent.getListPatentStatus().size() - 1 && !StringUtils.isNULL(statusStr)) {
                                    statusStr += "、";
                                }
                                index++;
                            }
                            row.createCell(columnCount).setCellValue(statusStr);
                        }
                        columnCount++;
                        break;
                    case Constants.PATENT_COST_FIELD:
                        log.info("columnCount: " + columnCount + ", PATENT_COST_FIELD: " + Constants.PATENT_COST_FIELD);
                        List<PatentCost> costList = patent.getListCost();
                        if (costList != null && !costList.isEmpty()) {
                            StringBuilder costStr = new StringBuilder();
                            for (int i = 0; i < costList.size(); i++) {
                                // 排除不同學校的business id
                                String costBusinessId = costList.get(i).getBusiness_id();
                                if (!costBusinessId.equals(businessId)) continue;

                                String costPrice = String.valueOf(costList.get(i).getCost_price());
                                String costCurrency = costList.get(i).getCost_currency();
                                String costName = costList.get(i).getCost_name();
                                String costUnit = costList.get(i).getCost_unit();
                                String costMemo = costList.get(i).getCost_memo();
                                Date costDate = costList.get(i).getCost_date();
                                String costDateStr = "";
                                if (costDate != null) costDateStr = DateUtils.getSimpleSlashFormatDate(costDate);

                                if (!StringUtils.isNULL(costPrice))
                                    costStr.append("費用金額：").append(costPrice);
                                if (!StringUtils.isNULL(costCurrency))
                                    costStr.append(" ").append(costCurrency);
                                if (!StringUtils.isNULL(costName))
                                    costStr.append("\n").append("項目：").append(costName);
                                if (!StringUtils.isNULL(costUnit))
                                    costStr.append("\n").append("收款人：").append(costUnit);
                                if (!StringUtils.isNULL(costMemo))
                                    costStr.append("\n").append("備註：").append(costMemo);
                                if (!StringUtils.isNULL(costDateStr))
                                    costStr.append("\n").append("日期：").append(costDateStr);
                                if (i != costList.size() - 1)
                                    costStr.append("\n\n");
                            }
                            row.createCell(columnCount).setCellValue(costStr.toString());
                        }
                        columnCount++;
                        break;
                    case Constants.APPLIANT_NAME_FIELD:
                        log.info("columnCount: " + columnCount + ", APPLIANT_NAME_FIELD: " + Constants.APPLIANT_NAME_FIELD);
                        if (patent.getListApplicant() != null) {
                            String applicantStr = "";
                            int lastIndex = patent.getListApplicant().size() - 1;
                            for (Applicant appl : patent.getListApplicant()) {
                                if (appl.getApplicant_id().equals(patent.getListApplicant().get(lastIndex).getApplicant_id())) {
                                    if (!StringUtils.isNULL(appl.getApplicant_name())) {
                                        applicantStr += appl.getApplicant_name();
                                    }
                                    if (!StringUtils.isNULL(appl.getApplicant_name_en())) {
                                        if (!StringUtils.isNULL(applicantStr) && !StringUtils.isNULL(appl.getApplicant_name())) {
                                            applicantStr += "_";
                                        }
                                        applicantStr += appl.getApplicant_name_en();
                                    }
                                } else {
                                    if (!StringUtils.isNULL(appl.getApplicant_name())) {
                                        applicantStr += appl.getApplicant_name();
                                    }
                                    if (!StringUtils.isNULL(appl.getApplicant_name_en())) {
                                        if (!StringUtils.isNULL(applicantStr) && !StringUtils.isNULL(appl.getApplicant_name())) {
                                            applicantStr += "_";
                                        }
                                        applicantStr += appl.getApplicant_name_en();
                                    }
                                    applicantStr += "、";
                                }
                            }
                            row.createCell(columnCount).setCellValue(applicantStr);
                        }
                        columnCount++;
                        break;
                    case Constants.ASSIGNEE_NAME_FIELD:
                        log.info("columnCount: " + columnCount + ", ASSIGNEE_NAME_FIELD: " + Constants.ASSIGNEE_NAME_FIELD);
                        if (patent.getListAssignee() != null) {
                            String assigneeStr = "";
                            int lastIndexAs = patent.getListAssignee().size() - 1;
                            for (Assignee assignee : patent.getListAssignee()) {
                                if (assignee.getAssignee_id().equals(patent.getListAssignee().get(lastIndexAs).getAssignee_id())) {
                                    if (!StringUtils.isNULL(assignee.getAssignee_name())) {
                                        assigneeStr += assignee.getAssignee_name();
                                    }
                                    if (!StringUtils.isNULL(assignee.getAssignee_name_en())) {
                                        if (!StringUtils.isNULL(assigneeStr) && !StringUtils.isNULL(assignee.getAssignee_name())) {
                                            assigneeStr += "_";
                                        }
                                        assigneeStr += assignee.getAssignee_name_en();
                                    }
                                } else {
                                    if (!StringUtils.isNULL(assignee.getAssignee_name())) {
                                        assigneeStr += assignee.getAssignee_name();
                                    }
                                    if (!StringUtils.isNULL(assignee.getAssignee_name_en())) {
                                        if (!StringUtils.isNULL(assigneeStr) && !StringUtils.isNULL(assignee.getAssignee_name())) {
                                            assigneeStr += "_";
                                        }
                                        assigneeStr += assignee.getAssignee_name_en();
                                    }
                                    assigneeStr += "、";
                                }
                            }
                            row.createCell(columnCount).setCellValue(assigneeStr);
                        }
                        columnCount++;
                        break;
                    case Constants.INVENTOR_NAME_FIELD:
                        log.info("columnCount: " + columnCount + ", INVENTOR_NAME_FIELD: " + Constants.INVENTOR_NAME_FIELD);
                        if (patent.getListInventor() != null) {
                            String inventorStr = "";
                            int lastIndexIv = patent.getListInventor().size() - 1;
                            for (Inventor inv : patent.getListInventor()) {
                                if (inv.getInventor_id().equals(patent.getListInventor().get(lastIndexIv).getInventor_id())) {
                                    if (!StringUtils.isNULL(inv.getInventor_name())) {
                                        inventorStr += inv.getInventor_name();
                                    }
                                    if (!StringUtils.isNULL(inv.getInventor_name_en())) {
                                        if (!StringUtils.isNULL(inventorStr) && !StringUtils.isNULL(inv.getInventor_name())) {
                                            inventorStr += "_";
                                        }
                                        inventorStr += inv.getInventor_name_en();
                                    }
                                } else {
                                    if (!StringUtils.isNULL(inv.getInventor_name())) {
                                        inventorStr += inv.getInventor_name();
                                    }
                                    if (!StringUtils.isNULL(inv.getInventor_name_en())) {
                                        if (!StringUtils.isNULL(inventorStr) && !StringUtils.isNULL(inv.getInventor_name())) {
                                            inventorStr += "_";
                                        }
                                        inventorStr += inv.getInventor_name_en();
                                    }
                                    inventorStr += "、";
                                }
                            }

                            row.createCell(columnCount).setCellValue(inventorStr);
                        }
                        columnCount++;
                        break;
                    case Constants.PATENT_APPL_DATE_FIELD:
                        log.info("columnCount: " + columnCount + ", PATENT_APPL_DATE_FIELD: " + Constants.PATENT_APPL_DATE_FIELD);
                        if (patent.getPatent_appl_date() != null) {
                            Cell dateOfApplCell = row.createCell(columnCount);
                            dateOfApplCell.setCellValue(patent.getPatent_appl_date());
                            dateOfApplCell.setCellStyle(dateCellStyle);
                        }
                        columnCount++;
                        break;
                    case Constants.PATENT_APPL_NO_FIELD: //
                        log.info("columnCount: " + columnCount + ", PATENT_APPL_NO_FIELD: " + Constants.PATENT_APPL_NO_FIELD);
                        String applWithoutAt;
                        if (StringUtils.isNULL(patent.getPatent_appl_no())) {
                            applWithoutAt = "";
                        } else {
                            applWithoutAt = StringUtils.getApplNoWithoutAt(patent.getPatent_appl_no());
                        }
                        row.createCell(columnCount).setCellValue(applWithoutAt);
                        columnCount++;
                        break;
                    case Constants.PATENT_PUBLISH_DATE_FIELD:
                        log.info("columnCount: " + columnCount + ", PATENT_PUBLISH_DATE_FIELD: " + Constants.PATENT_PUBLISH_DATE_FIELD);
                        if (patent.getPatent_publish_date() != null) {
                            Cell dateOfPublishCell = row.createCell(columnCount);
                            dateOfPublishCell.setCellValue(patent.getPatent_publish_date());
                            dateOfPublishCell.setCellStyle(dateCellStyle);
                        }
                        columnCount++;
                        break;
                    case Constants.PATENT_PUBLISH_NO_FIELD:
                        log.info("columnCount: " + columnCount + ", PATENT_PUBLISH_NO_FIELD: " + Constants.PATENT_PUBLISH_NO_FIELD);
                        row.createCell(columnCount).setCellValue(patent.getPatent_publish_no());
                        columnCount++;
                        break;
                    case Constants.PATENT_NOTICE_DATE_FIELD:
                        log.info("columnCount: " + columnCount + ", PATENT_NOTICE_DATE_FIELD: " + Constants.PATENT_NOTICE_DATE_FIELD);
                        if (patent.getPatent_notice_date() != null) {
                            Cell dateOfNoticeCell = row.createCell(columnCount);
                            dateOfNoticeCell.setCellValue(patent.getPatent_notice_date());
                            dateOfNoticeCell.setCellStyle(dateCellStyle);
                        }
                        columnCount++;
                        break;
                    case Constants.PATENT_NOTICE_NO_FIELD:
                        log.info("columnCount: " + columnCount + ", PATENT_NOTICE_NO_FIELD: " + Constants.PATENT_NOTICE_NO_FIELD);
                        row.createCell(columnCount).setCellValue(patent.getPatent_notice_no());
                        columnCount++;
                        break;
                    case Constants.PATENT_NO_FIELD:
                        log.info("columnCount: " + columnCount + ", PATENT_NO_FIELD: " + Constants.PATENT_NO_FIELD);
                        row.createCell(columnCount).setCellValue(patent.getPatent_no());
                        columnCount++;
                        break;
                    case Constants.ANNUITY_DATE: // 年費有效日期
                        log.info("columnCount: " + columnCount + ", ANNUITY_DATE: " + Constants.ANNUITY_DATE);
                        if (patent.getPatent_charge_expire_date() != null) {
                            Cell dateOfChargeDateCell = row.createCell(columnCount);
                            dateOfChargeDateCell.setCellValue(patent.getPatent_charge_expire_date());
                            dateOfChargeDateCell.setCellStyle(dateCellStyle);
                        }
                        columnCount++;
                        break;
                    case Constants.ANNUITY_CHARGE_YEAR: // 年費有效年次
                        log.info("columnCount: " + columnCount + ", ANNUITY_CHARGE_YEAR: " + Constants.ANNUITY_CHARGE_YEAR);
                        row.createCell(columnCount).setCellValue(patent.getPatent_charge_duration_year());
                        columnCount++;
                        break;
                    case Constants.PATENT_BDATE: // 專利起始日
                        log.info("columnCount: " + columnCount + ", PATENT_BDATE: " + Constants.PATENT_BDATE);
                        if (patent.getPatent_bdate() != null) {
                            Cell dateOfBDateCell = row.createCell(columnCount);
                            dateOfBDateCell.setCellValue(patent.getPatent_bdate());
                            dateOfBDateCell.setCellStyle(dateCellStyle);
                        }
                        columnCount++;
                        break;
                    case Constants.PATENT_EDATE: // 專利截止日
                        log.info("columnCount: " + columnCount + ", PATENT_EDATE: " + Constants.PATENT_EDATE);
                        if (patent.getPatent_edate() != null && patent.isIs_sync() && !StringUtils.isNULL(patent.getPatent_publish_no())) {
                            Cell dateOfEDateCell = row.createCell(columnCount);
                            dateOfEDateCell.setCellValue(patent.getPatent_edate());
                            dateOfEDateCell.setCellStyle(dateCellStyle);
                        }
                        columnCount++;
                        break;
                    case Constants.PATENT_CANCEL_DATE: // 專利取消日期
                        log.info("columnCount: " + columnCount + ", PATENT_CANCEL_DATE: " + Constants.PATENT_CANCEL_DATE);
                        if (patent.getPatent_cancel_date() != null) {
                            Cell dateOfCancelDateCell = row.createCell(columnCount);
                            dateOfCancelDateCell.setCellValue(patent.getPatent_cancel_date());
                            dateOfCancelDateCell.setCellStyle(dateCellStyle);
                        }
                        columnCount++;
                        break;
                    case Constants.PATENT_ABSTRACT: // 專利摘要
                        log.info("columnCount: " + columnCount + ", PATENT_ABSTRACT: " + Constants.PATENT_ABSTRACT);
                        if (patent.getPatentAbstract() != null) {
                            String abs = patent.getPatentAbstract().getContext_abstract();
                            if (abs.length() > 5000) {
                                abs = abs.substring(0, 5000) + "...(完整內容請由官方專利局取得)";
                            }
                            row.createCell(columnCount).setCellValue(abs.trim());
                        }
                        columnCount++;
                        break;
                    case Constants.PATENT_CLAIM: // 專利範圍
                        log.info("columnCount: " + columnCount + ", PATENT_CLAIM: " + Constants.PATENT_CLAIM);
                        if (patent.getPatentClaim() != null) {
                            String claim = patent.getPatentClaim().getContext_claim();
                            if (claim.length() > 5000) {
                                claim = claim.substring(0, 5000) + "...(完整內容請由官方專利局取得)";
                            }
                            row.createCell(columnCount).setCellValue(claim.trim());
                        }
                        columnCount++;
                        break;
                    case Constants.PATENT_DESC: // 專利描述
                        log.info("columnCount: " + columnCount + ", PATENT_DESC: " + Constants.PATENT_DESC);
                        if (patent.getPatentDesc() != null) {
                            String desc = patent.getPatentDesc().getContext_desc();
                            if (desc.length() > 5000) {
                                desc = desc.substring(0, 5000) + "...(完整內容請由官方專利局取得)";
                            }
                            row.createCell(columnCount).setCellValue(desc.trim());
                        }
                        columnCount++;
                        break;
                    case Constants.PATENT_IPC: // 國際專利分類
                        log.info("columnCount: " + columnCount + ", PATENT_IPC: " + Constants.PATENT_IPC);
                        if (patent.getListIPC() != null) {
                            String ipcStr = "";
                            int lastIndexIpc = patent.getListIPC().size() - 1;
                            for (IPCClass ipc : patent.getListIPC()) {
                                if (ipc != null) {
                                    if (ipc.getIpc_class_id().equals(
                                            patent.getListIPC().get(lastIndexIpc).getIpc_class_id())) {
                                        if (!StringUtils.isNULL(ipc.getIpc_class_id())) {
                                            ipcStr += ipc.getIpc_class_id();
                                        }
                                        if (!StringUtils.isNULL(ipc.getIpc_version())) {
                                            ipcStr += "(" + ipc.getIpc_version() + ")";
                                        }
                                    } else {
                                        if (!StringUtils.isNULL(ipc.getIpc_class_id())) {
                                            ipcStr += ipc.getIpc_class_id();
                                        }
                                        if (!StringUtils.isNULL(ipc.getIpc_version())) {
                                            ipcStr += "(" + ipc.getIpc_version() + ")";
                                        }
                                        ipcStr += "\n";
                                    }
                                }
                            }
                            row.createCell(columnCount).setCellValue(ipcStr);
                        }
                        columnCount++;
                        break;
                    case Constants.SCHOOL_DEPARTMENT_FIELD: // 學校科系
                        log.info("columnCount: " + columnCount + ", SCHOOL_DEPARTMENT_FIELD: " + Constants.SCHOOL_DEPARTMENT_FIELD);
                        if (patent.getListDepartment() != null) {
                            String departmentStr = "";
                            int lastIndexDe = patent.getListDepartment().size() - 1;
                            for (Department dep : patent.getListDepartment()) {
                                if (dep.getDepartment_id().equals(patent.getListDepartment().get(lastIndexDe).getDepartment_id())
                                        && businessId.equals(dep.getBusiness_id())) {
                                    if (!StringUtils.isNULL(dep.getDepartment_name())) {
                                        departmentStr += dep.getDepartment_name();
                                    }
                                    if (!StringUtils.isNULL(dep.getDepartment_name_en())
                                            && businessId.equals(dep.getBusiness_id())) {
                                        if (!StringUtils.isNULL(departmentStr) && !StringUtils.isNULL(dep.getDepartment_name())) {
                                            departmentStr += "_";
                                        }
                                        departmentStr += dep.getDepartment_name_en();
                                    }
                                } else {
                                    if (!StringUtils.isNULL(dep.getDepartment_name()) && businessId.equals(dep.getBusiness_id())) {
                                        departmentStr += dep.getDepartment_name();
                                    }
                                    if (!StringUtils.isNULL(dep.getDepartment_name_en()) && businessId.equals(dep.getBusiness_id())) {
                                        if (!StringUtils.isNULL(departmentStr) && !StringUtils.isNULL(dep.getDepartment_name())) {
                                            departmentStr += "_";
                                        }
                                        departmentStr += dep.getDepartment_name_en();
                                    }
                                    if (!StringUtils.isNULL(departmentStr)) {
                                        departmentStr += "、";
                                    }
                                }
                            }
                            row.createCell(columnCount).setCellValue(departmentStr);
                        }
                        columnCount++;
                        break;
                    case Constants.SCHOOL_NO_FIELD: // 學校編號
                        log.info("columnCount: " + columnCount + ", SCHOOL_NO_FIELD: " + Constants.SCHOOL_NO_FIELD);
                        if (patent.getListExtension() != null) {
                            for (PatentExtension extension : patent.getListExtension()) {
                                if (businessId.equals(extension.getBusiness_id())) {
                                    row.createCell(columnCount).setCellValue(extension.getBusiness_num());
                                }
                            }
                        }
                        columnCount++;
                        break;
                    case Constants.SCHOOL_APPL_YEAR_FIELD: // 申請年度
                        log.info("columnCount: " + columnCount + ", SCHOOL_APPL_YEAR_FIELD: " + Constants.SCHOOL_APPL_YEAR_FIELD);
                        if (patent.getListExtension() != null) {
                            for (PatentExtension extension : patent.getListExtension()) {
                                if (businessId.equals(extension.getBusiness_id())) {
                                    row.createCell(columnCount).setCellValue(extension.getExtension_appl_year());
                                }
                            }
                        }
                        columnCount++;
                        break;
                    case Constants.SCHOOL_SUBSIDY_UNIT: // 補助單位
                        log.info("columnCount: " + columnCount + ", SCHOOL_SUBSIDY_UNIT: " + Constants.SCHOOL_SUBSIDY_UNIT);
                        if (patent.getListExtension() != null) {
                            for (PatentExtension extension : patent.getListExtension()) {
                                if (businessId.equals(extension.getBusiness_id())) {
                                    row.createCell(columnCount).setCellValue(extension.getExtension_subsidy_unit());
                                }
                            }
                        }
                        columnCount++;
                        break;
                    case Constants.SCHOOL_SUBSIDY_NO: // 補助編號
                        log.info("columnCount: " + columnCount + ", SCHOOL_SUBSIDY_NO: " + Constants.SCHOOL_SUBSIDY_NO);
                        if (patent.getListExtension() != null) {
                            for (PatentExtension extension : patent.getListExtension()) {
                                if (businessId.equals(extension.getBusiness_id())) {
                                    row.createCell(columnCount).setCellValue(extension.getExtension_subsidy_num());
                                }
                            }
                        }
                        columnCount++;
                        break;
                    case Constants.SCHOOL_SUBSIDY_PLAN: // 補助計畫名稱
                        log.info("columnCount: " + columnCount + ", SCHOOL_SUBSIDY_PLAN: " + Constants.SCHOOL_SUBSIDY_PLAN);
                        if (patent.getListExtension() != null) {
                            for (PatentExtension extension : patent.getListExtension()) {
                                if (businessId.equals(extension.getBusiness_id())) {
                                    row.createCell(columnCount).setCellValue(extension.getExtension_subsidy_plan());
                                }
                            }
                        }
                        columnCount++;
                        break;
                    case Constants.SCHOOL_AGENT: // 事務所
                        log.info("columnCount: " + columnCount + ", SCHOOL_AGENT: " + Constants.SCHOOL_AGENT);
                        if (patent.getListExtension() != null) {
                            for (PatentExtension extension : patent.getListExtension()) {
                                if (businessId.equals(extension.getBusiness_id())) {
                                    row.createCell(columnCount).setCellValue(extension.getExtension_agent());
                                }
                            }
                        }
                        columnCount++;
                        break;
                    case Constants.SCHOOL_AGENT_NO: // 事務所編號
                        log.info("columnCount: " + columnCount + ", SCHOOL_AGENT_NO: " + Constants.SCHOOL_AGENT_NO);
                        if (patent.getListExtension() != null) {
                            for (PatentExtension extension : patent.getListExtension()) {
                                if (businessId.equals(extension.getBusiness_id())) {
                                    row.createCell(columnCount).setCellValue(extension.getExtension_agent_num());
                                }
                            }
                        }
                        columnCount++;
                        break;
                    case Constants.SCHOOL_MEMO_FIELD: // 學校備註
                        log.info("columnCount: " + columnCount + ", SCHOOL_MEMO_FIELD: " + Constants.SCHOOL_MEMO_FIELD);
                        if (patent.getListExtension() != null) {
                            for (PatentExtension extension : patent.getListExtension()) {
                                if (businessId.equals(extension.getBusiness_id())) {
                                    row.createCell(columnCount).setCellValue(extension.getExtension_memo());
                                }
                            }
                        }
                        columnCount++;
                        break;
                    case Constants.SCHOOL_OTHER_INFO: // 其他資訊
                        log.info("columnCount: " + columnCount + ", SCHOOL_OTHER_INFO: " + Constants.SCHOOL_OTHER_INFO);
                        if (patent.getListExtension() != null) {
                            for (PatentExtension extension : patent.getListExtension()) {
                                if (businessId.equals(extension.getBusiness_id())) {
                                    row.createCell(columnCount).setCellValue(extension.getExtension_other_information());
                                }
                            }
                        }
                        columnCount++;
                        break;
                    default:
                        break;
                }
            }
            log.info("row: " + rowNum + ", end ===");
        }

        // Resize all columns to fit the content size
        // 依據儲存格內容，調整欄位大小
        for (int i = 0; i < titleList.size(); i++) {
            sheet.autoSizeColumn(i);
        }

        // Write the output to a file
        ByteArrayOutputStream fileOut = new ByteArrayOutputStream();
        workbook.write(fileOut);
        fileOut.close();

        // Closing the workbook
        workbook.close();

        return new ByteArrayInputStream(fileOut.toByteArray());
    }
}
