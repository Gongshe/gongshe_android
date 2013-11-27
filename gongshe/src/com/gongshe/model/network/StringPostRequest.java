package com.gongshe.model.network;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;

public class StringPostRequest extends StringRequest {
    private Map<String, String> mBody;

    public StringPostRequest(String url, Map<String, String> body,
                             Response.Listener<String> listener,
                             Response.ErrorListener errorListener) {
        super(Method.POST, url, listener, errorListener);
        mBody = body;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mBody;
    }
}
