package com.philosup.demo;

import java.util.Optional;

import com.dataaliance.icon.IconRpcManager;
import com.philosup.demo.data.TransactionData;
import com.philosup.demo.data.TransactionRepository;
import com.philosup.demo.data.TransactionData.TransactionKey;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/block")
public class BlockchainController {

    @Autowired
    private TransactionRepository txRepo;

    @GetMapping("/transactions")
    public String getAllTransactions() {
        return txRepo.findAll().toString();
        // StringBuffer buf = new StringBuffer();
        // for(var item : txRepo.findAll())
        //     buf.append(item.hashCode());
        // return buf.toString();
    }

    public Optional<TransactionData> getTxHashFromDB(int nid, String txHash) {

        var id = new TransactionKey(nid,txHash).toString();
        System.out.println(id);
        return txRepo.findById(id);
    }

    @GetMapping("/hash/{nid}/{txHash}")
    public String getTxHash(@PathVariable int nid, @PathVariable String txHash) throws Throwable {

        var tryData = getTxHashFromDB(nid, txHash);
        if (tryData.isEmpty()){
            var rpcManager = new IconRpcManager(nid);
            var transaction = rpcManager.getTransaction(txHash);
            var result = rpcManager.getTransactionResult(txHash);
    
            var trData = new TransactionData();
            trData.id.nid = rpcManager.getNID();
            trData.id.txHash = txHash;
            trData.transaction = transaction;
            trData.result = result;

            // return trData.toString();
            txRepo.insert(trData);
            // txRepo.delete(trData);

            return "empty";
        }


        return tryData.get().toString();
    }    
}
