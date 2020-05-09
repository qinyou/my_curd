package com.github.qinyou.oa.activiti;

import com.jfinal.plugin.activerecord.DbKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.TransactionIsolationLevel;
import org.apache.ibatis.transaction.Transaction;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * ibatis 事务 使用 jfinal 数据源
 * @author chuang
 */
@Slf4j
public class ActivitiTransaction implements Transaction {

    private Connection connection;
    private DataSource dataSource;
    private TransactionIsolationLevel level;
    private boolean autoCommit;

    ActivitiTransaction(DataSource ds, TransactionIsolationLevel desiredLevel, boolean desiredAutoCommit) {
        dataSource = ds;
        level = desiredLevel;
        autoCommit = desiredAutoCommit;
    }

    ActivitiTransaction(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (connection == null) {
            log.debug("activiti open connection");
            openConnection();
        }
        return connection;
    }

    @Override
    public void commit() throws SQLException {
    }

    @Override
    public void rollback() throws SQLException {
    }

    @Override
    public void close() throws SQLException {
        if (connection != null) {
            log.debug("activiti close connection");
            DbKit.getConfig(ActivitiConfig.DATASOURCE_NAME).close(connection);
        }
    }

     void openConnection() throws SQLException {
        connection = DbKit.getConfig(ActivitiConfig.DATASOURCE_NAME).getConnection();
        if (level != null) {
            connection.setTransactionIsolation(level.getLevel());
        }
    }
}
