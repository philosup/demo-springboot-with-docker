package com.philosup.demo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.googlecode.jsonrpc4j.JsonRpcHttpClient;

import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import foundation.icon.icx.KeyWallet;
import foundation.icon.icx.data.Bytes;
import foundation.icon.icx.data.TransactionResult;

@RestController
@RequestMapping("/tracker")
public class TransactionFromTracker {

    @GetMapping
    public String getTx(int num) throws Exception {
        URL url = new URL(
                "http://15.164.184.105:8080/rest/v1/transactions?scaleNum=1&startNum=" + Integer.toString(num));

        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");

        int responseCode = con.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        JsonObject jsonObj = JsonParser.parseString(response.toString()).getAsJsonObject();

        return jsonObj.getAsJsonObject("data").getAsJsonArray("transactions").get(0).getAsJsonObject().get("txHash")
                .toString();
    }

    public ICONTransactionResult getTransactionResult(String txHash) throws Throwable {
        Map<String, String> headers = new HashMap();
        headers.put("nid", "0x1");
        JsonRpcHttpClient client = new JsonRpcHttpClient(new URL("http://54.180.120.65:9080/api/v3/1"), headers);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("txHash", txHash);
        return client.invoke("icx_getTransactionResult", params, ICONTransactionResult.class);
    }

    public Object getTransaction(String txHash) throws Throwable {
        Map<String, String> headers = new HashMap();
        headers.put("nid", "0x1");
        JsonRpcHttpClient client = new JsonRpcHttpClient(new URL("http://54.180.120.65:9080/api/v3/1"), headers);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("txHash", txHash);
        
        return client.invoke("icx_getTransactionByHash", params, Object.class);
    }

    @GetMapping("/hash")
    public String getTxHash(@RequestParam("hash") String txHash) throws Throwable {
        StringBuffer res = new StringBuffer();
        var gson = new Gson();
        res.append('[');
        res.append(gson.toJson(getTransaction(txHash))).append(',');
        res.append(getTransactionResult(txHash).toString());
        res.append(']');
        return res.toString();
    }

    @GetMapping("/all")
    public String getTxs() throws Exception {
        URL url = new URL("http://15.164.184.105:8080/rest/v1/transactions?scaleNum=60000");

        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");

        int responseCode = con.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        JsonObject jsonObj = JsonParser.parseString(response.toString()).getAsJsonObject();

        var jsonArray = jsonObj.getAsJsonObject("data").getAsJsonArray("transactions");

        StringBuffer res2 = new StringBuffer();
        for (int i = jsonArray.size() - 1; i >= 0; i--) {
            res2.append(jsonArray.get(i).getAsJsonObject().get("txHash").toString());
            res2.append("\n");
        }

        return res2.toString();
    }

