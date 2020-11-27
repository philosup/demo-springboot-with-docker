package com.philosup.demo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import foundation.icon.icx.IconService;
import foundation.icon.icx.Request;
import foundation.icon.icx.data.Address;
import foundation.icon.icx.data.Block;
import foundation.icon.icx.data.Bytes;
import foundation.icon.icx.data.ConfirmedTransaction;
import foundation.icon.icx.data.TransactionResult;
import foundation.icon.icx.transport.http.HttpProvider;
import foundation.icon.icx.transport.jsonrpc.RpcItem;
import foundation.icon.icx.transport.jsonrpc.RpcObject;
import okhttp3.OkHttpClient;
// import okhttp3.logging.HttpLoggingInterceptor;

public class GetDeployedPMRSCData {
	// network info
	private static final Address PMRSCAddress = new Address("cx4dee19c1f8969849cd87afa830f756ba6ff25242");
	private static final String URL = "http://54.180.120.65:9080/api/v3/1";
	// private static final String URL = "http://54.180.120.107:9080/api/v3/1";
	// private static final String URL = "http://52.79.230.147:9080/api/v3/1";
	// private static final String URL = "http://13.125.231.129:9080/api/v3/1";

	// private static final String NID = "1";

	// network setting value
	// HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
	OkHttpClient httpClient = new OkHttpClient.Builder().build();
	public IconService iconService = new IconService(new HttpProvider(httpClient, URL));

	// charge merkle tree data set
	private final Set<String> cmtDataSet = new HashSet<>(
			Arrays.asList("did", "value", "time", "address", "signature", "txTimestamp"));

	// payment merkle tree data set
	private final Set<String> pmtDataSet = new HashSet<>(
			Arrays.asList("service", "value", "paymentMethod", "time", "startTime", "endTime", "useTime", "assetId",
					"zoneId", "address", "signature", "txTimestamp", "did", "nonce"));

	// settle mekrle tree data set
	private final Set<String> smtDataSet = new HashSet<>(
			Arrays.asList("service", "value", "time", "address", "signature", "txTimestamp"));

	public GetDeployedPMRSCData() {
		// logging.setLevel(HttpLoggingInterceptor.Level.BODY);
	}

	public Map<String, String> getDeployedChargeTXDataByTXHash(String txHash) throws IOException {
		// init charge TX data
		Map<String, String> chargeTXData = new HashMap<String, String>();
		for (String key : cmtDataSet) {
			chargeTXData.put(key, null);
		}

		ConfirmedTransaction txData = iconService.getTransaction(new Bytes(txHash)).execute();

		String did = txData.getData().asObject().getItem("params").asObject().getItem("_did").toString();
		String value = txData.getData().asObject().getItem("params").asObject().getItem("_value").toString();
		String time = txData.getData().asObject().getItem("params").asObject().getItem("_time").toString();
		String address = txData.getFrom().toString();
		String signature = txData.getSignature().toString();
		String txTimestamp = txData.getTimestamp().toString();

		chargeTXData.put("did", did);
		chargeTXData.put("value", value);
		chargeTXData.put("time", time);
		chargeTXData.put("address", address);
		chargeTXData.put("signature", signature);
		chargeTXData.put("txTimestamp", txTimestamp);

		return chargeTXData;
	}

