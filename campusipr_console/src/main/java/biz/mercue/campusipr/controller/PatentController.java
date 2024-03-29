package biz.mercue.campusipr.controller;

import biz.mercue.campusipr.model.*;
import biz.mercue.campusipr.service.*;
import biz.mercue.campusipr.util.*;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@RestController
public class PatentController {
    private Logger log = Logger.getLogger(this.getClass().getName());

    @Autowired
    private PatentService patentService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private AdminTokenService adminTokenService;

    @Autowired
    private BusinessService businessService;

    @Autowired
    private ExcelTaskService excelTaskService;

    @Autowired
    private FieldService fieldService;

    @Autowired
    private ExcelExportService excelExportService;

    @PostMapping(value = "/api/addpatent", produces = Constants.CONTENT_TYPE_JSON)
    public String addPatent(HttpServletRequest request,
                            @RequestBody String receiveJSONString,
                            @RequestParam(value = "businessId", required = false) String businessId) throws Exception {
        AdminToken tokenBean = adminTokenService.getById(JWTUtils.getJwtToken(request));
        if (tokenBean == null) throw new CustomException.TokenNullException();

        Patent patent = (Patent) JacksonJSONUtils.readValue(receiveJSONString, Patent.class);
        BeanResponseBody responseBody = new BeanResponseBody();
        String ip = request.getRemoteAddr();
        patent.setPatent_id(KeyGeneratorUtils.generateRandomString());
        patent.setBusiness(tokenBean.getBusiness());
        patent.setAdmin(tokenBean.getAdmin());
        patent.setAdmin_ip(ip);

        int taskResult = patentService.addPatent(patent);
        patentService.patentHistoryFirstAdd(patent, patent.getPatent_id(), businessId);
        responseBody.setCode(taskResult);
        responseBody.setBean(patent);
        return responseBody.getJacksonString(View.Patent.class);
    }

    @PostMapping(value = "/api/syncpatentdata", produces = Constants.CONTENT_TYPE_JSON)
    public String syncPatentData(HttpServletRequest request,
                                 @RequestBody String receiveJSONString,
                                 @RequestParam(value = "no", required = false) String patentApplNo) throws Exception {
        AdminToken tokenBean = adminTokenService.getById(JWTUtils.getJwtToken(request));
        if (tokenBean == null) throw new CustomException.TokenNullException();

        BeanResponseBody responseBody = new BeanResponseBody();
        String ip = request.getRemoteAddr();
        Patent patent = (Patent) JacksonJSONUtils.readValue(receiveJSONString, Patent.class);
        Admin admin = adminService.getById(Constants.SYSTEM_ADMIN);
        patent.setAdmin(admin);
        patent.setEdit_source(Patent.EDIT_SOURCE_SERVICE);
        patent.setBusiness(tokenBean.getBusiness());
        patent.setAdmin_ip(ip);

        int taskResult = patentService.syncPatentData(patent);
        responseBody.setCode(taskResult);
        responseBody.setBean(patent);
        return responseBody.getJacksonString(View.PatentDetail.class);
    }

    @PostMapping(value = "/api/addpatentbyapplno", produces = Constants.CONTENT_TYPE_JSON)
    public String addPatentByApplNo(HttpServletRequest request,
                                    @RequestBody String receiveJSONString,
                                    @RequestParam(value = "businessId", required = false) String businessId) throws Exception {
        AdminToken tokenBean = adminTokenService.getById(JWTUtils.getJwtToken(request));
        if (tokenBean == null) throw new CustomException.TokenNullException();
        if (StringUtils.isNULL(businessId)) {
            businessId = tokenBean.getBusiness_id();
        }

        BeanResponseBody responseBody = new BeanResponseBody();
        String ip = request.getRemoteAddr();
        Patent patent = (Patent) JacksonJSONUtils.readValue(receiveJSONString, Patent.class);
        Admin admin = adminService.getById(Constants.SYSTEM_ADMIN);
        Business business = businessService.getById(businessId);
        patent.setAdmin(admin);
        patent.setEdit_source(Patent.EDIT_SOURCE_SERVICE);
        patent.setBusiness(business);
        patent.setAdmin_ip(ip);

        int taskResult = patentService.addPatentByApplNo(patent, tokenBean.getAdmin(), business, patent.getSourceFrom());
        responseBody.setCode(taskResult);
        responseBody.setBean(patent);
        return responseBody.getJacksonString(View.PatentDetail.class);
    }

