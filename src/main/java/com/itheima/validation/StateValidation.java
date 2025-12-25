package com.itheima.validation;


import com.itheima.anno.State;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

//第一个注解指定的是给哪个注解提供校验规则，第二个注解是验证的参数类型
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