    @GetMapping("/create")
    public String getFile() {
        File file = new File("transaction_hash.txt");
        String res = "OK";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(this.getTxs());
        } catch (IOException e) {
            e.printStackTrace();
            res = e.getLocalizedMessage();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            res = e.getLocalizedMessage();
        }
        return res;
    }

    @GetMapping("/payment")
    public String getPaymentMerkleRoot(@RequestParam("paymentMerkleRoot") String paymentMerkleRoot, @RequestParam("txHash") String txHash) throws JsonSyntaxException, IOException {
        var renewalPMRSCSDK = new RenewalPMRSCSDK();
        if (paymentMerkleRoot.isEmpty() == false){
            String existedPaymentTXHash = renewalPMRSCSDK.getTXHashByPaymentMerkleRoot(paymentMerkleRoot);
            return existedPaymentTXHash;
        }
        else if(txHash.isEmpty() == false){
            String existedPaymentMerkleRoot = renewalPMRSCSDK.getPaymentMerkleRootByTXHashCall(txHash);
            return existedPaymentMerkleRoot;
        }
        return "invalid params";
    }

    @GetMapping("/pmr_process")
    public String getPMRProcess() throws IOException, Exception {
        String res = "OK";

        // payment data transfer
        File filteredPaymentTXHashFile = new File("filteredPaymentTXHash.txt");
        File renewalPaymentDataFile = new File("renewalPaymentData.txt");
        FileReader paymentFileReader = new FileReader(filteredPaymentTXHashFile);
        BufferedReader paymentBufReader = new BufferedReader(paymentFileReader);
        BufferedWriter paymentBufWriter = new BufferedWriter(new FileWriter(renewalPaymentDataFile, true));

        String deployedPaymentTXHash = "";

        while ((deployedPaymentTXHash = paymentBufReader.readLine()) != null) {
            if (deployedPaymentTXHash.startsWith("0x") == false) {
                continue;
            }
            var paymentData = paymentDataTransferByTXHash(deployedPaymentTXHash);
            if (paymentData.isJsonNull()) {
                continue;
            }
            paymentData.addProperty("deployedPaymentTXHash", deployedPaymentTXHash);
            if (deployedPaymentTXHash.equals("0x448072b126e0ab54f61800857618e4472b15f9a77914c4c45f67ec294aafdd1a")) {
                paymentData.addProperty("useTime", "2678400000");

            } else if (deployedPaymentTXHash
                    .equals("0x75fe914a1c97b1211bf1049afb7fc8c0ead6957d1f5b00a5c2b7db86a35a9714")) {
                paymentData.addProperty("useTime", "2678400000");

            } else if (deployedPaymentTXHash
                    .equals("0x501cffa1d70aedd112c559e6324025ea8cb825c60f4a4eafb2fb1bd236f9f515")) {
                paymentData.addProperty("endTime", "1578933318000");
                paymentData.addProperty("useTime", "2678400000");

            }

            paymentBufWriter.write(paymentData.toString());
            paymentBufWriter.newLine();

            System.out.println(deployedPaymentTXHash);
        }
        paymentBufReader.close();
        paymentBufWriter.close();
        return res;
    }

    // payment merkle tree setting value
    public static final String[] paymentMainLeafList = { "service", "value", "paymentMethod" };
    public static final String[] paymentSubLeafList = { "time", "startTime", "endTime", "useTime", "assetId",
            "zoneId" };
    public static final String[] paymentAdditionalLeafList = { "address", "signature", "txTimestamp" };
    public static final String[] paymentPrivateLeafList = { "did", "nonce" };
        

    public static JsonObject paymentDataTransferByTXHash(String deployedPaymentTXHash) throws Exception {
        
        var getDeployedPMRSCData = new GetDeployedPMRSCData();
        // 1. get payment tx data by tx hash
        Map<String, String> paymentTXData = getDeployedPMRSCData.getDeployedPaymentTXDataByTXHash(deployedPaymentTXHash);

        // generate nonce
        byte[] bytes = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(bytes);
        String nonce = ByteUtils.toHexString(bytes);

        // set nonce
        paymentTXData.put("nonce", nonce);

        // 2. generate payment merkle tree
        PaymentMerkleTree pmt = new PaymentMerkleTree(paymentMainLeafList, paymentSubLeafList,
                paymentAdditionalLeafList, paymentPrivateLeafList);
        for (String leaf : paymentTXData.keySet()) {
            pmt.addLeaf(leaf, paymentTXData.get(leaf));
        }
        pmt.makeTree();
        String paymentMerkleRoot = pmt.getMerkleRoot();

        String paymentTXHash;
        var renewalPMRSCSDK = new RenewalPMRSCSDK();
        // 3. generate & send payment TX
        if (renewalPMRSCSDK.isMerkleRootExistedCall(paymentMerkleRoot).equals("1")) {
            String existedPaymentTXHash = renewalPMRSCSDK.getTXHashByPaymentMerkleRoot(paymentMerkleRoot);
            paymentTXHash = existedPaymentTXHash;

        } else {
            // paymentTXHash = renewalPMRSCSDK.paymentTX(operatorKeywallet, paymentMerkleRoot);
            // TransactionResult paymentTXResult = renewalPMRSCSDK.getTXResult(paymentTXHash);
            // if (paymentTXResult.getStatus().toString().equals("0")) {
            //     paymentTXHash = "";
            // }
        }

        // 4. store data
        var jsonObject = new JsonObject();
        jsonObject.addProperty("service", pmt.getLeaf("service"));
        jsonObject.addProperty("value", pmt.getLeaf("value"));
        jsonObject.addProperty("paymentMethod", pmt.getLeaf("paymentMethod"));
        jsonObject.addProperty("time", pmt.getLeaf("time"));
        jsonObject.addProperty("startTime", pmt.getLeaf("startTime"));
        jsonObject.addProperty("endTime", pmt.getLeaf("endTime"));
        jsonObject.addProperty("useTime", pmt.getLeaf("useTime"));
        jsonObject.addProperty("assetId", pmt.getLeaf("assetId"));
        jsonObject.addProperty("zoneId", pmt.getLeaf("zoneId"));
        jsonObject.addProperty("address", pmt.getLeaf("address"));
        jsonObject.addProperty("signature", pmt.getLeaf("signature"));
        jsonObject.addProperty("txTimestamp", pmt.getLeaf("txTimestamp"));
        jsonObject.addProperty("did", pmt.getLeaf("did"));
        jsonObject.addProperty("nonce", pmt.getLeaf("nonce"));
        jsonObject.addProperty("paymentMerkleRoot", paymentMerkleRoot);
        jsonObject.addProperty("paymentTXHash", paymentTXHash);

        return jsonObject;

    }
}
