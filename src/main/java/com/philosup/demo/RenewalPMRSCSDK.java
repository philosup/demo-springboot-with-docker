package com.philosup.demo;

import java.io.IOException;
import java.math.BigInteger;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

// import org.json.simple.JSONObject;
// import org.json.simple.parser.JSONParser;
// import org.json.simple.parser.ParseException;

import foundation.icon.icx.Call;
import foundation.icon.icx.IconService;
import foundation.icon.icx.KeyWallet;
import foundation.icon.icx.SignedTransaction;
import foundation.icon.icx.Transaction;
import foundation.icon.icx.TransactionBuilder;
import foundation.icon.icx.data.Address;
import foundation.icon.icx.data.Bytes;
import foundation.icon.icx.data.TransactionResult;
import foundation.icon.icx.transport.http.HttpProvider;
import foundation.icon.icx.transport.jsonrpc.RpcItem;
import foundation.icon.icx.transport.jsonrpc.RpcObject;
import foundation.icon.icx.transport.jsonrpc.RpcValue;
import okhttp3.OkHttpClient;
// import okhttp3.logging.HttpLoggingInterceptor;

public class RenewalPMRSCSDK {
	private static final Address pmrSCAddress = new Address("cxcb66a5cdb144120acc832656363a61fd0b0107e8");

	private static final String URL = "http://54.180.120.107:9080/api/v3/1";
	private static final String NID = "1";
	private static final String STEPLIMIT = "1000000";

	// HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
	// OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(logging).build();
	OkHttpClient httpClient = new OkHttpClient.Builder().build();
	IconService iconService = new IconService(new HttpProvider(httpClient, URL));
	// JsonParser jsonParser = new JsonParser();
	// JsonObject jsonObject = new JsonObject();
	// Object object = new Object();

	public RenewalPMRSCSDK() {
		// logging.setLevel(HttpLoggingInterceptor.Level.BODY);
	}

	// ========== PMR SmartContract TX method
	public String chargeTX(KeyWallet keywallet, String chargeMerkleRoot) throws IOException {
		RpcObject parameter = new RpcObject.Builder().put("_charge_merkle_root", new RpcValue(chargeMerkleRoot))
				.build();

		Transaction transaction = TransactionBuilder.newBuilder().nid(new BigInteger(NID)).from(keywallet.getAddress())
				.to(pmrSCAddress).stepLimit(new BigInteger(STEPLIMIT)).call("charge").params(parameter).build();

		SignedTransaction signedTransaction = new SignedTransaction(transaction, keywallet);
		String txHash = iconService.sendTransaction(signedTransaction).execute().toString();

		return txHash;

	}

	public String paymentTX(KeyWallet keywallet, String paymentMerkleRoot) throws IOException {
		RpcObject parameter = new RpcObject.Builder().put("_payment_merkle_root", new RpcValue(paymentMerkleRoot))
				.build();

		Transaction transaction = TransactionBuilder.newBuilder().nid(new BigInteger(NID)).from(keywallet.getAddress())
				.to(pmrSCAddress).stepLimit(new BigInteger(STEPLIMIT)).call("payment").params(parameter).build();

		SignedTransaction signedTransaction = new SignedTransaction(transaction, keywallet);
		String txHash = iconService.sendTransaction(signedTransaction).execute().toString();

		return txHash;

	}

	public String settleTX(KeyWallet keywallet, String settleMerkleRoot) throws IOException {
		RpcObject parameter = new RpcObject.Builder().put("_settle_merkle_root", new RpcValue(settleMerkleRoot))
				.build();

		Transaction transaction = TransactionBuilder.newBuilder().nid(new BigInteger(NID)).from(keywallet.getAddress())
				.to(pmrSCAddress).stepLimit(new BigInteger(STEPLIMIT)).call("settle").params(parameter).build();

		SignedTransaction signedTransaction = new SignedTransaction(transaction, keywallet);
		String txHash = iconService.sendTransaction(signedTransaction).execute().toString();

		return txHash;

	}

	// ========== PMR SmartContract Call method

	public String getChargeMerkleRootByTXHashCall(String txHash) throws IOException, JsonSyntaxException {
		RpcObject parameter = new RpcObject.Builder().put("_tx_hash", new RpcValue(txHash)).build();

		Call<RpcItem> call = new Call.Builder().to(pmrSCAddress).method("get_charge_merkle_root_by_tx_hash")
				.params(parameter).build();

		String callResult = iconService.call(call).execute().toString();

		var obj = JsonParser.parseString(callResult);// jsonParser.parse(callResult);
		var jsonObject = obj.getAsJsonObject();

		return jsonObject.get("charge_merkle_root").toString();

	}

