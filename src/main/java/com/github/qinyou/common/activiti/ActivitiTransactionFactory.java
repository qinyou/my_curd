package com.github.qinyou.common.activiti;

import org.apache.ibatis.session.TransactionIsolationLevel;
import org.apache.ibatis.transaction.Transaction;
import org.apache.ibatis.transaction.TransactionFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Properties;

/**
 * ibatis 事务 和 jfinal 事务整合
 *
 * @author chuang
 */
public class ActivitiTransactionFactory implements TransactionFactory {
    @Override
    public void setProperties(Properties props) {
    }

    @Override
    public Transaction newTransaction(Connection conn) {
        return new ActivitiTransaction(conn);
    }

    @Override
    public Transaction newTransaction(DataSource ds, TransactionIsolationLevel level, boolean autoCommit) {
        return new ActivitiTransaction(ds, level, autoCommit);
    }
}
