package com.tk.skywalking.agent.plugin;

import com.tk.skywalking.agent.plugin.loader.AgentClassLoader;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Date : 2023/03/07 15:43
 * @Auther : tiankun
 */
@Slf4j
public class PluginBootstrap {
    /**
     * 加载所有生效的插件,因为是自定义的路径下的jar:
     * 1. 先获取到agent.jar的路径
     * 2. 使用自定义类加载器进行加载插件
     */
    public List<AbstractClassEnhancePluginDefine> loadPlugins() {
        AgentClassLoader.initDefaultLoader();
        PluginResourcesResolver resourcesResolver = new PluginResourcesResolver();
        List<URL> resources = resourcesResolver.getResources();
        if (resources == null || resources.size() == 0) {
            log.info("no skywalking-plugin.def found");
            return new ArrayList<>();
        }
        for (URL resource : resources) {
            try {
                PluginCfg.INSTANCE.load(resource.openStream());
            }catch (Exception e) {
                log.error("plugin def file {} init fail",resource,e);
            }
        }
        List<PluginDefine> pluginClassList = PluginCfg.INSTANCE.getPluginClassList();
        log.info("loaded Plugin Class : {}",pluginClassList);
        // 拿到全类名通过反射获取到对象,这个对象就是插件定义对象
        List<AbstractClassEnhancePluginDefine> plugins = new ArrayList<>();
        for (PluginDefine pluginDefine : pluginClassList) {
            try {
                AbstractClassEnhancePluginDefine plugin = (AbstractClassEnhancePluginDefine)Class.forName(pluginDefine.getDefineClass(),
                        true, AgentClassLoader.getDefault()).newInstance();
                plugins.add(plugin);
            } catch (Exception e) {
                log.error("load class {} error",pluginDefine.getDefineClass(),e);
            }
        }
        return plugins;
    }
}
