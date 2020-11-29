package com.philosup.demo.data;

import com.dataaliance.icon.data.ICONTransaction;
import com.dataaliance.icon.data.ICONTransactionResult;
import com.google.gson.Gson;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "transaction")
public class TransactionData {

    public static class TransactionKey{
        public int nid;
        public String txHash;
        public TransactionKey(int nid, String txHash){
            this.nid = nid;
            this.txHash = txHash;
        }
        // @Override
        // public String toString() {
        //     Gson gson = new Gson();
        //     return gson.toJson(this);
        // }        
    }

    @Id
    public TransactionKey id;

    public ICONTransaction transaction;
    public ICONTransactionResult result;

    public TransactionData(){
        id = new TransactionKey(0, "");
    }
    
    @Override
	public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