	public Map<String, String> getDeployedPaymentTXDataByTXHash(String txHash) throws IOException {
		// init payment TX data
		Map<String, String> paymentTXData = new HashMap<String, String>();
		for (String key : pmtDataSet) {
			paymentTXData.put(key, null);
		}

		ConfirmedTransaction txData = iconService.getTransaction(new Bytes(txHash)).execute();

		String service = txData.getData().asObject().getItem("params").asObject().getItem("_service").toString();
		String value = txData.getData().asObject().getItem("params").asObject().getItem("_value").toString();
		String paymentMethod = txData.getData().asObject().getItem("params").asObject().getItem("_payment_method")
				.toString();
		String time = txData.getData().asObject().getItem("params").asObject().getItem("_time").toString();
		String startTime = txData.getData().asObject().getItem("params").asObject().getItem("_start_time").toString();
		String endTime = txData.getData().asObject().getItem("params").asObject().getItem("_end_time").toString();
		String useTime = txData.getData().asObject().getItem("params").asObject().getItem("_use_time").toString();
		String assetId = txData.getData().asObject().getItem("params").asObject().getItem("_asset_id").toString();
		String zoneId = txData.getData().asObject().getItem("params").asObject().getItem("_zone_id").toString();
		String address = txData.getFrom().toString();
		String signature = txData.getSignature().toString();
		String txTimestamp = txData.getTimestamp().toString();
		String did = txData.getData().asObject().getItem("params").asObject().getItem("_did").toString();

		paymentTXData.put("service", service);
		paymentTXData.put("value", value);
		paymentTXData.put("paymentMethod", paymentMethod);
		paymentTXData.put("time", time);
		paymentTXData.put("startTime", startTime);
		paymentTXData.put("endTime", endTime);
		paymentTXData.put("useTime", useTime);
		paymentTXData.put("assetId", assetId);
		paymentTXData.put("zoneId", zoneId);
		paymentTXData.put("address", address);
		paymentTXData.put("signature", signature);
		paymentTXData.put("txTimestamp", txTimestamp);
		paymentTXData.put("did", did);

		return paymentTXData;

	}

	public void getDeployedChargePaymentTXHashByBlockHeight(BigInteger blockHeight, BufferedWriter chargeBufWriter,
			BufferedWriter paymentBufWriter) throws IOException {
		Block block = iconService.getBlock(blockHeight).execute();

		List<ConfirmedTransaction> transactionList = block.getTransactions();

		for (ConfirmedTransaction transaction : transactionList) {
			Bytes txHash = transaction.getTxHash();
			TransactionResult transactionResult = iconService.getTransactionResult(txHash).execute();

			// filtering failed transaction
			if (transactionResult.getStatus().toString().equals("0")) {
				continue;
			}

			if (transactionResult.getTo().equals(PMRSCAddress) != false) {
				continue;
			}

			// get transaction data
			RpcItem transactionData = transaction.getData();
			RpcObject transactionDataObject = transactionData.asObject();

			if (transactionDataObject.keySet().contains("method") == false) {
				continue;
			}

			String method = transactionDataObject.getItem("method").toString();
			if (method.equals("charge")) {
				chargeBufWriter.write(txHash.toString());
				chargeBufWriter.newLine();
			} else if (method.equals("payment")) {
				paymentBufWriter.write(txHash.toString());
				paymentBufWriter.newLine();
			}

		}
	}

	public String getDeployedChargePaymentTXHashByBlockHeight(String blockHeight) throws IOException {
		// File deployedChargeTXHashFile = new File(
		// "D:\\Work\\deployedChargeTXHash.txt");
		// File deployedPaymentTXHashFile = new File(
		// "D:\\Work\\deployedPaymentTXHash.txt");

		// BufferedWriter chargeBufWriter = new BufferedWriter(new
		// FileWriter(deployedChargeTXHashFile, true));
		// BufferedWriter paymentBufWriter = new BufferedWriter(new
		// FileWriter(deployedPaymentTXHashFile, true));

		Block block = iconService.getBlock(new BigInteger(blockHeight)).execute();

		List<ConfirmedTransaction> transactionList = block.getTransactions();

		for (ConfirmedTransaction transaction : transactionList) {
			Bytes txHash = transaction.getTxHash();
			TransactionResult transactionResult = iconService.getTransactionResult(txHash).execute();

			// filtering failed transaction
			if (transactionResult.getStatus().toString().equals("0")) {
				continue;
			}

			if (transactionResult.getTo().equals(PMRSCAddress) == false) {
				continue;
			}

			// get transaction data
			RpcItem transactionData = transaction.getData();
			RpcObject transactionDataObject = transactionData.asObject();

			return transactionDataObject.toString();
			// if (transactionDataObject.keySet().contains("method") == false) {
			// continue;
			// }

			// String method = transactionDataObject.getItem("method").toString();
			// if (method.equals("charge")) {
			// chargeBufWriter.write(txHash.toString());
			// chargeBufWriter.newLine();
			// } else if (method.equals("payment")) {
			// paymentBufWriter.write(txHash.toString());
			// paymentBufWriter.newLine();
			// }

		}

		return "Not Found?";
		// chargeBufWriter.close();
		// paymentBufWriter.close();

	}