    @PostMapping(value = "/api/checknopublicapplno", produces = Constants.CONTENT_TYPE_JSON)
    public String checkNoPublicApplNo(HttpServletRequest request, @RequestBody String receiveJSONString) throws Exception {
        AdminToken tokenBean = adminTokenService.getById(JWTUtils.getJwtToken(request));
        if (tokenBean == null) throw new CustomException.TokenNullException();

        JSONResponseBody responseBody = new JSONResponseBody();
        String ip = request.getRemoteAddr();
        Patent patent = (Patent) JacksonJSONUtils.readValue(receiveJSONString, Patent.class);
        Admin admin = adminService.getById(Constants.SYSTEM_ADMIN);
        patent.setAdmin(admin);
        patent.setEdit_source(Patent.EDIT_SOURCE_SERVICE);
        patent.setBusiness(tokenBean.getBusiness());
        patent.setAdmin_ip(ip);

        JSONObject jsonObject = patentService.checkNoPublicApplNo(patent, tokenBean.getBusiness());
        responseBody.setCode(Constants.INT_SUCCESS);
        responseBody.setData(jsonObject);
        return responseBody.toString();
    }

    @PostMapping(value = "/api/mergepatent", produces = Constants.CONTENT_TYPE_JSON)
    public String mergePatent(HttpServletRequest request, @RequestBody String receiveJSONString) throws Exception {
        AdminToken tokenBean = adminTokenService.getById(JWTUtils.getJwtToken(request));
        if (tokenBean == null) throw new CustomException.TokenNullException();

        BeanResponseBody responseBody = new BeanResponseBody();
        String ip = request.getRemoteAddr();
        Patent patent = (Patent) JacksonJSONUtils.readValue(receiveJSONString, Patent.class);
        Admin admin = adminService.getById(Constants.SYSTEM_ADMIN);
        patent.setAdmin(admin);
        patent.setEdit_source(Patent.EDIT_SOURCE_SERVICE);
        patent.setBusiness(tokenBean.getBusiness());
        patent.setAdmin_ip(ip);

        int taskResult = patentService.addPatentByNoPublicApplNo(patent, tokenBean.getBusiness(), admin);
        responseBody.setCode(taskResult);
        responseBody.setBean(patent);
        return responseBody.getJacksonString(View.PatentDetail.class);
    }

    @PostMapping(value = "/api/updatepatent", produces = Constants.CONTENT_TYPE_JSON)
    public String updatePatent(HttpServletRequest request, @RequestBody String receiveJSONString) throws Exception {
        AdminToken tokenBean = adminTokenService.getById(JWTUtils.getJwtToken(request));
        if (tokenBean == null) throw new CustomException.TokenNullException();

        Permission permission = permissionService.getSettingPermissionByModule(Constants.MODEL_CODE_PATENT_CONTENT, Constants.EDIT);
        if (!tokenBean.checkPermission(permission.getPermission_id())) {
            throw new CustomException.NoPermission();
        }

        StringResponseBody responseBody = new StringResponseBody();
        Patent patent = (Patent) JacksonJSONUtils.readValue(receiveJSONString, Patent.class);
        patent.setEdit_source(Patent.EDIT_SOURCE_HUMAN);
        String ip = request.getRemoteAddr();
        patent.setAdmin(tokenBean.getAdmin());
        patent.setAdmin_ip(ip);

        int taskResult = patentService.authorizedUpdatePatent(tokenBean.getBusiness().getBusiness_id(), patent);
        responseBody.setCode(taskResult);
        return responseBody.getJacksonString(View.Patent.class);
    }

    @GetMapping(value = "/api/patentlist", produces = Constants.CONTENT_TYPE_JSON)
    public String getPatentList(HttpServletRequest request,
                                @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                @RequestParam(value = "order_field", required = false, defaultValue = "") String fieldId,
                                @RequestParam(value = "asc", required = false, defaultValue = "1") int is_asc) throws Exception {
        AdminToken tokenBean = adminTokenService.getById(JWTUtils.getJwtToken(request));
        if (tokenBean == null) throw new CustomException.TokenNullException();

        Permission permission = permissionService.getSettingPermissionByModule(Constants.MODEL_CODE_PATENT_CONTENT, Constants.VIEW);
        if (!tokenBean.checkPermission(permission.getPermission_id())) {
            throw new CustomException.NoPermission();
        }

        ListResponseBody responseBody = new ListResponseBody();
        ListQueryForm form = patentService.getByBusinessId(tokenBean.getBusiness().getBusiness_id(), page, fieldId, is_asc);
        responseBody.setCode(Constants.INT_SUCCESS);
        responseBody.setListQuery(form);
        return responseBody.getJacksonString(View.Patent.class);
    }

