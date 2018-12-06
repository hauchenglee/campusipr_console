package biz.mercue.campusipr.util;



public class Constants {
	
	public static final String LINE_VERIFY_ACCOUNT = "Udeadbeefdeadbeefdeadbeefdeadbeef";
	
	
	public static String SYSTEM_LANGUAGE_TW = "tw";
	public static String SYSTEM_LANGUAGE_US = "us";
	
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
	
	public static final String FB_GRAPH_API_URL = "https://graph.facebook.com/v2.11/";
	public static final String FB_ACCESS_TOKEN_URL = "https://graph.facebook.com/v2.10/oauth/access_token?";
	public static final String FB_LONG_LIVE_ACCESS_TOKEN_URL = "https://graph.facebook.com/v2.10/oauth/access_token?grant_type=fb_exchange_token&client_id=";
	public static final String FB_USER_ID_URL = "https://graph.facebook.com/v2.10/me?access_token=";
	public static final String FB_GET_FAN_PAGE_LIST = "/accounts?access_token=";
	public static final String FB_LINK_WEBHOOK = "/subscriptions";
	public static final String FB_PAGE_SUBSCRIB = "/subscribed_apps";
	public static final String FB_GET_PICTURE = "/picture?access_token=";
	
	public static String LINE_WEBHOOK_URL_BASE = "";
	public static String FB_WEBHOOK_URL_BASE = "";
	public static String FB_APP_SYSTEM_APP_ID = "";
	
	public static String DUCKLING_API_URL = "";
	
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
	
	
	public static final String  JSON_CODE = "code";
	public static final String  JSON_MESSAGE = "message";
	public static final String  JSON_DATA = "data";
	
	public static final String  JSON_TOKEN = "token";
	
	public static String PICTURE_UPLOAD_PATH = "";
	public static String PICTURE_LOAD_URL = "";
	public static String VIDEO_UPLOAD_PATH = "";
	public static String VIDEO_LOAD_URL = "";
	public static String FILE_UPLOAD_PATH = "";
	public static String FILE_LOAD_URL = "";
	
	
	public static String IMAGEMAGICK_PATH = "";
	public static String GRAPHICSMAGICK_PATH = "";
	public static String FFMPEG_PATH = "";
	
	public static String QUESTIONNAIRE_URL ="";
	public static String QUESTIONNAIRE_IMAGEURL ="";
	public static String JIEBA_DIC_PATH = "";
	public static String JIEBA_USERDIC_PATH = "";
	public static String JIEBA_STOP_WORD_PATH = "";
	public static String JIEBA_PROB_EMIT_PATH = "";
	
	public static final String LINE = "line";
	public static final String FB = "fb";
	
	public static final String FB_GRAPH_URL = "https://graph.facebook.com/v2.6/";
	public static String LINE_MESSAGE_CONTENT_URL_BASE="https://api.line.me/v2/bot/message/";
	public static String LINE_MESSAGE_CONTENT_URL_SUFFIX = "/content";
	public static String LINE_MESSAGE_WEBVIEW_OBTAIN_URL="https://api.line.me/liff/v1/apps";
	public static String LINE_MESSAGE_WEBVIEW_URL_BASE="line://app/";
	
	public static final String REDIRECT_LOGIN = "/login";
	public static final String REDIRECT_MAINPAGE = "/mainpage";
	public static final String REDIRECT_BOTPAGE = "/botpage";
	public static final String URL_LOGIN = "/static/LoginPage.html";
	public static final String URL_SIGN = "/static/Sign.html";
	public static final String URL_MAINPAGE = "/static/MainPage.html";
	public static final String URL_BOTPAGE = "/static/BotPage.html";
	public static final String URL_DASHBOARD = "/static/views/index.html";
	public static final String URL_CALL_CENTER = "/static/views/call_center.html";
	
	public static final int ACTION_CUSTOM_FINISH = 7;
	
	public static final int  INT_MESSAGE_COUNT = 15;
	
	
	public static final String WEBLOCMESSAGE = "getweblocation";
	public static final String LOCAIONMESSAGE = "LocationMessage";
	public static final String ADDRESSMESSAGE = "AddressMessage";
	
	public static final String FOLLOW = "Follow";
	public static final boolean IS_FOLLOW = true;
	public static final String UNFOLLOW = "unFollow";
	
