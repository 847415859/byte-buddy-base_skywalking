package com.tk.skywalking.agent.plugin;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

/**
 * @Description: 对应skywalking-plugin.def文件中的一行
 * @Date : 2023/03/07 15:46
 * @Auther : tiankun
 */
@Slf4j
@Data
public class PluginDefine {
    /**
     * 插件名称,比如mysql-8.x
     */
    private String name;

    /**
     * 插件定义的全类名
     */
    private String defineClass;

    private PluginDefine(String name, String defineClass) {
        this.name = name;
        this.defineClass = defineClass;
    }

    /**
     * @param define skywalking-plugin.def文件中的一行
     * @return PluginDefine实例对象
     */
    public static PluginDefine build(String define) {
        if (StringUtils.isEmpty(define)) {
            throw new RuntimeException(define);
        }

        String[] pluginDefine = define.split("=");
        if (pluginDefine.length != 2) {
            throw new RuntimeException(define);
        }

        String pluginName = pluginDefine[0];
        String defineClass = pluginDefine[1];
        return new PluginDefine(pluginName, defineClass);
    }
}
