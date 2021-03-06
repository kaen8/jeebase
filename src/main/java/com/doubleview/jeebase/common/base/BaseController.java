package com.doubleview.jeebase.common.base;

import com.doubleview.jeebase.common.utils.DateTimeUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.beans.PropertyEditorSupport;
import java.util.Date;

/**
 * Controller基础接口
 */
public abstract class BaseController {

    /**
     * 日志对象
     */
    protected Logger logger = LoggerFactory.getLogger(getClass());


    /**
     * 参数绑定异常
     */
    @ExceptionHandler({BindException.class, ConstraintViolationException.class, ValidationException.class})
    public String bindException() {
        return "error/400";
    }

    /**
     * 授权登录异常
     */
    @ExceptionHandler({AuthenticationException.class})
    public String authenticationException() {
        return "error/403";
    }


    /**
     * 初始化数据绑定
     */
    @InitBinder
    protected void initBinder(WebDataBinder binder) {

        // String类型转换，将所有传递进来的String进行HTML编码，防止XSS攻击
        binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(text == null ? null : StringEscapeUtils.escapeHtml4(text.trim()));
            }

            @Override
            public String getAsText() {
                Object value = getValue();
                return value != null ? value.toString() : "";
            }
        });

        // Date 类型转换
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(DateTimeUtils.parseDate(text));
            }

            @Override
            public String getAsText() {
                Object value = getValue();
                return value != null ? DateTimeUtils.formatDateTime((Date) value) : "";
            }
        });
    }
}
