package im.huoshi.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Lyson on 16/5/20.
 */
public class DailyAsked extends ApiObject {
    @SerializedName("question_id")
    private int questionId;
    private String title;
    private String content;
    private String url;

    public int getQuestionId() {
        return questionId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
