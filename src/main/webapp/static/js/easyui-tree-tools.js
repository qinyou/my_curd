/**
 * easyui tree 数据处理工具函数
 */
var EasyTree = function() {
    /**
     * rows 中 是否有任何 row de  ParentId 与 参数 ParentId 相同
     * @param   rows
     * @param  ParentId
     */
    var exists = function(rows, idFieldName, ParentId) {
        for (var i = 0; i < rows.length; i++) {
            if (rows[i][idFieldName] == ParentId) return true;
        }
        return false;
    }

    /*
     * 将一般的JSON格式转为EasyUI Tree 的JSON格式
     * @param rows:json数据对象
     * @param dFieldName:表id的字段名
     * @param pidFieldName:表父级id的字段名
     * @param fileds:要显示的字段,多个字段用逗号分隔
     */
    var treeDataBuild = function(rows, idFieldName, pidFieldName, fileds) {
        var nodes = [];
        // get the top level nodes
        //console.log(rows.length);
        for (var i = 0; i < rows.length; i++) {
            var row = rows[i];
            if (!exists(rows, idFieldName, row[pidFieldName])) {
                var data  = {};
                data[idFieldName] = row[idFieldName];
                var arrFiled = fileds.split(",");
                for (var j = 0; j < arrFiled.length; j++) {
                    if (arrFiled[j] != idFieldName) data[arrFiled[j]] = row[arrFiled[j]];
                }
                nodes.push(data);
            }
        }
        // console.info("根目录nodes："+JSON.stringify(nodes));
        var toDo = [];
        for (var i = 0; i < nodes.length; i++) {
            toDo.push(nodes[i]);
        }

        while (toDo.length) {
            var node = toDo.shift(); // the parent node
            // get the children nodes
            for (var i = 0; i < rows.length; i++) {
                var row = rows[i];
                if (row[pidFieldName] == node[idFieldName]) {
                    var child = {};
                    child[idFieldName] = row[idFieldName];


                    var arrFiled = fileds.split(",");
                    for (var j = 0; j < arrFiled.length; j++) {
                        if (arrFiled[j] != idFieldName) {
                            child[arrFiled[j]] = row[arrFiled[j]];
                        }
                    }
                    if (node.children) {
                        node.children.push(child);
                    } else {
                        node.children = [child];
                    }
                    toDo.push(child);
                }
            }
        }
        return nodes;
    };
    return {
        treeDataBuild: treeDataBuild
    };
};