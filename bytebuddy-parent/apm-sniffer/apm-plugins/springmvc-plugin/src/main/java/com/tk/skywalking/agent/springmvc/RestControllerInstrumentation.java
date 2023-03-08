package com.tk.skywalking.agent.springmvc;

import com.tk.skywalking.agent.plugin.match.ClassAnnotationNameMatch;
import com.tk.skywalking.agent.plugin.match.ClassMatch;

/**
 * @Description:
 * @Date : 2023/03/08 9:12
 * @Auther : tiankun
 */
public class RestControllerInstrumentation extends SpringMvcInstrumentation{

    // @RestController 注解全限定类名
    private static final String REST_CONTROLLER_NAME = "org.springframework.web.bind.annotation.RestController";

    @Override
    protected ClassMatch getEnhanceClass() {
        return ClassAnnotationNameMatch.byMultiAnnotationMatch(REST_CONTROLLER_NAME);
    }
}
