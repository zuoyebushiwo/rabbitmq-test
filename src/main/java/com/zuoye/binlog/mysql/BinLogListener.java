package com.zuoye.binlog.mysql;

/**
 * BinLogListener监听器
 *
 * @author zrj
 * @since 2021/7/26
 **/
@FunctionalInterface
public interface BinLogListener {

    void onEvent(BinLogItem item);
}

