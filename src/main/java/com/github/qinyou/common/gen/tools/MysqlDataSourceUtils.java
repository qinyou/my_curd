package com.github.qinyou.common.gen.tools;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.druid.DruidPlugin;

import javax.sql.DataSource;

/**
 * mysql 获得数据源, client main 方法使用 下 使用
 *
 * @author zhangchuang
 */
public class MysqlDataSourceUtils {
    /**
     * 获得数据库数据源，用于代码生成器
     *
     * @return 数据源
     */
    public static DataSource getDataSource() {
        // 根据实际情况配置
        Prop configProp = PropKit.use("jdbc.properties");
        DruidPlugin dp = new DruidPlugin(configProp.get("jdbc.url"), configProp.get("jdbc.user"),
                configProp.get("jdbc.password"), configProp.get("jdbc.driver"));
        dp.start();
        return dp.getDataSource();
    }
}