	public static final String GENDER_MALE = "男";
	public static final String CODE_GENDER_MALE  = "male";
	public static final String GENDER_FEMALE = "女";
	public static final String CODE_GENDER_FEMALE = "female";
	public static final String GENDER_OTHER = "其他";
	public static final String CODE_GENDER_OTHER = "other";
	public static final String CODE_ALL = "all";
	public static final String CITY_ALL = "全部";
	
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
	

	
	public static final String SOCIAL_TYPE_LINE = "line";
	public static final String SOCIAL_TYPE_FACEBOOK = "fb";
	public static final String SOCIAL_TYPE_WECHAT= "wechat";
	public static final String SOCIAL_TYPE_APP= "app";
	
	
	public static final String SOCIAL_MESSAGE_TYPE_TEXT = "text";
	public static final String SOCIAL_MESSAGE_TYPE_IMAGE = "image";
	public static final String SOCIAL_MESSAGE_TYPE_AUDIO = "audio";
	public static final String SOCIAL_MESSAGE_TYPE_VIDEO = "video";
	public static final String SOCIAL_MESSAGE_TYPE_FILE = "file";
	
	public static final String KEY_WIDTH = "width";
	public static final String KEY_HEIGHT = "height";
	public static final String KEY_SIZE = "size";
	public static final String KEY_SUFFIX = "suffix";
	public static final String KEY_RESULT = "result";
	public static final String VALIDATE_KEY_MESSAGE_EN = "message_en";
	public static final String VALIDATE_KEY_MESSAGE_CHI = "message_chi";
	public static final String VALIDATE_KEY_ERROR_ID = "error_id_list";
	
	public static final int THUMBNAIL_IMAGE_SIDE_LENGTH = 240;
	
	public static final int SMALL_IMAGE_SIDE_LENGTH = 460;
	public static final int MEDIUM_IMAGE_SIDE_LENGTH = 700;
	public static final int LARGE_IMAGE_SIDE_LENGTH = 1024;
	
	public static final int LINE_RICH_MENU_HEIGHT_LENGTH = 2500;
	public static final int LINE_RICH_MENU_WIDTH_LENGTH_FULL = 1686;
	public static final int LINE_RICH_MENU_WIDTH_LENGTH_HALF = 843;
	
	public static final int FB_FILE_SIZE_LIMIT = 25;//MB
	public static final int LINE_FILE_SIZE_LIMIT = 10;//MB
	public static final int LINE_IMAGE_SIZE_LIMIT = 1;//MB
	
	public static final int LINE_VIDEO_DURATION_LIMIT = 1;//Minute
	
	public static final int LINE_IMAGE_LENGTH_LIMIT = 1024;//px
	
	public static final String SOCIAL_MESSAGE_TYPE_BUTTON = "button";
	
	
	public static final String SOCIAL_MESSAGE_TYPE_LOCATION = "location";
	public static final String SOCIAL_MESSAGE_TYPE_FB_GENERIC = "generic";
	public static final String SOCIAL_MESSAGE_TYPE_FB_QUICK_REPLY = "quick_reply";
	public static final String SOCIAL_MESSAGE_TYPE_COMMENT_MESSAGE = "comment_message";
	public static final String SOCIAL_MESSAGE_TYPE_FB_LIST = "list";
	public static final String SOCIAL_MESSAGE_TYPE_FB_MEDIA = "media";

	
	public static final String SOCIAL_MESSAGE_TYPE_LINE_STICKER = "sticker";
	public static final String SOCIAL_MESSAGE_TYPE_LINE_CONFIRM = "confirm";
	public static final String SOCIAL_MESSAGE_TYPE_LINE_CAROUSEL = "carousel";
	public static final String SOCIAL_MESSAGE_TYPE_LINE_IMAGE_CAROUSEL = "image_carousel";
	public static final String SOCIAL_MESSAGE_TYPE_LINE_IMAGEMAP = "imagemap";
	
	public static final String SOCIAL_MENU_TYPE_GREETING = "greeting";
	public static final String SOCIAL_MENU_TYPE_GET_START = "get_started";
	public static final String SOCIAL_MENU_TYPE_MENU = "menu";
	
