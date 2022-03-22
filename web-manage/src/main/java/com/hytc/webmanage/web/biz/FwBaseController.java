package com.hytc.webmanage.web.biz;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;

import jp.co.jsto.web.common.WebConstants;
import jp.co.jsto.web.views.FileDownloadView;
import lombok.extern.log4j.Log4j2;

@Log4j2
abstract public class FwBaseController {

    final protected String redirect(String toWhere) {
        return WebConstants.REDIRECT + toWhere;
    }

    final protected String returnWhenBindError(final HttpServletResponse response, final String page, final String id) {
        response.setHeader(WebConstants.ERROR, WebConstants.ERROR_YES);
        response.setHeader(WebConstants.ERROR_TYPE, WebConstants.ERROR_BIND);
        return page + " :: #" + id;
    }

    final protected String returnWhenLogicError(final HttpServletResponse response, final String page, final String id) {
        response.setHeader(WebConstants.ERROR, WebConstants.ERROR_YES);
        response.setHeader(WebConstants.ERROR_TYPE, "logic");
        return page + " :: #" + id;
    }

    final protected String download(final Model model, final String fileName, final byte[] bytes) {
        model.addAttribute(FileDownloadView.BIN_ATTR, bytes);
        model.addAttribute(FileDownloadView.BIN_EXT_ATTR, this.fileNameEncode(fileName));
        return "fileDownload";
    }

    final private String fileNameEncode(final String fileName) {
        try {
            return URLEncoder.encode(fileName, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            log.error("file name encode error : " + fileName, e);
        }
        return fileName;
    }
}
