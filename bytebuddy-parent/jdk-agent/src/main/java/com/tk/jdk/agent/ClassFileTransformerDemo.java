package com.tk.jdk.agent;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import lombok.extern.slf4j.Slf4j;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * @Description:
 * @Date : 2023/03/05 10:36
 * @Auther : tiankun
 */
@Slf4j
public class ClassFileTransformerDemo implements ClassFileTransformer {
    /**
     * 当某个类将要被加载之前都会先进入到此方法
     * @param loader
     * @param className 将要加载的类的全限定名，如：com/tk/app/service/HelloService.java
     * @param classBeingRedefined
     * @param protectionDomain
     * @param classfileBuffer
     * @return  需要增强就返回增强后的字节码，如果不需要增强返回Null即可。
     * @throws IllegalClassFormatException
     */
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        byte[] bytes = null;
        if("com/tk/app/service/HelloService".equals(className)) {
            // 使用 asm javassist cglib bytebuddy 都可以对类进行增强
            // 下面使用javassist增强
            ClassPool classPool = ClassPool.getDefault();
            try {
                CtClass ctClass = classPool.get("com.tk.app.service.HelloService");
                CtMethod ctMethod = ctClass.getDeclaredMethod("say", new CtClass[]{classPool.get("java.lang.String")});
                ctMethod.insertBefore("{System.out.println(\"before say\");}");
                // 获取修改后的字节码
                bytes = ctClass.toBytecode();
                log.info("transformed class:{}",className);
            }catch (Exception e){
                log.error("transform error:{}",e);
            }
        }
        return bytes;
    }
}
