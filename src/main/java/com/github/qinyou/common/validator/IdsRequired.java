package com.github.qinyou.common.validator;

import com.jfinal.core.Controller;
import com.jfinal.kit.Ret;
import com.jfinal.validate.Validator;

/**
 * 通用参数校验, ids 参数不可为空, 用于多选操作
 * @author chuang
 */
public class IdsRequired extends Validator {
    @Override
    protected void validate(Controller c) {
        validateRequired("ids", "ids", "ids 参数为空");
    }

    @Override
    protected void handleError(Controller c) {
        Ret ret = Ret.create().setFail().set("msg", "ids 参数为空");
        c.renderJson(ret);
    }
}
