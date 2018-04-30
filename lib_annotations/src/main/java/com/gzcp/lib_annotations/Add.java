package com.gzcp.lib_annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.CLASS;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by leo on 2018/4/8.
 */
/* 用来指明注解的访问范围
*  1.源码级注解SOURCE，该类型的注解信息会留在.java源码中，源码编译后，注解信息会被丢弃，不会保留在编译好的.class文件中；
*  2.编译时注解CLASS，注解信息会保留在.java源码里和.class文件中，在执行的时候，会被Java虚拟机丢弃不回家再到虚拟机中；
*  3.运行时注解RUNTIME，java源码里，.class文件中和Java虚拟机在运行期也保留注解信息，可通过反射读取
* */
@Retention(CLASS)
//是一个ElementType类型的数组，用来指定注解所使用的对象范围
@Target(value = FIELD)
public @interface Add {
    float ele1() default 0f;
    float ele2() default 0f;
}
