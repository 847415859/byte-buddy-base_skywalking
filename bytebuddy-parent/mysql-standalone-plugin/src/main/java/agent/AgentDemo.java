package agent;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.agent.builder.AgentBuilder;

import java.lang.instrument.Instrumentation;

import static net.bytebuddy.matcher.ElementMatchers.*;

/**
 * @Description: 实现功能：拦截SpringMVC请求，并计算出耗时
 * @Date : 2023/03/05 11:40
 * @Auther : tiankun
 */
@Slf4j
public class AgentDemo {
    private static final String SERVER_PS_NAME = "com.mysql.cj.jdbc.ServerPreparedStatement";
    private static final String CLIENT_PS_NAME = "com.mysql.cj.jdbc.ClientPreparedStatement";
    /**
     * 在main方法之前会被自动调用，是插桩的入口
     * @param args
     * @param instrumentation
     */
    public static void premain(String args, Instrumentation instrumentation){
        log.info("进入到premain, args：{}",args);
        AgentBuilder builder = new AgentBuilder.Default()
                .type((named(SERVER_PS_NAME).or(named(CLIENT_PS_NAME))))
                .transform(new AgentTransformer());
        builder.installOn(instrumentation);
    }
}
