package org.big.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

import static org.springframework.web.servlet.DispatcherServlet.LOCALE_RESOLVER_ATTRIBUTE;
import static org.springframework.web.servlet.support.RequestContextUtils.getLocaleResolver;

/**
 *<p><b>LanguageController类</b></p>
 *<p> Language的Controller</p>
 * @author WangTianshan (王天山)
 *<p>Created date: 2017/11/3 14:13</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@RestController  //返回json
@RequestMapping("/language")
public class LanguageController extends WebMvcConfigurerAdapter {

    public static Locale getLocale (HttpServletRequest request) {
        LocaleResolver localeResolver = getLocaleResolver (request);
        if (localeResolver != null ) {
            return localeResolver.resolveLocale(request);
        }
        else {
            return request.getLocale();
        }
    }

    //English
    @RequestMapping("/en")
    public void en(HttpServletRequest request,HttpServletResponse response) {
        Locale this_locale = getLocale(request);
        LocaleResolver localeResolver = getLocaleResolver (request);
        localeResolver.setLocale(request,response,Locale.ENGLISH);
        request.setAttribute(LOCALE_RESOLVER_ATTRIBUTE, localeResolver);
//        System.out.println("================");
//        System.out.println(this_locale+"--->"+ LocaleContextHolder.getLocale());
//        System.out.println("================");
    }

    //中文-简体
    @RequestMapping("/zh_CN")
    public void zh_CN(HttpServletRequest request,HttpServletResponse response) {
        Locale this_locale = getLocale(request);
        LocaleResolver localeResolver = getLocaleResolver (request);
        localeResolver.setLocale(request,response,Locale.CHINESE);
        request.setAttribute(LOCALE_RESOLVER_ATTRIBUTE, localeResolver);
//        System.out.println("================");
//        System.out.println(this_locale+"--->"+LocaleContextHolder.getLocale());
//        System.out.println("================");
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        //设置默认语言
        //En
        //slr.setDefaultLocale(Locale.ENGLISH);
        //中文
        //slr.setDefaultLocale(Locale.CHINESE);
        return slr;
    }
}