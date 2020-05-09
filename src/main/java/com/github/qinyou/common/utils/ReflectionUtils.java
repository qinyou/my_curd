package com.github.qinyou.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 反射工具类
 *
 * @author zhangchuang
 */
@SuppressWarnings({"WeakerAccess", "unchecked", "unused"})
public class ReflectionUtils {
    /**
     * 运行方法
     *
     * @param instance
     * @param methodName
     * @param args       方法参数
     * @return 方法返回值, 无返回值结果为null
     * @throws Exception
     */
    public static Object runMethod(Object instance, String methodName, Object... args) throws Exception {
        Class classObj = instance.getClass();
        Method method;
        method = classObj.getMethod(methodName);              // 查公共方法
        if (method == null) {
            method = classObj.getDeclaredMethod(methodName);  // 查类内定义方法
        }
        if (method == null) {
            throw new Exception(methodName + " 方法 找不到 或者 无法调用 ");
        }
        method.setAccessible(true);
        return method.invoke(instance, args);
    }


    /**
     * 运行方法
     *
     * @param className  类全名
     * @param methodName 方法名
     * @param args       方法参数
     * @return 方法返回值, 方法无返回值 返回值为 null
     * @throws Exception
     */
    public static Object runMethod(String className, String methodName, Object... args) throws Exception {
        Class classObj = Class.forName(className);
        Object instance = classObj.newInstance();
        return runMethod(instance, methodName, args);
    }


    /**
     * 返回 对象字段值
     *
     * @param instance  对象实例
     * @param fieldName 字段名字
     * @return
     * @throws Exception
     */
    public static Object getFieldValue(Object instance, String fieldName) throws Exception {
        Field field;
        field = instance.getClass().getField(fieldName);
        if (field == null) {
            field = instance.getClass().getDeclaredField(fieldName);
        }
        if (field == null) {
            throw new Exception(fieldName + " 字段 找不到 或者 无法调用 ");
        }
        field.setAccessible(true);

        return field.get(instance);
    }


    /**
     * 设置字段值
     *
     * @param instance
     * @param fieldName
     * @param fieldValue
     * @throws Exception
     */
    public static void setFieldValue(Object instance, String fieldName, Object fieldValue) throws Exception {
        Field field;
        field = instance.getClass().getField(fieldName);
        if (field == null) {
            field = instance.getClass().getDeclaredField(fieldName);
        }
        if (field == null) {
            throw new Exception(fieldName + " 字段 找不到 或者 无法调用 ");
        }
        field.setAccessible(true);
        field.set(instance, fieldValue);
    }
}
