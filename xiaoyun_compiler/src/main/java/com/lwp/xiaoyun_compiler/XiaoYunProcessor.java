package com.lwp.xiaoyun_compiler;

import com.google.auto.service.AutoService;
import com.lwp.xiaoyun_annotations.AppRegisterGenerator;
import com.lwp.xiaoyun_annotations.EntryGenerator;
import com.lwp.xiaoyun_annotations.PayEntryGenerator;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.AnnotationValueVisitor;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2019/12/30 4:09
 *     desc   : 效仿 ButterKnifeProcessor 来完成
 *              https://github.com/JakeWharton/butterknife/blob/master/butterknife-compiler/src/main/java/butterknife/compiler/ButterKnifeProcessor.java
 * </pre>
 */

@SuppressWarnings("unused")
@AutoService(Processor.class)//使用AutoService 自动生成spi 信息代码
public class XiaoYunProcessor extends AbstractProcessor {

    //循环取出 getSupportedAnnotations() 中的 Set 的元素
    //将 <Class<? extends Annotation> 类型的Set 迭代转化成 <String> 类型的 Set
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        final Set<String> types = new LinkedHashSet<>();
        final Set<Class<? extends Annotation>> supportAnnotations = getSupportedAnnotations();
        for (Class<? extends Annotation> annotation : supportAnnotations) {
            types.add(annotation.getCanonicalName());
        }
        return types;
    }

    //用一个 Set 存储所有要用到的 注解类类型
    private Set<Class<? extends Annotation>> getSupportedAnnotations() {

        final Set<Class<? extends Annotation>> annotations = new LinkedHashSet<>();
        annotations.add(EntryGenerator.class);
        annotations.add(PayEntryGenerator.class);
        annotations.add(AppRegisterGenerator.class);
        return annotations;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        generateEntryCode(roundEnvironment);
        generatePayEntryCode(roundEnvironment);
        generateAppRegisterCode(roundEnvironment);
        return true;
    }

    private void scan(RoundEnvironment env, Class<? extends Annotation> annotation,
                      AnnotationValueVisitor visitor) {

        //env 类似相当于 整个代码的环境
        for (Element typeElement : env.getElementsAnnotatedWith(annotation)) {
            final List<? extends AnnotationMirror> annotationMirrors =
                    typeElement.getAnnotationMirrors();

            //嵌套循环
            for (AnnotationMirror annotationMirror : annotationMirrors) {
                final Map<? extends ExecutableElement, ? extends AnnotationValue> elementValues
                        = annotationMirror.getElementValues();

                for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry
                        : elementValues.entrySet()) {
                    entry.getValue().accept(visitor, null);
                }
            }
        }
    }

    //visitor 相当于 注解 所注解的类、变量、方法 里面 所传入的值
    //本方法在 process() 中被调用
    private void generateEntryCode(RoundEnvironment env) {

        final EntryVisitor entryVisitor = new EntryVisitor();

        entryVisitor.setFiler(processingEnv.getFiler());

        scan(env, EntryGenerator.class, entryVisitor);
    }
    private void generatePayEntryCode(RoundEnvironment env) {
        final PayEntryVisitor payEntryVisitor = new PayEntryVisitor();
        payEntryVisitor.setFiler(processingEnv.getFiler());
        scan(env, EntryGenerator.class, payEntryVisitor);
    }
    private void generateAppRegisterCode(RoundEnvironment env) {
        final AppRegisterVisitor appRegisterVisitor = new AppRegisterVisitor();
        appRegisterVisitor.setFiler(processingEnv.getFiler());
        scan(env, EntryGenerator.class, appRegisterVisitor);
    }
}
