<!--用于开发 流程申请表单模板-->
<!-- 此处填充表单代码 -->
<table class="gridtable">
    <tr>
        <th><span v-if="editable" class="need">*</span>物资类型</th>
        <td>
            <select v-if="editable"  v-model="form.type" style="width: 180px;"  datatype="*" nullmsg="请选择物资类型">
                <option value="">请选择物资类型</option>
                <option value="办公用品">办公用品</option>
                <option value="生产物料">生产物料</option>
                <option value="后勤劳保">后勤劳保</option>
            </select>
            <template v-else>
                {{form.type}}
            </template>
        </td>
        <th ><span v-if="editable" class="need">*</span>最晚到货时间</th>
        <td>
            <input v-if="editable" v-model="form.dueTime" type="text" datatype="*" nullmsg="请选择最晚到货时间" class="date"/>
            <template v-else>{{form.dueTime}}</template>
        </td>
    </tr>
    <tr>
        <th><span v-if="editable" class="need">*</span>申请原因</th>
        <td colspan="3">
            <textarea v-if="editable" v-model="form.reason" datatype="*" nullmsg="请输入申请原因" rows="5" style="width: 90%;"></textarea>
            <template v-else>{{form.reason}}</template>
        </td>
    </tr>
</table>
<div class="sub-title">物资子表</div>
<table class="gridtable"  >
    <tr v-if="editable">
        <td v-bind:colspan="editable?5:4"><button type="button" class="sub-btn" v-on:click="handleAddPerson">增行</button></td>
    </tr>
    <tr>
        <th><span v-if="editable" class="need">*</span>名称</th>
        <th><span v-if="editable" class="need">*</span>品牌</th>
        <th><span v-if="editable" class="need">*</span>款式</th>
        <th>数量</th>
        <th v-if="editable">操作</th>
    </tr>
    <tr v-for="(item, index) in form.products" :key="index">
        <td>
            <input style="width: 180px;" v-if="editable" v-model="item.name" type="text" datatype="*"/>
            <template v-else>{{item.name}}</template>
        </td>
        <td>
            <input style="width: 180px;" v-if="editable" v-model="item.brand" type="text" datatype="*"/>
            <template v-else>{{item.brand}}</template>
        </td>
        <td>
            <input style="width: 180px;" v-if="editable" v-model="item.desc" type="text" datatype="*"/>
            <template v-else>{{item.desc}}</template>
        </td>
        <td>
            <input style="width:95px;" v-if="editable" v-model="item.amount" type="text" datatype="n"/>
            <template v-else>{{item.amount}}</template>
        </td>
        <td v-if="editable"><button type="button" v-on:click="handleRemovePerson(index)" class="sub-btn">删</button></td>
    </tr>
</table>
<!---->

<!--此处填充数据绑定、操作业务逻辑-->
<script>
    /* vue data */
    function data() {
        return {
            editable: true,
            form: {
                type: "",
                reason: "",
                dueTime: "",
                products: [
                    {
                        name: '',
                        brand: '',
                        age: '',
                        sex: '',
                    }
                ]
            }
        }
    }
    /*  vue methods */
    function methods() {
       return {
           handleRemovePerson: function (index) {
               console.log("remove ary index: " + index);
               if(this.form.products.length === 1){
                   alert('采购单最少需要一个商品');
                   return;
               }
               this.form.products.splice(index, 1);
           },
           handleAddPerson: function () {
               var product = {
                   name: '',
                   brand: '',
                   desc: '',
                   amount: 1,
               };
               this.form.products.push(product);
           },
       }
   }
</script>
<!---->
