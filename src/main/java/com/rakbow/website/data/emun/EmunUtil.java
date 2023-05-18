package com.rakbow.website.data.emun;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;

public class EmunUtil {

    public static <T extends Enum<T>> Object getNameById(Class<T> enumClass, String methodName, Object... params) {
        String lang = LocaleContextHolder.getLocale().getLanguage();
        String fieldName = "nameZh";

        if (Locale.ENGLISH.getLanguage().equals(lang)) {
            fieldName = "nameEn";
        }
        try {
            Method method = enumClass.getDeclaredMethod("get" + StringUtils.capitalize(fieldName), params.getClass());
            method.setAccessible(true);
            return method.invoke(null, params);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
