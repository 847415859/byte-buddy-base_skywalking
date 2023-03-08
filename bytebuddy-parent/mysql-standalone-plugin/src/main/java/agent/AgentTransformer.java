package agent;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.utility.JavaModule;

import java.security.ProtectionDomain;

import static net.bytebuddy.matcher.ElementMatchers.*;

/**
 * @Description:
 * @Date : 2023/03/05 13:57
 * @Auther : tiankun
 */
@Slf4j
public class AgentTransformer implements AgentBuilder.Transformer {

    private static final String SERVER_PS_NAME = "com.mysql.cj.jdbc.ServerPreparedStatement";
    private static final String CLIENT_PS_NAME = "com.mysql.cj.jdbc.ClientPreparedStatement";

    @Override
    public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader,
                                            JavaModule module, ProtectionDomain protectionDomain) {
        log.info("actualName to transform:{}", typeDescription.getActualName());
        DynamicType.Builder.MethodDefinition.ReceiverTypeDefinition<?> intercept = builder
                .method(
                       named("execute")
                        .or(named("executeQuery"))
                        .or(named("executeUpdate"))
                ).intercept(MethodDelegation.to(new MysqlInterceptor()));
        return intercept;
    }
}
