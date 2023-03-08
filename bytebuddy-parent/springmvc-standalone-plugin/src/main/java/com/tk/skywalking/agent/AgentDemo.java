package com.tk.skywalking.agent;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.agent.builder.AgentBuilder;

import java.lang.instrument.Instrumentation;

import static net.bytebuddy.matcher.ElementMatchers.*;

/**
 * @Description: 实现功能：拦截SpringMVC请求，并计算出耗时
 * @Date : 2023/03/05 11:40
 * @Auther : tiankun
 */
@Slf4j
public class AgentDemo {
    // @Controller 注解全限定类名
    private static final String CONTROLLER_NAME = "org.springframework.stereotype.Controller";
    // @RestController 注解全限定类名
    private static final String REST_CONTROLLER_NAME = "org.springframework.web.bind.annotation.RestController";
    /**
     * 在main方法之前会被自动调用，是插桩的入口
     * @param args
     * @param instrumentation
     */
    public static void premain(String args, Instrumentation instrumentation){
        log.info("进入到premain, args：{}",args);
        AgentBuilder builder = new AgentBuilder.Default()
                // 配置忽略拦截的包
                .ignore(nameStartsWith("net.bytebuddy")
                        .or(nameStartsWith("org.springframework"))
                )
                // 配置哪些类需要拦截,什么时候会进入到这个方法呢?
                // 当类第一次要被加载的时候会进入到此方法
                .type(isAnnotatedWith(named(CONTROLLER_NAME).or(named(REST_CONTROLLER_NAME))))
                .transform(new AgentTransformer());
        builder.installOn(instrumentation);
    }
}
