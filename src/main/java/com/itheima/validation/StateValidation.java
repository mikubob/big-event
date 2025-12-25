package com.itheima.validation;


import com.itheima.anno.State;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StateValidation implements ConstraintValidator<State, String> {

    /**
     * 验证逻辑
     *
     * @param value//验证的参数
     * @param constraintValidatorContext//验证的上下文
     * @return //验证结果
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        //提供校验的规则
        if (value == null) {
            return false;
        }
        return "已发布".equals(value) || "草稿".equals(value);
    }
}
