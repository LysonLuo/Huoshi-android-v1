package im.huoshi.model;

/**
 * Created by Lyson on 16/2/16.
 */
public class City extends ApiObject {
    private String id;
    private String cityName;

    public String getId() {
        return id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
