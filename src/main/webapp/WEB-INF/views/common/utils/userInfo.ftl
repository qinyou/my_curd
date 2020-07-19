<#--用户信息弹窗-->
<#include "../common.ftl"/>
<@layout>
<style>
    .card table{
        width: 100%;
    }
    .card .avatarWrap {
        width: 80px;
        height: 80px;
        margin: 0 auto;
        overflow: hidden;
        -webkit-border-radius: 50%;
        -moz-border-radius: 50%;
        border-radius: 50%;
        transition: .3s;
        -webkit-transition: .3s;
        -moz-transition: .3s;
    }
    .card table.lsrm tr > td{
        font-size: 14px;
    }
</style>
    <#if sysUser??>
  <div class="card">
      <table class=" pure-table pure-table-horizontal labelInputTable lsrm" style="margin: 0 auto;">
          <tbody>
          <tr>
              <td colspan="2" style="background-color: #eee;">
                  <div class="avatarWrap">
                       <#if (sysUser.gender)=='M' >
                             <img src="${ctx!}/${(sysUser.avatar)!'static/image/male.jpg'}" class="pure-img" alt="${(sysUser.username)!} avatar">
                       </#if>
                       <#if (sysUser.gender)=='F' >
                         <img src="${ctx!}/${(sysUser.avatar)!'static/image/female.jpg'}" class="pure-img" alt="${(sysUser.username)!} avatar">
                       </#if>
                  </div>
              </td>
          </tr>
          <tr>
              <td style="width:80px">姓名：</td>
              <td>
                  ${(sysUser.realName)!}
              </td>
          </tr>
          <tr>
              <td>职位：</td>
              <td>
                  ${(sysUser.job)!}
              </td>
          </tr>
          <#if sysUser.orgNames??>
              <tr>
                  <td>部门：</td>
                  <td>
                      <#if orgs?? && orgs?size gt 0>
                          <#list orgs as org>
                              <div style="font-size: 14px;">${(org.name)!}</div>
                          </#list>
                      </#if>
                  </td>
              </tr>
          </#if>
          <#if sysUser.roleNames??>
              <tr>
                  <td>角色：</td>
                  <td>
                      ${(sysUser.roleNames)!}
                  </td>
              </tr>
          </#if>
          <#if sysUser.email??>
              <tr >
                  <td>邮箱：</td>
                  <td>
                      ${(sysUser.email)!}
                  </td>
              </tr>
          </#if>
          <#if sysUser.phone??>
              <tr>
                  <td>电话：</td>
                  <td>
                      ${(sysUser.phone)!}
                  </td>
              </tr>
          </#if>
          <tr>
              <td>性别：</td>
              <td>
                  <#if sysUser.gender=='M'>男</#if>
                  <#if sysUser.gender=='F'>女</#if>
              </td>
          </tr>
          <tr>
              <td>状态：</td>
              <td>
                  ${((sysUser.userState=='0')?string('正常','禁用'))!}
              </td>
          </tr>
          </tbody>
      </table>
  </div>
    <#else>
        <div style="text-align: center; height: 100px;">
            找不到用户信息: ${(username)!}
        </div>
    </#if>
</@layout>
