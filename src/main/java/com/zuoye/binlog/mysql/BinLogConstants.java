package com.zuoye.binlog.mysql;

import lombok.Data;

/**
 * 监听配置信息
 *
 * @author zrj
 * @since 2021/7/27
 **/
@Data
public class BinLogConstants {

    private String host = "localhost";

    private int port = 3306;

    private String username = "root";

    private String passwd = "12345678";

    private String db = "bigdata-priv";
    private String table = "bigdata_priv_casbin_rule";

    public static final int consumerThreads = 5;

    public static final long queueSleep = 1000;

}