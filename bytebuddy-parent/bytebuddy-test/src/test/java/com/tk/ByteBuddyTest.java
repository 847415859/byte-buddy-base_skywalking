package com.tk;


import cn.hutool.core.util.IdUtil;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.NamingStrategy;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.dynamic.scaffold.TypeValidation;
import net.bytebuddy.implementation.*;
import net.bytebuddy.implementation.bind.annotation.Morph;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import org.junit.Test;

import java.beans.MethodDescriptor;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @Description:
 * @Date : 2023/03/04 14:37
 * @Auther : tiankun
 */
public class ByteBuddyTest {

    public static final String CLASS_PATH = ByteBuddyTest.class.getClassLoader().getResource("").getPath();

    /**
     * 创建一个类
     */
    @Test
    public void testCreateClass() throws IOException {
        // 默认是 ByteBuddy
        NamingStrategy.SuffixingRandom tkSuffix = new NamingStrategy.SuffixingRandom("TkSuffix");
        // Unloaded 表示生成的字节码还未加载到JVM
        DynamicType.Unloaded<UserMapper> unloaded = new ByteBuddy()
                // 设置是否需要检验字节码，默认是：
                .with(TypeValidation.ENABLED)
                // 指定生成的类的命名策略
                .with(tkSuffix)
                // 指定父类
                .subclass(UserMapper.class)
                /*
                  生成类的命名规则为：
                    在不指定命名规则的情况下：
                        1.对于父类是Jdk自带的类的情况 : net.bytebuddy.renamed.java.lang.Object$ByteBuddy$9pRDSD4K
                        2.对于父类不是Jdk自带的类的情况 : com.tk.UserMapper$ByteBuddy$LrEgvtdu
                    在指定命名规则的情况下：
                        1.对于父类是Jdk自带的类的情况 : net.bytebuddy.renamed.java.lang.Object$TkSuffix$9pRDSD4K
                        2.对于父类不是Jdk自带的类的情况 : com.tk.UserMapper$TkSuffix$Vq7RR1ks
                 */
                // 指定具体的类目（包名+类名）  ByteBuddy会对我们指定的名字会做很多的校验，如果不要则关闭校验即可。
                .name("a.b.SubObj")
                .make();
        // 获取生成的字节码
        byte[] bytes = unloaded.getBytes();
        // 把生成的类字节码文件保存在那个目录
        unloaded.saveIn(new File(CLASS_PATH));
    }


    /**
     * 对实例方法进行插桩
     */
    @Test
    public void testInstrument() throws IOException, InstantiationException, IllegalAccessException {
        // Unloaded 表示生成的字节码还未加载到JVM
        DynamicType.Unloaded<UserMapper> uploaded = new ByteBuddy()
                .subclass(UserMapper.class)
                .name("a.b.SubObj")
                // named 通过名字指定要拦截的方法
                .method(ElementMatchers.named("toString"))
                // 指定拦截到方法后该如何处理
                .intercept(FixedValue.value("Hello ByteBuddy"))
                .make();
        // Loaded 表示生成的字节码已经加载到JVM
        DynamicType.Loaded<UserMapper> loaded = uploaded.load(this.getClass().getClassLoader());
        Class<? extends UserMapper> loadedClass = loaded.getLoaded();
        UserMapper userMapper = loadedClass.newInstance();
        String toStringResult = userMapper.toString();
        System.out.println("loadedClass.getClassLoader() = " + loadedClass.getClassLoader().toString());
        System.out.println("loadedClass.toStringResult = " + toStringResult);
        uploaded.saveIn(new File(CLASS_PATH));
        // loaded 同样也有 saveIn getBytes, inject方法，和 Unloaded类继承自DynamicType
        // loaded.saveIn(new File(CLASS_PATH));
    }


    /**
     * 动态增强的三种方式：
     * 1. subclass
     * 2. rebase： 变基，效果是保留原方法并重命名为xx&original();  xx为拦截后的逻辑
     * @throws IOException
     */
    @Test
    public void testDynamicEnhanceSubClass() throws IOException {
        DynamicType.Unloaded<UserMapper> unloaded = new ByteBuddy()
                .subclass(UserMapper.class)
                .name("a.b.SubObj")
                .method(ElementMatchers.named("selectUsername")
                        .and(
                                // 返回值类型是 Class || Object || String
                                ElementMatchers.returns(TypeDescription.CLASS)
                                        .or(ElementMatchers.returns(TypeDescription.OBJECT))
                                        .or(ElementMatchers.returns(TypeDescription.STRING))
                        ))
                .intercept(FixedValue.nullValue())
                .make();
        unloaded.saveIn(new File(CLASS_PATH));
    }


