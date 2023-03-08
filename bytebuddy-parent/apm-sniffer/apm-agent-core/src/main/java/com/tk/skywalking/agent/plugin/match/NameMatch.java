package com.tk.skywalking.agent.plugin.match;

import lombok.Data;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import static net.bytebuddy.matcher.ElementMatchers.named;

/**
 * @Description: 拦截单个方法的匹配
 *              专门用于类名称=xx,仅适用于named(xx)
 * @Date : 2023/03/06 13:49
 * @Auther : tiankun
 */
@Data
public class NameMatch implements ClassMatch {

    private String className;

    public NameMatch(String className) {
        this.className = className;
    }

    public ElementMatcher<TypeDescription> buildJunction(){
        return named(className);
    }

    public static NameMatch byClassName(String className){
        return new NameMatch(className);
    }
}