	public String getPaymentMerkleRootByTXHashCall(String txHash) throws IOException, JsonSyntaxException {
		RpcObject parameter = new RpcObject.Builder().put("_tx_hash", new RpcValue(txHash)).build();

		Call<RpcItem> call = new Call.Builder().to(pmrSCAddress).method("get_payment_merkle_root_by_tx_hash")
				.params(parameter).build();

		String callResult = iconService.call(call).execute().toString();

		var obj = JsonParser.parseString(callResult);// jsonParser.parse(callResult);
		var jsonObject = obj.getAsJsonObject();

		return jsonObject.get("payment_merkle_root").toString();

	}

	public String getSettleMerkleRootByTXHashCall(String txHash) throws IOException, JsonSyntaxException {
		RpcObject parameter = new RpcObject.Builder().put("_tx_hash", new RpcValue(txHash)).build();

		Call<RpcItem> call = new Call.Builder().to(pmrSCAddress).method("get_settle_merkle_root_by_tx_hash")
				.params(parameter).build();

		String callResult = iconService.call(call).execute().toString();

		var obj = JsonParser.parseString(callResult);// jsonParser.parse(callResult);
		var jsonObject = obj.getAsJsonObject();

		return jsonObject.get("settle_merkle_root").toString();

	}

	public String getTXHashByChargeMerkleRoot(String chargeMerkleRoot) throws IOException, JsonSyntaxException {
		RpcObject parameter = new RpcObject.Builder().put("_charge_merkle_root", new RpcValue(chargeMerkleRoot))
				.build();

		Call<RpcItem> call = new Call.Builder().to(pmrSCAddress).method("get_tx_hash_by_charge_merkle_root")
				.params(parameter).build();

		String callResult = iconService.call(call).execute().toString();

		var obj = JsonParser.parseString(callResult);// jsonParser.parse(callResult);
		var jsonObject = obj.getAsJsonObject();

		return jsonObject.get("charge_tx_hash").toString();
	}

	public String getTXHashByPaymentMerkleRoot(String paymentMerkleRoot) throws IOException, JsonSyntaxException {
		RpcObject parameter = new RpcObject.Builder().put("_payment_merkle_root", new RpcValue(paymentMerkleRoot))
				.build();

		Call<RpcItem> call = new Call.Builder().to(pmrSCAddress).method("get_tx_hash_by_payment_merkle_root")
				.params(parameter).build();

		String callResult = iconService.call(call).execute().toString();

		var obj = JsonParser.parseString(callResult);// jsonParser.parse(callResult);
		System.out.println(callResult);
		var jsonObject = obj.getAsJsonObject();

		return jsonObject.get("payment_tx_hash").toString();
	}

	public String getTXHashBySettleMerkleRoot(String settleMerkleRoot) throws IOException, JsonSyntaxException {
		RpcObject parameter = new RpcObject.Builder().put("_settle_merkle_root", new RpcValue(settleMerkleRoot))
				.build();

		Call<RpcItem> call = new Call.Builder().to(pmrSCAddress).method("get_tx_hash_by_settle_merkle_root")
				.params(parameter).build();

		String callResult = iconService.call(call).execute().toString();

		var obj = JsonParser.parseString(callResult);// jsonParser.parse(callResult);
		var jsonObject = obj.getAsJsonObject();

		return jsonObject.get("settle_tx_hash").toString();
	}

	public String isMerkleRootExistedCall(String merkleRoot) throws IOException, JsonSyntaxException {
		RpcObject parameter = new RpcObject.Builder().put("_merkle_root", new RpcValue(merkleRoot)).build();

		Call<RpcItem> call = new Call.Builder().to(pmrSCAddress).method("is_merkle_root_existed").params(parameter)
				.build();

		String callResult = iconService.call(call).execute().toString();

		var obj = JsonParser.parseString(callResult);// jsonParser.parse(callResult);
		var jsonObject = obj.getAsJsonObject();

		return jsonObject.get("result").toString();

	}

	// ========== PMR SmartContract END ==========

	public TransactionResult getTXResult(String _txHash) {
		TransactionResult txResult = null;
		while (true) {
			try {
				txResult = iconService.getTransactionResult(new Bytes(_txHash)).execute();
			} catch (Exception e) {
//				System.out.println(e);
				if (e.toString().contains("Pending") || e.toString().contains("Invalid params txHash")
						|| e.toString().contains("Executing")) {
					continue;
				}
			}
			break;
		}
		return txResult;
	}

}
