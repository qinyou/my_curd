/*input 转 combotree 工具*/

/**
 * 表单中的 input 初始化为 combotree 通用, 排除自身节点，展开节点等
 * @param selector       input 输出框选择器
 * @param selectorValue  input 输入框值
 * @param excludeNodeId   需要排除的Node id, 新增表单 为空字符串，编辑表单为 对象id
 * @param dataUrl        combotree 数据接口，数据接口要返回完整的数据,不支持动态加载
 * @param required      true 必须，false 非必需
 */
function initFormCombotree(selector,selectorValue,excludeNodeId,dataUrl,required){
    var easyTree = new EasyTree();
    $(selector).combotree({
        url: dataUrl,
        value: selectorValue,
        panelHeight:'auto',
        required:required,
        multiple:false,
        loadFilter: function (data, parent) {
            /*数据处理*/
            data = easyTree.treeDataBuild(data, "id", "pid", "text,iconCls,state");
            return data;
        },
        onLoadSuccess:function(){
            var t = $(selector).combotree('tree');
            // 排除node
            if(notEmpty(excludeNodeId)){
                var node= t.tree("find",excludeNodeId);
                var pNode = t.tree("getParent",node.target);
                t.tree("remove",node.target);
                if(pNode!=null){
                    t.tree("expandTo",pNode.target).tree('select', pNode.target);
                }
            }
        }
    });
}

/**
 * 表单中的 input 初始化为 combotree 多选框
 * @param selector       input 输出框选择器
 * @param selectorValue  input 输入框值
 * @param dataUrl        combotree 数据接口，数据接口要返回完整的数据,不支持动态加载
 * @param required      true 必须，false 非必需
 */
function initFormCombotreeMultiple(selector,selectorValue,dataUrl,required){
    // 提交数据 会 多一个空值 （easyui bug)
    var easyTree = new EasyTree();
    $(selector).combotree({
        url: dataUrl,
        value: selectorValue,
        panelHeight:'auto',
        required:required,
        multiple:true,
        cascadeCheck:false,
        loadFilter: function (data, parent) {
            data = easyTree.treeDataBuild(data, "id", "pid", "text,iconCls,state");
            return data;
        }
    });
}

