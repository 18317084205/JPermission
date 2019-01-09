package com.liang.plugin.cache

import com.android.build.api.transform.QualifiedContent
import com.android.builder.model.AndroidProject
import com.google.common.collect.ImmutableSet
import com.liang.plugin.internal.TaskManager
import org.apache.commons.io.FileUtils
import org.gradle.api.Project;

class AspectJCache {
    Project project
    String variantName

    String cachePath
    String aspectPath
    String includeFilePath

    TaskManager taskManager

    Set<QualifiedContent.ContentType> contentTypes = ImmutableSet.<QualifiedContent.ContentType> of(QualifiedContent.DefaultContentType.CLASSES)
    Set<QualifiedContent.Scope> scopes = ImmutableSet.<QualifiedContent.Scope> of(QualifiedContent.Scope.EXTERNAL_LIBRARIES)


    AspectJCache(Project project, String variantName) {
        this.project = project
        this.variantName = variantName
        init()
    }


    private void init() {
        //初始化目录，并把之前编译的结果以及配置缓存下来
        cachePath = project.buildDir.absolutePath + File.separator + AndroidProject.FD_INTERMEDIATES + "/apt/" + variantName
        aspectPath = cachePath + File.separator + "aspectJs"
        includeFilePath = cachePath + File.separator + "includeFiles"
        if (!aspectDir.exists()) {
            aspectDir.mkdirs()
        }

        if (!getCacheDir().exists()) {
            getCacheDir().mkdirs()
        }
    }

    File getCacheDir() {
        return new File(cachePath)
    }

    File getAspectDir() {
        return new File(aspectPath)
    }

    File getIncludeFileDir() {
        return new File(includeFilePath)
    }

    void add(File sourceFile, File cacheFile) {
        if (sourceFile == null || cacheFile == null) {
            return
        }

        byte[] bytes = FileUtils.readFileToByteArray(sourceFile)
        add(bytes, cacheFile)
    }

    void add(byte[] classBytes, File cacheFile) {
        if (classBytes == null || cacheFile == null) {
            return
        }

        FileUtils.writeByteArrayToFile(cacheFile, classBytes)
    }

    void reset() {
        close()
        init()
    }

    void close() {
        FileUtils.deleteDirectory(cacheDir)
    }
}
