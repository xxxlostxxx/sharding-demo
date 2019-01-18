package com.lst.sharding.conf;

import com.google.common.collect.Maps;
import io.shardingsphere.api.config.ShardingRuleConfiguration;
import io.shardingsphere.api.config.TableRuleConfiguration;
import io.shardingsphere.core.rule.ShardingRule;
import io.shardingsphere.shardingjdbc.jdbc.core.datasource.ShardingDataSource;
import java.sql.SQLException;
import java.util.Properties;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author youzhu@dian.so
 * @version 1.0.0
 * @Date 2019-01-17
 * @Copyright 北京伊电园网络科技有限公司 2016-2018 © 版权所有 京ICP备17000101号
 */
@Configuration
@MapperScan(basePackages = {"com.lst.sharding.dao.sharding"},
        sqlSessionFactoryRef = "shardingSqlSessionFactory",
        sqlSessionTemplateRef = "shardingSqlSessionTemplate")
public class ShardingDataSourceConf extends  AbstractShardingDatasourceConfiguration{

    @Resource
    private TableRuleConfiguration userShardingRule;


    @Bean(name = "shardingDataSource")
    public DataSource dataSource() throws SQLException {

        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        shardingRuleConfig.getTableRuleConfigs().add(userShardingRule);
        ShardingRule shardingRule = new ShardingRule(shardingRuleConfig, getDataSourceMap().keySet());
        Properties props = new Properties();
        props.setProperty("sql.show", getShowsql());
        ShardingDataSource shardingDataSource = new ShardingDataSource(getDataSourceMap(), shardingRule, Maps.newHashMap(), props);
        return shardingDataSource;
    }



    @Bean(name = "shardingSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("shardingDataSource") DataSource dataSource) throws Exception{
        return DataSourceConf.createSqlSessionFactory(dataSource, "classpath:/mappers/sharding/*.xml");
    }

    @Bean(name = "shardingSqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("shardingDataSource") DataSource dataSource) throws Exception{
        SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory(dataSource));
        return sqlSessionTemplate;
    }
    @Override
    protected String getSchemePrefix() {
        return "user_";
    }

    @Override
    protected String getDatasourcePrefix() {
        return "sharding_";
    }
}
