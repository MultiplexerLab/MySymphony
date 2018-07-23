package harmony.app.Helper;

public class Endpoints {
    public static String DOMAIN_PREFIX = "http://bot.sharedtoday.com:9500";
    public static final String GET_APP_INFO="http://bot.sharedtoday.com:9500/ws/mysymphony/getInstanceConfig";
    public static final String ADD_NEW_USER=DOMAIN_PREFIX+"/ws/mysymphony/commonInsertIntoSymUsers?tbl=symUsers";
    public static final String NEW_HOME_GET_URL = DOMAIN_PREFIX+"/ws/mysymphony/getCategorisedContentFromTable?cat=ALL";
    public static final String ICON_GET_URL = DOMAIN_PREFIX+"/ws/mysymphony/getAllCategory";
    public static final String JAPITO_JIBON_GET_URL = DOMAIN_PREFIX+"/ws/mysymphony/getCategorizedContents?cat=daily_life";
    public static final String CATEGORY_BASE_URL=DOMAIN_PREFIX+"/ws/mysymphony/getCategorisedContentFromTable?cat=";
    public static final String AUTTOHASI_GET_URL=DOMAIN_PREFIX+"/ws/mysymphony/getCategorisedContentFromTable?cat=autto_hashi";
    public static final String PORASHUNA_GET_URL=DOMAIN_PREFIX+"/ws/mysymphony/getCategorisedContentFromTable?cat=education";
    public static final String SEND_MOBILE_NUMBER_TO_SERVER_URL=DOMAIN_PREFIX+"/ws/gen2FACode?prcName=signUp&uid=";
    public static final String PHONE_NUMBER_ENTRY_URL=DOMAIN_PREFIX+"/ws/signUpInsertIntoSingleTable?tbl=partner";
    public static final String USER_MOBILE_VARIFICATION_GET_URL=DOMAIN_PREFIX+"/ws/gen2FACode?prcName=signUp&uid=";
    public static final String USER_SIGN_IN_GET_VALIDATE_MOBILE_VARIFICATION_URL=DOMAIN_PREFIX+"/ws/validate2FACode?prcName=signUp&uid=01717&genRef=451&code=532526";
    public static final String USER_SIGN_IN_POST_URL=DOMAIN_PREFIX+"/mysymphony/login";
    public static final String USER_SIGN_UP_STATUS_POST_URL=DOMAIN_PREFIX+"/login";
    public static final String UPDATE_DEFAULT_PASSWORD_POST_URL=DOMAIN_PREFIX+"/ws/changePassword?pass=01717&newpass=";
    public static final String GET_USER_INFO_URL=DOMAIN_PREFIX+"/ws/commonGetFromTable?tbl=Partner&key=partnerId&val=";
    public static final String GET_REFEREL_CODE_URL=DOMAIN_PREFIX+"/ws/mysymphony/getPaymentRefCode";
    public static final String APPLOGS_POST_URL=DOMAIN_PREFIX+"/ws/mysymphony/insertAppLogs";
    public static final String GET_EMOTICONS_INFO=DOMAIN_PREFIX+"/ws/mysymphony/getCategorisedContentFromTable?cat=emoticon";
    public static final String GET_APPS_INFO=DOMAIN_PREFIX+"/ws/mysymphony/getCategorisedContentFromTable?cat=mobile_app";
    public static final String GET_INTRO_INFO=DOMAIN_PREFIX+"/ws/mysymphony/getCategorisedContentFromTable?cat=intro_slides";
    public static final String CONTACTUS_POST_URL=DOMAIN_PREFIX+"/ws/commonInsertIntoSingleTable?tbl=contactUs";
    public static final String OBJECTIVES_GET_URL=DOMAIN_PREFIX+"/ws/commonGetFromTableForSym?tbl=contactPurpose&key=id&val=%25";
    public static final String FIREBASE_TOKEN_POST_URL=DOMAIN_PREFIX+"/ws/refreshFirebaseToken";
    public static final String GET_PAYMENT_INFO=DOMAIN_PREFIX+"/ws/commonGetFromTableForSym?tbl=paymentData";
    public static final String PAYMENT_POST_URL=DOMAIN_PREFIX+"/ws/addPaymentData";
    public static final String GET_SUBSCRIPTION_CONFIG=DOMAIN_PREFIX+"/ws/getSubscriptionConfig";
    public static final String GET_CONTENT_SUGGESTION=DOMAIN_PREFIX+"/ws/getContentSuggestion";
    public static final String SUBSCRIPTION_POST_URL=DOMAIN_PREFIX+"/ws/insertUserSubscription";
    public static final String GET_USER_SUBSCRIPTION=DOMAIN_PREFIX+"/ws/getUsersSubscription?userId=";
}
