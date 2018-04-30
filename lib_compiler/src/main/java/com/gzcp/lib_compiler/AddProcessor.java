package com.gzcp.lib_compiler;

import com.google.auto.service.AutoService;
import com.gzcp.lib_annotations.Add;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

/**
 * Created by leo on 2018-02-10.
 */


@AutoService(Processor.class)
public class AddProcessor extends AbstractProcessor{

    private static final String ADD_SUFFIX = "_Add";
    private static final String TARGET_STATEMENT_FORMAT = "target.%1$s = %2$s";
    private static final String CONST_PARAM_TARGET_NAME = "target";

    private static final char CHAR_DOT = '.';

    private Messager messager;
    private Types typesUtil;
    private Elements elementsUtil;
    private Filer filer;
    Map<String, List<AnnotatedClass>> annotatedElementMap = new LinkedHashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        messager = processingEnv.getMessager();
        typesUtil = processingEnv.getTypeUtils();
        elementsUtil = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotataions = new LinkedHashSet<String>();
        annotataions.add(Add.class.getCanonicalName());
        return annotataions;
    }


    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        annotatedElementMap.clear();

        for (Element element : roundEnv.getElementsAnnotatedWith(Add.class)) {
            if (element.getKind() != ElementKind.FIELD) {
                messager.printMessage(Diagnostic.Kind.ERROR, "Only FIELD can be annotated with @%s");
            }
            TypeElement encloseElement = (TypeElement) element.getEnclosingElement();
            String fullClassName = encloseElement.getQualifiedName().toString();
            AnnotatedClass annotatedClass = new AnnotatedClass(element);
            if(annotatedElementMap.get(fullClassName) == null){
                annotatedElementMap.put(fullClassName, new ArrayList<AnnotatedClass>());
            }
            annotatedElementMap.get(fullClassName).add(annotatedClass);

        }

        if (annotatedElementMap.size() == 0) {
            return true;
        }

        try {
            for (Map.Entry<String, List<AnnotatedClass>> entry : annotatedElementMap.entrySet()) {
                MethodSpec constructor = createConstructor(entry.getValue());
                TypeSpec binder = createClass(getClassName(entry.getKey()), constructor);
                JavaFile javaFile = JavaFile.builder(getPackage(entry.getKey()), binder).build();
                javaFile.writeTo(filer);
            }

        } catch (IOException e) {
            messager.printMessage(Diagnostic.Kind.ERROR, "Error on creating java file");
        }

        return true;
    }



    private MethodSpec createConstructor(List<AnnotatedClass> randomElements) {
        AnnotatedClass firstElement = randomElements.get(0);
        MethodSpec.Builder builder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(TypeName.get(firstElement.mClassElement.getEnclosingElement().asType()), CONST_PARAM_TARGET_NAME);
        for (int i = 0; i < randomElements.size(); i++) {
            addStatement(builder, randomElements.get(i));
        }
        return builder.build();
    }

    private void addStatement(MethodSpec.Builder builder, AnnotatedClass randomElement) {
        builder.addStatement(String.format(
                TARGET_STATEMENT_FORMAT,
                randomElement.getElementName().toString(),
                randomElement.getTotal())
        );
    }

    private TypeSpec createClass(String className, MethodSpec constructor) {
        return TypeSpec.classBuilder(className + ADD_SUFFIX)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(constructor)
                .build();
    }

    private String getPackage(String qualifier) {
        return qualifier.substring(0, qualifier.lastIndexOf(CHAR_DOT));
    }

    private String getClassName(String qualifier) {
        return qualifier.substring(qualifier.lastIndexOf(CHAR_DOT) + 1);
    }

}
