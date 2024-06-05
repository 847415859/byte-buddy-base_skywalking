package com.tk.skywalking.agent.boot;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.net.URL;

/**
 * @Description:
 * @Date : 2023/03/07 15:42
 * @Auther : tiankun
 */
@Slf4j
public class AgentPackagePath {
    /**
     * apm-agent.jar所在目录的file对象
     */
    private static File AGENT_PACKAGE_PATH;

    public static File getPath() {
        if (AGENT_PACKAGE_PATH == null) {
            AGENT_PACKAGE_PATH = findPath();
        }
        return AGENT_PACKAGE_PATH;
    }

    /**
     * apm-agent.jar所在目录的file对象
     */
    private static File findPath() {
        // com/roadjava/skywalking/demo/apm/agent/core/boot/AgentPackagePath.class
        String classResourcePath = AgentPackagePath.class.getName().replaceAll("\\.", "/") + ".class";
        URL resource = ClassLoader.getSystemClassLoader().getResource(classResourcePath);
        // jar:file:/D:/Studynotes/self-study/ssjava/24.APM/Skywalking/SourceCode/byte-buddy-base_skywalking/bytebuddy-parent/dist/apm-agent-1.0-SNAPSHOT-jar-with-dependencies.jar!/com/tk/skywalking/agent/boot/AgentPackagePath.class
        if (resource != null) {
            String urlString = resource.toString();

            log.info("The beacon class location is {}.", urlString);
            // 判断是否是jar包
            boolean isInJar = urlString.indexOf('!') > -1;

            if (isInJar) {
                // /D:/Studynotes/self-study/ssjava/24.APM/Skywalking/SourceCode/byte-buddy-base_skywalking/bytebuddy-parent/dist/apm-agent-1.0-SNAPSHOT-jar-with-dependencies.jar
                urlString = StringUtils.substringBetween(urlString,"file:","!");
                File agentJarFile = null;
                try {
                    agentJarFile = new File(urlString);
                } catch (Exception e) {
                    log.error("Can not locate agent jar file by url:{}",urlString,e);
                }
                if (agentJarFile.exists()) {
                    return agentJarFile.getParentFile();
                }
            }
        }
        log.error("Can not locate agent jar file.");
        throw new RuntimeException("Can not locate agent jar file.");
    }
}
