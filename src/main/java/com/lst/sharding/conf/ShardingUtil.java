package com.lst.sharding.conf;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import org.apache.tomcat.util.security.MD5Encoder;

public class ShardingUtil {

    /**
     * 共256个物理表
     *
     * rds0 = scheme0 ~ scheme3
     * rds1 = scheme4 ~ scheme7
     * rds2 = scheme8 ~ scheme11
     * rds3 = scheme12 ~ scheme15
     *
     *
     * scheme0 = table0 ~ table15
     * scheme1 = table16 ~ table31
     * ...
     * scheme15 = table240 ~ table255
     *
     * */

    // = 物理表数量 - 1
    private static final int TABLE_MASK = 0xFF;

    // 后缀输出格式
    private static final String SUFFIX_FORMAT = "%01d";
    private static final String SUFFIX_FORMAT_TABLE = "%02d";

    /**
     * 字符串MD5的前2位，范围落在0x00 ~ 0xFF内
     * */
    private static int shardingForString(String shardingValue){
        String md5 = MD5Encoder.encode(shardingValue.getBytes());
        return Integer.valueOf(md5.substring(0, 2), 16);
    }

    /**
     * @param shardingValue 分表键键值
     * @return 分表结果后缀，格式由SUFFIX_FORMAT决定
     * */
    public static String shardingInstanceSuffix(String shardingValue){
        int result = shardingForString(shardingValue);
        int instanceSuffix = result >> 6;
        return String.format(SUFFIX_FORMAT, instanceSuffix);
    }

    /**
     * @param shardingValue 分表键键值
     * @return 分表结果后缀，格式由SUFFIX_FORMAT决定
     * */
    public static String shardingSchemeSuffix(String shardingValue){
        int result = shardingForString(shardingValue);
        int schemeSuffix = result >> 4;
        return String.format(SUFFIX_FORMAT, schemeSuffix);
    }

    /**
     * @param shardingValue 分表键键值
     * @return 分表结果后缀，格式由SUFFIX_FORMAT决定
     * */
    public static String shardingTableSuffix(String shardingValue){
        return String.format(SUFFIX_FORMAT, shardingForString(shardingValue));
    }

    /**
     * long和0xFF做AND使范围落在0x00 ~ 0xFF内
     * */
    private static long shardingForLong(long shardingValue){
        return shardingValue & TABLE_MASK;
    }

    /**
     * @param shardingValue 分表键键值
     * @return 分表结果后缀，格式由SUFFIX_FORMAT决定
     * */
    public static String shardingInstanceSuffix(long shardingValue){
        long instanceSuffix = shardingForLong(shardingValue) >> 6;
        return String.format(SUFFIX_FORMAT, instanceSuffix);
    }

    /**
     * @param shardingValue 分表键键值
     * @return 分表结果后缀，格式由SUFFIX_FORMAT决定
     * */
    public static String shardingSchemeSuffix(long shardingValue){
        long schemeSuffix = shardingForLong(shardingValue) %2;
        return String.format(SUFFIX_FORMAT, schemeSuffix);
    }

    /**
     * @param shardingValue 分表键键值
     * @return 分表结果后缀，格式由SUFFIX_FORMAT决定
     * */
    public static String shardingTableSuffix(long shardingValue){
        return String.format(SUFFIX_FORMAT_TABLE, shardingForLong(shardingValue%3));
    }

    public static String genActualDataNodes(String dataSourcePrefix, String table){
        List<String> actualDataNodes = Lists.newArrayList();
        for (int j = 0; j < 256; j++){
            actualDataNodes.add(dataSourcePrefix + shardingSchemeSuffix(j) + "." + table + "_" + shardingTableSuffix(j));
        }
        Iterator<String> actualDataNodesIt = actualDataNodes.iterator();
        StringBuilder stringBuilder = new StringBuilder();
        while (actualDataNodesIt.hasNext()){
            String actualDataNode = actualDataNodesIt.next();
            stringBuilder.append(actualDataNode);
            if (actualDataNodesIt.hasNext()){
                stringBuilder.append(",");
            }
        }
        return stringBuilder.toString();
    }

}
