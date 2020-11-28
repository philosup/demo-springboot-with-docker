package com.philosup.demo.data;

import com.dataaliance.icon.data.ICONTransaction;
import com.dataaliance.icon.data.ICONTransactionResult;
import com.google.gson.Gson;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

// @Document(collection = "transaction")
public class TransactionData {
    @Id
    public int nid;
    @Id
    public String txHash;

    public ICONTransaction transaction;
    public ICONTransactionResult result;
    
    @Override
	public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
