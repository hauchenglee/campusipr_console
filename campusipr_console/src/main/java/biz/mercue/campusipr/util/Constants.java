package biz.mercue.campusipr.util;

import java.util.Arrays;
import java.util.List;

public class Constants {
	

	
	
	public static String SYSTEM_LANGUAGE_TW = "tw";
	public static String SYSTEM_LANGUAGE_US = "us";
	public static String SYSTEM_LANGUAGE_CN = "cn";
	
	//Duckling LOCALE
	//台灣
	public static final String DUCKLING_LOCALE_ZH_TW = "zh_TW";
	//香港
	public static final String DUCKLING_LOCALE_ZH_HK = "zh_HK";
	//大陸
	public static final String DUCKLING_LOCALE_ZH_CN = "zh_CN";
	//澳洲
	public static final String DUCKLING_LOCALE_EN_AU = "en_AU";
	//英國
	public static final String DUCKLING_LOCALE_EN_BG = "en_BG";
	//美國
	public static final String DUCKLING_LOCALE_EN_US = "en_US";
	
	//Duckling LANGUAGE
	public static final String DUCKLING_LANGUAGE_ZH = "zh";
	public static final String DUCKLING_LANGUAGE_EN = "en";
	
	
	//patent api for local
	public static String PATENT_WEB_SERVICE_TW = "";
	public static String PATENT_KEY_TW = "";
	public static String PATENT_WEB_SERVICE_US = "";
	public static String PATENT_INVENTOR_WEB_SERVICE_US = "";
	public static String PATENT_CONTEXT_WEB_SERVICE_US = "";
	public static String PATENT_WEB_SERVICE_EU = "";
	public static String PATENT_TOKEN_EU = "";
	
	public static String DUCKLING_API_URL = "";
	
	public static String MAIL_STARTTLS = "";
	public static String MAIL_HOST = "";
	public static String MAIL_PORT = "";
	public static String MAIL_USER_NAME = "";
	public static String MAIL_FROM = "";
	public static String MAIL_PASSWORD = "";
	public static String SYSTEM_EMAIL = "";
	
	public static String RECAPTCHA_SECRET_KEY = "";
	
	public static String ELASTICSEARCH_IP = "";
	public static String ELASTICSEARCH_PORT = "";
	
	
	public static final String JSON_APP_ID = "bot_channel_id";
	public static final String JSON_APP_SECRET = "bot_secret";
	public static final String JSON_PAGE_ACCESS_TOKEN = "access_token";
	public static final String JSON_ACCESS_TOKEN = "access_token";
	public static final String JSON_WEBHOOK_URL = "webhook_url";
	public static final String JSON_VERIFY_TOKEN = "verify_token";
	
	public static final String JSON_PAGE_ID = "page_id";
	public static final String JSON_PAGE_IMAGE_URL = "page_image_url";
	
	public static final String ENCODE_UTF_8 = "UTF-8";
	public static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded;charset=UTF-8";
	public static final String CONTENT_TYPE_JSON = "application/json;charset=utf-8";
	public static final String CONTENT_TYPE_PNG = "image/png";
	
	public static final int SHORT_IMAGE_NAME_LENGTH = 8;

	
	public static final String  JSON_CODE = "code";
	public static final String  JSON_MESSAGE = "message";
	public static final String  JSON_DATA = "data";
	
	public static final String  JSON_TOKEN = "token";
	
	public static String IMAGE_UPLOAD_PATH = "";
	public static String IMAGE_LOAD_URL = "";
	public static String VIDEO_UPLOAD_PATH = "";
	public static String VIDEO_LOAD_URL = "";
	public static String FILE_UPLOAD_PATH = "";
	public static String FILE_LOAD_URL = "";
	
	
	public static String IMAGEMAGICK_PATH = "";
	public static String GRAPHICSMAGICK_PATH = "";
	public static String FFMPEG_PATH = "";
	

	public static String JIEBA_DIC_PATH = "";
	public static String JIEBA_USERDIC_PATH = "";
	public static String JIEBA_STOP_WORD_PATH = "";
	public static String JIEBA_PROB_EMIT_PATH = "";

	
	public static final String REDIRECT_LOGIN = "/login";
	public static final String REDIRECT_MAINPAGE = "/mainpage";


	
	
