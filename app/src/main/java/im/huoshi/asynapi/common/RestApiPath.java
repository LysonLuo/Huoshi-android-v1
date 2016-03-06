package im.huoshi.asynapi.common;

/**
 * API请求路径
 * <p>
 * Created by Lyson on 16/1/15.
 */
public class RestApiPath {
    public static final String GET_VERIFICATION_CODE = "register/code";//获取验证码
    public static final String USER_REGISTER = "users/register";//用户注册
    public static final String USER_LOGIN = "users/login";//用户登录
    public static final String GET_QINIU_TOKEN = "qiniu/token";//获取七牛上传凭证
    public static final String USER_DATA = "users/data";//完善用户信息
    public static final String READ_STAT = "reading/time";//阅读统计
    public static final String HUOSHI_TAB = "huoshi/tab";//活石页面统计
    public static final String PUB_INTERCES = "intercession";//发布代祷
    public static final String INTERCES_LIST = "intercession/list";//代祷列表
    public static final String ASYN_CONTACTS = "contacts";//同步通讯录

}