    @GetMapping(value = "/api/getpatentbyid/{patentId}", produces = Constants.CONTENT_TYPE_JSON)
    public String getPatentbyId(HttpServletRequest request, @PathVariable String patentId) throws Exception {
        BeanResponseBody responseBody = new BeanResponseBody();
        AdminToken tokenBean = adminTokenService.getById(JWTUtils.getJwtToken(request));
        if (tokenBean == null) throw new CustomException.TokenNullException();

        Permission permission = permissionService.getSettingPermissionByModule(Constants.MODEL_CODE_PATENT_CONTENT, Constants.VIEW);
        if (!tokenBean.checkPermission(permission.getPermission_id())) {
            throw new CustomException.NoPermission();
        }

        if (tokenBean.checkPermission(Constants.PERMISSION_CROSS_BUSINESS)) {
            Patent patent = patentService.getById(null, patentId);
            responseBody.setCode(Constants.INT_SUCCESS);
            responseBody.setBean(patent);
            return responseBody.getJacksonString(View.PatentDetail.class);
        } else {
            Patent patent = patentService.getById(tokenBean.getBusiness().getBusiness_id(), patentId);
            responseBody.setCode(Constants.INT_SUCCESS);
            responseBody.setBean(patent);
            return responseBody.getJacksonString(View.PatentEnhance.class);
        }
    }

    @GetMapping(value = "/api/getallpatentbyid/{patentId}", produces = Constants.CONTENT_TYPE_JSON)
    public String getAllPatentbyId(HttpServletRequest request, @PathVariable String patentId) throws Exception {
        AdminToken tokenBean = adminTokenService.getById(JWTUtils.getJwtToken(request));
        if (tokenBean == null) throw new CustomException.TokenNullException();

        BeanResponseBody responseBody = new BeanResponseBody();
        Patent patent = patentService.getById(null, patentId);
        responseBody.setCode(Constants.INT_SUCCESS);
        responseBody.setBean(patent);
        return responseBody.getJacksonString(View.PatentDetail.class);
    }

    @PostMapping(value = "/api/deletepatentbyid/{patentId}", produces = Constants.CONTENT_TYPE_JSON)
    public String deletePatentById(HttpServletRequest request, @PathVariable String patentId) throws Exception {
        AdminToken tokenBean = adminTokenService.getById(JWTUtils.getJwtToken(request));
        if (tokenBean == null) throw new CustomException.TokenNullException();

        BeanResponseBody responseBody = new BeanResponseBody();
        patentService.deleteById(patentId, tokenBean.getBusiness_id());
        responseBody.setCode(Constants.INT_SUCCESS);
        return responseBody.getJacksonString(View.PatentDetail.class);
    }

    @PostMapping(value = "/api/deletepatentbyids", produces = Constants.CONTENT_TYPE_JSON)
    public String deletePatentByIds(HttpServletRequest request, @RequestBody String receiveJSONString) throws Exception {
        AdminToken tokenBean = adminTokenService.getById(JWTUtils.getJwtToken(request));
        if (tokenBean == null) throw new CustomException.TokenNullException();

        BeanResponseBody responseBody = new BeanResponseBody();
        JSONObject jsonObject = new JSONObject(receiveJSONString);
        String jsonPid = jsonObject.optJSONArray("patent_ids").toString();
        TypeReference<?> typeReference = new TypeReference<List<String>>() {
        };
        List<String> patentIds = (List<String>) JacksonJSONUtils.readValue(jsonPid, typeReference);

        patentService.deleteByIds(patentIds, tokenBean.getBusiness_id());
        responseBody.setCode(Constants.INT_SUCCESS);
        return responseBody.getJacksonString(View.PatentDetail.class);
    }

