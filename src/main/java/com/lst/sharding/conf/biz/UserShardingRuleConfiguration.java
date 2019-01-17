package com.lst.sharding.conf.biz;

import com.lst.sharding.conf.DataSourceConstants;
import com.lst.sharding.conf.ShardingUtil;
import com.lst.sharding.conf.key.PrimKeyGenerator;
import com.lst.sharding.conf.strategy.SchemeByLongAlgorithm;
import com.lst.sharding.conf.strategy.SchemeByStringAlgorithm;
import com.lst.sharding.conf.strategy.TableByLongAlgorithm;
import com.lst.sharding.conf.strategy.TableByStringAlgorithm;
import io.shardingsphere.api.config.TableRuleConfiguration;
import io.shardingsphere.api.config.strategy.ShardingStrategyConfiguration;
import io.shardingsphere.api.config.strategy.StandardShardingStrategyConfiguration;
import javax.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author youzhu@dian.so
 * @version 1.0.0
 * @Date 2019-01-17
 * @Copyright 北京伊电园网络科技有限公司 2016-2018 © 版权所有 京ICP备17000101号
 */
@Configuration
public class UserShardingRuleConfiguration {

    private static final String TABLE_NAME = "user";
    private static final String SHARDING_COLUMN = "type";
    private static final String PRIMARY_KEY = "id";

    @Resource
    private SchemeByLongAlgorithm schemeByLongAlgorithm;
    @Resource
    private TableByLongAlgorithm tableByLongAlgorithm;


    @Bean
    public TableRuleConfiguration userShardingRule(){

        TableRuleConfiguration userTableRule = new TableRuleConfiguration();
        userTableRule.setLogicTable(TABLE_NAME);
        userTableRule.setActualDataNodes("sharding_${0..1}.user_0${0..2}");

        ShardingStrategyConfiguration dbStrategy = new StandardShardingStrategyConfiguration(SHARDING_COLUMN, schemeByLongAlgorithm);
        ShardingStrategyConfiguration tableStrategy = new StandardShardingStrategyConfiguration(SHARDING_COLUMN, tableByLongAlgorithm);

        userTableRule.setDatabaseShardingStrategyConfig(dbStrategy);
        userTableRule.setTableShardingStrategyConfig(tableStrategy);

        userTableRule.setKeyGeneratorColumnName(PRIMARY_KEY);
        userTableRule.setKeyGenerator(PrimKeyGenerator.build(TABLE_NAME));

        return userTableRule;
    }

}
