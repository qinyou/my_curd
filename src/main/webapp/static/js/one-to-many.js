/*功能结构具有局限型，只适用通用的 一对多布局*/

/**
 * 重新计算行号
 * @param dom
 */
function buildRowNumber(dom){
    var trAry = $("tr",dom);
    var replaceAttr = ['name','textboxname','numberboxname','comboname','spinnername','slidername','checkboxname','radiobuttonname'];
    for(var i=0;i<trAry.length;i++){
        $('td.number',trAry[i]).text(i+1);  // 行号

        // 所有 input, 替换相关 属性 eq.    a[0].y  a[1].y   a[2].y
        var inputAry = $('input',trAry[i]);
        for(var j =0;j<inputAry.length;j++){
            for(var x=0;x<replaceAttr.length;x++){
                if($(inputAry[j]).attr(replaceAttr[x])!=undefined){
                    $(inputAry[j]).attr(replaceAttr[x],   $(inputAry[j]).attr(replaceAttr[x]).replace(/\[(.+?)\]/g,'['+i+']'));
                }
            }
        }
    }
}

/**
 * 增行
 * @param o
 */
function addRow(o){
    var tbody = $(o).parent().find("table>tbody");
    var tpl = $(o).parent().find(".tpl").html();
    tpl = tpl.replace(/tpltr/g,'tr').replace(/tpltd/g,'td').replace(/myinput/g,"input").replace(/myui/g,'easyui');
    tbody.append(tpl);
    $.parser.parse(tbody.find("tr:last"));
    buildRowNumber(tbody);
}

/**
 * 删行
 * @param o
 * @param url
 */
function delRow(o,url) {
    var tr = $(o).parents('tr');
    var tbody =  $(o).parents('tbody');
    var idVal = tr.find("input[name*='.id']").first().val();
    if(notEmpty(idVal)){
        $.getJSON(url+"?id="+idVal,function (data) {
            if(data.state==='ok'){
                tr.remove();
                buildRowNumber(tbody);
                popup.msg(data.msg);
            }else if(data.state==='error'){
                popup.errMsg('系统异常',data.msg);
            }else{
                popup.msg(data.msg);
            }
        }).error(function () {
            popup.errMsg();
        });
    }else{
        tr.remove();
        buildRowNumber(tbody);
    }
}