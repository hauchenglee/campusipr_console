package biz.mercue.campusipr.util;



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
	
	public static String DUCKLING_API_URL = "";
	
	public static String MAIL_STARTTLS = "";
	public static String MAIL_HOST = "";
	public static String MAIL_PORT = "";
	public static String MAIL_USER_NAME = "";
	public static String MAIL_PASSWORD = "";
	public static String SYSTEM_EMAIL = "";
	
	public static String RECAPTCHA_SECRET_TOKEN = "";
	
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
	
	public static final int  INT_SUCCESS_AND_INFORM = 2;
	
	public static final int  INT_FAIL = -1;
	public static final String  MSG_FAIL = "失效";
	public static final String  MSG_MENU_EXPIRED = "圖文選單過期";
	
	public static final int INT_SYSTEM_PROBLEM =  -1 ;
	public static final String MSG_SYSTEM_PROBLEM  =  "系統發生問題" ;
	
	public static final String MSG_BUILD_BOT_PROBLEM  =  "建立機器人失敗，請重新嘗試或是聯絡客服人員。" ;
	
	public static final int INT_CANNOT_FIND_USER =  -2 ;
	public static final String MSG_CANNOT_FIND_USER =  "找不到使用者" ;
	
	public static final int INT_CANNOT_FIND_DATA =  -2 ;
	public static final String MSG_CANNOT_FIND_DATA =  "找不到資料" ;
	
	public static final int INT_CANNOT_FIND_AGENT =  -2 ;
	public static final String MSG_CANNOT_FIND_AGENT =  "尚未有客服";
	
	public static final int INT_MODULE_CODE_UNREGISTERED = -2;
	public static final String MSG_MODULE_CODE_UNREGISTERED = "module code尚未註冊";
	
	public static final int INT_PASSWORD_ERROR =  -3 ;
	public static final String MSG_PASSWORD_ERROR =  "帳號或密碼錯誤" ;
	
	public static final int INT_USER_DUPLICATE =  -4 ;
	public static final String MSG_USER_DUPLICATE =  "Email已被註冊" ;
	
	public static final int INT_DATA_DUPLICATE =  -4 ;
	public static final String MSG_DATA_DUPLICATE =  "資料重覆" ;
	
	public static final int INT_MODULE_CODE_DUPICATE = -4;
	public static final String MSG_MODULE_CODE_DUPLICATE = "module code重複";
	
	public static final int INT_MODULE_LANGUAGE_DUPLICATE = -4;
	public static final String MSG_MODULE_LANGUAGE_DUPLICATE = "language重複";
	
	
	public static final int INT_DATA_ERROR =  -5 ;
	public static final int INT_LINE_IMAGE_SIZE_ERROR =  -1 ;
	public static final int INT_LINE_FILE_SIZE_ERROR =  -2 ;
	public static final int INT_FB_FILE_SIZE_ERROR =  -3 ;
	public static final int INT_LINE_IMAGE_FORMAT_ERROR =  -4 ;
	public static final int INT_LINE_VIDEO_FORMAT_ERROR =  -5 ;
	public static final int INT_LINE_AUDIO_FORMAT_ERROR =  -6 ;
	public static final int INT_LINE_IMAGE_WIDTH_HEIGHT_ERROR =  -7 ;
	public static final int INT_LINE_VIDEO_DURATION_ERROR =  -8 ;
	public static final int INT_LINE_NOT_SUPPORT_ERROR =  -9 ;
	public static final String MSG_DATA_ERROR =  "輸入資料錯誤" ;
	public static final String MSG_IMAGE_SIZE_ERROR =  "圖片大於1MB" ;
	public static final String MSG_FILE_SIZE_ERROR =  "圖片大於10MB" ;
	public static final String MSG_FB_FILE_SIZE_ERROR =  "檔案大於25MB" ;
	public static final String MSG_LINE_IMAGE_FORMAT_ERROR =  "圖片格式錯誤，僅支援jepg、jpg、png" ;
	public static final String MSG_LINE_VIDEO_FORMAT_ERROR =  "圖片格式錯誤，僅支援mp4" ;
	public static final String MSG_LINE_AUDIO_FORMAT_ERROR =  "音檔格式錯誤，僅支援m4a" ;
	public static final String MSG_LINE_IMAGE_WIDTH_HEIGHT_ERROR =  "圖片大小超出，圖片最大長寬1024x1024 px" ;
	public static final String MSG_LINE_VIDEO_DURATION_ERROR =  "影片長度過長，目前僅支援長度一分鐘內的影片" ;
	public static final String MSG_MESSAGE_FORMAT_ERROR = "訊息格式錯誤";
	public static final String MSG_LINE_NOT_SUPPORT_FILE_ERROR =  "Line不支援其他檔案格式" ;

	public static final int INT_USER_LOGOUT =  -6 ;
	public static final String MSG_USER_LOGOUT =  "已登出系統" ;
	
	public static final int INT_NO_PERMISSION=  -7 ;
	public static final String MSG_NO_PERMISSION  =  "無登入系統權限" ;
	
	public static final int  INT_ACCESS_TOKEN_ERROR = -10;
	public static final String  MSG_ACCESS_TOKEN_ERROR= "Token錯誤！";
	
	public static final int  INT_LACK_ESSENTIAL_DATA= -11;
	public static final String  MSG_LACK_ESSENTIAL_DATA = "缺少必備資料！";
	
	public static final int INT_FILE_TOO_BIG_ERROR= -12;
	public static final String MSA_FILE_TOO_BIG_ERROR= "檔案過大！";
		
	public static final int INT_FILE_FORMAT_ERROR= -13;
	public static final String MSG_FILE_FORMAT_ERROR= "檔案格式錯誤！";
	
	public static final int  INT_NODE_LINK_DELETE_FAIL = -14;
	public static final String  MSG_NODE_LINK_DELETE_FAIL = "不需要刪除線";
	
	public static final int INT_FILE_SAVE_ERROR= -15;
	public static final String MSG_FILE_SAVE_ERROR= "檔案儲存失敗！";

	public static final int INT_FLOW_OPRATION_ERROR = -16;
	public static final String MSG_SYSTEM_FLOW_CANNOT_BE_COPIED = "無法複製系統流程";
	public static final String MSG_FLOW_CANNOT_BE_DELETED = "無法刪除系統流程";


	//web socket ACTION CODE
	public static final int AGENT_CLOSE_CONNECTION = -1;
	public static final int AGENT_GET_ALL_ACCOUNTS_MESSAGES = 1;
	public static final int AGENT_SEND_A_MESSAGE = 2;
	public static final int AGENT_RECEIVE_A_MESSAGE = 3;
	public static final int AGENT_GET_SINGLE_ACCOUNT_MESSAGES = 4;
	//public static final int SEND_NEW_ACCOUNT_AND_MESSAGE_TO_SOCKET = 5;
	//public static final int SEND_NEW_ACCOUNT_LIST_TO_ADMIN_BY_HTTP = 6;
	public static final int AGENT_CHECK_CONNECTION = 7;
	
	public static final int AGENT_READ_MESSAGE = 8;
	
	public static final int AGENT_ALL_READ_MESSAGE = 9;
	
	public static final int AGENT_IS_TYPING = 10;
	
	public static final int AGENT_UNTYPED= 11;
	
	
	public static final int AGENT_REFRESH_OFFLINE_MESSAGE= 12;
	
	//app socket action code
	public static final int APP_GET_ALL_ACCOUNTS_MESSAGES = 21;
	public static final int APP_SEND_A_MESSAGE = 22;
	public static final int APP_RECEIVE_A_MESSAGE = 23;
	
	public static final int APP_READ_MESSAGE = 24;
	
	public static final int APP_ALL_READ_MESSAGE = 25;
	
	public static final int APP_LOGOUT = 26;
	
	public static final int APP_IS_TYPING = 27;
	
	public static final int APP_UNTYPED = 28;
	

	

	
	

	

	

	public static final int SYSTEM_PAGE_SIZE = 20;

	

	
//	public static final int FB_FILE_SIZE_LIMIT = 25;//MB
//	public static final int LINE_FILE_SIZE_LIMIT = 10;//MB
//	public static final int LINE_IMAGE_SIZE_LIMIT = 1;//MB
	

	

	
	public static final String WEB_SOCKET_MESSAGEMAPPING_URL = "/chat/";
	public static final String WEB_SOCKET_MESSAGE_SEND_URL = "/topic/messages/";
	public static final String WEB_SOCKET_MESSAGE_BROADCAST_URL = "/topic/messages/broadcast";
	public static final String APP_SOCKET_MESSAGEMAPPING_URL = "/app/chat/";
	public static final String APP_SOCKET_MESSAGE_SEND_URL = "/topic/app/messages/";
	
	

	public static final String SYSTEM_ADMIN_ID = "1";
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
	
}
