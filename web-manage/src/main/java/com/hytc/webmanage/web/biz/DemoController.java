package com.hytc.webmanage.web.biz;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * 準備中画面
 */
@Controller
@Deprecated
public class DemoController extends FwBaseController {

    @GetMapping("/demo")
    public String demo(@ModelAttribute("funcId") String gm, @ModelAttribute("funcSubId") String lm) {
        return "/demo/demo.html";
    }
}