	public static final String KEY_WIDTH = "width";
	public static final String KEY_HEIGHT = "height";
	public static final String KEY_SIZE = "size";
	public static final String KEY_SUFFIX = "suffix";
	public static final String KEY_RESULT = "result";

	
	public static final String  USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.97 Safari/537.11";
	public static final String  REQUEST_METHOD_GET = "GET";
	public static final String  REQUEST_METHOD_POST = "POST";
	
	
	
	public static final int  INT_SUCCESS = 1;
	public static final String  MSG_SUCCESS = "成功";
	public static final String  MSG_MAIL_SEND_SUCCESS = "Email發送成功";
	public static final String  MSG_EN_SUCCESS = "Success";
	

	public static final int INT_SYSTEM_PROBLEM =  -1 ;
	public static final String MSG_SYSTEM_PROBLEM  =  "系統發生問題" ;
	public static final String MSG_EN_SYSTEM_PROBLEM  =  "System problem" ;
	
	
//	public static final int INT_CANNOT_FIND_USER =  -2 ;
//	public static final String MSG_CANNOT_FIND_USER =  "找不到使用者" ;
//	public static final String MSG_EN_CANNOT_FIND_USER =  "Can not find User" ;
	
	public static final int INT_CANNOT_FIND_DATA =  -2 ;
	public static final String MSG_CANNOT_FIND_DATA =  "找不到資料" ;
	public static final String MSG_EN_CANNOT_FIND_DATA =  "Can not find Data" ;
	
	public static final int INT_PASSWORD_ERROR =  -3 ;
	public static final String MSG_PASSWORD_ERROR =  "帳號或密碼錯誤" ;
	public static final String MSG_EN_PASSWORD_ERROR =  "Account and password do not match" ;
	
	public static final int INT_USER_DUPLICATE =  -4 ;
	public static final String MSG_USER_DUPLICATE =  "Email已被註冊" ;
	public static final String MSG_EN_USER_DUPLICATE =  "The email address is already in use by another account" ;

	
	public static final int INT_DATA_ERROR =  -5 ;
	public static final String MSG_DATA_ERROR =  "輸入資料錯誤" ;
	public static final String MSG_EN_DATA_ERROR =  "The data you entered is not correct" ;
	
	public static final int INT_USER_LOGOUT =  -6 ;
	public static final String MSG_USER_LOGOUT =  "已登出系統" ;
	public static final String MSG_EN_USER_LOGOUT =  "Your login session has expired" ;

	
	public static final int INT_NO_PERMISSION=  -7 ;
	public static final String MSG_NO_PERMISSION  =  "無使用權限" ;
	public static final String MSG_EN_NO_PERMISSION  =  "You have no permission to operate" ;

	
	public static final int  INT_ACCESS_TOKEN_ERROR = -10;
	public static final String  MSG_ACCESS_TOKEN_ERROR= "已登出系統" ;
	public static final String  MSG_EN_ACCESS_TOKEN_ERROR =  "Your login session has expired" ;
	
//	public static final int  INT_LACK_ESSENTIAL_DATA= -11;
//	public static final String  MSG_LACK_ESSENTIAL_DATA = "缺少必備資料！";
//	
//	public static final int INT_FILE_TOO_BIG_ERROR= -12;
//	public static final String MSA_FILE_TOO_BIG_ERROR= "檔案過大！";
//		
//	public static final int INT_FILE_FORMAT_ERROR= -13;
//	public static final String MSG_FILE_FORMAT_ERROR= "檔案格式錯誤！";
//		
//	public static final int INT_FILE_SAVE_ERROR= -15;
//	public static final String MSG_FILE_SAVE_ERROR= "檔案儲存失敗！";

	
	public static final int INT_DATA_DUPLICATE =  -16 ;
	public static final String MSG_DATA_DUPLICATE =  "資料重複" ;
	public static final String MSG_EN_DATA_DUPLICATE =  "The data you entered is not correct" ;




	
	//message type
	public static final String MESSAGE_TYPE_TEXT = "text";

	
	

	

	

	public static final int SYSTEM_PAGE_SIZE = 20;

	

	
//	public static final int FB_FILE_SIZE_LIMIT = 25;//MB
//	public static final int LINE_FILE_SIZE_LIMIT = 10;//MB
//	public static final int LINE_IMAGE_SIZE_LIMIT = 1;//MB
	

	

	
	public static final String WEB_SOCKET_MESSAGEMAPPING_URL = "/chat/";
	public static final String WEB_SOCKET_MESSAGE_SEND_URL = "/topic/messages/";
	public static final String WEB_SOCKET_MESSAGE_BROADCAST_URL = "/topic/messages/broadcast";
	public static final String APP_SOCKET_MESSAGEMAPPING_URL = "/app/chat/";
	public static final String APP_SOCKET_MESSAGE_SEND_URL = "/topic/app/messages/";
	
	

