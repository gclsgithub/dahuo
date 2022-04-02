/*
 * Copyright(c) 2013 NTT DATA Corporation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package com.hytc.webmanage.common.exception;

import java.util.ArrayList;
import java.util.List;

import com.hytc.webmanage.common.resolve.ResultMessages;
import com.hytc.webmanage.common.web.IErrors;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

/**
 * Exception to inform you that it has detected a violation of business rules.<br>
 */
@Log4j2
public class BusinessException extends ResultMessagesNotificationException {

    @Getter
    @Setter
    private List<BusinessException> exceptionList = new ArrayList<>();

    private static final long serialVersionUID = 1L;

    final static public BusinessException of(ResultMessages rm) {
        return new BusinessException(rm);
    }

    /**
     * Constructor for specify a message.
     * <p>
     * generate a {@link ResultMessages} instance of error type and add a message.
     * </p>
     *
     * @param iError result iError
     * @param args
     */
    public BusinessException(IErrors<?> iError, Object... args) {
        super(ResultMessages.error().add(iError, args));
    }

    /**
     * Constructor for specify a code.
     * <p>
     * generate a {@link ResultMessages} instance of error type and add a message.
     * </p>
     *
     * @param code result message
     */
    public BusinessException(String code) {
        super(ResultMessages.error().add(ResultMessages.fromCode(code)));
    }

    /**
     * Constructor for specify messages.
     * <p>
     * Takes multiple {@code String} messages as argument.
     * </p>
     *
     * @param messages {@link ResultMessages} instance
     */
    public BusinessException(ResultMessages messages) {
        super(messages);
    }

    /**
     * Constructor for specify messages and exception.
     * <p>
     * Takes multiple {@code String} messages and cause of exception as argument.
     * </p>
     *
     * @param messages {@link ResultMessages} instance
     * @param cause    {@link Throwable} instance
     */
    public BusinessException(ResultMessages messages, Throwable... cause) {
        super(messages, cause.length == 0 ? null : cause[0]);
    }

}
