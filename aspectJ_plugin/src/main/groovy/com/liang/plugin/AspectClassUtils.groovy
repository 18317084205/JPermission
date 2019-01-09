package com.liang.plugin

import com.liang.plugin.internal.AspectJClassVisitor
import org.apache.commons.io.FileUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter

class AspectClassUtils {
    static boolean isAspectClass(File classFile) {
        if (isClassFile(classFile?.getAbsolutePath())) {
            return isAspectClass(FileUtils.readFileToByteArray(classFile))
        }
        return false
    }

    // 判断是否是Aspectj注解的class文件，通过asm来判断Class文件上是否有@Aspect注解
    static boolean isAspectClass(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return false
        }

        try {
            ClassReader classReader = new ClassReader(bytes)
            ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES)
            AspectJClassVisitor classVisitor = new AspectJClassVisitor(classWriter)
            classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)
            return classVisitor.isAspectClass
        } catch (Exception e) {

        }

        return false
    }

    static boolean isClassFile(File file) {
        return file?.getAbsolutePath()?.toLowerCase()?.endsWith('.class')
    }

    static boolean isClassFile(String filePath) {
        return filePath?.toLowerCase()?.endsWith('.class')
    }

}
