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
	
	
	public static final String ROLE_PLATFORM_MANAGER = "a45e5e977721ed92dd3b914e3efd8dcf";
	public static final String ADD_ADMIN_PLATFORM_MANAGER = "d8492b4ec0b23574cdc4cfc9407ed4f0";
	public static final String EDIT_ADMIN_PLATFORM_MANAGER = "63f5ce8ac07727e82044a8ac09979fd4";
	public static final String VIEW_ADMIN_PLATFORM_MANAGER = "55a3d1e1e26ea93e51f7de262a2c4c48";
	

	
	public static final String ROLE_PLATFORM_PATENT = "ed9f24e7998c679c3932161880327984";
	public static final String ADD_ADMIN_PLATFORM_PATENT = "4a1c7dae9894c686d984ed431db79514";
	public static final String EDIT_ADMIN_PLATFORM_PATENT  = "269ab3f26085ad2a18796f704fe3a187";
	public static final String VIEW_ADMIN_PLATFORM_PATENT  = "93112edc7d5c9a2a628a6d45b87ebb04";
	
	
	
	public static final String ROLE_BUSINESS_MANAGER = "ab9baaedd20c3a62e89891df8ef38365";
	public static final String ADD_BUSINESS_MANAGER  = "3bdc558b3004cadb5820db7fc47b512d";
	public static final String EDIT_BUSINESS_MANAGER  = "ff7acd10470d4b3b72b637da7e904d76";
	public static final String VIEW_BUSINESS_MANAGER  = "b1ab967b591ed32bec1f9256594c8493";
	
	
	public static final String ROLE_BUSINESS_PATENT = "a19c8c01d3cea53cf0722509100d1754";
	public static final String ADD_ADMIN_BUSINESS_PATENT = "ed9f24e7998c679c3932161880327984";
	public static final String EDIT_ADMIN_BUSINESS_PATENT  = "269ab3f26085ad2a18796f704fe3a187";
	public static final String VIEW_ADMIN_BUSINESS_PATENT  = "93112edc7d5c9a2a628a6d45b87ebb04";
	
	
	public static final String ROLE_COMMON_USER = "f950f7d5be2cff1d20228e1c9a3adf0d";
	public static final String ADD_ADMIN_USER = "86206d55203397ff7103b07244049f89";
	public static final String EDIT_ADMIN_USER  = "54dcc500a45d746605abbe4df85a07b0";
	public static final String VIEW_ADMIN_USER  = "400eacc099896a08be66393c32804f40";
	
	
	
	public static final String VIEW  = "view";
	public static final String EDIT  = "edit";
	public static final String ADD  = "add";
}
