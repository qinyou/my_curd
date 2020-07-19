package com.github.qinyou.common.activiti.method;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.Map;

public class CommonMethod implements Serializable {

    /**
     * json 字符串 转 map
     *
     * @param json json字符串
     * @return map 对象
     */
    public Map json2Map(String json) {
        return JSON.parseObject(json, Map.class);
    }
}
