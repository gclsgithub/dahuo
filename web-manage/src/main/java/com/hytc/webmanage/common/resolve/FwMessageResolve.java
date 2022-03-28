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
package com.hytc.webmanage.common.resolve;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.LocaleResolver;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * Contains static utility methods to resolve ResultMessage into corresponding message strings<br>
 */
@Log4j2
@Component
@RequiredArgsConstructor
public final class FwMessageResolve {

    final private MessageSource messageSource;

    final private LocaleResolver localeResolver;

    /**
     * resolve message text of <code>ResultMessage</code><br>
     * <ol>
     * <li>if <code>ResultMessage</code> has message code, try to resolve message using it
     * <ol>
     * <li>if there is no message for that code, try to use text of <code>ResultMessage</code>.</li>
     * <li>if there is no text, throw {@link NoSuchMessageException}</li>
     * </ol>
     * </li>
     * <li>return text of <code>ResultMessage</code> even if it is <code>null</code></li>
     * </ol>
     * @param message result message to resolve (must not be <code>null</code>)
     * @param messageSource message source (must not be <code>null</code>)
     * @param locale locate to resolve message (must not be <code>null</code>)
     * @return message text (must not be <code>null</code>)
     * @throws NoSuchMessageException if message is not found and no default text is given
     * @throws IllegalArgumentException if message or messageSoruce or locale is <code>null</code>
     */
    public String resolveMessage(ResultMessages message,
                                 Locale locale) throws NoSuchMessageException {
        Assert.notNull(message, "message must not be null!");
        // default set
        if (locale == null) {
            locale = localeResolver.resolveLocale(this.request());
        }
        String msg;
        String code = message.getCode();
        if (code != null) {
            // try to resolve from code at first.
            try {
                if (message.getArgs().length > 0) {
                    String[] args = new String[message.getArgs().length];
                    for (int i = 0; i < args.length; i++) {
                        if (message.getArgs()[i] == null) {
                            continue;
                        }
                        String arg = message.getArgs()[i].toString();
                        args[i] = resolveMessage(arg);
                    }
                    msg = messageSource.getMessage(code, args, locale);
                } else {
                    msg = messageSource.getMessage(code, message.getArgs(), locale);
                }
            } catch (NoSuchMessageException e) {
                String text = message.getText();
                if (text != null) {
                    if (log.isDebugEnabled()) {
                        log.debug("messege is not found under code '" + code + "' for '" + locale + "'. use '" + text + "' instead", e);
                    }
                    // if ResultMessage has a text, then use it.
                    msg = text;
                } else {
                    // otherwise throw exception
                    if (log.isDebugEnabled()) {
                        log.catching(e);
                    }
                    msg = code;
                }
            }
        } else {
            msg = message.getText();
        }
        return msg;
    }

    /**
     * resolve message text of <code>ResultMessage</code><br>
     * <ol>
     * <li>if <code>ResultMessage</code> has message code, try to resolve message using it
     * <ol>
     * <li>if there is no message for that code, try to use text of <code>ResultMessage</code>.</li>
     * <li>if there is no text, throw {@link NoSuchMessageException}</li>
     * </ol>
     * </li>
     * <li>return text of <code>ResultMessage</code> even if it is <code>null</code></li>
     * </ol>
     * @param message ResultMessage instance
     * @param messageSource a MessageSource instance for solving a complete message
     * @return message text
     * @throws NoSuchMessageException If does not resolve a message
     */
    public String resolveMessage(ResultMessages message) {
        return resolveMessage(message, null);
    }

    /**
     * resolve message text of <code>code</code><br>
     * <ol>
     * <li>if <code>ResultMessage</code> has message code, try to resolve message using it
     * <ol>
     * <li>if there is no message for that code, try to use text of <code>ResultMessage</code>.</li>
     * <li>if there is no text, throw {@link NoSuchMessageException}</li>
     * </ol>
     * </li>
     * <li>return text of <code>ResultMessage</code> even if it is <code>null</code></li>
     * </ol>
     *
     * @param message ResultMessage instance
     * @param messageSource a MessageSource instance for solving a complete message
     * @return message text
     * @throws NoSuchMessageException If does not resolve a message
     */
    public String resolveMessage(String code, Object... args) {
        ResultMessages message = ResultMessages.fromCode(code, args);
        return resolveMessage(message, null);
    }

    private HttpServletRequest request() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }
}
