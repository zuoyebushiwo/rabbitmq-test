package com.zuoye.binlog.mysql;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.*;
import com.github.shyiko.mysql.binlog.event.deserialization.EventDeserializer;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.*;

import static com.github.shyiko.mysql.binlog.event.EventType.*;

/**
 * 数据库监听器
 *
 * @author zrj
 * @since 2021/7/26
 **/
@Slf4j
public class MysqlBinLogListener implements BinaryLogClient.EventListener {

    private int consumerThreads = BinLogConstants.consumerThreads;

    private BinaryLogClient parseClient;

    private BlockingQueue<BinLogItem> queue;
    private final ExecutorService consumer;

    // 存放每张数据表对应的listener
    private Map<String, BinLogListener> listeners;

    private MyConf conf;
    private Map<String, Map<String, Colum>> dbTableCols;
    private String dbTable;

    /**
     * 监听器初始化
     *
     * @param myConf
     */
    public MysqlBinLogListener(MyConf myConf) {
        BinaryLogClient client = new BinaryLogClient(myConf.getHost(), myConf.getPort(), myConf.getUsername(), myConf.getPasswd());
        EventDeserializer eventDeserializer = new EventDeserializer();
        //eventDeserializer.setCompatibilityMode(//序列化
        //        EventDeserializer.CompatibilityMode.DATE_AND_TIME_AS_LONG,
        //        EventDeserializer.CompatibilityMode.CHAR_AND_BINARY_AS_BYTE_ARRAY
        //);
        client.setEventDeserializer(eventDeserializer);
        this.parseClient = client;
        this.queue = new ArrayBlockingQueue<>(1024);
        this.conf = myConf;
        this.listeners = Maps.newHashMap();
        this.dbTableCols = new ConcurrentHashMap<>();
        this.consumer = Executors.newFixedThreadPool(consumerThreads);
    }

    /**
     * 监听处理
     *
     * @param event
     */
    @Override
    public void onEvent(Event event) {
        EventType eventType = event.getHeader().getEventType();

        if (eventType == EventType.TABLE_MAP) {
            TableMapEventData tableData = event.getData();
            String db = tableData.getDatabase();
            String table = tableData.getTable();
            dbTable = BinLogUtils.getdbTable(db, table);
        }

        // 只处理添加删除更新三种操作
        if (isWrite(eventType) || isUpdate(eventType) || isDelete(eventType)) {
            if (isWrite(eventType)) {
                WriteRowsEventData data = event.getData();
                for (Serializable[] row : data.getRows()) {
                    if (dbTableCols.containsKey(dbTable)) {
                        BinLogItem item = BinLogItem.itemFromInsertOrDeleted(row, dbTableCols.get(dbTable), eventType);
                        item.setDbTable(dbTable);
                        queue.add(item);
                    }
                }
            }
            if (isUpdate(eventType)) {
                UpdateRowsEventData data = event.getData();
                for (Map.Entry<Serializable[], Serializable[]> row : data.getRows()) {
                    if (dbTableCols.containsKey(dbTable)) {
                        BinLogItem item = BinLogItem.itemFromUpdate(row, dbTableCols.get(dbTable), eventType);
                        item.setDbTable(dbTable);
                        queue.add(item);
                    }
                }

            }
            if (isDelete(eventType)) {
                DeleteRowsEventData data = event.getData();
                for (Serializable[] row : data.getRows()) {
                    if (dbTableCols.containsKey(dbTable)) {
                        BinLogItem item = BinLogItem.itemFromInsertOrDeleted(row, dbTableCols.get(dbTable), eventType);
                        item.setDbTable(dbTable);
                        queue.add(item);
                    }
                }
            }
        }
    }

    /**
     * 注册监听
     *
     * @param db       数据库
     * @param table    操作表
     * @param listener 监听器
     * @throws Exception
     */
    public void regListener(String db, String table, BinLogListener listener) throws Exception {
        String dbTable = BinLogUtils.getdbTable(db, table);
        // 获取字段集合
        Map<String, Colum> cols = BinLogUtils.getColMap(conf, db, table);
        // 保存字段信息
        dbTableCols.put(dbTable, cols);
        // 保存当前注册的listener
        listeners.put(dbTable, listener);
    }

    /**
     * 开启多线程消费
     *
     * @throws IOException
     */
    public void parse() throws IOException {
        parseClient.registerEventListener(this);

        for (int i = 0; i < consumerThreads; i++) {
            consumer.submit(() -> {
                while (true) {
                    if (queue.size() > 0) {
                        try {
                            BinLogItem item = queue.take();
                            String dbtable = item.getDbTable();
                            listeners.get(dbtable).onEvent(item);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Thread.sleep(BinLogConstants.queueSleep);
                }
            });
        }
        parseClient.connect();
    }

}
