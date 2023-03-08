package com.tk.skywalking.agent.plugin.match;


/**
 * @Description: 表示要匹配哪些类的最顶层接口（用于skywalking byteBuddy进行对哪些类进行拦截。）
 *  - 实现主要有两类
 *      NameMatch   单个方法拦截匹配
 *      IndirectMatch 非直接拦截匹配
 * @Date : 2023/03/06 10:12
 * @Auther : tiankun
 */
public interface ClassMatch {
}
