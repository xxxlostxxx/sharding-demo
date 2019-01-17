package com.lst.sharding.conf;


import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.StringUtils;

/**
 * @author youzhu@dian.so
 * @version 1.0.0
 * @Date 2019-01-11
 * @Copyright 北京伊电园网络科技有限公司 2016-2018 © 版权所有 京ICP备17000101号
 */
public class DataSourceConf {

    public static DataSource getDataSource(String url, RelaxedPropertyResolver propertyResolver) {
        DruidDataSource dataSource = new DruidDataSource();
        if (Objects.nonNull(propertyResolver)) {
            //基本属性 url、user、password
            dataSource.setDriverClassName(propertyResolver.getProperty("driver-class-name"));
            dataSource.setUrl(url);
            dataSource.setName(propertyResolver.getProperty("name"));
            dataSource.setUsername(propertyResolver.getProperty("username"));
            dataSource.setPassword(propertyResolver.getProperty("password"));
            //配置初始化大小、最小、最大
            dataSource.setMaxActive(Integer.valueOf(propertyResolver.getProperty("max-active")));
            dataSource.setInitialSize(Integer.valueOf(propertyResolver.getProperty("initial-size")));
            dataSource.setMinIdle(Integer.valueOf(propertyResolver.getProperty("min-idle")));
            String validationQueryTimeout = propertyResolver.getProperty("validation-query-timeout");
            if(!StringUtils.isEmpty(validationQueryTimeout)){
                dataSource.setValidationQueryTimeout(Integer.valueOf(validationQueryTimeout));
            }
        }
        //配置获取连接等待超时的时间
        dataSource.setMaxWait(10000L);
        //配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
        dataSource.setTimeBetweenEvictionRunsMillis(60000L);
        //配置一个连接在池中最小生存的时间，单位是毫秒
        dataSource.setMinEvictableIdleTimeMillis(300000L);

        dataSource.setValidationQuery("SELECT 'x'");
        dataSource.setTestWhileIdle(true);
        dataSource.setTestOnBorrow(false);
        dataSource.setTestOnReturn(false);
        dataSource.setUseUnfairLock(true);
        dataSource.setProxyFilters(proxyFilters());
        return dataSource;
    }
    public static DataSource getShardingDataSource(String dbUrl, String scheme, RelaxedPropertyResolver propertyResolver){
        String url = "jdbc:mysql://%s?useUnicode=true&amp;autoReconnect=true&amp;rewriteBatchedStatements=true&amp;socketTimeout=45000&amp;connectTimeout=3000";
        return getDataSource(String.format(url, dbUrl), propertyResolver);
    }

    public static SqlSessionFactory createSqlSessionFactory(DataSource dataSource, String xmlPath) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources(xmlPath));
      /*  sqlSessionFactoryBean.setConfigLocation(new DefaultResourceLoader().getResource(DataSourceConstants.MYBATIS_CONFIG_PATH));
        sqlSessionFactoryBean.setTypeAliasesPackage(DataSourceConstants.TYPE_ALIASES_PACKAGE);
        sqlSessionFactoryBean.setTypeHandlersPackage(DataSourceConstants.TYPE_HANDLERS_PACKAGE);*/
        return sqlSessionFactoryBean.getObject();
    }



    private static List<Filter> proxyFilters() {
        List<Filter> filters = new ArrayList<>();
        StatFilter filter = new StatFilter();
        filter.setSlowSqlMillis(3000L);
        filter.setLogSlowSql(true);
        filter.setMergeSql(true);
        filters.add(filter);
        return filters;
    }

}
