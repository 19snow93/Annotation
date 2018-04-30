package com.gzcp.lib_compiler;


import com.gzcp.lib_annotations.Add;

import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

/**
 * Created by leo on 2018/4/8.
 */

public class AnnotatedClass {
    public Element mClassElement;
    /**
     * 元素相关的辅助类
     */
    public Elements mElementUtils;

    public TypeMirror elementType;

    public Name elementName;

    private float value1;
    private float value2;


    public AnnotatedClass(Element classElement) {


        this.mClassElement = classElement;
        this.elementType = classElement.asType();
        this.elementName = classElement.getSimpleName();

        value1 = mClassElement.getAnnotation(Add.class).ele1();
        value2 = mClassElement.getAnnotation(Add.class).ele2();
    }

    Name getElementName() {
        return elementName;
    }

    TypeMirror getElementType(){
        return elementType;
    }

    Float getTotal(){
        return (value1 + value2);
    }


    /**
     * 包名
     */
    public String getPackageName(TypeElement type) {
        return mElementUtils.getPackageOf(type).getQualifiedName().toString();
    }
    /**
     * 类名
     */
    private static String getClassName(TypeElement type, String packageName) {
        int packageLen = packageName.length() + 1;
        return type.getQualifiedName().toString().substring(packageLen).replace('.', '$');
    }
}
