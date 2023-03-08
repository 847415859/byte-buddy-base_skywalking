package com.tk.skywalking.agent.plugin;

import com.tk.skywalking.agent.plugin.loader.AgentClassLoader;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @Description:
 * @Date : 2023/03/07 15:45
 * @Auther : tiankun
 */
@Slf4j
public class PluginResourcesResolver {
    /**
     * 获取插件目录(/plugins)下所有jar内的skywalking-plugin.def文件的url
     * @return
     */
    public List<URL> getResources() {
        List<URL> cfgUrlPaths = new ArrayList<>();
        try {
            Enumeration<URL> urls = AgentClassLoader.getDefault().getResources("skywalking-plugin.def");
            while (urls.hasMoreElements()) {
                URL pluginDefineDefUrl = urls.nextElement();
                cfgUrlPaths.add(pluginDefineDefUrl);
                log.info("find skywalking plugin define file url :{}",pluginDefineDefUrl);
            }
            return cfgUrlPaths;
        }catch (Exception e) {
            log.error("read resource error",e);
        }
        return null;
    }
}
