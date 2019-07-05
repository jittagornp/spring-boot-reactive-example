/*
 * Copy right 2016 - 2017 Genius-Tree Co., Ltd.
 */
package com.pamarin.learning.webflux.validation;

import javax.validation.groups.Default;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.bind.support.WebExchangeBindException;

/**
 * @author jittagorn create : 2016/12/15
 */
public class ManualValidation {

    private final Object object;
    private final String objectName;
    private Class<?> groupClass;

    private ManualValidation(Object object, String objectName) {
        this.object = object;
        this.objectName = objectName;
    }

    /**
     * @param object
     * @param objectName
     * @return ManualValidator instance
     */
    public static ManualValidation of(Object object, String objectName) {
        return new ManualValidation(object, objectName);
    }

    /**
     * @param object
     * @return ManualValidator instance
     */
    public static ManualValidation of(Object object) {
        return of(object, "");
    }

    /**
     * @param groupClass
     * @return ManualValidator instance
     */
    public ManualValidation group(Class<?> groupClass) {
        this.groupClass = groupClass;
        return this;
    }

    /**
     * @param validator
     * @throws BindException
     */
    public void validate(Validator validator) throws BindException {
        BindingResult bindingResult = new BindException(object, objectName);
        WebExchangeBindException ex = new WebExchangeBindException(null, bindingResult);
        ValidationUtils.invokeValidator(validator, object, ex, groupClass, Default.class);
        if (ex.hasErrors()) {
            throw ex;
        }
    }
}
