package com.tk.skywalking.agent.plugin;

import com.tk.skywalking.agent.plugin.interceptor.ConstructorMethodsInterceptorPoint;
import com.tk.skywalking.agent.plugin.interceptor.InstanceMethodsInterceptorPoint;
import com.tk.skywalking.agent.plugin.interceptor.StaticMethodsInterceptorPoint;
import com.tk.skywalking.agent.plugin.match.ClassMatch;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;

/**
 * @Description:  在Skywalking源码中基本上说有插件都要直接或间接的继承该类
 * @Date : 2023/03/06 10:08
 * @Auther : tiankun
 */
@Slf4j
public abstract class AbstractClassEnhancePluginDefine {

    /**
     * 获取到当前插件需要增强的类的匹配规则
     * @return
     */
    protected abstract ClassMatch getEnhanceClass();

    /**
     * 实例方法的拦截点
     * @return
     */
    protected abstract InstanceMethodsInterceptorPoint[] getInstanceMethodsInterceptorPoints();

    /**
     * 构造方法的拦截点
     * @return
     */
    protected abstract ConstructorMethodsInterceptorPoint[] getConstructorMethodsInterceptorPoints();

    /**
     * 静态方法的拦截点
     * @return
     */
    protected abstract StaticMethodsInterceptorPoint[] getStaticeMethodsInterceptorPoints();

    /**
     * 获取到增强的Method
     * @param typeDescription
     * @param builder
     * @param classLoader
     * @return
     */
    public DynamicType.Builder<?> define(TypeDescription typeDescription, DynamicType.Builder<?> builder, ClassLoader classLoader,EnhanceContext enhanceContext){
        // com.tk.skywalking.agent.mysql.MysqlInstrumentation
        String pluginDefineClassName = this.getClass().getName();
        String typeName = typeDescription.getTypeName();
        log.info("开始使用{}增强{}",pluginDefineClassName,typeName);
        DynamicType.Builder<?> newBuilder = this.enhance(typeDescription,builder,classLoader,enhanceContext);
        // 设置为已进行增强处理
        enhanceContext.initializationStageCompleted();
        log.info("使用{}增强{}结束",pluginDefineClassName,typeName);
        return newBuilder;
    }

    private DynamicType.Builder<?> enhance(TypeDescription typeDescription, DynamicType.Builder<?> builder, ClassLoader classLoader,EnhanceContext enhanceContext) {
        // 增强静态方法
        builder = this.enhanceClass(typeDescription,builder,classLoader);
        // 增加实例方法和构造方法
        builder = this.enhanceInstance(typeDescription,builder,classLoader,enhanceContext);
        return builder;
    }

    /**
     * 增强实例方法和构造方法
     * @param typeDescription
     * @param builder
     * @param classLoader
     * @return
     */
    protected abstract DynamicType.Builder<?> enhanceInstance(TypeDescription typeDescription, DynamicType.Builder<?> builder, ClassLoader classLoader,EnhanceContext enhanceContext);

    /**
     * 增强静态方法
     * @param typeDescription
     * @param builder
     * @param classLoader
     * @return
     */
    protected abstract DynamicType.Builder<?> enhanceClass(TypeDescription typeDescription, DynamicType.Builder<?> builder, ClassLoader classLoader);
}
