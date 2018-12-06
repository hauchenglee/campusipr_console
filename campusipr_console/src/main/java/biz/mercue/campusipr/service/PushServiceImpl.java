package biz.mercue.campusipr.service;


import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import biz.mercue.campusipr.dao.PushDao;
import biz.mercue.campusipr.model.MessageTemplate;
import biz.mercue.campusipr.model.PushTask;
import biz.mercue.campusipr.util.Constants;
import biz.mercue.campusipr.util.KeyGeneratorUtils;
import biz.mercue.campusipr.util.StringUtils;


@Service("pushService")
@Transactional
public class PushServiceImpl implements PushService{
	private Logger log = Logger.getLogger(this.getClass().getName());

	@Autowired
	private PushDao pushDao;



	


	@Override
	public PushTask getById(String id) {
		log.info("get push by id: " + id);
		PushTask push = pushDao.getById(id);
		if (push != null) {
			getPushDetail(push);
		}
		return push;
	}

	@Override
	public void addPush(PushTask push) {
		log.info("1");
		if(push.getContentList()!= null && push.getContentList().size() > 0 ) {
			List<String> ids = push.getContentList();
			StringBuilder builder = new StringBuilder();
			String prefix = "";
			for(String id : ids) {
				builder.append(prefix);
				prefix = ",";
				builder.append(id);
			};
			push.setContent_ids(builder.toString());
		}
		log.info("2");
		if (push.getMessageTemplate() != null && StringUtils.isNULL(push.getMessageTemplate().getMessage_template_id()) ) {
			push.getMessageTemplate().setMessage_template_id(KeyGeneratorUtils.generateRandomString());
		}
		if(push.getMessageTemplate() != null) {
			push.getMessageTemplate().setBusiness_id(push.getBusiness_id());
			push.getMessageTemplate().setCreate_date(new Date());
			push.getMessageTemplate().setUpdate_date(new Date());
		}
		if (Constants.CODE_ALL.equals(push.getTarget_gender())) {
			push.setTarget_gender(null);
		}
		if (Constants.CITY_ALL.equals(push.getPush_location())) {
			push.setPush_location(null);
		}

		pushDao.create(push);
	}


	@Override
	public int  updatePush(PushTask push){
		PushTask dbBean = pushDao.getById(push.getPush_id());

		if(dbBean!=null){

			dbBean.setPush_type(push.getPush_type());
			dbBean.setIs_send(push.getIs_send());
			dbBean.setIs_instant(push.is_instant());
			dbBean.setUpdate_date(new Date());
			dbBean.setPush_date(push.getPush_date());
			dbBean.setTarget_gender(push.getTarget_gender());
			dbBean.setHighest_age(push.getHighest_age());
			dbBean.setLowest_age(push.getLowest_age());
			dbBean.setPush_location(push.getPush_location());
			dbBean.setPush_extension_name(push.getPush_extension_name());
		

			if (push.getContent_ids() != null) {				
				List<String> ids = push.getContentList();
				StringBuilder builder = new StringBuilder();
				String prefix = "";
				for(String id : ids) {
					builder.append(prefix);
					prefix = ",";
					builder.append(id);
				};
				push.setContent_ids(builder.toString());
			}

			MessageTemplate dbTemplate = dbBean.getMessageTemplate();
			MessageTemplate updateTemplate = push.getMessageTemplate();

			if(dbTemplate!=null) {
				if(updateTemplate !=null) {
					dbTemplate.setMessage_text(updateTemplate.getMessage_text());
					dbTemplate.setMessage_type(updateTemplate.getMessage_type());
					dbTemplate.setUpdate_date(new Date());
				}else {
					dbBean.setMessageTemplate(null);
				}
			}
			if (Constants.CODE_ALL.equals(push.getTarget_gender())) {
				push.setTarget_gender(null);
			}
			if (Constants.CITY_ALL.equals(push.getPush_location())) {
				push.setPush_location(null);
			}
		}
		return 0;
	}

	@Override
	public void changePushStatus(String id, boolean status) {
		PushTask dbBean = pushDao.getById(id);
		if(dbBean!=null){
			dbBean.setIs_send(status);
		}
	}

	@Override
	public List<PushTask> getAvailableUnsendPush() {
		List<PushTask> pushList = pushDao.getAvailableUnsendPush();
		pushList.forEach(push-> {
			getPushDetail(push);
		});
		return pushList;
	}



	@Override
	public void deletePush(PushTask push) {
		pushDao.delete(push.getPush_id());
	}

	@Override
	public List<PushTask> getByBusinessId(String businessId) {
		List<PushTask> pushList = pushDao.getByBusinessId(businessId);
		pushList.forEach(push-> {
			getPushDetail(push);
		});
		return pushList;
	}

	@Override
	public List<PushTask> getByStatus(String businessId, boolean status) {
		List<PushTask> pushList = pushDao.getByStatus(businessId, status);
		pushList.forEach(push-> {
			getPushDetail(push);
		});
		return pushList;
	}



	@Override
	public List<PushTask> getByAccount(String accountId) {
		List<PushTask> pushList = pushDao.getByAccount(accountId);
		return pushList;
	}

	@Override
	public List<PushTask> getByAccountBussiness(String businessId, String accountId) {
		List<PushTask> pushList = pushDao.getByAccountBussiness(businessId, accountId);
		return pushList;
	}


	//	@Override
	//	public List<BotAccount> getByBotAndTag(List<String> botIdList, List<Tag> tagList) {
	//		List<BotAccount> recordList = botAccountDao.getByBotTag(botIdList, tagList);
	//		recordList.forEach(record-> {
	//			log.info("account: " + record.getAccount_id() + ", bot: " + record.getBot_id());			
	//		});
	//		return recordList;
	//	}


	private void getPushDetail(PushTask push) {
		if (!StringUtils.isNULL(push.getContent_ids())) {
			List<String> contentList = Arrays.asList(push.getContent_ids().split(","));		
			push.setContentList(contentList);
		}
		if (push.getMessageTemplate() != null) {
			push.getMessageTemplate().getMessage_template_id();
		}


		switch(push.getPush_type()) {
		case Constants.PUSH_TYPE_TEXT:
			if (push.getMessageTemplate() != null) {
				push.setPush_title(push.getMessageTemplate().getMessage_text());
			}
			break;
		default:
			break;
		}
	}




}
