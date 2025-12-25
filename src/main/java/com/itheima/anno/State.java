package com.itheima.anno;

import com.itheima.validation.StateValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented//元注解，表示该注解被其他注解引用时，会保留在Javadoc中
@Target({ElementType.FIELD})//表示该注解只能用于字段
@Retention(RetentionPolicy.RUNTIME)//表示该注解在运行时保留
@Constraint(validatedBy = StateValidation.class) // 指定验证器
public @interface State {
    // 错误提示信息
    String message() default "state参数只能是已发布或草稿";

    // 指定分组
    Class<?>[] groups() default {};//作用是告诉JSR303，这个注解在哪个组中

    // 负载 获取到State注解的附加信息
    Class<? extends Payload>[] payload() default {};//作用是告诉JSR303，这个注解的负载是哪些
}