package com.tk.skywalking.agent.mysql;

import com.tk.skywalking.agent.plugin.AbstractClassEnhancePluginDefine;
import com.tk.skywalking.agent.plugin.EnhanceContext;
import com.tk.skywalking.agent.plugin.interceptor.ConstructorMethodsInterceptorPoint;
import com.tk.skywalking.agent.plugin.interceptor.InstanceMethodsInterceptorPoint;
import com.tk.skywalking.agent.plugin.interceptor.StaticMethodsInterceptorPoint;
import com.tk.skywalking.agent.plugin.interceptor.enhance.ClassEnhancePluginDefine;
import com.tk.skywalking.agent.plugin.match.ClassMatch;
import com.tk.skywalking.agent.plugin.match.MultiClassNameMatch;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.matcher.ElementMatcher;

import static net.bytebuddy.matcher.ElementMatchers.named;

/**
 * @Description: Mysql插件插桩
 * @Date : 2023/03/06 12:30
 * @Auther : tiankun
 */
public class MysqlInstrumentation extends ClassEnhancePluginDefine {

    private static final String SERVER_PS_NAME = "com.mysql.cj.jdbc.ServerPreparedStatement";
    private static final String CLIENT_PS_NAME = "com.mysql.cj.jdbc.ClientPreparedStatement";

    private static final String INTERCEPTOR_CLASS_NAME = "com.tk.skywalking.agent.mysql.intercepter.MysqlInterceptor";

    @Override
    protected ClassMatch getEnhanceClass() {
        return MultiClassNameMatch.byMultiClassMatch(SERVER_PS_NAME,CLIENT_PS_NAME);
    }

    @Override
    protected InstanceMethodsInterceptorPoint[] getInstanceMethodsInterceptorPoints() {
        return new InstanceMethodsInterceptorPoint[]{
            new InstanceMethodsInterceptorPoint() {
                // 获取需要拦截的类的匹配规则
                @Override
                public ElementMatcher<MethodDescription> getMethodsMatcher() {
                    return  named("execute")
                            .or(named("executeQuery"))
                            .or(named("executeUpdate"));
                }
                // 获取插件拦截器
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
