package com.tk.skywalking.agent.plugin;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Date : 2023/03/07 15:46
 * @Auther : tiankun
 */
@Slf4j
public enum PluginCfg {
    INSTANCE;

    /**
     * 存放所有插件.def文件构造出来的PluginDefine实例
     */
    private List<PluginDefine> pluginClassList = new ArrayList<>();

    /**
     * 转换.def文件的内容为PluginDefine
     * @param input
     * @throws IOException
     */
    void load(InputStream input) throws IOException {
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
            String pluginDefine;
            while ((pluginDefine = reader.readLine()) != null) {
                try {
                    if (pluginDefine.trim().length() == 0 || pluginDefine.startsWith("#")) {
                        continue;
                    }
                    PluginDefine plugin = PluginDefine.build(pluginDefine);
                    pluginClassList.add(plugin);
                } catch (Exception e) {
                    log.error("Failed to format plugin({}) define.", pluginDefine,e);
                }
            }
        }
    }

    public List<PluginDefine> getPluginClassList() {
        return pluginClassList;
    }

}

