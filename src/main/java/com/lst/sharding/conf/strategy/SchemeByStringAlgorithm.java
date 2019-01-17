package com.lst.sharding.conf.strategy;

import com.lst.sharding.conf.ShardingUtil;
import io.shardingsphere.api.algorithm.sharding.PreciseShardingValue;
import io.shardingsphere.api.algorithm.sharding.standard.PreciseShardingAlgorithm;
import java.util.Collection;
import org.springframework.stereotype.Component;

@Component
public class SchemeByStringAlgorithm implements PreciseShardingAlgorithm<Integer> {

    protected String sharding(Collection<String> availableTargetNames, Integer shardingValue){
        String schemeNameSuffix = ShardingUtil.shardingSchemeSuffix(shardingValue);
        for (String schemeName : availableTargetNames){
            if (schemeName.endsWith(schemeNameSuffix)){
                return schemeName;
            }
        }
//        throw new IllegalArgumentException(String.format("sharding failure. availableTargetNames=%s, shardingValue=%s", availableTargetNames, shardingValue));
        return null;
    }

    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<Integer> preciseShardingValue) {
        return sharding(collection, preciseShardingValue.getValue());
    }
}