	public static final String SYSTEM_MANAGER_ROLE_NAME="系統管理者";
	
	//image
	public static final String MEDIA_TYPE_JPEG = "jpeg";
	public static final String MEDIA_TYPE_JPG = "jpg";
	public static final String MEDIA_TYPE_PNG = "png";
	public static final String MEDIA_TYPE_GIF = "gif";
	
	//video
	public static final String MEDIA_TYPE_MOV = "mov";
	public static final String MEDIA_TYPE_MP4 = "mp4";
	public static final String MEDIA_TYPE_3GP = "3gp";
	
	//audio
	public static final String MEDIA_TYPE_WAV = "wav";
	public static final String MEDIA_TYPE_MP3 = "mp3";
	public static final String MEDIA_TYPE_M4A = "m4a";
	
	//model code
	public static final String MODEL_CODE_PATENT_CONTENT = "patent_content";
	public static final String MODEL_CODE_PLATFORM_MANAGER = "platform_manager";
	public static final String MODEL_CODE_PLATFORM_PATENT = "platform_patent";
	public static final String MODEL_CODE_BUSINESS_MANAGER = "business_manager";
	public static final String MODEL_CODE_BUSINESS_PATENT = "business_patent";
	public static final String MODEL_CODE_COMMON_USER = "common_user";
	
	
	public static final String PERMISSION_CROSS_BUSINESS = "59277fabb99ee27bc42012750f5e87c3";
	

	
	
	public static final String ROLE_PLATFORM_MANAGER = "a45e5e977721ed92dd3b914e3efd8dcf";

	public static final String ROLE_PLATFORM_PATENT = "ed9f24e7998c679c3932161880327984";

	public static final String ROLE_BUSINESS_MANAGER = "ab9baaedd20c3a62e89891df8ef38365";

	public static final String ROLE_BUSINESS_PATENT = "a19c8c01d3cea53cf0722509100d1754";
	
	public static final String ROLE_COMMON_USER = "f950f7d5be2cff1d20228e1c9a3adf0d";
	
	public static final String PATENT_ALL_FIELD = "7c393e277483570943bd7290ecf8bfae";
	public static final String PATENT_NAME_FIELD = "edad617ccdc004f37cac8f8710c6e965";
	public static final String PATENT_NAME_EN_FIELD = "4e765c02d8a5afc000eaa77ba419ff53";
	public static final String PATENT_COUNTRY_FIELD = "11507a5a70670d1329e3a7effc24ca60";
	public static final String PATENT_NO_FIELD = "0094b3bb46157502c406b50e899b0c19";
	public static final String PATENT_APPL_NO_FIELD = "9de48893637f0c68664accdfe515f2ea";
	public static final String PATENT_APPL_DATE_FIELD = "42437fd3b6fd38418a6d0e6068a32f2a";
	public static final String PATENT_NOTICE_NO_FIELD = "d3fc8c21aa05c0c8e7c1c52a704d2f72";
	public static final String PATENT_NOTICE_DATE_FIELD = "8cec5a2fbf128bca2cbf39059e0908d4";
	public static final String PATENT_PUBLISH_NO_FIELD = "76ab6a77047bd64250795aa9e1bc9e49";
	public static final String PATENT_PUBLISH_DATE_FIELD = "eb2f853e0fefd4af566cbd1fa8f5f744";
	public static final String ASSIGNEE_NAME_FIELD = "614191c8ec65d0e6429801429794ebcd";
	public static final String APPLIANT_NAME_FIELD = "6c761184252dd9f0148a361ed9c4c8c2";
	public static final String INVENTOR_NAME_FIELD = "f08b87006f899e74c0b570f76349f49d";
	public static final String PATENT_STATUS_FIELD = "bca0a0b8d0cc16e64758ad3a0fec31ee";
	public static final String PATENT_COST_FIELD = "3892cf473179f665709031feccfc8ac8";
	public static final String PATENT_FAMILY_FIELD = "ce47e85fa784fd2a4a681ccf572712c9";
	public static final String SCHOOL_NO_FIELD = "71edfc0d2783857fefb360e574a90355";
	public static final String SCHOOL_APPL_YEAR_FIELD = "78f94e6e1b246d9419faa044a1119cc7";
	public static final String SCHOOL_MEMO_FIELD = "c53e8e4e240d9d7a0f1bcb956fbe7630";
	public static final String PATENT_MEMO = "c53e8e4e240d9d7a0f1bcb956fbe7630";
	
