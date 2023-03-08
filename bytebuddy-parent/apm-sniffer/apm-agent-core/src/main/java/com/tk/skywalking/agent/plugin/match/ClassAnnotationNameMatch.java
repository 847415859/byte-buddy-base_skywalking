package com.tk.skywalking.agent.plugin.match;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.annotation.AnnotationList;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static net.bytebuddy.matcher.ElementMatchers.isAnnotatedWith;
import static net.bytebuddy.matcher.ElementMatchers.named;

/**
 * @Description:  某及各类含有一个或多个注解
 * @Date : 2023/03/07 11:54
 * @Auther : tiankun
 */
@Data
@Slf4j
public class ClassAnnotationNameMatch implements IndirectMatch {

    private List<String> classNames;

    public ClassAnnotationNameMatch(List<String> classNames) {
        this.classNames = classNames;
    }

    public ClassAnnotationNameMatch(String[] classNames) {
        this.classNames = Arrays.asList(classNames);
    }

    @Override
    public ElementMatcher<TypeDescription> buildJunction() {
        ElementMatcher.Junction<TypeDescription> junction = null;
        for (String className : classNames) {
            if(junction == null){
                junction = isAnnotatedWith(named(className));
            }else {
                junction = junction.or(isAnnotatedWith(named(className)));
            }
        }
        return junction;
    }

    /**
     *
     * @param typeDescription 待判断的类
     * @return true: annotations是typeDescription上注解的真子集
     */
    @Override
    public boolean isMatch(TypeDescription typeDescription) {
        AnnotationList declaredAnnotations = typeDescription.getDeclaredAnnotations();
        for (AnnotationDescription declaredAnnotation : declaredAnnotations) {
            if(classNames.contains(declaredAnnotation.getAnnotationType().getTypeName())){
                return true;
            }
        }
        return false;
    }

    public static IndirectMatch byMultiAnnotationMatch(String... classNames){
        return new ClassAnnotationNameMatch(classNames);
    }
}
