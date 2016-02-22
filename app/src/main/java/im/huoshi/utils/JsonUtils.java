package im.huoshi.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import im.huoshi.model.City;

import java.util.*;

/**
 * Created by Lyson on 15/7/18.
 * Json文件解析
 */
public class JsonUtils {

    public ArrayList<String> province_list_code = new ArrayList<>();
    public ArrayList<String> city_list_code = new ArrayList<>();

    public List<City> getJSONParserResult(String JSONString, String key) {
        List<City> list = new ArrayList<>();
        JsonObject result = new JsonParser().parse(JSONString).getAsJsonObject().getAsJsonObject(key);

        Iterator iterator = result.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, JsonElement> entry = (Map.Entry<String, JsonElement>) iterator.next();
            City City = new City();

            City.setCityName(entry.getValue().getAsString());
            City.setId(entry.getKey());
            province_list_code.add(entry.getKey());
            list.add(City);
        }
        return list;
    }

    public HashMap<String, List<City>> getJSONParserResultArray(
            String JSONString, String key) {
        HashMap<String, List<City>> hashMap = new HashMap<>();
        JsonObject result = new JsonParser().parse(JSONString).getAsJsonObject().getAsJsonObject(key);

        Iterator iterator = result.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, JsonElement> entry = (Map.Entry<String, JsonElement>) iterator.next();
            List<City> list = new ArrayList<City>();
            JsonArray array = entry.getValue().getAsJsonArray();
            for (int i = 0; i < array.size(); i++) {
                City City = new City();
                City.setCityName(array.get(i).getAsJsonArray().get(0).getAsString());
                City.setId(array.get(i).getAsJsonArray().get(1).getAsString());
                city_list_code.add(array.get(i).getAsJsonArray().get(1).getAsString());
                list.add(City);
            }
            hashMap.put(entry.getKey(), list);
        }
        return hashMap;
    }
}
