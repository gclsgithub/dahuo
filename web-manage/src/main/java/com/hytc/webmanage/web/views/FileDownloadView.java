package com.hytc.webmanage.web.views;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.thymeleaf.util.StringUtils;

/**
 * View Class for file downloads<br/>
 * <p>
 * When using this view, the model attributes {@linkplain FileDownloadView#TYPE_ATTR}<br/>
 * and {@linkplain FileDownloadView#PATH_ATTR} / {@linkplain FileDownloadView#URL_ATTR} should be
 * set in the controller before returning this view.
 * </p>
 *
 * @author GT
 */
@Component("fileDownload")
public class FileDownloadView extends AbstractFileDownloadView {
    /**
     * Name of the {@link Model} attribute expected to have the MIME type of the downloadable file
     */
    public static final String TYPE_ATTR = "type";

    /**
     * Name of the {@link Model} attribute expected to have the URL of the downloadable file
     */
    public static final String URL_ATTR = "url";

    /**
     * Name of the {@link Model} attribute expected to have the path to the downloadable file
     */
    public static final String PATH_ATTR = "path";

    /**
     * Name of the {@link Model} attribute expected to have the binary data of the downloadable file
     */
    public static final String BIN_ATTR = "bin";

    /**
     * Name of the {@link Model} attribute expected to have the file extension for the binary data
     * of the downloadable file
     */
    public static final String BIN_EXT_ATTR = "ext";

    /*
     * (non-Javadoc)
     *
     * @see jp.co.gt.web.download.AbstractFileDownloadView#getInputStream(java.util.Map,
     * javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected InputStream getInputStream(Map<String, Object> model, HttpServletRequest request) throws IOException {
        byte[] bin = (byte[]) model.get(BIN_ATTR);
        if (null != bin) {
            return new ByteArrayInputStream(bin);
        }

        String path = (String) model.get(PATH_ATTR);
        if (StringUtils.isEmptyOrWhitespace(path)) {
            path = (String) model.get(URL_ATTR);
            return new BufferedInputStream(new URL(path).openStream());
        } else {
            return new FileInputStream(path);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see jp.co.gt.web.download.AbstractFileDownloadView#addResponseHeader(java.util.Map,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void addResponseHeader(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {
        String ext = (String) model.get(BIN_EXT_ATTR);

        if (null == ext) {
            String path = (String) model.get(PATH_ATTR);

            if (StringUtils.isEmptyOrWhitespace(path)) {
                path = (String) model.get(URL_ATTR);
            }

            ext = path.replaceAll(".*(\\.[^\\.]+$)", "$1"); // Get file extension from path
        }

        response.setHeader("Content-Disposition", "attachment; filename=" + ext);
        response.setContentType((String) model.get(TYPE_ATTR));
    }
}
