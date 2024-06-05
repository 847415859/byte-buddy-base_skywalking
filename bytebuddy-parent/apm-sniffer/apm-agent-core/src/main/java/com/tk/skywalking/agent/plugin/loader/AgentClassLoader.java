package com.tk.skywalking.agent.plugin.loader;

import com.tk.skywalking.agent.boot.AgentPackagePath;
import com.tk.skywalking.agent.plugin.PluginBootstrap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @Description: 用于加载插件和插件的拦截器
 * @Date : 2023/03/07 15:37
 * @Auther : tiankun
 */
@Slf4j
public class AgentClassLoader extends ClassLoader{

    /**
     * 用于加载插件的定义相关的类(除了插件的interceptor),如 MysqlInstrumentation
     */
    private static AgentClassLoader DEFAULT_LOADER;
    /**
     * 自定义类加载器加载类的路径
     */
    private List<File> classpath;
    private List<Jar> allJars;
    private ReentrantLock jarScanLock = new ReentrantLock();

    public AgentClassLoader(ClassLoader parent) {
        super(parent);
        // 获取agent.jar的目录
        File agentJarDir = AgentPackagePath.getPath();
        // 获取agent.jar的目录下的plugins目录
        classpath = new LinkedList<>();
        classpath.add(new File(agentJarDir,"plugins"));
    }

    public static AgentClassLoader getDefault() {
        return DEFAULT_LOADER;
    }

    public static void initDefaultLoader() {
        if (DEFAULT_LOADER == null) {
            DEFAULT_LOADER = new AgentClassLoader(PluginBootstrap.class.getClassLoader());
        }
    }

    /**
     * loadClass --> 自动回调findClass(自定义自己的类加载逻辑)-->defineClass
     * @param name com.roadjava.skywalking.agent.demo.apm.plugins.springmvc.SpringmvcCommonInstrumentation
     */
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        List<Jar> allJars = getAllJars();
        // 转为/
        String path = name.replace(".", "/").concat(".class");
        for (Jar jar : allJars) {
            JarEntry jarEntry = jar.jarFile.getJarEntry(path);
            if (jarEntry == null) {
                continue;
            }
            try {
                URL url = new URL("jar:file:" + jar.sourceFile.getAbsolutePath() + "!/" + path);
                byte[] bytes = IOUtils.toByteArray(url);
                return defineClass(name,bytes,0,bytes.length);
            }catch (Exception e) {
                log.error("find class {} error",name);
            }
        }
        throw new ClassNotFoundException("can not find " + name);
    }

    @Override
    public URL getResource(String name) {
        List<Jar> allJars = getAllJars();
        for (Jar jar : allJars) {
            JarEntry jarEntry = jar.jarFile.getJarEntry(name);
            if (jarEntry != null) {
                try {
                    return new URL("jar:file:" + jar.sourceFile.getAbsolutePath() + "!/" + name);
                } catch (MalformedURLException e) {
                    log.error("getResource error",e);
                }
            }
        }
        return null;
    }

    @Override
    public Enumeration<URL> getResources(String name) throws IOException {
        List<URL> allResources = new LinkedList<>();
        List<Jar> allJars = getAllJars();
        for (Jar jar : allJars) {
            JarEntry jarEntry = jar.jarFile.getJarEntry(name);
            if (jarEntry != null) {
                allResources.add(new URL("jar:file:" + jar.sourceFile.getAbsolutePath() + "!/" + name));
            }
        }
        Iterator<URL> iterator = allResources.iterator();
        return new Enumeration<URL>() {
            @Override
            public boolean hasMoreElements() {
                return iterator.hasNext();
            }

            @Override
            public URL nextElement() {
                return iterator.next();
            }
        };
    }

    private List<Jar> getAllJars() {
        if (allJars == null) {
            jarScanLock.lock();
            try {
                // double check
                if (allJars == null) {
                    allJars = doGetJars();
                }
            }finally {
                jarScanLock.unlock();
            }
        }
        return allJars;
    }

    private List<Jar> doGetJars() {
        List<Jar> list = new LinkedList<>();
        // d:/xx/plugins
        for (File path : classpath) {
            if (path.exists() && path.isDirectory()) {
                String[] jarFileNames = path.list((dir, name) -> name.endsWith(".jar"));
                if (ArrayUtils.isEmpty(jarFileNames)) {
                    continue;
                }
                for (String jarFileName : jarFileNames) {
                    try {
                        File jarSourceFile = new File(path, jarFileName);
                        Jar jar = new Jar(new JarFile(jarSourceFile), jarSourceFile);
                        list.add(jar);
                        log.info("jar {} loaded",jarSourceFile.getAbsolutePath());
                    }catch (Exception e) {
                        log.error("jar {} load fail",jarFileName,e);
                    }

                }
            }
        }
        return list;
    }

    @RequiredArgsConstructor
    private static class Jar {
        /**
         * jar文件对应的jarFile对象
         */
        private final JarFile jarFile;
        /**
         * jar文件对象
         */
        private final File sourceFile;
    }

}