	public static final int LINE_MENU_TYPE_1 = 1;
	public static final int LINE_MENU_TYPE_2 = 2;
	public static final int LINE_MENU_TYPE_3 = 3;
	public static final int LINE_MENU_TYPE_4 = 4;
	public static final int LINE_MENU_TYPE_5 = 5;
	public static final int LINE_MENU_TYPE_6 = 6;
	
	public static String IP = "localhost:8080";
	
	public static final int DM_LAYOUT_ONE = 1;
	public static final int DM_LAYOUT_TWO_HORI = 2;
	public static final int DM_LAYOUT_SIX = 3;
	
	public static final int DM_LAYOUT_TWO_VERT = 4;
	public static final int DM_LAYOUT_THREE_EQUAL_VERT = 5;
	
	public static final int DM_LAYOUT_FOUR_SQUARE = 6;
	
	public static final int DM_LAYOUT_THREE_MAIN_HORI_SLAVE = 7;
	
	public static final int DM_LAYOUT_THREE_MAIN_VERT_SLAVE = 8;
	
	public static final String DM_SUB_TYPE_URL = "url";
	
	public static final String  DM_SUB_TYPE_PRODUCT = "product";
	
	//push types
	public static final String PUSH_TYPE_PRODUCT = "product";
	public static final String PUSH_TYPE_DM = "dm";
	public static final String PUSH_TYPE_CAMPAIGN = "campaign";
	public static final String PUSH_TYPE_MEDIA= "media";
	public static final String PUSH_TYPE_TEXT = "text";
	public static final String PUSH_TYPE_QUESTIONAIRE = "questionnaire";
	
	public static final String WEB_SOCKET_MESSAGEMAPPING_URL = "/chat/";
	public static final String WEB_SOCKET_MESSAGE_SEND_URL = "/topic/messages/";
	public static final String WEB_SOCKET_MESSAGE_BROADCAST_URL = "/topic/messages/broadcast";
	public static final String APP_SOCKET_MESSAGEMAPPING_URL = "/app/chat/";
	public static final String APP_SOCKET_MESSAGE_SEND_URL = "/topic/app/messages/";
	
	
	public static final String ACTIVATION_CODE_SUCCESS = "success";
	public static final String ACTIVATION_CODE_FAIL = "fail";
	public static final String ACTIVATION_CODE_EXPIRING = "expiring";
	public static final String ACTIVATION_CODE_EXPIRED = "expired";
	public static final String ACTIVATION_CODE_VALID = "valid";
	public static final String ACTIVATION_CODE_INVALID = "invalid";
	public static final String ACTIVATION_CODE_WELCOME = "welcome";
	public static final String ACTIVATION_CODE_SELF = "self";
	public static final String ACTIVATION_CODE_PUSH = "push";
	
	
	public static final String ADMIN_ID = "80f5d577bc721c2c02b3ee00a25b092e";
	public static final String APP_BOT_ID = "1f9d09f1976f0e1286206f3d7d7045fa";
	public static final String BUSINESS_ID = "ad8ab7be26d597e88a20f616ce524405";
	public static final String SYSTEM_UNKNOWN_FLOW_ID = "659d0b8de63d2cfe6d8c645866d6644f";
	public static final String FLOWTYPE_UNKNOWN = "unknown";
	public static final String FLOWTYPE_SYSTEM_UNKNOWN = "system_unknown";
	public static final String ADMIN_MANAGEMENT_MODULE_CODE = "admin_management";
	
	public static final String QUESTIONNAIRE_TYPE_NUMBER = "num";
	public static final String QUESTIONNAIRE_TYPE_TEXT = "text";
	public static final String QUESTIONNAIRE_TYPE_LIST = "list";
	
	public static final String ORDER_TYPE_UNHANDLED_ID = "axmdlseirh932ha8s";
	public static final String ORDER_TYPE_CANCELLED = "已取消";
	public static final String ORDER_TYPE_SUCCESSFUL = "成功";
	public static final String ORDER_TYPE_UNSUCCESSFUL = "未成功";
	public static final String ORDER_TYPE_UNHANDLED = "尚未處理";
	public static final String ORDER_TYPE_HANDLING = "處理中";
	
