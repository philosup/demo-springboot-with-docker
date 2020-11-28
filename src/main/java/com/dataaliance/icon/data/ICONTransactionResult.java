package com.dataaliance.icon.data;

import java.util.List;

import com.google.gson.Gson;

public class ICONTransactionResult {
	public String status;
    public String to;
	public String txHash;
	public String txIndex;
	public String blockHeight;
	public String blockHash;
	public String cumulativeStepUsed;
	public String stepUsed;
	public String stepPrice;
	public String scoreAddress;
	public List<EventLog> eventLogs;
	public String logsBloom;
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
