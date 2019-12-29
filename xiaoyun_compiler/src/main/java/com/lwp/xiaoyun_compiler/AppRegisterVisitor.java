package com.lwp.xiaoyun_compiler;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.SimpleAnnotationValueVisitor7;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2019/12/30 5:58
 *     desc   :
 * </pre>
 */
public final class AppRegisterVisitor extends SimpleAnnotationValueVisitor7<Void, Void> {

    private Filer mFiler = null;// XiaoYunProcessor.scan() 中 需要被遍历的东西
    private TypeMirror mTypeMirror = null;//要循环找的类型
    private String mPackageName = null;//最终要拿到的 包名,在注解里面写入的PackageName

    public void setFiler(Filer filer) {
        this.mFiler = filer;
    }

    @Override
    public Void visitString(String s, Void p) {
        mPackageName = s;
        return p;
    }

    //找出 注解类型 所注解的类 上面所标注的原信息，并且来生成我们的代码
    @Override
    public Void visitType(TypeMirror t, Void p) {
        mTypeMirror = t;
        generateJavaCode();
        return p;
    }

    //TypeSpec 是 javapoet 里面的类，这个类就是帮我们生成我们需要的类，
    // Type 指的就是 Class，具体可以在 GitHub上 看 javapoet 的使用，
    // 这里都是标准的使用方式
    //生成 模板代码
    private void generateJavaCode() {

        //classBuilder 传入 要生成的类的类名，
        // 微信接入 要求的类是 WXEntryActivity、WXPayEntryActivity等
        final TypeSpec targetActivity =
                TypeSpec.classBuilder("AppRegister")
                        .addModifiers(Modifier.PUBLIC)
                        .addModifiers(Modifier.FINAL)
                        .superclass(TypeName.get(mTypeMirror))//要生成的类要继承的是 从注解中拿出来的 模板类类型
                        .build();

        // JavaFile.builder( 最终包名 + 类名 )
        final JavaFile javaFile = JavaFile.builder(mPackageName+".wxapi",targetActivity)
                .addFileComment("微信广播接收器")// 文件注释
                .build();

        try {
            javaFile.writeTo(mFiler);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
