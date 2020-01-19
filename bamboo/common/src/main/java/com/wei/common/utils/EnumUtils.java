package com.wei.common.utils;

import com.wei.common.exceptions.CheckException;

import java.util.Arrays;
import java.util.List;

public class EnumUtils {

    public static <E extends Enum<?>> List<E> values(Class<E> enumClass) {
        E[] enumConstants = enumClass.getEnumConstants();
        return Arrays.asList(enumConstants);
    }

    public static <E extends Enum<?>> E getEnum(Class<E> enumClass, String name) {
        List<E> enumConstants = EnumUtils.values(enumClass);
        for (E enumConstant : enumConstants) {
            if(enumConstant.name().equalsIgnoreCase(name)) {
                return enumConstant;
            }
        }
        return null;
    }

}