	public void storeDeployedPaymentTXHashByBlockHeight(String blockHeight) throws IOException {
		File inputPaymentDataFile = new File("inputdataset\\deployedpaymenttxdata");

		try (BufferedWriter bufWriter = new BufferedWriter(new FileWriter(inputPaymentDataFile, true))) {

			Block block = iconService.getBlock(new BigInteger(blockHeight)).execute();

			List<ConfirmedTransaction> transactionList = block.getTransactions();
			for (ConfirmedTransaction transaction : transactionList) {
				Bytes txHash = transaction.getTxHash();
				// filtering failure transactions
				TransactionResult transactionResult = iconService.getTransactionResult(txHash).execute();
				if (transactionResult.getStatus().toString().equals("0")) {
					continue;
				}
				if (transactionResult.getTo().equals("cx4dee19c1f8969849cd87afa830f756ba6ff25242") == false) {
					continue;
				}

				// get transaction data
				RpcItem transactionData = transaction.getData();
				RpcObject transactionDataObject = transactionData.asObject();

				if (transactionDataObject.keySet().contains("method") == false) {
					continue;
				}

				String method = transactionDataObject.getItem("method").toString();
				if (method.equals("payment")) {
					bufWriter.write(txHash.toString());
					bufWriter.newLine();
				}

			}
		}

	}

	public void getOriginPaymentTXByBlockHeight(String startRange, String endRange) throws IOException {
		Block block = iconService.getBlock(new BigInteger("27650930")).execute();
		List<ConfirmedTransaction> transactionList = block.getTransactions();
		for (ConfirmedTransaction transaction : transactionList) {
			Bytes txHash = transaction.getTxHash();

			// filtering failure transactions
			Request<TransactionResult> transactionResult = iconService.getTransactionResult(txHash);
			if (transactionResult.execute().getStatus().toString().equals("0")) {
				continue;
			}

			// get transaction data
			RpcItem transactionData = transaction.getData();

			RpcObject transactionDataObject = transactionData.asObject();

			String method = transactionDataObject.getItem("method").toString();
			System.out.println(method);
			RpcObject transactionDataParameterObject = transactionDataObject.getItem("params").asObject();

		}

	}

	public Map<String, String> getSettleTXDataByTXHash(String txHash) throws IOException {
		// init charge TX data
		Map<String, String> settleTXData = new HashMap<String, String>();
		for (String key : smtDataSet) {
			settleTXData.put(key, null);
		}

		ConfirmedTransaction txData = iconService.getTransaction(new Bytes(txHash)).execute();

		String service = txData.getData().asObject().getItem("params").asObject().getItem("_service").toString();
		String value = txData.getData().asObject().getItem("params").asObject().getItem("_value").toString();
		String time = txData.getData().asObject().getItem("params").asObject().getItem("_time").toString();
		String address = txData.getFrom().toString();
		String signature = txData.getSignature().toString();
		String txTimestamp = txData.getTimestamp().toString();

		settleTXData.put("service", service);
		settleTXData.put("value", value);
		settleTXData.put("time", time);
		settleTXData.put("address", address);
		settleTXData.put("signature", signature);
		settleTXData.put("txTimestamp", txTimestamp);

		return settleTXData;

	}

}
