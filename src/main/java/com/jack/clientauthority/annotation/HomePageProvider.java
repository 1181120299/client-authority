package com.jack.clientauthority.annotation;

import org.springframework.web.bind.annotation.GetMapping;

/**
 * 定义应用主页
 * <p></p>
 *
 * 实现此接口的类，必须是一个{@link org.springframework.stereotype.Controller}。
 * 接口的方法，需要提供请求路径映射。例如：
 * <blockquote>
 *     <pre>
 *          &#064;GetMapping("/homePage")
 *          public String homePage() {}
 *     </pre>
 * </blockquote>
 *
 * @see com.jack.clientauthority.annotation.defaultImpl.DefaultHomePageController
 */
public interface HomePageProvider {

    /**
     * 应用首页访问地址。
     * 前后端分离的项目，可以重定向到首页。如果是thymeleaf等模板引擎渲染页面，
     * 则返回首页文件路径。
     * @return  应用首页
     */
    @GetMapping("/")
    String homePage();
}
