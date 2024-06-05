package com.tk.app;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 单独生效：
 * -javaagent:D:\Studynotes\self-study\ssjava\24.APM\Skywalking\SourceCode\byte-buddy-base_skywalking\bytebuddy-parent\springmvc-standalone-plugin\target\springmvc-standalone-plugin-1.0-SNAPSHOT-jar-with-dependencies.jar
 * -javaagent:D:\Studynotes\self-study\ssjava\24.APM\Skywalking\SourceCode\byte-buddy-base_skywalking\bytebuddy-parent\mysql-standalone-plugin\target\mysql-standalone-plugin-1.0-SNAPSHOT-jar-with-dependencies.jar
 * 同时生效：
 * 可以指定多个-javaagent：用逗号隔开 格式：-javaagent:xx.jar -javaagent:xxx.jar
 *
 * 自己实现可插拔式的agnet：
 * -javaagent:D:\Studynotes\self-study\ssjava\24.APM\Skywalking\SourceCode\byte-buddy-base_skywalking\bytebuddy-parent\dist\apm-agent-1.0-SNAPSHOT-jar-with-dependencies.jar
 *
 */
@SpringBootApplication
@MapperScan(value = "com.tk.app.mapper")
public class AppApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);
    }

}
