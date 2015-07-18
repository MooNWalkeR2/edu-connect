package in.edconnect.JsonRequest;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class NewCustomJsonRequest extends StringRequest
{
    private final Map<String, String> _params;
    String newurl;
    public NewCustomJsonRequest(int method, String url, Map<String, String> params, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method,url,listener,errorListener);
        this._params = params;
        newurl=url;
        System.out.println("new custom request called");

    }
    @Override
    protected Map<String, String> getParams() {
        return _params;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<String, String>();

        //VolleyApplication.getInstance().addSessionCookie(headers);

        return headers;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {

        return super.parseNetworkResponse(response);
    }

}
