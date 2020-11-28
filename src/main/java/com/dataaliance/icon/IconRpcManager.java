package com.dataaliance.icon;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.dataaliance.icon.data.ICONTransaction;
import com.dataaliance.icon.data.ICONTransactionResult;
import com.googlecode.jsonrpc4j.JsonRpcHttpClient;

public class IconRpcManager {

    public String url = "http://54.180.120.65:9080/api/v3/";
    public int nid = 1;
    public JsonRpcHttpClient client = null;

    public IconRpcManager() throws MalformedURLException {
        initClient(nid);
    }

    public URL getNodeUrl(int nid) throws MalformedURLException {
        return new URL(url + Integer.toString(nid));
    }

    public void initClient(int nid) throws MalformedURLException
    {
        Map<String, String> headers = new HashMap();
        headers.put("nid", Integer.toHexString(nid));
        client = new JsonRpcHttpClient(getNodeUrl(nid), headers);
    }

    public ICONTransactionResult getTransactionResult(String txHash) throws Throwable {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("txHash", txHash);
        return client.invoke("icx_getTransactionResult", params, ICONTransactionResult.class);
    }

    public ICONTransaction getTransaction(String txHash) throws Throwable {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("txHash", txHash);
        
        return client.invoke("icx_getTransactionByHash", params, ICONTransaction.class);
    }    
}
