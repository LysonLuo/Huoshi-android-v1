package im.huoshi.model;

/**
 * Created by Lyson on 16/1/4.
 */
public class ApiError {
    public static final String API_TOKEN_WRONG_ERROR = "api_token_wrong";
    private static final String DEFAULT_ERROE_MESSAGE = "网络不太给力，请重试！";
    public int statusCode;
    public String errorMessage;


    /**
     * json字符串错误
     *
     * @param responseString
     * @param statusCode
     * @return
     */
    public static ApiError errorFromString(String responseString, int statusCode) {
        ApiError apiError = new ApiError();
        apiError.statusCode = statusCode;
        apiError.errorMessage = responseString;
        return apiError;
    }
}
