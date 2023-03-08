package com.tk.skywalking.agent.springmvc;

import com.tk.skywalking.agent.plugin.match.ClassAnnotationNameMatch;
import com.tk.skywalking.agent.plugin.match.ClassMatch;

/**
 * @Description: Controller
 * @Date : 2023/03/08 9:11
 * @Auther : tiankun
 */
public class ControllerInstrumentation extends SpringMvcInstrumentation{

    // @Controller 注解全限定类名
    private static final String CONTROLLER_NAME = "org.springframework.stereotype.Controller";

    @Override
    protected ClassMatch getEnhanceClass() {
        return ClassAnnotationNameMatch.byMultiAnnotationMatch(CONTROLLER_NAME);
    }
}
