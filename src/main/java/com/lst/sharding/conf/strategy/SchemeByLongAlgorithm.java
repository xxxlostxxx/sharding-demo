package com.lst.sharding.conf.strategy;

import com.lst.sharding.conf.ShardingUtil;
import io.shardingsphere.api.algorithm.sharding.PreciseShardingValue;
import io.shardingsphere.api.algorithm.sharding.standard.PreciseShardingAlgorithm;
import java.util.Collection;
import org.springframework.stereotype.Component;

@Component
public class SchemeByLongAlgorithm implements PreciseShardingAlgorithm<Long> {

    protected String sharding(Collection<String> availableTargetNames, Long shardingValue){
        String schemeNameSuffix = ShardingUtil.shardingSchemeSuffix(shardingValue);
        for (String schemeName : availableTargetNames){
            if (schemeName.endsWith(schemeNameSuffix)){
                return schemeName;
            }
        }
        return null;
    }

    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<Long> preciseShardingValue) {
        if (preciseShardingValue.getValue() instanceof Long){
            long value = preciseShardingValue.getValue();
            return sharding(collection, value);
        }else if ((Object)preciseShardingValue.getValue() instanceof Integer){
            long value = ((Number)preciseShardingValue.getValue()).longValue();
            return sharding(collection, value);
        }else {
            throw new IllegalArgumentException(String.format("sharding failure. availableTargetNames=%s, preciseShardingValue=%s", collection, preciseShardingValue));
        }
    }
}
