package com.github.qinyou.process.model;

import com.github.qinyou.process.model.base.BaseActReProcdefCate;

import java.util.List;

/**
 * Generated model
 * DB: act_re_procdef_cate  流程定义分类
 * @author chuang
 * @since 2020-07-09 16:14:29
 */
@SuppressWarnings({"serial", "Duplicates"})
public class ActReProcdefCate extends BaseActReProcdefCate<ActReProcdefCate> {
    public static final ActReProcdefCate dao = new ActReProcdefCate().dao();
    // 查询全部，根据排序号排序
    public List<ActReProcdefCate> findAllSort() {
        String sql = "select * from act_re_procdef_cate order by sortNum,id";
        return find(sql);
    }
}
