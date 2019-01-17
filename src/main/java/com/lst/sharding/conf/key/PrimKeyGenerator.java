package com.lst.sharding.conf.key;

import io.shardingsphere.core.keygen.KeyGenerator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class PrimKeyGenerator implements KeyGenerator {

    private static Map<String,AtomicInteger> key =new ConcurrentHashMap();

    private  String tableName;


    // 需要用redis 等

    public void setTableName(String tableName){

        this.tableName = tableName;
        key.put(tableName, new AtomicInteger(1));

    }

    @Override
    public  Number generateKey() {
        return key.get(tableName).getAndIncrement();
    }
    public static PrimKeyGenerator build(String name) {
        PrimKeyGenerator primKeyGenerator = new PrimKeyGenerator();
        primKeyGenerator.setTableName(name);
        return primKeyGenerator;
    }

}
