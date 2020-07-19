package com.github.qinyou.common.activiti;

import com.github.qinyou.system.model.SysUser;
import com.github.qinyou.system.model.SysUserRole;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.Picture;
import org.activiti.engine.identity.User;
import org.activiti.engine.identity.UserQuery;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.UserQueryImpl;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.IdentityInfoEntity;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.activiti.engine.impl.persistence.entity.UserEntityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("Duplicates")
public class UserManager extends UserEntityManager {
    @Override
    public User findUserById(String userId) {
        SysUser sysUser = SysUser.dao.findByUsername(userId);
        User user = new UserEntity();
        user.setId(sysUser.getUsername());
        user.setFirstName(sysUser.getRealName());
        return user;
    }

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
    public User createNewUser(String userId) {
        throw new RuntimeException("not implement method.");
    }

    @Override
    public void insertUser(User user) {
        throw new RuntimeException("not implement method.");
    }

    @Override
    public void updateUser(User updatedUser) {
        throw new RuntimeException("not implement method.");
    }

    @Override
    public void deleteUser(String userId) {
        throw new RuntimeException("not implement method.");
    }

    @Override
    public List<User> findUserByQueryCriteria(UserQueryImpl query, Page page) {
        throw new RuntimeException("not implement method.");
    }

    @Override
    public long findUserCountByQueryCriteria(UserQueryImpl query) {
        throw new RuntimeException("not implement method.");
    }

    @Override
    public UserQuery createNewUserQuery() {
        throw new RuntimeException("not implement method.");
    }

    @Override
    public IdentityInfoEntity findUserInfoByUserIdAndKey(String userId, String key) {
        throw new RuntimeException("not implement method.");
    }

    @Override
    public List<String> findUserInfoKeysByUserIdAndType(String userId, String type) {
        throw new RuntimeException("not implement method.");
    }

    @Override
    public Boolean checkPassword(String userId, String password) {
        throw new RuntimeException("not implement method.");
    }

    @Override
    public List<User> findPotentialStarterUsers(String proceDefId) {
        throw new RuntimeException("not implement method.");
    }

    @Override
    public List<User> findUsersByNativeQuery(Map<String, Object> parameterMap, int firstResult, int maxResults) {
        throw new RuntimeException("not implement method.");
    }

    @Override
    public long findUserCountByNativeQuery(Map<String, Object> parameterMap) {
        throw new RuntimeException("not implement method.");
    }

    @Override
    public boolean isNewUser(User user) {
        throw new RuntimeException("not implement method.");
    }

    @Override
    public Picture getUserPicture(String userId) {
        throw new RuntimeException("not implement method.");
    }

    @Override
    public void setUserPicture(String userId, Picture picture) {
        throw new RuntimeException("not implement method.");
    }
}
