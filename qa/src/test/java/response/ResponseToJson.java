package response;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

@Generated("jsonschema2pojo")
public class ResponseToJson {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private Integer status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @SerializedName("token")
    @Expose
    private String token;

    public String getToken() {
        return token;
    }

    public String setToken(String token) {
        this.token = token;
        return token;
    }

    @SerializedName("shelfName")
    @Expose
    private String shelfName;

    public String getShelfName() {
        return shelfName;
    }

    public String setShelfName(String shelfName) {
        this.shelfName = shelfName;
        return shelfName;
    }

    @SerializedName("shelfItems")
    @Expose
    private List<Map<String, Object>> shelfItems = null;

    public List<Map<String, Object>> getShelfItems() {
        return shelfItems;
    }

    public List<Map<String, Object>> setShelfItems(List<Map<String, Object>> shelfItems) {
        this.shelfItems = shelfItems;
        return shelfItems;
    }

}



