package com.example.elasticsearch.configurer;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry.*;
import com.alibaba.otter.canal.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * Canal
 *
 * @author knox
 * @date 2020/12/13
 * @since 1.0.0
 */
@Component
public class SimpleCanalClient implements CommandLineRunner {
    public static final Logger LOGGER = LoggerFactory.getLogger(SimpleCanalClient.class);


    public static final String IP = "127.0.0.1";
    public static final String POST = "11111";
    public static final String DESTINATION = "example";
    public static final String SUBSCRIBE = ".*\\..*"; // 订阅所有表
    public static final String USERNAME = "";
    public static final String PASSWORD = "";

    @Override
    public void run(String... args) {
        // 创建单链接的客户端链接
        CanalConnector connector = CanalConnectors.newSingleConnector(
                new InetSocketAddress(IP, 11111),
                DESTINATION, USERNAME, PASSWORD);

        int batchSize = 1000;
        int emptyCount = 0;
        try {
            // 连接
            connector.connect();
            // 订阅
            connector.subscribe(SUBSCRIBE);
            // 回滚到未进行 {@link #ack} 的地方，下次fetch的时候，可以从最后一个没有 {@link #ack} 的地方开始拿
            connector.rollback();
            // 次数
            int totalEmptyCount = 10;
            while (emptyCount < totalEmptyCount) {
                // 获取指定数量的数据
                Message message = connector.getWithoutAck(batchSize);
                long batchId = message.getId();
                int size = message.getEntries().size();
                if (batchId == -1 || size == 0) {
                    emptyCount++;
                    LOGGER.info("empty count : " + emptyCount);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ignored) {
                    }
                } else {
                    emptyCount = 0;
                    // 实体类
                    printEntry(message.getEntries());
                }

                // 提交确认
                connector.ack(batchId);
                // connector.rollback(batchId); // 处理失败, 回滚数据
            }
            LOGGER.warn("empty too many times, exit");
        } finally {
            connector.disconnect();
        }
    }

    private static void printEntry(List<Entry> entries) {
        for (Entry entry : entries) {
            if (entry.getEntryType() == EntryType.TRANSACTIONBEGIN || entry.getEntryType() == EntryType.TRANSACTIONEND) {
                continue;
            }

            RowChange rowChange;
            try {
                rowChange = RowChange.parseFrom(entry.getStoreValue());
            } catch (Exception e) {
                throw new RuntimeException("ERROR ## parser of eromanga-event has an error , data:" + entry.toString(),
                        e);
            }

            EventType eventType = rowChange.getEventType();
            LOGGER.info("");
            LOGGER.info(String.format("================&gt; binlog[%s:%s] , name[%s,%s] , eventType : %s",
                    entry.getHeader().getLogfileName(), entry.getHeader().getLogfileOffset(),
                    entry.getHeader().getSchemaName(), entry.getHeader().getTableName(),
                    eventType));

            for (RowData rowData : rowChange.getRowDatasList()) {
                if (eventType == EventType.DELETE) {
                    printColumn(rowData.getBeforeColumnsList());
                } else if (eventType == EventType.INSERT) {
                    printColumn(rowData.getAfterColumnsList());
                } else {
                    LOGGER.info("-------&gt; before");
                    printColumn(rowData.getBeforeColumnsList());
                    LOGGER.info("-------&gt; after");
                    printColumn(rowData.getAfterColumnsList());
                }
            }
        }
    }

    private static void printColumn(List<Column> columns) {
        for (Column column : columns) {
            LOGGER.info(column.getName() + " : " + column.getValue() + "    update=" + column.getUpdated());
        }
    }

}
