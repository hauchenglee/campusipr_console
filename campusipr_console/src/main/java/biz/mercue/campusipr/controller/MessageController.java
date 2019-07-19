package biz.mercue.campusipr.controller;

import biz.mercue.campusipr.model.*;
import biz.mercue.campusipr.service.AdminTokenService;
import biz.mercue.campusipr.service.MessageService;
import biz.mercue.campusipr.util.*;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


@Controller
public class MessageController {

    private Logger log = Logger.getLogger(this.getClass().getName());

    @Autowired
    AdminTokenService adminTokenService;

    @Autowired
    MessageService messageService;

    @RequestMapping(value = "/api/addmessage", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
    @ResponseBody
    public String addMessage(HttpServletRequest request, @RequestBody String receiveJSONString) {
        log.info("add message");
        Message message = (Message) JacksonJSONUtils.readValue(receiveJSONString, Message.class);
        BeanResponseBody responseBody = new BeanResponseBody();
        AdminToken adminToken = adminTokenService.getById(JWTUtils.getJwtToken(request));
        if (adminToken != null) {
            messageService.addMessage(message);
            responseBody.setCode(Constants.INT_SUCCESS);
            responseBody.setBean(message);
        } else {
            responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
        }
        return responseBody.getJacksonString(View.Message.class);
    }

    @RequestMapping(value = "/api/getchannelmessage", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
    @ResponseBody
    public String getChannelMessage(HttpServletRequest request,
                                    @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                    @RequestParam(value = "lang", required = false, defaultValue = "tw") String lang) {
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

    @RequestMapping(value = "/api/getmessagelist", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
    @ResponseBody
    public String getMessageList(HttpServletRequest request, @RequestBody String receiveJSONString) {
//        log.info("/api/getmessagelist");
        ListResponseBody responseBody = new ListResponseBody();
        AdminToken adminToken = adminTokenService.getById(JWTUtils.getJwtToken(request));
        if (adminToken != null) {
            JSONObject jsonObject = new JSONObject(receiveJSONString);
            String senderId = jsonObject.optString("sender_id");
            String receiverId = jsonObject.optString("receiver_id");
            List<Message> messageList = messageService.getMessagesList(senderId, receiverId);
            responseBody.setCode(Constants.INT_SUCCESS);
            responseBody.setList(messageList);
            responseBody.setTotal_count(messageList.size());
        } else {
            responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
        }
        return responseBody.getJacksonString(View.Message.class);
    }

    @RequestMapping(value = "/api/getpreviousmessage", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
    @ResponseBody
    public String getPreviousMessageList(HttpServletRequest request, @RequestBody String receiveJSONString) {
//        log.info("/api/getpreviousmessage");
        ListResponseBody responseBody = new ListResponseBody();
        AdminToken adminToken = adminTokenService.getById(JWTUtils.getJwtToken(request));
        if (adminToken != null) {
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
        } else {
            responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
        }
        return responseBody.getJacksonString(View.Message.class);
    }

    @RequestMapping(value = "/api/getlastestmessage", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
    @ResponseBody
    public String getLastestMessage(HttpServletRequest request, @RequestBody String receiveJSONString) {
//        log.info("/api/getlastestmessage");
        ListResponseBody responseBody = new ListResponseBody();
        AdminToken tokenBean = adminTokenService.getById(JWTUtils.getJwtToken(request));
        if (tokenBean != null) {
            JSONObject jsonObject = new JSONObject(receiveJSONString);
            String senderId = jsonObject.optString("sender_id");
            String receiverId = jsonObject.optString("receiver_id");
            long lastTimestamp = jsonObject.getLong("lastTimestamp");
            List<Message> messageList = messageService.getMessagesAfterTime(senderId, receiverId, lastTimestamp);
            responseBody.setCode(Constants.INT_SUCCESS);
            responseBody.setList(messageList);
            responseBody.setTotal_count(messageList.size());
        } else {
            responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
        }
        return responseBody.getJacksonString(View.Message.class);
    }

    @RequestMapping(value = "/api/searchpreviousmessage", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
    @ResponseBody
    public String searchPreviousMessageList(HttpServletRequest request, @RequestBody String receiveJSONString) {
        log.info("/api/searchpreviousmessage");
        ListResponseBody responseBody = new ListResponseBody();
        AdminToken adminToken = adminTokenService.getById(JWTUtils.getJwtToken(request));
        if (adminToken != null) {
            JSONObject jsonObject = new JSONObject(receiveJSONString);
            String senderId = jsonObject.optString("sender_id");
            String receiverId = jsonObject.optString("receiver_id");
            long startTimestamp = jsonObject.getLong("startTimestamp");
            List<Message> messageList = messageService.getMessagesBeforeAndEqualTime(senderId, receiverId, startTimestamp);
            responseBody.setCode(Constants.INT_SUCCESS);
            responseBody.setList(messageList);
            responseBody.setTotal_count(messageList.size());
        } else {
            responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
        }
        return responseBody.getJacksonString(View.Message.class);
    }

    @RequestMapping(value = "/api/searchmessage/{text}", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
    @ResponseBody
    public String searchText(HttpServletRequest request, @RequestBody String receiveJSONString, @PathVariable String text) {
        log.info("/api/search/" + text);
        ListResponseBody responseBody = new ListResponseBody();
        AdminToken adminToken = adminTokenService.getById(JWTUtils.getJwtToken(request));
        if (adminToken != null) {
            JSONObject jsonObject = new JSONObject(receiveJSONString);
            String senderId = jsonObject.optString("sender_id");
            String receiverId = jsonObject.optString("receiver_id");
            List<Message> messageList = messageService.searchText(senderId, receiverId, text);
            responseBody.setCode(Constants.INT_SUCCESS);
            responseBody.setList(messageList);
            responseBody.setTotal_count(messageList.size());
        } else {
            responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
        }
        return responseBody.getJacksonString(View.Message.class);
    }

    @RequestMapping(value = "/api/getchatterlist", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
    @ResponseBody
    public String getChatterList(HttpServletRequest request) {
//        log.info("/api/getchatterlist");
        ListResponseBody responseBody = new ListResponseBody();
        AdminToken adminToken = adminTokenService.getById(JWTUtils.getJwtToken(request));
        if (adminToken != null) {
            List<Admin> adminList = messageService.getAllAdminAndNewestMessage(adminToken);
            responseBody.setCode(Constants.INT_SUCCESS);
            responseBody.setList(adminList);
            responseBody.setTotal_count(adminList.size());
        } else {
            responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
        }
        return responseBody.getJacksonString(View.Admin.class);
    }

    @RequestMapping(value = "/api/readmessage", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
    @ResponseBody
    public String readMessage(HttpServletRequest request, @RequestBody String receiveJSONString) {
//        log.info("/api/readmessage");
        StringResponseBody responseBody  = new StringResponseBody();
        AdminToken adminToken = adminTokenService.getById(JWTUtils.getJwtToken(request));
        if (adminToken != null) {
            JSONObject jsonObject = new JSONObject(receiveJSONString);
            String senderId = jsonObject.optString("sender_id");
            String receiverId = jsonObject.optString("receiver_id");
            messageService.readMessage(senderId, receiverId);
            responseBody.setCode(Constants.INT_SUCCESS);
        } else {
            responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
        }
        return responseBody.getJacksonString(View.Message.class);
    }
}