    @Test
    public void testDynamicEnhanceRebase() throws IOException {
        DynamicType.Unloaded<UserMapper> unloaded = new ByteBuddy()
                .rebase(UserMapper.class)
                .name("a.b.SubObj")
                .method(ElementMatchers.named("selectUsername")
                        .and(
                                // 返回值类型是 Class || Object || String
                                ElementMatchers.returns(TypeDescription.CLASS)
                                        .or(ElementMatchers.returns(TypeDescription.OBJECT))
                                        .or(ElementMatchers.returns(TypeDescription.STRING))
                        ))
                .intercept(FixedValue.nullValue())
                .make();
        unloaded.saveIn(new File(CLASS_PATH));
    }


    @Test
    public void testDynamicEnhanceRedefine() throws IOException {
        DynamicType.Unloaded<UserMapper> unloaded = new ByteBuddy()
                .redefine(UserMapper.class)
                .name("a.b.SubObj")
                .method(ElementMatchers.named("selectUsername")
                        .and(
                                // 返回值类型是 Class || Object || String
                                ElementMatchers.returns(TypeDescription.CLASS)
                                        .or(ElementMatchers.returns(TypeDescription.OBJECT))
                                        .or(ElementMatchers.returns(TypeDescription.STRING))
                        ))
                .intercept(FixedValue.nullValue())
                .make();
        unloaded.saveIn(new File(CLASS_PATH));
    }


    /**
     * 添加方法
     * @throws IOException
     */
    @Test
    public void testAddMethod() throws IOException {
        DynamicType.Unloaded<UserMapper> unloaded = new ByteBuddy()
                .redefine(UserMapper.class)
                .name("a.b.SubObj")
                // 指定方法的名字，返回值，修饰符
                .defineMethod("selectUsername2",String.class, Modifier.PUBLIC)
                // 指定方法的参数
                .withParameter(String[].class,"args")
                // 指定拦截到方法后该如何处理
                .intercept(FixedValue.value("生成的新方法"))
                .make();
        unloaded.saveIn(new File(CLASS_PATH));
    }


    /**
     * 添加新属性
     * @throws IOException
     */
    @Test
    public void testAddAttribute() throws IOException {
        DynamicType.Unloaded<UserMapper> unloaded = new ByteBuddy()
                .redefine(UserMapper.class)
                .name("a.b.SubObj")
                // 定义属性
                .defineField("age",int.class,Modifier.PRIVATE)
                // 指定avg的 getter和 setter所在的接口
                .implement(UserAgeInterface.class)
                // 指定拦截到方法后该如何处理
                .intercept(FieldAccessor.ofField("age"))
                .make();
        unloaded.saveIn(new File(CLASS_PATH));
    }


    /**
     * 方法委托
     */
    @Test
    public void testMethodDelegating() throws IOException, InstantiationException, IllegalAccessException {
        // Unloaded 表示生成的字节码还未加载到JVM
        DynamicType.Unloaded<UserMapper> uploaded = new ByteBuddy()
                .subclass(UserMapper.class)
                .name("a.b.SubObj")
                // named 通过名字指定要拦截的方法
                .method(ElementMatchers.named("selectUsername"))
                // 委托给UserMapperInterceptor1中与被拦截方法同签名的静态方法
                // .intercept(MethodDelegation.to(UserMapperInterceptor1.class))
                // 委托给UserMapperInterceptor2中与被拦截方法同签名的成员方法
                //.intercept(MethodDelegation.to(new UserMapperInterceptor2()))
                // 通过ByteBuddy的注解来指定增强方法
                .intercept(MethodDelegation.to(new UserMapperInterceptor3()))
                .make();
        // Loaded 表示生成的字节码已经加载到JVM
        DynamicType.Loaded<UserMapper> loaded = uploaded.load(this.getClass().getClassLoader());
        Class<? extends UserMapper> loadedClass = loaded.getLoaded();
        UserMapper userMapper = loadedClass.newInstance();
        String toStringResult = userMapper.selectUsername(1L);
        System.out.println("loadedClass.toStringResult = " + toStringResult);
        uploaded.saveIn(new File(CLASS_PATH));
    }





