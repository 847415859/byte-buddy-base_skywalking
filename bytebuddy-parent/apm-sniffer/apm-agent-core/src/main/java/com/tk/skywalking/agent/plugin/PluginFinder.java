package com.tk.skywalking.agent.plugin;

import com.tk.skywalking.agent.plugin.match.ClassMatch;
import com.tk.skywalking.agent.plugin.match.IndirectMatch;
import com.tk.skywalking.agent.plugin.match.NameMatch;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.description.NamedElement;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import java.util.*;

import static net.bytebuddy.matcher.ElementMatchers.isInterface;
import static net.bytebuddy.matcher.ElementMatchers.not;

/**
 * @Description: 加载插件类
 * @Date : 2023/03/07 14:06
 * @Auther : tiankun
 */
@Slf4j
public class PluginFinder {
    /**
     * 用于存储ClassMatch类型为NameMatch的插件
     * key: 全类名 ,如: com.roadjava.skywalking.agent.demo.app.service.UserInfoService
     * value: 说明同一个类可以同时被多个plugin进行增强
     */
    private final Map<String, LinkedList<AbstractClassEnhancePluginDefine>> nameMatchDefine = new HashMap<>();
    /**
     * 用于存储ClassMatch类型为IndirectMatch的插件
     */
    private final List<AbstractClassEnhancePluginDefine> signatureMatchDefine = new ArrayList<>();
    /**
     * 对插件进行分类
     * @param plugins 加载到的所有的插件
     */
    public PluginFinder(List<AbstractClassEnhancePluginDefine> plugins) {
        for (AbstractClassEnhancePluginDefine plugin : plugins) {
            ClassMatch classMatch = plugin.getEnhanceClass();
            if (classMatch == null) {
                continue;
            }
            if (classMatch instanceof NameMatch) {
                NameMatch nameMatch = (NameMatch) classMatch;
                LinkedList<AbstractClassEnhancePluginDefine> list = nameMatchDefine.computeIfAbsent(nameMatch.getClassName(), a -> new LinkedList<>());
                list.add(plugin);
            }else {
                signatureMatchDefine.add(plugin);
            }
        }
    }


    /**
     * 返回已加载的所有插件最终拼接后的条件
     * @return  plugin1_junction.or(plugin2_junction).or(plugin3_junction)
     */
    public ElementMatcher<? super TypeDescription> buildMatch() {
        ElementMatcher.Junction<? super TypeDescription> junction = new ElementMatcher.Junction.AbstractBase<NamedElement>() {
            @Override
            public boolean matches(NamedElement target) {
                // 当某个类第一次被加载时都会回调至此 a.b.C
                return nameMatchDefine.containsKey(target.getActualName());
            }
        };
        // 只增强类,排除接口
        junction = junction.and(not(isInterface()));
        for (AbstractClassEnhancePluginDefine pluginDefine : signatureMatchDefine) {
            ClassMatch classMatch = pluginDefine.getEnhanceClass();
            if (classMatch instanceof IndirectMatch) {
                IndirectMatch indirectMatch = (IndirectMatch) classMatch;
                junction = junction.or(indirectMatch.buildJunction());
            }
        }
        return junction;
    }


    /**
     *
     * @param typeDescription 当前匹配到的类
     * @return typeDescription 对应的插件集合
     */
    public List<AbstractClassEnhancePluginDefine> find(TypeDescription typeDescription) {

        List<AbstractClassEnhancePluginDefine> matchedPlugins = new LinkedList<>();
        // 获取到全类名
        String typeName = typeDescription.getTypeName();
        // 处理nameMatchDefine
        if (nameMatchDefine.containsKey(typeName)) {
            matchedPlugins.addAll(nameMatchDefine.get(typeName));
        }
        // 处理signatureMatchDefine
        for (AbstractClassEnhancePluginDefine pluginDefine : signatureMatchDefine) {
            IndirectMatch indirectMatch = (IndirectMatch) pluginDefine.getEnhanceClass();
            if (indirectMatch.isMatch(typeDescription)) {
                matchedPlugins.add(pluginDefine);
            }
        }
        return matchedPlugins;
    }
}
