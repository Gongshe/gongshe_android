package com.gongshe.model.network;

public interface OnNetListener {
    public void OnResponse(String response);
    public void onError(String errorMessage);
}
