package com.hytc.webmanage.common.util;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

import lombok.extern.log4j.Log4j2;

@Log4j2
public enum FwBeanUtils {
    me;

    public final <T, S> T createCopy(S oSrc, Class<T> type) {
        try {
            T copy = type.newInstance();
            BeanUtils.copyProperties(oSrc, copy);
            return copy;
        } catch (InstantiationException | IllegalAccessException e) {
            log.catching(e);
        }
        return null;
    }

    public final <T, S> List<T> createCopy(List<S> oSrc, Class<T> type) {
        return oSrc.stream().map(o -> createCopy(o, type)).collect(Collectors.toList());
    }
}
