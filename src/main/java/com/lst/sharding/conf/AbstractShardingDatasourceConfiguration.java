package com.lst.sharding.conf;

import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

public abstract class AbstractShardingDatasourceConfiguration implements EnvironmentAware {

    private Environment environment;

    @Value("spring.datasource.sharding.")
    private String prefix;
    @Value("${sharding.showsql}")
    private String showsql;
    @Value("${spring.datasource.sharding.db0.url}")
    private String db0Url;
    @Value("${spring.datasource.sharding.db1.url}")
    private String db1Url;


    private Map<String, DataSource> dataSourceMap = Maps.newHashMap();

    @PostConstruct
    public void init(){
        Map<String, String> urlMap = new HashMap<String, String>(){{
            put("0", db0Url);
            put("1", db1Url);

        }};

        for (int i = 0; i < 6; i++){
            String dbSuffix = String.valueOf(i%2);
            String schemeSourceSuffix = String.format("%01d",i%2);
            DataSource shardingDataSource = DataSourceConf.getShardingDataSource(urlMap.get(dbSuffix), getSchemePrefix() + schemeSourceSuffix, new RelaxedPropertyResolver(environment, prefix));
            dataSourceMap.put(getDatasourcePrefix() + schemeSourceSuffix, shardingDataSource);
        }
    }

    protected abstract String getSchemePrefix();

    protected abstract String getDatasourcePrefix();

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public String getShowsql() {
        return showsql;
    }

    public Map<String, DataSource> getDataSourceMap() {
        return dataSourceMap;
    }
}
