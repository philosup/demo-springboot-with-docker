package com.philosup.demo;

import com.dataaliance.icon.IconRpcManager;
import com.philosup.demo.data.TransactionData;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/block")
public class BlockchainController {
    @GetMapping("/hash/{txHash}")
    public String getTxHash(@PathVariable String txHash) throws Throwable {

        var rpcManager = new IconRpcManager();
        var transaction = rpcManager.getTransaction(txHash);
        var result = rpcManager.getTransactionResult(txHash);

        var trData = new TransactionData();
        trData.nid = rpcManager.nid;
        trData.txHash = txHash;
        trData.transaction = transaction;
        trData.result = result;

        return trData.toString();
    }    
}
