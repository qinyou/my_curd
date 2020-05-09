package com.github.qinyou.system.model;

import com.github.qinyou.common.utils.StringUtils;
import com.github.qinyou.system.model.base.BaseSysNotice;
import com.jfinal.plugin.activerecord.Page;

/**
 * Generated model
 * DB: sys_notice  通知消息
 *
 * @author zhangchuang
 * @since 2019-02-21 13:10:49
 */
@SuppressWarnings("serial")
public class SysNotice extends BaseSysNotice<SysNotice> {
    public static final SysNotice dao = new SysNotice().dao();

    /**
     * 分页查询
     *
     * @param pageNumber
     * @param pageSize
     * @param where
     * @return
     */
    public Page<SysNotice> page(int pageNumber, int pageSize, String where) {
        String sqlSelect = " SELECT a.id,a.title,a.content,a.createTime, c.id as detailId,c.hasRead,b.logo ";
        String sqlExceptSelect = " FROM sys_notice a " +
                "  left join sys_notice_type b on a.typeCode= b.typeCode " +
                "  left join sys_notice_detail c on c.sysNoticeId = a.id " +
                "  left join sys_user d on c.receiver = d.id ";
        if (StringUtils.notEmpty(where)) {
            sqlExceptSelect += " where " + where;
        }

        sqlExceptSelect += " order by a.createTime desc ";
        return paginate(pageNumber, pageSize, sqlSelect, sqlExceptSelect);
    }

}
