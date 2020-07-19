package com.github.qinyou.system.service;

import com.alibaba.fastjson.JSON;
import com.github.qinyou.common.utils.Id.IdUtils;
import com.github.qinyou.common.utils.freemarker.FreemarkerUtils;
import com.github.qinyou.common.ws.SendMsgUtils;
import com.github.qinyou.system.model.*;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;

import java.util.*;

/**
 * 消息通知工具
 *
 * @author chuang
 */
@SuppressWarnings("Duplicates")
@Slf4j
public class SysNoticeService {

    /**
     * 发送系统通知, 使用消息类型关联 角色 相关人
     *
     * @param noticeCode     系统通知类型编码
     * @param templateParams 系统通知模板参数 (模板基于freemarker)
     * @return
     */
    @Before(Tx.class)
    public void sendNotice(String noticeCode, Map<String, Object> templateParams) {
        // 查询通知模板
        SysNoticeType sysNoticeType = SysNoticeType.dao.findByCode(noticeCode);
        if (sysNoticeType == null) {
            log.error("{}, 未找到消息通知类型", noticeCode);
            return;
        }

        // 查询通知接收人
        Set<String> receivers = getReceivers(sysNoticeType.getId());
        if (receivers.size() == 0) {
            log.info(" noticeCode:{} 类型 找不到关联的用户", noticeCode);
            return;
        }

        String msgTitle = sysNoticeType.getTypeName();
        String msgContent = FreemarkerUtils.renderAsText(sysNoticeType.getTpl().replaceAll("FTL", "\\$"), templateParams);
        saveData(sysNoticeType, msgTitle, msgContent, receivers);

        // 消息推送
        Map<String, Object> msg = new HashMap<>();
        msg.put("cate", sysNoticeType.getCate());
        msg.put("code", sysNoticeType.getTypeCode());
        msg.put("logo", sysNoticeType.getLogo());
        msg.put("title", msgTitle);
        msg.put("content", msgContent);
        SendMsgUtils.sendToUsers(receivers, JSON.toJSONString(msg));
    }

    // 发送消息
    @Before(Tx.class)
    public  void sendNotice(String username, String noticeCode, Map<String, Object> templateParams) {
        SysUser sysUser = SysUser.dao.findByUsername(username);
        if(sysUser==null){
            return;
        }
        Set<String> receivers = new HashSet<>();
        receivers.add(sysUser.getId());

        // 查询通知模板
        SysNoticeType sysNoticeType = SysNoticeType.dao.findByCode(noticeCode);
        if (sysNoticeType == null) {
            log.error("{}, 未找到消息通知类型", noticeCode);
            return ;
        }
        String msgTitle = sysNoticeType.getTypeName();
        String msgContent = FreemarkerUtils.renderAsText(sysNoticeType.getTpl().replaceAll("FTL", "\\$"), templateParams);
        // 存库
        saveData(sysNoticeType, msgTitle, msgContent, receivers);
        // 服务器消息推送
        Map<String, Object> msg = new HashMap<>();
        msg.put("cate", sysNoticeType.getCate());
        msg.put("code", sysNoticeType.getTypeCode());
        msg.put("logo", sysNoticeType.getLogo());
        msg.put("title", msgTitle);
        msg.put("content", msgContent);
        SendMsgUtils.sendToUsers(receivers, JSON.toJSONString(msg));
    }

    // 通过 通知类型id 查询 关联的 用户id 集合
    private Set<String> getReceivers(String typeId) {
        Set<String> userIdSet = new HashSet<>();
        List<Record> userIds = SysNoticeTypeSysRole.dao.findUserIdsByNoticeType(typeId);        // 通知类型 关联的角色，角色关联的用户
        userIds.forEach(record -> userIdSet.add(record.get("sysUserId")));
        return userIdSet;
    }


    /**
     * 通知数据 存库
     * @param sysNoticeType 通知类型对象
     * @param msgTitle      通知标题
     * @param msgConent     通知内容
     * @param receivers     接收人集合，不可为null，不可为空集
     */
    @Before(Tx.class)
    private void saveData(SysNoticeType sysNoticeType, String msgTitle, String msgConent, Set<String> receivers) {
        SysNotice sysNotice = new SysNotice();
        sysNotice.setTypeCode(sysNoticeType.getTypeCode())
                .setTitle(msgTitle)
                .setContent(msgConent)
                .setCreateTime(new Date())
                .setExpiryTime(new DateTime().plusDays(sysNoticeType.getUntilExpiryDay()).toDate())
                .setDeadTime(new DateTime().plusDays(sysNoticeType.getUntilDeadDay()).toDate())
                .setId(IdUtils.id())
                .save();
        receivers.forEach(receiverId -> {
            new SysNoticeDetail().setId(IdUtils.id())
                    .setSysNoticeId(sysNotice.getId())
                    .setReceiver(receiverId)
                    .setHasRead("N")
                    .save();
        });
    }
}
