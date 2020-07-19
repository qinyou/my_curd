<#include "../common/pure-page.ftl"/>
<@layout>
    <style>
        .category{
            width: 100%;
        }
        .title{
            width: 100%;
            padding: 10px;
            font-size: 22px;
        }
        .title>small{
            font-size: 13px;
        }
        .item{
            width: 80px;
            padding: 10px;
            display: inline-block;
        }
        .item .icon{
            margin: 0 auto;
            width: 80px;
            height: 80px;
            line-height: 80px;
            text-align: center;
            background-color: #60f3a5;
            color: #fff;
        }
        .item .icon:hover{
            background-color: #fff;
            color: #60f3a5;
            -webkit-box-shadow: inset 0px 0px 2px #60f3a5;
            -moz-box-shadow: inset 0px 0px 2px #60f3a5;
            box-shadow: inset 0px 0px 2px #60f3a5;
        }
        .item .icon>i{
            font-size: 50px !important;
        }
        .item>.desc{
            text-align: center;
            padding: 4px 0;
            font-size: 14px;
        }
    </style>
    <div style="padding: 20px;">
    <#list categoryList as category>
        <#if (category.processList)?? && category.processList?size gt 0 >
            <div class="category">
                <div class="title">
                    ${(category.name)!}
                    <#if (category.remark)??><small>${(category.remark)!}</small></#if>
                </div>
                <div class="items">
                    <#list category.processList as process>
                        <div class="item">
                            <a href="${ctx!}/myApply/newApply?processKey=${(process.defKey)!}&test=test">
                                <div class="icon" <#if (process.remark)?? > title="${(process.remark)!}"</#if>>
                                    <i class="${(process.icon)!}"></i>
                                </div>
                            </a>
                            <div class="desc">
                                ${(process.name)!}
                            </div>
                        </div>
                    </#list>
                </div>
            </div>
        </#if>
    </#list>
    </div>
    <script>
    </script>
</@layout>
