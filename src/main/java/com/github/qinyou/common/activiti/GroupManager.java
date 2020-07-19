package com.github.qinyou.common.activiti;

import com.github.qinyou.system.model.SysUserRole;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.GroupQuery;
import org.activiti.engine.impl.GroupQueryImpl;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.GroupEntityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("Duplicates")
public class GroupManager extends GroupEntityManager {
    @Override
    public List<Group> findGroupsByUser(String userId) {
        List<Group> groups = new ArrayList<>();
        SysUserRole.dao.findRolesByUsername(userId).forEach(record -> {
            Group group = new GroupEntity(record.getStr("roleCode"));
            group.setName(record.getStr("roleName"));
            groups.add(group);
        });
        return groups;
    }

    @Override
    public Group createNewGroup(String groupId) {
        throw new RuntimeException("not implement method.");
    }

    @Override
    public void insertGroup(Group group) {
        throw new RuntimeException("not implement method.");
    }

    @Override
    public void updateGroup(Group updatedGroup) {
        throw new RuntimeException("not implement method.");
    }

    @Override
    public void deleteGroup(String groupId) {
        throw new RuntimeException("not implement method.");
    }

    @Override
    public GroupQuery createNewGroupQuery() {
        throw new RuntimeException("not implement method.");
    }

    @Override
    public List<Group> findGroupByQueryCriteria(GroupQueryImpl query, Page page) {
        throw new RuntimeException("not implement method.");
    }

    @Override
    public long findGroupCountByQueryCriteria(GroupQueryImpl query) {
        throw new RuntimeException("not implement method.");
    }

    @Override
    public List<Group> findGroupsByNativeQuery(Map<String, Object> parameterMap, int firstResult, int maxResults) {
        throw new RuntimeException("not implement method.");
    }

    @Override
    public long findGroupCountByNativeQuery(Map<String, Object> parameterMap) {
        throw new RuntimeException("not implement method.");
    }

    @Override
    public boolean isNewGroup(Group group) {
        throw new RuntimeException("not implement method.");
    }
}