	public static final String AUTOREPLY_TYPE_OFFLINE = "agent_offine";
	public static final String AUTOREPLY_TYPE_BUSY = "agent_busy";
	public static final String AUTOREPLY_TYPE_OUT_OF_WORK = "out_of_work";
	public static final String AUTOREPLY_TYPE_WELCOME = "welcome";
	public static final String AUTOREPLY_TYPE_SELF_DEFINE = "self_define";
	
	public static final String AUTOREPLY_TYPE_SESSION_EXPIRE = "session_expire";
	
	
	public static final String SYS_AUTOREPLY_TYPE_OFFLINE = "system_agent_offine";
	public static final String SYS_AUTOREPLY_TYPE_BUSY = "system_agent_busy";
	public static final String SYS_AUTOREPLY_TYPE_OUT_OF_WORK = "system_out_of_work";
	public static final String SYS_AUTOREPLY_TYPE_WELCOME = "system_welcome";
	
	public static final String SYS_AUTOREPLY_TYPE_SESSION_EXPIRE = "system_session_expire";
	
	public static final String MESSAGE_SEND = "send";
	public static final String MESSAGE_RECEIVE = "receive";
	
	public static final String SYSTEM_ADMIN_ID = "1";
	public static final String SYSTEM_MANAGER_ROLE_NAME="系統管理者";
	
	public static final String INTENT_TYPE_PRODUCT = "product";
	
	public static final String AGENT_STATUS_ONLINE = "online";
	public static final String AGENT_STATUS_OFFLINE = "offline";
	public static final String AGENT_STATUS_LEAVING = "leaving";
	public static final String AGENT_STATUS_BUSY = "busy";
	
	public static final String NODE_TYPE_QA = "qa";
	
	public static final String NODE_TYPE_QA_ASK = "ask";
	
	public static final String NODE_TYPE_CS = "cs";
	
	public static final String NODE_CS_KEYWORD = "專員,真人,轉客服";
	
	public static final String NODE_TYPE_TEXT = "text";
	
	public static final String NODE_TYPE_BUTTON = "button";
	
	public static final String NODE_TYPE_IMAGE = "image";
	
	public static final String NODE_TYPE_CAROUSEL= "carousel";
	
	public static final String NODE_TYPE_POSITION= "position";
	
	public static final String NODE_TYPE_VIDEO= "video";
	
	public static final String NODE_TYPE_AUDIO= "audio";
	
	
	public static final String NODE_TYPE_QUESTIONNAIRE= "questionnaire";
	
	public static final String NODE_TYPE_COMMENT_MESSENGER= "comment_messenger";
	
	public static final String NODE_TYPE_FLOW= "flow";
	
	public static final String NODE_TYPE_API= "api";
	
	public static final String NODE_TYPE_END= "end";
	
	public static final String NODE_TYPE_QRCODE = "qrcode";
	
	public static final String NODE_TYPE_SYSTEM_END= "system_end";
	
	public static final String NODE_TYPE_PRODUCT= "product";
	
	public static final String NODE_TYPE_IMAGEMAP = "imagemap";
	
	public static final String NODE_TYPE_SYSTEM_QA = "system_qa";
	
	public static final String NODE_TYPE_SYSTEM_CS = "system_cs";
	
	public static final String NODE_TYPE_SYSTEM_API= "system_api";
	
	public static final String NODE_TYPE_SYSTEM_QUESTIONNAIRE= "system_questionnaire";
	
	public static final String NODE_TYPE_SYSTEM_COMMENT_MESSENGER= "system_cm";
	
	public static final String NODE_TYPE_SYSTEM_FLOW= "system_flow";
	
	public static final String NODE_TYPE_SYSTEM_QA_ASK = "system_qa_ask";
	
	public static final String NODE_TYPE_SYSTEM_TEXT = "system_text";
	
	public static final String NODE_TYPE_SYSTEM_IMAGE = "system_image";
	
	public static final String NODE_TYPE_SYSTEM_AUDIO = "system_audio";
	
	public static final String NODE_TYPE_SYSTEM_VIDEO = "system_video";
	
	public static final String NODE_TYPE_SYSTEM_POSITION = "system_position";
	
	public static final String NODE_TYPE_SYSTEM_BUTTON = "system_button";
	public static final String NODE_TYPE_SYSTEM_CAROUSEL = "system_carousel";
	public static final String NODE_TYPE_SYSTEM_PRODUCT = "system_product";
	
