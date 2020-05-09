package com.github.qinyou.common.utils;

import com.jfinal.plugin.activerecord.Model;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Jfinal model 转 bean 工具 ，M 为 入参（Model 或 其字类)，B 为方法返回值(Bean)
 * easypoi excel 等使用, 通过调整 bean 适配 Jfinal model
 *
 * @author zhangchuang
 */
@Slf4j
public class Model2Bean<M, B> {

    /**
     * model 转 bean
     *
     * @param model     model或 其字类
     * @param beanClass bean class
     * @param mapping   model 字段 到 bean 字段的映射
     * @return bean 不为 null
     */
    public B toBean(@NotNull M model, @NotNull Class<B> beanClass, @NotNull Map<String, String> mapping) {

        // 初始化
        Model m = (Model) model;
        B o;
        try {
            o = beanClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(" bean 初始化失败");
        }

        // 字段设置值
        Field field;
        for (Map.Entry<String, String> entry : mapping.entrySet()) {
            try {
                field = beanClass.getDeclaredField(entry.getValue());
                field.setAccessible(true);
                field.set(o, m.get(entry.getKey()));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                log.error(e.getMessage(), e);
                throw new RuntimeException(entry.getValue() + " field 设值失败");
            }
        }
        return o;
    }

    /**
     * model list  转换为 Bean list
     *
     * @param models    model或其子类
     * @param beanClass bean class
     * @param mapping   model 字段到 bean 字段的映射
     * @return bean list, 不为null,可为空数组
     */
    public List<B> toBeans(@NotNull List<M> models, @NotNull Class<B> beanClass, @NotNull Map<String, String> mapping) {
        List<B> list = new ArrayList<>();
        if (models.size() == 0) {
            return list;
        }
        for (M model : models) {
            B t = toBean(model, beanClass, mapping);
            list.add(t);
        }
        return list;
    }


    /**
     * model 转 bean,  , bean 字段为驼峰， Model为 下划线格式
     *
     * @param model
     * @param beanClass
     * @param modelKeyUpper true model 的 key 为 大写下划线，false 不强制大写
     * @return
     */
    public B toBean(@NotNull M model, @NotNull Class<B> beanClass, boolean modelKeyUpper) {
        // 初始化
        Model m = (Model) model;
        B o;
        try {
            o = beanClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(" bean 初始化失败");
        }
        Field[] fields = beanClass.getDeclaredFields();
        String fieldName;

        for (Field field : fields) {
            if (field.getName().equals("serialVersionUID")) {
                continue;
            }

            field.setAccessible(true);
            fieldName = modelKeyUpper ? StringUtils.toUnderline(field.getName()).toUpperCase() : StringUtils.toUnderline(field.getName());
            try {
                field.set(o, m.get(fieldName));
            } catch (IllegalAccessException e) {
                log.error(e.getMessage(), e);
//                throw new  RuntimeException(e);
            }
        }
        return o;
    }

    /**
     * model list 转 bean list , bean 字段为驼峰， Model为 下划线格式
     *
     * @param models
     * @param beanClass
     * @param modelKeyUpper true model 的 key 为 大写下划线，false 不强制大写
     * @return
     */
    public List<B> toBeans(List<M> models, Class<B> beanClass, boolean modelKeyUpper) {
        List<B> list = new ArrayList<>();
        for (M model : models) {
            B t = toBean(model, beanClass, modelKeyUpper);
            list.add(t);
        }
        return list;
    }
}
