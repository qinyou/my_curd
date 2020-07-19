package com.github.qinyou.process.model.base;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

import java.util.Date;

/**
 * Generated baseModel
 * DB table: act_re_procdef_cate  流程定义分类
 * @author chuang
 * @since 2020-07-09 16:14:29
 */
@SuppressWarnings({"serial", "unchecked", "Duplicates"})
public abstract class BaseActReProcdefCate<M extends BaseActReProcdefCate<M>> extends Model<M> implements IBean {




     // 主键
     public String getId() {
        return getStr("id");
     }

     public M setId(String id) {
        set("id", id);
        return (M)this;
     }


     // 名称
     public String getName() {
        return getStr("name");
     }

     public M setName(String name) {
        set("name", name);
        return (M)this;
     }


     // 编码
     public String getCode() {
        return getStr("code");
     }

     public M setCode(String code) {
        set("code", code);
        return (M)this;
     }


     // 流程定义KEY
     public String getDefKey() {
        return getStr("defKey");
     }

     public M setDefKey(String defKey) {
        set("defKey", defKey);
        return (M)this;
     }


     // 父ID
     public String getPid() {
        return getStr("pid");
     }

     public M setPid(String pid) {
        set("pid", pid);
        return (M)this;
     }


     // 备注信息
     public String getRemark() {
        return getStr("remark");
     }

     public M setRemark(String remark) {
        set("remark", remark);
        return (M)this;
     }



    // 排序号
    public Integer getSortNum() {
        return getInt("sortNum");
    }

    public M setSortNum(Integer sortNum) {
        set("sortNum", sortNum);
        return (M) this;
    }

    //状态
    public String getState(){
        return getStr("state");
    }
    public M setState(String state){
        set("state", state);
        return (M) this;
    }

    // 流程图标
    public String getIcon() {
        return getStr("icon");
    }

    public M setIcon(String icon) {
        set("icon", icon);
        return (M) this;
    }



    // 创建人
    public String getCreater() {
        return getStr("creater");
    }

    public M setCreater(String creater) {
        set("creater", creater);
        return (M) this;
    }


    // 创建时间
    public Date getCreateTime() {
        return get("createTime");
    }

    public M setCreateTime(Date createTime) {
        set("createTime", createTime);
        return (M) this;
    }


    // 最后修改人
    public String getUpdater() {
        return getStr("updater");
    }

    public M setUpdater(String updater) {
        set("updater", updater);
        return (M) this;
    }


    // 最后修改时间
    public Date getUpdateTime() {
        return get("updateTime");
    }

    public M setUpdateTime(Date updateTime) {
        set("updateTime", updateTime);
        return (M) this;
    }
}
