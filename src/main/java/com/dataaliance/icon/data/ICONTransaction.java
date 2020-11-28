package com.dataaliance.icon.data;

import com.google.gson.Gson;

public class ICONTransaction {

    public String blockHash;
    public String blockHeight;
    public Object data;
    public String dataType;
    public String from;
    public String nid;
    public String signature;
    public String stepLimit;
    public String timestamp;
    public String to;
    public String txHash;
    public String txIndex;
    public String version;

    @Override
	public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}