package com.dataaliance.icon;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.dataaliance.icon.data.ICONTransaction;
import com.dataaliance.icon.data.ICONTransactionResult;
import com.googlecode.jsonrpc4j.JsonRpcHttpClient;

public class IconRpcManager {

    private String url = "http://54.180.120.65:9080/api/v3/";
    private JsonRpcHttpClient client = null;

    private int nid = 1;

    public int getNID(){
        return nid;
    }

    public IconRpcManager(int nid) throws MalformedURLException {
        this.nid = nid;
        initClient();
    }

    public URL getNodeUrl(int nid) throws MalformedURLException {
        return new URL(url + Integer.toString(nid));
    }

    protected void initClient() throws MalformedURLException
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
