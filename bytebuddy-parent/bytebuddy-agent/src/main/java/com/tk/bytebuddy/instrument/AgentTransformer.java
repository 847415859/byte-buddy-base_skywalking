package com.tk.bytebuddy.instrument;

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

    private static final String MAPPING_PKG_PREFIX = "org.springframework.web.bind.annotation";
    private static final String MAPPING_SUFFIX = "Mapping";

    @Override
    public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader,
                                            JavaModule module, ProtectionDomain protectionDomain) {
        log.info("actualName to transform:{}", typeDescription.getActualName());
        DynamicType.Builder.MethodDefinition.ReceiverTypeDefinition<?> intercept = builder
                .method(
                        not(isStatic()).and(isAnnotatedWith(
                                nameStartsWith(MAPPING_PKG_PREFIX).and(nameEndsWith(MAPPING_SUFFIX))
                        ))
                ).intercept(MethodDelegation.to(new SpringMvcInterceptor()));
        // 不能返回builder,因为bytebuddy库里的类基本都是不可变的,修改之后需要返回修改后的对象避免修改丢失
        return intercept;
    }
}
