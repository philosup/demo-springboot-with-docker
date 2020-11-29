package com.philosup.demo.data;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TransactionRepository extends MongoRepository<TransactionData, String> {
    // @Override
    // default Optional<TransactionData> findById(String id) {
    //     // TODO Auto-generated method stub
    //     return null;
    // }

    
    // default Optional<TransactionData> findByNidTxHash(int nid, String txHash){
    //     return findById(new TransactionKey(nid, txHash).toString())
    // }
}