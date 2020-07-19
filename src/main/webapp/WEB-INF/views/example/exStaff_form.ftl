<#-- zhangchuang  2019-02-22 22:15:06 -->
<#include "../common/common.ftl"/>
<@layout>
<link rel="stylesheet" href="/static/css/one-to-many.css">
<form id="modelForm" method="POST"
      action="<#if exStaff?? >${ctx!}/exStaff/updateAction<#else>${ctx!}/exStaff/addAction</#if>">
    <table class=" pure-table pure-table-horizontal fullWidthTable labelInputTable">
        <input id="id" name="exStaff.id" type="hidden" value="${(exStaff.id)!}">
        <tbody>
        <tr>
            <td>
                姓名:
            </td>
            <td>
                <input name="exStaff.name" value="${(exStaff.name)!}" class="easyui-textbox"
                       data-options="required:true">
            </td>

            <td>
                性别:
            </td>
            <td>
                <input name="exStaff.gender" value="${(exStaff.gender)!}" class="easyui-textbox"
                       data-options="required:true">
            </td>
        </tr>
        <tr>
            <td>
                身份证号:
            </td>
            <td>
                <input name="exStaff.idCard" value="${(exStaff.idCard)!}" class="easyui-textbox"
                       data-options="required:true">
            </td>

            <td>
                民族:
            </td>
            <td>
                <input name="exStaff.nation" value="${(exStaff.nation)!}" class="easyui-textbox"
                       data-options="required:true">
            </td>
        </tr>
        <tr>
            <td>
                身高CM:
            </td>
            <td>
                <input name="exStaff.height" value="${(exStaff.height)!}" class="easyui-textbox"
                       data-options="required:true">
            </td>

            <td>
                体重KG:
            </td>
            <td>
                <input name="exStaff.weight" value="${(exStaff.weight)!}" class="easyui-textbox"
                       data-options="required:true">
            </td>
        </tr>
        <tr>
            <td>
                职位:
            </td>
            <td>
                <input name="exStaff.job" value="${(exStaff.job)!}" class="easyui-textbox" data-options="required:true">
            </td>
            <td>
                手机号:
            </td>
            <td>
                <input name="exStaff.phoneNumber" value="${(exStaff.phoneNumber)!}" class="easyui-textbox"
                       data-options="required:true">
            </td>

        </tr>
        <tr>
            <td>
                家庭地址:
            </td>
            <td colspan="3">
                <input name="exStaff.homeAddress" value="${(exStaff.homeAddress)!}" class="easyui-textbox"
                       data-options="required:true">
            </td>
        </tr>
        </tbody>
    </table>

    <div class="easyui-tabs" style="width:100%; height: auto;margin-top: 2px;" >
        <div title="教育经历" class="sonTablePanel">
            <a onclick="addRow(this)" class="easyui-linkbutton addRowBtn" title="增行" iconCls="iconfont icon-add"
               plain="true">增行</a>
            <div class="tpl">
                <tpltr>
                    <myinput name="exStaffEducation[number].id" type="hidden"/>
                    <myinput name="exStaffEducation[number].exStaffId" type="hidden"/>
                    <tpltd class="number"></tpltd>
                    <tpltd>
                        <myinput style="width: 157px;" name="exStaffEducation[number].schoolName"
                                 class="myui-textbox" data-options="required:true">
                    </tpltd>
                    <tpltd>
                        <myinput style="width: 157px;" name="exStaffEducation[number].grade"
                                 class="myui-combobox"
                                 data-options="required:true,panelHeight:'auto',valueField:'value',textField:'label',url:'${ctx!}/utils/dictList?groupCode=education'">
                    </tpltd>
                    <tpltd>
                        <myinput style="width: 157px;" name="exStaffEducation[number].startTime"
                                 class="myui-datebox" data-options="required:true">
                    </tpltd>
                    <tpltd>
                        <myinput style="width: 157px;" name="exStaffEducation[number].endTime"
                                 class="myui-datebox" data-options="required:true">
                    </tpltd>
                    <tpltd><a onclick="delRow(this,'${ctx!}/exStaff/deleteExStaffEducationAction')" title="删行"
                              class="myui-linkbutton" iconCls="iconfont icon-delete" plain="true"></a></tpltd>
                </tpltr>
            </div>
            <table class="pure-table  pure-table-bordered">
                <thead>
                <tr>
                    <th>序号</th>
                    <th>学校名字</th>
                    <th>等级</th>
                    <th>开始时间</th>
                    <th>结束时间</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
             <#if  exStaffEducations  ??>
                 <#list  exStaffEducations as item>
                             <tr>
                                 <input name="exStaffEducation[${item_index}].id" type="hidden" value="${(item.id)!}">
                                 <input name="exStaffEducation[${item_index}].exStaffId" type="hidden"
                                        value="${(item.exStaffId)!}">
                                 <td class="number">${item_index+1}</td>
                                 <td><input style="width: 157px;" name="exStaffEducation[${item_index}].schoolName"
                                            value="${(item.schoolName)!}" class="easyui-textbox"
                                            data-options="required:true"></td>
                                 <td><input style="width: 157px;" name="exStaffEducation[${item_index}].grade"
                                            value="${(item.grade)!}" class="easyui-combobox"
                                            data-options="required:true,panelHeight:'auto',valueField:'value',textField:'label',url:'${ctx!}/utils/dictList?groupCode=education'">
                                 </td>
                                 <td><input style="width: 157px;" name="exStaffEducation[${item_index}].startTime"
                                            value="${(item.startTime)!}" class="easyui-datebox"
                                            data-options="required:true"></td>
                                 <td><input style="width: 157px;" name="exStaffEducation[${item_index}].endTime"
                                            value="${(item.endTime)!}" class="easyui-datebox"
                                            data-options="required:true"></td>
                                 <td><a onclick="delRow(this,'${ctx!}/exStaff/deleteExStaffEducationAction')" title="删行"
                                        class="easyui-linkbutton" iconCls="iconfont icon-delete" plain="true"></a></td>
                             </tr>
                 </#list>
             </#if>
                </tbody>
            </table>
        </div>
        <div title="工作经历" class="sonTablePanel">
            <a onclick="addRow(this)" class="easyui-linkbutton addRowBtn" title="增行" iconCls="iconfont icon-add"
               plain="true">增行</a>
            <div class="tpl">
                <tpltr>
                    <myinput name="exStaffExperience[number].id" type="hidden"/>
                    <myinput name="exStaffExperience[number].exStaffId" type="hidden"/>
                    <tpltd class="number"></tpltd>
                    <tpltd>
                        <myinput style="width: 157px;" name="exStaffExperience[number].companyName"
                                 class="myui-textbox" data-options="required:true">
                    </tpltd>
                    <tpltd>
                        <myinput style="width: 157px;" name="exStaffExperience[number].trade"
                                 class="myui-textbox" data-options="required:true">
                    </tpltd>
                    <tpltd>
                        <myinput style="width: 157px;" name="exStaffExperience[number].startTime"
                                 class="myui-datebox" data-options="required:true">
                    </tpltd>
                    <tpltd>
                        <myinput style="width: 157px;" name="exStaffExperience[number].endTime"
                                 class="myui-datebox" data-options="required:true">
                    </tpltd>
                    <tpltd>
                        <myinput style="width: 157px;" name="exStaffExperience[number].witness"
                                 class="myui-textbox" data-options="required:true">
                    </tpltd>
                    <tpltd>
                        <myinput style="width: 157px;" name="exStaffExperience[number].witnessPhone"
                                 class="myui-textbox" data-options="required:true">
                    </tpltd>
                    <tpltd><a onclick="delRow(this,'${ctx!}/exStaff/deleteExStaffExperienceAction')" title="删行"
                              class="myui-linkbutton" iconCls="iconfont icon-delete" plain="true"></a></tpltd>
                </tpltr>
            </div>
            <table class="pure-table  pure-table-bordered">
                <thead>
                <tr>
                    <th>序号</th>
                    <th>公司名</th>
                    <th>行业</th>
                    <th>开始时间</th>
                    <th>结束时间</th>
                    <th>证明人</th>
                    <th>证明人联系方式</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
             <#if  exStaffExperiences  ??>
                 <#list  exStaffExperiences as item>
                             <tr>
                                 <input name="exStaffExperience[${item_index}].id" type="hidden" value="${(item.id)!}">
                                 <input name="exStaffExperience[${item_index}].exStaffId" type="hidden"
                                        value="${(item.exStaffId)!}">
                                 <td class="number">${item_index+1}</td>
                                 <td><input style="width: 157px;" name="exStaffExperience[${item_index}].companyName"
                                            value="${(item.companyName)!}" class="easyui-textbox"
                                            data-options="required:true"></td>
                                 <td><input style="width: 157px;" name="exStaffExperience[${item_index}].trade"
                                            value="${(item.trade)!}" class="easyui-textbox"
                                            data-options="required:true"></td>
                                 <td><input style="width: 157px;" name="exStaffExperience[${item_index}].startTime"
                                            value="${(item.startTime)!}" class="easyui-datebox"
                                            data-options="required:true"></td>
                                 <td><input style="width: 157px;" name="exStaffExperience[${item_index}].endTime"
                                            value="${(item.endTime)!}" class="easyui-datebox"
                                            data-options="required:true"></td>
                                 <td><input style="width: 157px;" name="exStaffExperience[${item_index}].witness"
                                            value="${(item.witness)!}" class="easyui-textbox"
                                            data-options="required:true"></td>
                                 <td><input style="width: 157px;" name="exStaffExperience[${item_index}].witnessPhone"
                                            value="${(item.witnessPhone)!}" class="easyui-textbox"
                                            data-options="required:true"></td>
                                 <td><a onclick="delRow(this,'${ctx!}/exStaff/deleteExStaffExperienceAction')"
                                        title="删行" class="easyui-linkbutton" iconCls="iconfont icon-delete"
                                        plain="true"></a></td>
                             </tr>
                 </#list>
             </#if>
                </tbody>
            </table>
        </div>
        <div title="家庭成员" class="sonTablePanel">
            <a onclick="addRow(this)" class="easyui-linkbutton addRowBtn" title="增行" iconCls="iconfont icon-add"
               plain="true">增行</a>
            <div class="tpl">
                <tpltr>
                    <myinput name="exStaffFamily[number].id" type="hidden"/>
                    <myinput name="exStaffFamily[number].exStaffId" type="hidden"/>
                    <tpltd class="number"></tpltd>
                    <tpltd>
                        <myinput style="width: 157px;" name="exStaffFamily[number].name" class="myui-textbox"
                                 data-options="required:true">
                    </tpltd>
                    <tpltd>
                        <myinput style="width: 157px;" name="exStaffFamily[number].relation"
                                 class="myui-combobox"
                                 data-options="required:true,panelHeight:'auto',valueField:'value',textField:'label',url:'${ctx!}/utils/dictList?groupCode=familyRelation'">
                    </tpltd>
                    <tpltd>
                        <myinput style="width: 157px;" name="exStaffFamily[number].job" class="myui-textbox"
                                 data-options="required:true">
                    </tpltd>
                    <tpltd>
                        <myinput style="width: 157px;" name="exStaffFamily[number].phone" class="myui-textbox"
                                 data-options="required:true">
                    </tpltd>
                    <tpltd><a onclick="delRow(this,'${ctx!}/exStaff/deleteExStaffFamilyAction')" title="删行"
                              class="myui-linkbutton" iconCls="iconfont icon-delete" plain="true"></a></tpltd>
                </tpltr>
            </div>
            <table class="pure-table  pure-table-bordered">
                <thead>
                <tr>
                    <th>序号</th>
                    <th>姓名</th>
                    <th>关系</th>
                    <th>工作</th>
                    <th>联系方式</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
             <#if  exStaffFamilys  ??>
                 <#list  exStaffFamilys as item>
                             <tr>
                                 <input name="exStaffFamily[${item_index}].id" type="hidden" value="${(item.id)!}">
                                 <input name="exStaffFamily[${item_index}].exStaffId" type="hidden"
                                        value="${(item.exStaffId)!}">
                                 <td class="number">${item_index+1}</td>
                                 <td><input style="width: 157px;" name="exStaffFamily[${item_index}].name"
                                            value="${(item.name)!}" class="easyui-textbox" data-options="required:true">
                                 </td>
                                 <td><input style="width: 157px;" name="exStaffFamily[${item_index}].relation"
                                            value="${(item.relation)!}" class="easyui-combobox"
                                            data-options="required:true,panelHeight:'auto',valueField:'value',textField:'label',url:'${ctx!}/utils/dictList?groupCode=familyRelation'">
                                 </td>
                                 <td><input style="width: 157px;" name="exStaffFamily[${item_index}].job"
                                            value="${(item.job)!}" class="easyui-textbox" data-options="required:true">
                                 </td>
                                 <td><input style="width: 157px;" name="exStaffFamily[${item_index}].phone"
                                            value="${(item.phone)!}" class="easyui-textbox"
                                            data-options="required:true"></td>
                                 <td><a onclick="delRow(this,'${ctx!}/exStaff/deleteExStaffFamilyAction')" title="删行"
                                        class="easyui-linkbutton" iconCls="iconfont icon-delete" plain="true"></a></td>
                             </tr>
                 </#list>
             </#if>
                </tbody>
            </table>
        </div>
    </div>
</form>
<div class="formBtnsDiv">
    <button class=" pure-button button-small" onclick="popup.close(window.name);"><i class="iconfont icon-cancel"></i>
        取消
    </button>
    <button class=" button-small   pure-button pure-button-primary" onclick="saveAction('modelForm','reload','dg')"><i
            class="iconfont icon-save"></i> 确定
    </button>
</div>
<script src="/static/js/one-to-many.js"></script>
<script src="${ctx!}/static/js/dg-curd.js"></script>
</@layout>