    @PostMapping(value = "/api/getpatenthistorybyid", produces = Constants.CONTENT_TYPE_JSON)
    public String getPatentHistorybyId(HttpServletRequest request,
                                       @RequestBody String receiveJSONString,
                                       @RequestParam(value = "page", required = false, defaultValue = "1") int page) throws Exception {
        AdminToken tokenBean = adminTokenService.getById(JWTUtils.getJwtToken(request));
        if (tokenBean == null) throw new CustomException.TokenNullException();

        Permission permission = permissionService.getSettingPermissionByModule(Constants.MODEL_CODE_PATENT_CONTENT, Constants.VIEW);
        if (!tokenBean.checkPermission(permission.getPermission_id())) {
            throw new CustomException.NoPermission();
        }

        ListResponseBody responseBody = new ListResponseBody();
        JSONObject jsonObject = new JSONObject(receiveJSONString);
        String patentId = jsonObject.getString("patent_id");
        String fieldId = jsonObject.getString("field_id");
        if (tokenBean.checkPermission(Constants.PERMISSION_CROSS_BUSINESS)) {
            ListQueryForm form = patentService.getHistoryBypatentId(null, patentId, fieldId, page);
            responseBody.setCode(Constants.INT_SUCCESS);
            responseBody.setListQuery(form);
        } else {
            ListQueryForm form = patentService.getHistoryBypatentId(tokenBean.getBusiness().getBusiness_id(), patentId, fieldId, page);
            responseBody.setCode(Constants.INT_SUCCESS);
            responseBody.setListQuery(form);
        }
        return responseBody.getJacksonString(View.PatentHistory.class);
    }

    @PostMapping(value = "/api/combinepatentfamily", produces = Constants.CONTENT_TYPE_JSON)
    public String combinePatentFamily(HttpServletRequest request,
                                      @RequestBody String receiveJSONString,
                                      @RequestParam(value = "patent_id", required = false) String patentId) throws Exception {
        AdminToken tokenBean = adminTokenService.getById(JWTUtils.getJwtToken(request));
        if (tokenBean == null) throw new CustomException.TokenNullException();

        BeanResponseBody responseBody = new BeanResponseBody();
        PatentFamily family = (PatentFamily) JacksonJSONUtils.readValue(receiveJSONString, PatentFamily.class);
        boolean permissionResult = tokenBean.checkPermission(Constants.PERMISSION_CROSS_BUSINESS);
        String businessId = permissionResult ? Constants.BUSINESS_PLATFORM : tokenBean.getBusiness().getBusiness_id();

        int taskResult = patentService.combinePatentFamily(family, businessId, patentId, tokenBean.getAdmin(), request.getRemoteAddr());
        responseBody.setCode(taskResult);
        if (family.getListPatentIds().size() == 1) {
            responseBody.setBean(null);
        } else {
            responseBody.setBean(family);
        }
        return responseBody.getJacksonString(View.Patent.class);
    }

    @GetMapping(value = "/api/getpatentbyfamily/{familyId}", produces = Constants.CONTENT_TYPE_JSON)
    public String gePatentbyFamily(HttpServletRequest request, @PathVariable String familyId) throws Exception {
        ListResponseBody responseBody = new ListResponseBody();
        AdminToken tokenBean = adminTokenService.getById(JWTUtils.getJwtToken(request));
        if (tokenBean == null) throw new CustomException.TokenNullException();

        Permission permission = permissionService.getSettingPermissionByModule(Constants.MODEL_CODE_PATENT_CONTENT, Constants.VIEW);
        if (!tokenBean.checkPermission(permission.getPermission_id())) {
            throw new CustomException.NoPermission();
        }

        List<Patent> list = patentService.getByFamily(familyId);
        responseBody.setCode(Constants.INT_SUCCESS);
        responseBody.setList(list);
        return responseBody.getJacksonString(View.Patent.class);
    }