	public static final String APPL_COUNTRY_TW = "tw";
	public static final String APPL_COUNTRY_US = "us";
	public static final String APPL_COUNTRY_CN = "cn";
	
	public static final String BUSSINESS_PLATFORM = "04ea692278889b6621409d68c88aab17";
	
	public static final String SYSTEM_ADMIN = "40w9dse0277455f634fw40439sd";
	
	public static final String EXCEL_COLUMN_FILE_NO = "檔號";
	public static final String EXCEL_COLUMN_BUSSINESS = "單位";
	public static final String EXCEL_COLUMN_SCHOOL_NO = "校內編號";
	public static final String EXCEL_COLUMN_PATENT_NAME = "專利名稱";
	public static final String EXCEL_COLUMN_PATENT_NAME_EN = "專利英文名稱";
	public static final String EXCEL_COLUMN_APPLICANT_COUNTRY = "申請國家";
	public static final String EXCEL_COLUMN_PATENT_STATUS = "專利狀態";
	public static final String EXCEL_COLUMN_APPLICANT = "申請人";
	public static final String EXCEL_COLUMN_ASSIGNEE = "專利權人";
	public static final String EXCEL_COLUMN_INVENTOR = "發明人";
	public static final String EXCEL_COLUMN_APPLICATION_DATE = "專利申請日期";
	public static final String EXCEL_COLUMN_APPLICATION_NO = "專利申請號";
	public static final String EXCEL_COLUMN_NOTICE_DATE = "專利公開日期";
	public static final String EXCEL_COLUMN_NOTICE_NO = "專利公開號";
	public static final String EXCEL_COLUMN_PUBLIC_DATE = "專利公告日期";
	public static final String EXCEL_COLUMN_PUBLIC_NO = "專利公告號";
	public static final String EXCEL_COLUMN_PATENT_NO = "專利號";
	
	public static final String EXCEL_COLUMN_PAY_VAILD_DATE = "年費有效日期";
	public static final String EXCEL_COLUMN_PAY_EXPIRE_YEAR = "年費有效年次";
	public static final String EXCEL_COLUMN_PATENT_GEGIN_DATE = "專利起始日";
	public static final String EXCEL_COLUMN_PATENT_END_DATE = "專利截止日";
	public static final String EXCEL_COLUMN_PATENT_CANEL_DATE = "專利取消日期";
	public static final String EXCEL_COLUMN_PATENT_EXPIRE_DATE = "專利到期日期";
	public static final String EXCEL_COLUMN_PATENT_VAILD_DATE = "專利有效年份";
	public static final String EXCEL_COLUMN_PATENT_ABSTRACT = "專利摘要";
	public static final String EXCEL_COLUMN_PATENT_CLAIM = "專利claim";
	public static final String EXCEL_COLUMN_PATENT_CLASSIFICATION = "國際專利分類";
	public static final String EXCEL_COLUMN_PATENT_DESC = "專利描述";
	public static final String EXCEL_COLUMN_PATENT_BUILD_DATE = "建檔時間";
	public static final String EXCEL_COLUMN_PATENT_BUILD_PERSON = "建檔人員";

	public static final String EXCEL_COLUMN_SCHOOL_APPL_YEAR = "申請年度";
	public static final String EXCEL_COLUMN_SCHOOL_NOTE = "學校備註";
	
	public static final String STATUS_FROM_USPTO = "uspto";
	public static final String STATUS_FROM_EPO = "epo";
	public static final String STATUS_FROM_SYSTM = "sys";
	
	public static final String VIEW  = "view";
	public static final String EDIT  = "edit";
	public static final String ADD  = "add";
	
	
	public static String HTML_FORGET_PASSWORD  = "";
	public static String HTML_NEW_ACCOUNT  = "";
	public static String HTML_ONE_PATENT_CHANGE  = "";
	public static String HTML_MULTIPLE_PATENT_CHANGE  = "";
	public static String HTML_ANNUITY_REMINDER  = "";
	
	public static String URL_RESET_PASSWORD  = "";
	public static String URL_ENABLE_PASSWORD  = "";
	public static String URL_PATENT_CONTENT  = "";
	
	public static final List<Integer> defaultReminderDays = Arrays.asList(90, 30, 1, -1); 
}
