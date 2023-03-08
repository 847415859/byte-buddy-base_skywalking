package com.tk.skywalking.agent.plugin.match;

import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

/**
 * @Description: 所有的非NameMatch的情况都需要实现 IndirectMatch
 * @Date : 2023/03/06 13:53
 * @Auther : tiankun
 */
public interface IndirectMatch extends ClassMatch{

    /**
     * 用于构造 type() 的参数
     * @return
     */
    ElementMatcher<TypeDescription> buildJunction();

    boolean isMatch(TypeDescription typeDescription);
}
