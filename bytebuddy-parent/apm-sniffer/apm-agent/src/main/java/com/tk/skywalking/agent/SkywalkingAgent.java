package com.tk.skywalking.agent;

import com.tk.skywalking.agent.plugin.AbstractClassEnhancePluginDefine;
import com.tk.skywalking.agent.plugin.EnhanceContext;
import com.tk.skywalking.agent.plugin.PluginBootstrap;
import com.tk.skywalking.agent.plugin.PluginFinder;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.scaffold.TypeValidation;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.utility.JavaModule;

import java.lang.instrument.Instrumentation;
import java.nio.ByteBuffer;
import java.security.ProtectionDomain;
import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.named;

/**
 * @Description:
 * @Date : 2023/03/05 17:32
 * @Auther : tiankun
 */
@Slf4j
public class SkywalkingAgent {

    /**
     * 在main方法之前会被自动调用，是插桩的入口
     * @param args
     * @param instrumentation
     */
    public static void premain(String args, Instrumentation instrumentation){
        log.info("进入到premain, args：{}",args);
        PluginFinder pluginFinder = null;
        try {
            pluginFinder = new PluginFinder(new PluginBootstrap().loadPlugins());
            ElementMatcher<? super TypeDescription> elementMatcher = pluginFinder.buildMatch();
            log.info("ElementMatcher<? super TypeDescription> elementMatcher :{}",elementMatcher);
        }catch (Exception e){
            log.error("初始化失败：{}",e);
            return;
        }
        // 设置是否需要检验字节码，默认是
        ByteBuddy byteBuddy = new ByteBuddy().with(TypeValidation.ENABLED);
        AgentBuilder builder = new AgentBuilder.Default(byteBuddy)
                .type(pluginFinder.buildMatch())
                .transform(new Transformer(pluginFinder));
        builder.installOn(instrumentation);
    }

    private static class Transformer implements AgentBuilder.Transformer{

        private PluginFinder pluginFinder;

        public Transformer(PluginFinder pluginFinder) {
            this.pluginFinder = pluginFinder;
        }

        @Override
        public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription,
                                                // 加载 typeDescription 这个类的加载器（被拦截类的类加载器）
                                                ClassLoader classLoader,
                                                JavaModule module, ProtectionDomain protectionDomain) {
            log.info("actualName to transform:{}", typeDescription.getActualName());
            List<AbstractClassEnhancePluginDefine> pluginDefines = pluginFinder.find(typeDescription);
            log.info("获取到插件定义为：{}",pluginDefines);
            if (pluginDefines.size() > 0) {
                DynamicType.Builder<?> newBuilder = builder;
                EnhanceContext context = new EnhanceContext();
                for (AbstractClassEnhancePluginDefine pluginDefine : pluginDefines) {
                    DynamicType.Builder<?> possibleNewBuilder = pluginDefine.define(typeDescription,newBuilder,classLoader,context);
                    if (possibleNewBuilder != null) {
                        newBuilder = possibleNewBuilder;
                    }
                }
                if (context.isEnhanced()) {
                    log.debug("finish the enhance for {}",typeDescription.getTypeName());
                }
                return newBuilder;
            }
            log.debug("匹配到了类:{},但是未find到插件集合",typeDescription.getActualName());
            return builder;

//            DynamicType.Builder<?> newBuilder = builder
                    /*
                        // springmvc
                        not(isStatic()).and(isAnnotatedWith(
                                nameStartsWith(MAPPING_PKG_PREFIX).and(nameEndsWith(MAPPING_SUFFIX))
                        ))
                        // mysql
                        .or(named("execute")
                                .or(named("executeUpdate"))
                                .or(named("executeQuery")))
                        // es
                        .or(xx)
                     */
//                    .method()
                    /*
                        // springmvc
                        new SpringMvcInterceptor()
                        // mysql
                        new MysqlInterceptor()
                        // es
                        new EsInterceptor()
                     */
//                    .intercept(MethodDelegation.to(null));
//            return newBuilder;
        }
    }

}


