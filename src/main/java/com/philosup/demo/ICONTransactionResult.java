package com.philosup.demo;

import java.math.BigInteger;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import org.bson.json.JsonReader;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "transaction")
public class ICONTransactionResult {
	public String status;
    public String to;
    @Id
	public String txHash;
	public String txIndex;
	public String blockHeight;
	public String blockHash;
	public String cumulativeStepUsed;
	public String stepUsed;
	public String stepPrice;
	public String scoreAddress;
	public String logsBloom;
	public List<EventLog> eventLogs;
	public Failure failure;

	@Override
	public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
    
    public static class EventLog {
        public String scoreAddress;
        public String[] indexed;
        public Object[] data;
        
        @Override
        public String toString() {
            Gson gson = new Gson();
            return gson.toJson(this);
        }
    }    

    public static class Failure {
        public String code;
        public String message;

        @Override
        public String toString() {
            Gson gson = new Gson();
            return gson.toJson(this);
        }
    }    
}
