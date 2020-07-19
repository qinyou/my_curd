function initDatePlugin() {
    // 更多参数定制，或直接在 dom 上绑定事件
    $(".datetime").on("click", function () {
        var that = this;
        WdatePicker({
            dateFmt: "yyyy-MM-dd HH:mm:ss",
            onpicked: function () {
                // 触发数据绑定
                $(that)[0].dispatchEvent(new Event("input"));
            },
            oncleared: function (dp) {
                // 触发数据绑定
                $(that)[0].dispatchEvent(new Event("input"));
            },
        });
    });
    $(".date").on("click", function () {
        var that = this;
        WdatePicker({
            dateFmt: "yyyy-MM-dd",
            onpicked: function () {
                // 触发数据绑定
                $(that)[0].dispatchEvent(new Event("input"));
            },
            oncleared: function (dp) {
                // 触发数据绑定
                $(that)[0].dispatchEvent(new Event("input"));
            },
        });
    });
}
var vm;
$(function(){
    vm = new Vue({
        el: "#app",
        data: data(),
        methods: methods()
    });
    if(typeof needDatePicker !=='undefined' && needDatePicker){
        initDatePlugin();
    }
    $("#applyForm").Validform({
        label: ".label",
        postonce: typeof postonce === 'undefined' ? true:postonce,
        tiptype: function (msg, o, cssctl) {
            if (!o.obj.is("form")) {
                var parent = o.obj.parent();
                var objtip = parent.find(".Validform_checktip")[0];
                if (objtip === undefined) {
                    objtip = $('<div class="Validform_checktip"></div>');
                    parent.append(objtip);
                } else {
                    objtip = $(objtip);
                }
                cssctl(objtip, o.type);
                objtip.text(msg);
            }
        },
        callback: function (form) {
            handleSubmit();
            return false;
        }
    });
});
