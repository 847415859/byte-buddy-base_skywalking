package agent;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.utility.JavaModule;

/**
 * @Description: 类加载Listener
 * @Date : 2023/03/05 14:00
 * @Auther : tiankun
 */
@Slf4j
public class ByteBuddyListener implements AgentBuilder.Listener{
    /**
     * 当某个类将要被加载时就会回调此方法
     */
    @Override
    public void onDiscovery(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {
        log.info("onDiscovery typeName:{}",typeName);
    }

    /**
     * 对某个类完成了transform之后会回调
     */
    @Override
    public void onTransformation(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, boolean loaded, DynamicType dynamicType) {
        log.info("onTransformation typeName:{}",typeDescription.getActualName());
    }

    /**
     * 当某个类将要被加载了并且配置了被bytebuddy忽略时(ignore配置或不匹配的)会回调此方法
     */
    @Override
    public void onIgnored(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, boolean loaded) {
        log.info("onIgnored typeName:{}",typeDescription.getActualName());
    }

    /**
     * 当bytebuddy在transform过程中发生异常时会回调此方法
     */
    @Override
    public void onError(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded, Throwable throwable) {
        log.info("onError typeName:{}",typeName);
    }

    /**
     * 某个类处理完(transform/ignore/error)时会回调此方法
     */
    @Override
    public void onComplete(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {
        log.info("onComplete typeName:{}",typeName);
    }
}
