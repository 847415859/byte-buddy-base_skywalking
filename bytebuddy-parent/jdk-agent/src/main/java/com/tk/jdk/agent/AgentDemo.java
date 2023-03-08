package com.tk.jdk.agent;

import lombok.extern.slf4j.Slf4j;

import java.lang.instrument.Instrumentation;


/**
 * @Description:
 *  java -javaagent:D:\Develop\WorkSpace\2023\bytebuddy-parent\jdk-agent\target\jdk-agent-1.0-SNAPSHOT.jar=k1=v1,k2=v2 -jar xxx.jar
 *  =k1=v1,k2=v2：表示通过命令行给传参
 * @Date : 2023/03/05 10:29
 * @Auther : tiankun
 */
@Slf4j
public class AgentDemo {

    /**
     * 在main方法之前会被自动调用，是插桩的入口
     * @param args
     * @param instrumentation
     */
    public static void premain(String args, Instrumentation instrumentation){
        log.info("进入到premain, args：{}",args);
        instrumentation.addTransformer(new ClassFileTransformerDemo());
    }
}
