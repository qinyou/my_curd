package com.github.qinyou.common.web;

import com.github.qinyou.system.model.SysMenu;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

public class MainService {

    /**s
     * 通过 rootId  获得 所有子菜单
     * @param allMenuList
     * @param rootId
     * @param chainSet
     */
    public  static void getCChain(Collection<SysMenu> allMenuList, String rootId, Set<SysMenu> chainSet) {
        for (SysMenu m : allMenuList) {
            if (Objects.equals(rootId, m.getPid())) {
                chainSet.add(m);
                getCChain(allMenuList, m.getId(), chainSet);
            }
        }
    }
}
