package com.hytc.webmanage.web.ajax;

import javax.servlet.http.HttpSession;

import com.hytc.webmanage.web.common.WebConstants;
import com.hytc.webmanage.web.config.MappingMaster;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class MenuController {

    /**
     * メニュー切り替え
     *
     * @param gmMulti
     */
    @PostMapping(MappingMaster.OPEN_MENU)
    @ResponseBody
    public String menuOpen(@RequestParam String gmMulti, @RequestParam(required = false) String openFlg, final HttpSession session) {
        // 開くメニュー複数指定
        String[] gmArr = gmMulti.split(",", -1);
        if (openFlg != null) {
            for (String gm : gmArr) {
                session.setAttribute(WebConstants.OPEN_MENU + gm, gm);
            }
            return "{}";
        }
        for (String gm : gmArr) {
            Object openMenu = session.getAttribute(WebConstants.OPEN_MENU + gm);
            if (openMenu != null) {
                session.removeAttribute(WebConstants.OPEN_MENU + gm);
            } else {
                session.setAttribute(WebConstants.OPEN_MENU + gm, gm);
            }
        }
        return "{}";
    }
}
