package com.github.qinyou.common.web;

import com.github.qinyou.common.utils.StringUtils;
import com.github.qinyou.system.model.SysButton;
import com.github.qinyou.system.model.SysMenu;

import java.util.*;

@SuppressWarnings("Duplicates")
public class LoginService {
    /**
     * 获取 所有 父祖 菜单
     *
     * @param allMenuList
     * @param menu
     * @param chainSet
     */
    public static void getPChain(Collection<SysMenu> allMenuList, SysMenu menu, Set<SysMenu> chainSet) {
        for (SysMenu m : allMenuList) {
            if (Objects.equals(menu.getPid(), m.getId())) {
                chainSet.add(m);
                getPChain(allMenuList, m, chainSet);
            }
        }
    }

    /**
     * 用户完整的菜单
     *
     * @param roleIds 多个role id，以逗号分隔
     * @return
     */
    public static List<SysMenu> findUserMenus(String roleIds) {
        if (StringUtils.isEmpty(roleIds)) {
            return new ArrayList<>();
        }
        if (roleIds.contains(",") && !roleIds.contains("'")) {
            roleIds = roleIds.replaceAll(",", "','");
        }

        // 所有菜单
        List<SysMenu> allMenuList = SysMenu.dao.findAll();
        // 用户菜单
        List<SysMenu> userMenuList = SysMenu.dao.findByRoleIds(roleIds);
        // 完整的用户菜单
        Set<SysMenu> chainSet = new HashSet<>();
        for (SysMenu menu : userMenuList) {
            chainSet.add(menu);
            getPChain(allMenuList, menu, chainSet);
        }
        //排序
        userMenuList = new ArrayList<>(chainSet);
        userMenuList.sort((o1, o2) -> {
            if (o1.getSortNum() == null || o2.getSortNum() == null || o1.getSortNum() < o2.getSortNum()) {
                return -1;
            }
            return 0;
        });
        return userMenuList;
    }

    /**
     * 查询用户菜单按钮
     *
     * @return
     */
   public static List<String> findUserButtons(String roleIds) {
        if (StringUtils.isEmpty(roleIds)) {
            return new ArrayList<>();
        }
        if (roleIds.contains(",") && !roleIds.contains("'")) {
            roleIds = roleIds.replaceAll(",", "','");
        }

        String sql = " select distinct a.buttonCode from sys_button a , sys_role_button b ,sys_role c " +
                " where b.sysButtonId = a.id  and b.sysRoleId = c.id and b.sysRoleId in ('" + roleIds + "')";
        List<SysButton> sysButtons = SysButton.dao.find(sql);

        List<String> buttonCodes = new ArrayList<>();
        sysButtons.forEach(sysButton -> buttonCodes.add(sysButton.getButtonCode()));
        return buttonCodes;
    }


}