    /**
     * 动态修改入参
     * 1.自定义MyCallable
     * 2.在UserMapperInterceptor4处使用@Morph代替@SuperCallable
     * 3.指定拦截器前需要先调用withBinders
     */
    @Test
    public void testDynamicUpdateInArgs() throws IOException, InstantiationException, IllegalAccessException {
        // Unloaded 表示生成的字节码还未加载到JVM
        DynamicType.Unloaded<UserMapper> uploaded = new ByteBuddy()
                .subclass(UserMapper.class)
                .name("a.b.SubObj")
                // named 通过名字指定要拦截的方法
                .method(ElementMatchers.named("selectUsername"))
                .intercept(MethodDelegation
                        .withDefaultConfiguration()
                        .withBinders(
                                // 在UserMapperInterceptor4中石油MyCallable之前需要告诉byteBuddy类型是什么
                                // 参数类型为 MyCallable
                                Morph.Binder.install(MyCallable.class)
                        )
                        .to(new UserMapperInterceptor4()))
                .make();
        // Loaded 表示生成的字节码已经加载到JVM
        DynamicType.Loaded<UserMapper> loaded = uploaded.load(this.getClass().getClassLoader());
        Class<? extends UserMapper> loadedClass = loaded.getLoaded();
        UserMapper userMapper = loadedClass.newInstance();
        String toStringResult = userMapper.selectUsername(1L);
        System.out.println("loadedClass.toStringResult = " + toStringResult);
        uploaded.saveIn(new File(CLASS_PATH));
    }


    /**
     * 对构造方法进行插桩
     */
    @Test
    public void testConstructMethodInstrumentation() throws IOException, InstantiationException, IllegalAccessException {
        // Unloaded 表示生成的字节码还未加载到JVM
        DynamicType.Unloaded<UserMapper> uploaded = new ByteBuddy()
                .subclass(UserMapper.class)
                .name("a.b.SubObj")
                // 拦截任意的构造方法
                .constructor(ElementMatchers.any())
                .intercept(
                        // 指定在构造方法执行完成之后在委托给拦击诶去
                        SuperMethodCall.INSTANCE.andThen(
                            MethodDelegation.to(new UserMapperInterceptor5())
                        )
                )
                .make();
        // Loaded 表示生成的字节码已经加载到JVM
        DynamicType.Loaded<UserMapper> loaded = uploaded.load(this.getClass().getClassLoader());
        Class<? extends UserMapper> loadedClass = loaded.getLoaded();
        UserMapper userMapper = loadedClass.newInstance();
        uploaded.saveIn(new File(CLASS_PATH));
    }


    /**
     * 对静态方法插桩
     */
    @Test
    public void testStaticMethodInstrmentation() throws IOException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        // Unloaded 表示生成的字节码还未加载到JVM
        String id = IdUtil.simpleUUID();
        System.out.println("获取到的id为："+id);
        DynamicType.Unloaded<IdUtil> uploaded = new ByteBuddy()
                .rebase(IdUtil.class)
                // 静态方法拦截不能用redefine,因为redefine不会保留原始方法，在拦截器中就没有通过被@SuperCall修饰的参数调用原始方法
                // .redefine(IdUtil.class)
                // 静态方法不能被继承，所以不会被拦截
                // .subclass(IdUtil.class)
                .name("a.b.SubObj")
                // 拦截任意的构造方法
                .method(ElementMatchers.named("simpleUUID").and(ElementMatchers.isStatic()))
                .intercept(MethodDelegation.to(new UserMapperInterceptor6()))
                .make();
        // Loaded 表示生成的字节码已经加载到JVM
        // 自己指定类加载器 ClassLoadingStrategy.Default.CHILD_FIRST,打破双亲委派机制，采用子类加载器优先
        DynamicType.Loaded<IdUtil> loaded = uploaded.load(this.getClass().getClassLoader(), ClassLoadingStrategy.Default.CHILD_FIRST);
        Class<? extends IdUtil> loadedClass = loaded.getLoaded();
        Method loadedClassMethod = loadedClass.getMethod("simpleUUID");
        Object enhanceUuid = loadedClassMethod.invoke(null);
        System.out.println("增强后的UUID为："+enhanceUuid);
        uploaded.saveIn(new File(CLASS_PATH));
    }



    /**
     * 对静态方法插桩
     */
    @Test
    public void testMethodBody() throws IOException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        DynamicType.Unloaded<UserMapper> uploaded = new ByteBuddy()
                .rebase(UserMapper.class)
                .method(ElementMatchers.any())
                .intercept(StubMethod.INSTANCE)
                .make();
        uploaded.saveIn(new File(CLASS_PATH));
    }
}