	public static final String NODE_TYPE_SYSTEM_QRCODE = "system_qrcode";
	
	public static final String NODE_TYPE_SYSTEM_IMAGEMAP = "system_imagemap";
	
	
	public static final String FLOW_TYPE_SYSTEM_COMMON = "system_common";
	public static final String FLOW_TYPE_SYSTEM_FB_COMMENT= "system_comment_messenger";
	public static final String FLOW_TYPE_COMMON = "common";
	public static final String FLOW_TYPE_FB_COMMENT = "comment_messenger";
	public static final String FLOW_TYPE_BOT_START = "bot_start";

	public static String WEB_REPLY_URL = "";
	
	
	
	
	public static final String NODE_LINK_TYPE_TEXT = "text";
	public static final String NODE_LINK_TYPE_BUTTON = "button";
	
	
	public static final String NODE_INPUT_TYPE_TEXT = "text";
	public static final String NODE_INPUT_TYPE_BUTTON = "button";
	public static final String NODE_INPUT_TYPE_URL = "url";
	
	
	public static final int NODE_LINK_STATE_SUCCESS = 1;
	public static final int NODE_LINK_STATE_FAIL = 0;
	
	
	public static final String FIELD_TEMPLATE = "{@}";
	public static final String FIELD_TEMPLATE_PREFFIX = "{@";
	public static final String FIELD_TEMPLATE_SUFFIX = "}";
	public static final String FIELD_GROUP_SYSTEM= "system";
	public static final String FIELD_GROUP_SLEF= "self";
	
	public static final String ITEM_TYPE_FLOW= "flow";
	
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
	
	
	public static String CODE_URL= "";
	public static final String CODE_REGEX_PATTERN = "\\{@(.*?)\\}";
	public static final String CODE_TYPE_QRCODE= "qrcode";
	public static final String CODE_TYPE_BARCODE= "barcode";
	
	public static final String MEDIA_VIDEO_PRIFIX= "orginal";
	
	public static final String FIELD_GROUP_TYPE_SYSTEM= "system";
	public static final String FIELD_GROUP_TYPE_SELF= "self";
	public static final String FIELD_GROUP_ITEM_TYPE_USER= "user";
	public static final String FIELD_GROUP_ITEM_TYPE_FLOW= "flow";
	
	public static final String FIELD_TYPE_STRING = "string";
	public static final String FIELD_TYPE_PHONE = "phone";
	public static final String FIELD_TYPE_NUM = "num";
	public static final String FIELD_TYPE_ADDRESS = "address";
	public static final String FIELD_TYPE_DATETIME = "datetime";
	public static final String FIELD_TYPE_GENDER = "gender";
	public static final String FIELD_TYPE_YESNO = "boolean";
	public static final String FIELD_TYPE_RESOURCE_URL = "url";
	
	
	public static final String FIELD_NAME_PHONE= "user_phone";
	public static final String FIELD_NAME_USER_NAME= "user_name";
	public static final String FIELD_NAME_ADDRESS= "user_address";
	public static final String FIELD_NAME_BIRTHDAY= "user_birthday";
	public static final String FIELD_NAME_GENDER= "user_gender";
	public static final String FIELD_NAME_EMAIL= "user_email";
	public static final String FIELD_NAME_USER_ID= "user_id";
	public static final String FIELD_NAME_BOT= "bot_id";
	public static final String FIELD_NAME_BUSINESS= "business_id";
	
	public static final int BOT_STATE_BOT = 1;
	public static final int BOT_STATE_CS = 2;
	public static final int BOT_STATE_MIXED = 3;
	
	public static final int IMAGEMAP_TYPR_NO_BUTTON = 0;
	public static final int IMAGEMAP_TYPR_1 = 1;
	public static final int IMAGEMAP_TYPR_2 = 2;
	public static final int IMAGEMAP_TYPR_3 = 3;
	public static final int IMAGEMAP_TYPR_4 = 4;
	public static final int IMAGEMAP_TYPR_5 = 5;
	public static final int IMAGEMAP_TYPR_6 = 6;
	public static final int IMAGEMAP_TYPR_7 = 7;
	public static final int IMAGEMAP_TYPR_8 = 8;
}
