package com.liang.plugin

import com.android.build.api.transform.*
import com.liang.plugin.internal.AspectJPTask
import com.liang.plugin.internal.AspectJProcedure
import com.liang.plugin.internal.TaskManager
import org.apache.commons.io.FileUtils
import org.gradle.api.Project
import com.android.build.gradle.internal.pipeline.TransformManager

class AspectJTransform extends Transform {
    AspectJProcedure procedure

    AspectJTransform(Project project) {
        this.procedure = new AspectJProcedure(project)
    }

    @Override
    String getName() {
        return 'apt'
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {

        if (transformInvocation.isIncremental()) {
            //TODO 增量
            print("====================增量编译=================")
            return
        }
        print("====================非增量编译=================")
        //非增量,需要删除输出目录
        transformInvocation.outputProvider.deleteAll()
        procedure.aspectJCache.reset()

        new AspectJFileProcess(procedure.aspectJCache, transformInvocation).proceed()

        AspectJPTask aspectJPTask = new AspectJPTask(procedure.project.logger)

        TaskManager taskManager = procedure.aspectJCache.taskManager
        taskManager.aspectPath << procedure.aspectJCache.aspectDir
        taskManager.classPath << procedure.aspectJCache.includeFileDir

        File includeJar = transformInvocation.getOutputProvider().getContentLocation("include",
                procedure.aspectJCache.contentTypes, procedure.aspectJCache.scopes, Format.JAR)

        if (!includeJar.parentFile.exists()) {
            FileUtils.forceMkdir(includeJar.getParentFile())
        }

        FileUtils.deleteQuietly(includeJar)

        aspectJPTask.outputJar = includeJar.absolutePath
        aspectJPTask.inPath << procedure.aspectJCache.includeFileDir
        taskManager.addTask(aspectJPTask)
        process(transformInvocation, taskManager)
        taskManager.execute()
    }

    private void process(TransformInvocation transformInvocation, TaskManager taskManager) {
        transformInvocation.inputs.each { TransformInput input ->
            input.jarInputs.each { JarInput jarInput ->

                taskManager.classPath << jarInput.file
                AspectJPTask aspectJPTask = new AspectJPTask(procedure.project.logger)
                aspectJPTask.inPath << jarInput.file
                println "```````jarInput file: ${aspectJPTask.inPath.size()}"
                File outputJar = transformInvocation.getOutputProvider().getContentLocation(jarInput.name, jarInput.contentTypes,
                        jarInput.scopes, Format.JAR)
                if (!outputJar.getParentFile()?.exists()) {
                    outputJar.getParentFile()?.mkdirs()
                }
                aspectJPTask.outputJar = outputJar.absolutePath
                taskManager.addTask(aspectJPTask)
            }
        }
    }
}