    @PostMapping(value = "/api/exportpatentexcel", produces = Constants.CONTENT_TYPE_JSON)
    public ResponseEntity<InputStreamResource> exportPatentExcel(HttpServletRequest request, @RequestBody String receiveJSONString) throws Exception {
        AdminToken tokenBean = adminTokenService.getById(JWTUtils.getJwtToken(request));
        if (tokenBean == null) throw new CustomException.TokenNullException();

        Permission permission = permissionService.getSettingPermissionByModule(Constants.MODEL_CODE_PATENT_CONTENT, Constants.VIEW);
        if (!tokenBean.checkPermission(permission.getPermission_id())) {
            throw new CustomException.NoPermission();
        }

        String businessId = tokenBean.getBusiness().getBusiness_id();
        String fileName = tokenBean.getBusiness().getBusiness_name();

        JSONObject jsonObject = new JSONObject(receiveJSONString);
        String jsonPid = jsonObject.optJSONArray("patent_ids").toString();
        String jsonFid = jsonObject.optJSONArray("field_ids").toString();

        TypeReference<?> typeReference = new TypeReference<List<String>>() {
        };
        List<String> patentIds = (List<String>) JacksonJSONUtils.readValue(jsonPid, typeReference);
        List<String> fieldIds = (List<String>) JacksonJSONUtils.readValue(jsonFid, typeReference);

        List<Patent> listPatent = patentService.getExcelByPatentIds(patentIds, businessId);
        ByteArrayInputStream fileOut = excelExportService.PatentToExcel(fieldIds, listPatent, businessId);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-disposition", "attachment; filename=" + fileName + ".xls");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/ms-excel"))
                .body(new InputStreamResource(fileOut));

    }

    @GetMapping(value = "/api/getallexcelfield", produces = Constants.CONTENT_TYPE_JSON)
    public String getAllExcelField(HttpServletRequest request) {
        ListResponseBody responseBody = new ListResponseBody();
        responseBody.setCode(Constants.INT_SUCCESS);
        responseBody.setList(fieldService.getAllFields());
        return responseBody.getJacksonString(View.FieldMap.class);
    }

    @PostMapping(value = "/api/importpatentexcel", produces = Constants.CONTENT_TYPE_JSON, consumes = {"multipart/mixed",
            "multipart/form-data"})
    public String importPatentExcel(HttpServletRequest request, @RequestParam("file") MultipartFile file) throws Exception {
        AdminToken tokenBean = adminTokenService.getById(JWTUtils.getJwtToken(request));
        if (tokenBean == null) throw new CustomException.TokenNullException();

        if (file == null || file.getOriginalFilename() == null) {
            throw new Exception();
        }

        BeanResponseBody responseBody = new BeanResponseBody();
        ExcelTask task = excelTaskService.addTaskByFile(file, tokenBean.getAdmin());
        responseBody.setCode(Constants.INT_SUCCESS);
        responseBody.setBean(task);
        return responseBody.getJacksonString(View.ExcelTask.class);
    }

    @PostMapping(value = "/api/gettaskfield/{taskId}", produces = Constants.CONTENT_TYPE_JSON)
    public String getTaskField(HttpServletRequest request, @PathVariable String taskId) throws Exception {
        AdminToken tokenBean = adminTokenService.getById(JWTUtils.getJwtToken(request));
        if (tokenBean == null) throw new CustomException.TokenNullException();

        BeanResponseBody responseBody = new BeanResponseBody();
        ExcelTask task = excelTaskService.getTaskField(tokenBean.getAdmin(), taskId);
        responseBody.setCode(Constants.INT_SUCCESS);
        responseBody.setBean(task);
        return responseBody.getJacksonString(View.ExcelTask.class);
    }

    @PostMapping(value = "/api/previewexceltask", produces = Constants.CONTENT_TYPE_JSON)
    public String previewExcelTask(HttpServletRequest request, @RequestBody String receiveJSONString) throws Exception {
        AdminToken tokenBean = adminTokenService.getById(JWTUtils.getJwtToken(request));
        if (tokenBean == null) throw new CustomException.TokenNullException();

        BeanResponseBody responseBody = new BeanResponseBody();
        ExcelTask task = (ExcelTask) JacksonJSONUtils.readValue(receiveJSONString, ExcelTask.class);
        int result = excelTaskService.previewTask(task, tokenBean.getAdmin());
        responseBody.setCode(result);
        responseBody.setBean(task);
        return responseBody.getJacksonString(View.ExcelTask.class);
    }

    @PostMapping(value = "/api/submitexceltask", produces = Constants.CONTENT_TYPE_JSON)
    public String submitExcelTask(HttpServletRequest request, @RequestBody String receiveJSONString) throws Exception {
        AdminToken tokenBean = adminTokenService.getById(JWTUtils.getJwtToken(request));
        if (tokenBean == null) throw new CustomException.TokenNullException();

        BeanResponseBody responseBody = new BeanResponseBody();
        String ip = request.getRemoteAddr();
        Admin admin = tokenBean.getAdmin();
        Business business = tokenBean.getBusiness();
        ExcelTask task = (ExcelTask) JacksonJSONUtils.readValue(receiveJSONString, ExcelTask.class);
        List<Patent> patentList = excelTaskService.submitTask(task, admin);

        // 同步作法，全部處理完成才回傳結果
//        Map<String, Patent> mergeMap = patentService.addPatentByExcel(patentList, admin, business, ip);
//        if (!mergeMap.isEmpty()) {
//            patentService.mergeDiffPatentByExcel(mergeMap, admin, business);
//        }

        int nThread = 100;
        ExecutorService threadPool = Executors.newFixedThreadPool(nThread);
        for (Patent patent : patentList) {
            threadPool.execute(() -> {
                try {
                    if (!StringUtils.isNULL(patent.getPatent_appl_no())) {
                        patentService.syncPatentData(patent);
                    }
                } catch (Exception e) {
                    log.error("exception", e);
                }
            });
        }
        threadPool.shutdown();
        try {
            threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS); // 異步
        } catch (InterruptedException e) {
            log.error("threadPool await exception: " + e.getMessage());
        }

        Map<String, Patent> mergeListMap = patentService.addPatentByExcel(patentList, admin, business, ip);
        if (!mergeListMap.isEmpty()) {
            patentService.mergeDiffPatentByExcel(mergeListMap, admin, business);
        }

