<#-- zhangchuang  2019-07-23 15:50:30 -->
<#include "../../common/common.ftl"/>
<@layout>
    <table class=" pure-table pure-table-horizontal  labelInputTable fullWidthTable"  style="border: none;" >
        <input id="id" name="id"  type="hidden" value="${(formLeave.id)!}">
        <tbody>
        <tr>
            <td>
                请假类型:
            </td>
            <td>
                ${(formLeave.leaveType)!}
            </td>
        </tr>
        <tr>
            <td>
                开始时间:
            </td>
            <td>
                ${(formLeave.startTime)!}
            </td>
        </tr>
        <tr>
            <td>
                结束时间:
            </td>
            <td>
                ${(formLeave.endTime)!}
            </td>
        </tr>
        <tr>
            <td>
                请假原因:
            </td>
            <td>
                ${(formLeave.leaveReason)!}
            </td>
        </tr>
        </tbody>
    </table>
</@layout>
