package com.lst.sharding.conf.strategy;

import com.lst.sharding.conf.ShardingUtil;
import io.shardingsphere.api.algorithm.sharding.PreciseShardingValue;
import io.shardingsphere.api.algorithm.sharding.standard.PreciseShardingAlgorithm;
import java.util.Collection;
import org.springframework.stereotype.Component;

@Component
public class TableByStringAlgorithm implements PreciseShardingAlgorithm<String> {

    private String sharding(Collection<String> availableTargetNames, String shardingValue){
        String tableNameSuffix = ShardingUtil.shardingTableSuffix(shardingValue);
        for (String tableName : availableTargetNames){
            if (tableName.endsWith(tableNameSuffix)){
                return tableName;
            }
        }
//        throw new IllegalArgumentException("分表路由错误. availableTargetNames=" + availableTargetNames.size());
        return null;
    }

    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<String> preciseShardingValue) {
        return sharding(collection, preciseShardingValue.getValue());
    }
}
