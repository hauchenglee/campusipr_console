package biz.mercue.campusipr.controller;

import biz.mercue.campusipr.model.*;
import biz.mercue.campusipr.service.AdminTokenService;
import biz.mercue.campusipr.service.MessageService;
import biz.mercue.campusipr.util.*;
import lombok.extern.log4j.Log4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.List;

@RestController
@Log4j
public class MessageController {
    @Autowired
    AdminTokenService adminTokenService;

    @Autowired
    MessageService messageService;

    @PostMapping(value = "/api/addmessage", produces = Constants.CONTENT_TYPE_JSON)
    public String addMessage(HttpServletRequest request, @RequestBody String receiveJSONString) throws Exception {
        log.info("add message");
        Message message = (Message) JacksonJSONUtils.readValue(receiveJSONString, Message.class);
        BeanResponseBody responseBody = new BeanResponseBody();
        AdminToken adminToken = adminTokenService.getById(JWTUtils.getJwtToken(request));
        if (adminToken == null) {
            throw new CustomException.TokenNullException();
        }

        messageService.addMessage(message, adminToken.getAdmin());
        responseBody.setCode(Constants.INT_SUCCESS);
        responseBody.setBean(message);

        return responseBody.getJacksonString(View.Message.class);
    }

    @RequestMapping(value = "/api/getchannelmessage", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
    public String getChannelMessage(HttpServletRequest request,
                                    @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                    @RequestParam(value = "lang", required = false, defaultValue = "tw") String lang) throws Exception {
        log.info("getChannelMessage ");

        ListResponseBody responseBody = new ListResponseBody();
        AdminToken tokenBean = adminTokenService.getById(JWTUtils.getJwtToken(request));
        if (tokenBean != null) {
            ListQueryForm form = messageService.getMessagesList(tokenBean.getAdmin().getAdmin_id(), Constants.SYSTEM_ADMIN, page, Constants.SYSTEM_PAGE_SIZE);
            responseBody.setCode(Constants.INT_SUCCESS);
            responseBody.setListQuery(form);
        } else {
            responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
        }

        return responseBody.getJacksonString(View.Message.class);
    }

    @PostMapping(value = "/api/getmessagelist", produces = Constants.CONTENT_TYPE_JSON)
    public String getMessageList(HttpServletRequest request, @RequestBody String receiveJSONString) throws Exception {
        ListResponseBody responseBody = new ListResponseBody();
        AdminToken adminToken = adminTokenService.getById(JWTUtils.getJwtToken(request));
        if (adminToken == null) {
            throw new CustomException.TokenNullException();
        }

        JSONObject jsonObject = new JSONObject(receiveJSONString);
        String senderId = jsonObject.optString("sender_id");
        String receiverId = jsonObject.optString("receiver_id");

        List<Message> messageList = messageService.getMessagesList(senderId, receiverId);
        responseBody.setCode(Constants.INT_SUCCESS);
        responseBody.setList(messageList);
        responseBody.setTotal_count(messageList.size());
        return responseBody.getJacksonString(View.Message.class);
    }

    @PostMapping(value = "/api/getpreviousmessage", produces = Constants.CONTENT_TYPE_JSON)
    public String getPreviousMessageList(HttpServletRequest request, @RequestBody String receiveJSONString) throws Exception {
        ListResponseBody responseBody = new ListResponseBody();
        AdminToken adminToken = adminTokenService.getById(JWTUtils.getJwtToken(request));
        if (adminToken == null) {
            throw new CustomException.TokenNullException();
        }

        JSONObject jsonObject = new JSONObject(receiveJSONString);
        String senderId = jsonObject.optString("sender_id");
        String receiverId = jsonObject.optString("receiver_id");
        long lastTimestamp = jsonObject.getLong("startTimestamp");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(lastTimestamp);

        List<Message> messageList = messageService.getMessagesBeforeTime(senderId, receiverId, lastTimestamp);
        responseBody.setCode(Constants.INT_SUCCESS);
        responseBody.setList(messageList);
        responseBody.setTotal_count(messageList.size());
        return responseBody.getJacksonString(View.Message.class);
    }

    @PostMapping(value = "/api/getlastestmessage", produces = Constants.CONTENT_TYPE_JSON)
    public String getLastestMessage(HttpServletRequest request, @RequestBody String receiveJSONString) throws Exception {
        ListResponseBody responseBody = new ListResponseBody();
        AdminToken adminToken = adminTokenService.getById(JWTUtils.getJwtToken(request));
        if (adminToken == null) throw new CustomException.TokenNullException();

        JSONObject jsonObject = new JSONObject(receiveJSONString);
        String senderId = jsonObject.optString("sender_id");
        String receiverId = jsonObject.optString("receiver_id");
        long lastTimestamp = jsonObject.getLong("lastTimestamp");

        List<Message> messageList = messageService.getMessagesAfterTime(senderId, receiverId, lastTimestamp);
        responseBody.setCode(Constants.INT_SUCCESS);
        responseBody.setList(messageList);
        responseBody.setTotal_count(messageList.size());
        return responseBody.getJacksonString(View.Message.class);
    }

    @PostMapping(value = "/api/searchpreviousmessage", produces = Constants.CONTENT_TYPE_JSON)
    public String searchPreviousMessageList(HttpServletRequest request, @RequestBody String receiveJSONString) throws Exception {
        log.info("/api/searchpreviousmessage");
        ListResponseBody responseBody = new ListResponseBody();
        AdminToken adminToken = adminTokenService.getById(JWTUtils.getJwtToken(request));
        if (adminToken == null) {
            throw new CustomException.TokenNullException();
        }

        JSONObject jsonObject = new JSONObject(receiveJSONString);
        String senderId = jsonObject.optString("sender_id");
        String receiverId = jsonObject.optString("receiver_id");
        long startTimestamp = jsonObject.getLong("startTimestamp");

        List<Message> messageList = messageService.getMessagesBeforeAndEqualTime(senderId, receiverId, startTimestamp);
        responseBody.setCode(Constants.INT_SUCCESS);
        responseBody.setList(messageList);
        responseBody.setTotal_count(messageList.size());
        return responseBody.getJacksonString(View.Message.class);
    }

    @PostMapping(value = "/api/searchmessage/{text}", produces = Constants.CONTENT_TYPE_JSON)
    public String searchText(HttpServletRequest request, @RequestBody String receiveJSONString, @PathVariable String text) throws Exception {
        log.info("/api/search/" + text);
        ListResponseBody responseBody = new ListResponseBody();
        AdminToken adminToken = adminTokenService.getById(JWTUtils.getJwtToken(request));
        if (adminToken == null) {
            throw new CustomException.TokenNullException();
        }

        JSONObject jsonObject = new JSONObject(receiveJSONString);
        String senderId = jsonObject.optString("sender_id");
        String receiverId = jsonObject.optString("receiver_id");

        List<Message> messageList = messageService.searchText(senderId, receiverId, text);
        responseBody.setCode(Constants.INT_SUCCESS);
        responseBody.setList(messageList);
        responseBody.setTotal_count(messageList.size());
        return responseBody.getJacksonString(View.Message.class);
    }

    @RequestMapping(value = "/api/getchatterlist", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
    public String getChatterList(HttpServletRequest request) throws Exception {
        ListResponseBody responseBody = new ListResponseBody();
        AdminToken adminToken = adminTokenService.getById(JWTUtils.getJwtToken(request));
        if (adminToken == null) {
            throw new CustomException.TokenNullException();
        }

        List<Admin> adminList = messageService.getAllAdminAndNewestMessage(adminToken);
        responseBody.setCode(Constants.INT_SUCCESS);
        responseBody.setList(adminList);
        responseBody.setTotal_count(adminList.size());

        return responseBody.getJacksonString(View.Admin.class);
    }

    @RequestMapping(value = "/api/readmessage", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
    public String readMessage(HttpServletRequest request, @RequestBody String receiveJSONString) throws Exception {
        StringResponseBody responseBody = new StringResponseBody();
        AdminToken adminToken = adminTokenService.getById(JWTUtils.getJwtToken(request));
        if (adminToken == null) {
            throw new CustomException.TokenNullException();
        }

        JSONObject jsonObject = new JSONObject(receiveJSONString);
        String senderId = jsonObject.optString("sender_id");
        String receiverId = jsonObject.optString("receiver_id");

        messageService.readMessage(senderId, receiverId);
        responseBody.setCode(Constants.INT_SUCCESS);
        return responseBody.getJacksonString(View.Message.class);
    }
}
