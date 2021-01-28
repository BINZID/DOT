package org.big.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *<p><b>测试用相关的Controller类</b></p>
 *<p> 测试用相关的Controller</p>
 * @author WangTianshan (王天山)
 *<p>Created date: 2017/9/1 21:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Controller
@RequestMapping("/test")
public class TestController {

    //test
    @RequestMapping(value="", method = {RequestMethod.GET})
    public String Test() {
        return "index";
    }
}
