package com.tk.skywalking.agent.springmvc;

import com.tk.skywalking.agent.plugin.AbstractClassEnhancePluginDefine;
import com.tk.skywalking.agent.plugin.interceptor.ConstructorMethodsInterceptorPoint;
import com.tk.skywalking.agent.plugin.interceptor.InstanceMethodsInterceptorPoint;
import com.tk.skywalking.agent.plugin.interceptor.StaticMethodsInterceptorPoint;
import com.tk.skywalking.agent.plugin.interceptor.enhance.ClassEnhancePluginDefine;
import com.tk.skywalking.agent.plugin.match.ClassAnnotationNameMatch;
import com.tk.skywalking.agent.plugin.match.ClassMatch;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.matcher.ElementMatcher;

import static net.bytebuddy.matcher.ElementMatchers.*;

/**
 * @Description: SpringMVC插件插桩
 * @Date : 2023/03/06 16:41
 * @Auther : tiankun
 */
public abstract class SpringMvcInstrumentation extends ClassEnhancePluginDefine {

    private static final String MAPPING_PKG_PREFIX = "org.springframework.web.bind.annotation";
    private static final String MAPPING_SUFFIX = "Mapping";

    private static final String INTERCEPTOR_CLASS_NAME = "com.tk.skywalking.agent.springmvc.interceptor.SpringMvcInterceptor";

    @Override
    protected InstanceMethodsInterceptorPoint[] getInstanceMethodsInterceptorPoints() {
        return new InstanceMethodsInterceptorPoint[]{
                new InstanceMethodsInterceptorPoint() {
                    @Override
                    public ElementMatcher<MethodDescription> getMethodsMatcher() {
                        return  not(isStatic()).and(isAnnotatedWith(
                                nameStartsWith(MAPPING_PKG_PREFIX).and(nameEndsWith(MAPPING_SUFFIX))
                            ));
                    }
                    @Override
                    public String getMethodsInterceptor() {
                        return INTERCEPTOR_CLASS_NAME;
                    }
                }
        };
    }

    @Override
    protected ConstructorMethodsInterceptorPoint[] getConstructorMethodsInterceptorPoints() {
        return new ConstructorMethodsInterceptorPoint[0];
    }

    @Override
    protected StaticMethodsInterceptorPoint[] getStaticeMethodsInterceptorPoints() {
        return new StaticMethodsInterceptorPoint[0];
    }
}