//		// 同步作法，將patentList以十為一組，每組為一個task，並用runnable分別執行
//        int patentListSize = patentList.size();
//        int spiltSize = 1;
//        int quotient = patentListSize / spiltSize; // 商數
//        int remainder = patentListSize % spiltSize; // 餘數
//
//        if (patentListSize <= spiltSize) {
//            Runnable runnable = () -> {
//                try {
//                    Map<String, Patent> mergeMap = patentService.addPatentByExcel(patentList, admin, business, ip);
//                    if (!mergeMap.isEmpty()) patentService.mergeDiffPatentByExcel(mergeMap, admin, business);
//                } catch (Exception e) {
//                    log.error("exception", e);
//                }
//            };
//            ExecutorService executorService = Executors.newSingleThreadExecutor();
//            executorService.execute(runnable);
//            executorService.shutdown();
//            try {
//                executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
//            } catch (InterruptedException e) {
//                System.out.println("threadPool await exception:");
//                System.out.println(e.toString());
//            }
//        } else {
//            // 以十為一組
//            for (int i = 1; i <= patentListSize; i++) {
//                if (i % spiltSize == 0) {
//                    List<Patent> patentListQuotient = patentList.subList(i - spiltSize, i);
//                    Runnable runnable = () -> {
//                        try {
//                            Map<String, Patent> mergeMap = patentService.addPatentByExcel(patentListQuotient, admin, business, ip);
//                            if (!mergeMap.isEmpty()) patentService.mergeDiffPatentByExcel(mergeMap, admin, business);
//                        } catch (Exception e) {
//                            log.error("exception", e);
//                        }
//                    };
//                    ExecutorService executorService = Executors.newSingleThreadExecutor();
//                    executorService.execute(runnable);
//                    executorService.shutdown();
//                    try {
//                        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
//                    } catch (InterruptedException e) {
//                        System.out.println("threadPool await exception:");
//                        System.out.println(e.toString());
//                    }
//                }
//            }
//
//            // 處理餘數List
//            if (remainder != 0) {
//                List<Patent> patentListRemainder = patentList.subList(quotient * spiltSize, patentListSize);
//                Runnable runnable = () -> {
//                    try {
//                        Map<String, Patent> mergeMap = patentService.addPatentByExcel(patentListRemainder, admin, business, ip);
//                        if (!mergeMap.isEmpty()) patentService.mergeDiffPatentByExcel(mergeMap, admin, business);
//                    } catch (Exception e) {
//                        log.error("exception", e);
//                    }
//                };
//                ExecutorService executorService = Executors.newSingleThreadExecutor();
//                executorService.execute(runnable);
//                executorService.shutdown();
//                try {
//                    executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
//                } catch (InterruptedException e) {
//                    System.out.println("threadPool await exception:");
//                    System.out.println(e.toString());
//                }
//            }
//        }

        responseBody.setCode(Constants.INT_SUCCESS);
        responseBody.setBean(task);
        return responseBody.getJacksonString(View.ExcelTask.class);
    }

    @PostMapping(value = "/api/searchpatent", produces = Constants.CONTENT_TYPE_JSON)
    public String searchPatent(HttpServletRequest request,
                               @RequestBody String receiveJSONString,
                               @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                               @RequestParam(value = "order_field", required = false, defaultValue = "") String fieldId,
                               @RequestParam(value = "asc", required = false, defaultValue = "1") int is_asc) throws Exception {
        AdminToken tokenBean = adminTokenService.getById(JWTUtils.getJwtToken(request));
        if (tokenBean == null) throw new CustomException.TokenNullException();

        Permission permission = permissionService.getSettingPermissionByModule(Constants.MODEL_CODE_PATENT_CONTENT, Constants.VIEW);
        if (!tokenBean.checkPermission(permission.getPermission_id())) {
            throw new CustomException.NoPermission();
        }

        ListResponseBody responseBody = new ListResponseBody();
        JSONObject jsonObject = new JSONObject(receiveJSONString);
        String fieldStr = jsonObject.getJSONObject("field").toString();
        PatentField field = (PatentField) JacksonJSONUtils.readValue(fieldStr, PatentField.class);
        Object searchText = jsonObject.get("searchText");
        log.info("page: " + page);
        log.info("order_field: " + fieldId);
        log.info("asc: " + is_asc);
        log.info("searchText: " + searchText);

        String businessId = tokenBean.getBusiness().getBusiness_id();
        ListQueryForm form = patentService.fieldSearchPatent(searchText, field.getField_id(), businessId, page, fieldId, is_asc);
        responseBody.setCode(Constants.INT_SUCCESS);
        responseBody.setListQuery(form);
        return responseBody.getJacksonString(View.Patent.class);
    }

    @GetMapping(value = "/api/geteditpatentstatus", produces = Constants.CONTENT_TYPE_JSON)
    public String getEditPatentStatus(HttpServletRequest request) throws Exception {
        AdminToken tokenBean = adminTokenService.getById(JWTUtils.getJwtToken(request));
        if (tokenBean == null) throw new CustomException.TokenNullException();

        Permission permission = permissionService.getSettingPermissionByModule(Constants.MODEL_CODE_PATENT_CONTENT, Constants.VIEW);
        if (!tokenBean.checkPermission(permission.getPermission_id())) {
            throw new CustomException.NoPermission();
        }

        ListResponseBody responseBody = new ListResponseBody();
        List<Status> list = patentService.getEditStatus();
        responseBody.setCode(Constants.INT_SUCCESS);
        responseBody.setList(list);
        return responseBody.getJacksonString(View.Patent.class);
    }

    @PostMapping(value = "/api/downloadexport", produces = Constants.CONTENT_TYPE_JSON)
    public ResponseEntity<InputStreamResource> downloadFile(HttpServletRequest request,
                                                            @RequestBody String receiveJSONString) throws IOException {
        JSONObject dataJSON = new JSONObject(receiveJSONString);
        String fileName = dataJSON.optString("file_name");
        File f = new File(Constants.FILE_UPLOAD_PATH + fileName);

        ByteArrayInputStream fileOut = FileUtils.convertFileToByteArray(f);

        HttpHeaders headers = new HttpHeaders();
//			String fileName = "錯誤回報";
        headers.add("Content-disposition", "attachment;");
        log.info("回報成功");
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/ms-excel"))
                .body(new InputStreamResource(fileOut));
    }

    @PostMapping(value = "/api/advancedsearch", produces = Constants.CONTENT_TYPE_JSON)
    public String advanceSearch(HttpServletRequest request,
                                @RequestBody String receiveJSONString,
                                @RequestParam(value = "page", required = false, defaultValue = "1") int page) throws Exception {
        AdminToken tokenBean = adminTokenService.getById(JWTUtils.getJwtToken(request));
        if (tokenBean == null) throw new CustomException.TokenNullException();

        ListResponseBody responseBody = new ListResponseBody();
        JSONObject jsonObject = new JSONObject(receiveJSONString);
        String query = jsonObject.optString("searchText");

        try {
            ListQueryForm form = patentService.advancedSearch(query, tokenBean.getBusiness_id(), page, Constants.SYSTEM_PAGE_SIZE);
            responseBody.setCode(Constants.INT_SUCCESS);
            responseBody.setListQuery(form);
        } catch (Exception e) {
            responseBody.setCode(Constants.INT_INCORRECT_SYNTAX);
        }
        return responseBody.getJacksonString(View.Patent.class);
    }
}
