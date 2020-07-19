<#--机构信息弹窗-->
<#include "../common.ftl"/>
<@layout>
<style>
    .card table{
        width: 100%;
    }
    .card table.lsrm tr > td{
        font-size: 14px;
    }
</style>
    <#if sysOrg??>
  <div class="card">
      <table class=" pure-table pure-table-horizontal labelInputTable lsrm" style="margin: 0 auto;">
          <tbody>
          <tr>
              <td>名称：</td>
              <td>
                  ${(sysOrg.orgName)!}
              </td>
          </tr>

          <#if sysOrg.orgCode?? >
          <tr >
              <td>编码：</td>
              <td>
                  ${(sysOrg.orgCode)!}
              </td>
          </tr>
          </#if>

          <tr>
              <td>序号：</td>
              <td>
                  ${(sysOrg.sortNum)!}
              </td>
          </tr>

         <#if sysOrg.address?? >
          <tr>
              <td>地址：</td>
              <td>
                  ${(sysOrg.address)!}
              </td>
          </tr>
         </#if>

          <#if (sysOrg.mark)??>
          <tr>
              <td>备注：</td>
              <td>
                  ${(sysOrg.remark)!}
              </td>
          </tr>
          </#if>
          </tbody>
      </table>
  </div>
    <#else>
        <div style="text-align: center;height: 100px;">
           找不到组织机构信息: ${(orgId)!}
        </div>
    </#if>
</@layout>
