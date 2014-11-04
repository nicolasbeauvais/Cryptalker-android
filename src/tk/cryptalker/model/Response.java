package tk.cryptalker.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Response {

    protected static final String TAG = "Response";

    private String status;

    private ResponseCode code;

    public Response(){
    }

    public Response(String status, ResponseCode code) {
        super();
        this.status = status;
        this.code = code;
    }

    public static Response parseFromJSONObject(Response r, JSONObject json) throws JSONException{

        if (json == null){
            Log.e(TAG, "Unable to create Response from Json caused by json null");
            return null;
        }

        if (r == null){
            Log.e(TAG, "Unable to create Response from Json caused by Response null");
            return null;
        }

        r.setStatus(json.getString("status"));
        r.setCode(ResponseCode.valueOf(ResponseCode.class, json.getString("code")));

        return r;
    }

    public static Response parseFromJSONObject(JSONObject json) throws JSONException{

        if (json == null){
            Log.e(TAG, "Unable to create Response from Json caused by json null");
            return null;
        }

        Response r = new Response();

        r.setStatus(json.getString("status"));
        r.setCode(ResponseCode.valueOf(json.getString("code")));

        return r;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((code == null) ? 0 : code.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Response other = (Response) obj;
        if (code != other.code)
            return false;
        if (status == null) {
            if (other.status != null)
                return false;
        } else if (!status.equals(other.status))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Response [status=" + status + ", code=" + code + "]";
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ResponseCode getCode() {
        return code;
    }

    public void setCode(ResponseCode code) {
        this.code = code;
    }

}
