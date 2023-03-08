package com.tk.skywalking.agent.plugin.match;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import java.util.Arrays;
import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.named;

/**
 * @Description:  多个类名相等匹配器
 * @Date : 2023/03/06 16:30
 * @Auther : tiankun
 */
@Data
@Slf4j
public class MultiClassNameMatch implements IndirectMatch{

    /**
     * 要匹配的类的名字
     */
    private List<String> needMatchClassNames;

    public MultiClassNameMatch(List<String> needMatchClassNames) {
        this.needMatchClassNames = needMatchClassNames;
    }

    public MultiClassNameMatch(String[] needMatchClassNames) {
        this.needMatchClassNames = Arrays.asList(needMatchClassNames);
    }

    @Override
    public ElementMatcher<TypeDescription> buildJunction() {
        ElementMatcher.Junction<TypeDescription> junction = null;
        for (String className : needMatchClassNames) {
            if(junction == null){
                junction = named(className);
            }else {
                junction = junction.or(named(className));
            }
        }
        return junction;
    }

    @Override
    public boolean isMatch(TypeDescription typeDescription) {
          /*
            比如needMatchClassNames里面是com.mysql.cj.jdbc.ServerPreparedStatement、com.mysql.cj.jdbc.ClientPreparedStatement
            而typeDescription是a.b.C
         */
        return needMatchClassNames.contains(typeDescription.getTypeName());
    }

    public static IndirectMatch byMultiClassMatch(String... classNames){
        return new MultiClassNameMatch(classNames);
    }
}
