package com.liang.plugin.internal

import com.android.build.gradle.api.BaseVariant
import com.liang.plugin.cache.AspectJCache
import org.aspectj.weaver.Dump
import org.gradle.api.DomainObjectCollection
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile

class AspectJProcedure {
    Project project
    AspectJCache aspectJCache

    AspectJProcedure(Project project) {
        this.project = project
        System.setProperty("aspectj.multithreaded", "true")
        DomainObjectCollection<BaseVariant> variants = project.android.applicationVariants
        project.afterEvaluate {
            variants.all { variant ->
                aspectJCache = new AspectJCache(project, variant.name)
                TaskManager taskManager = new TaskManager()
                JavaCompile javaCompile = (variant.hasProperty('javaCompiler') ? variant.javaCompiler : variant.javaCompile) as JavaCompile
                taskManager.sourceCompatibility = javaCompile.sourceCompatibility
                taskManager.targetCompatibility = javaCompile.targetCompatibility
                taskManager.encoding = javaCompile.options.encoding
                taskManager.bootClassPath = project.android.bootClasspath.join(File.pathSeparator)
                aspectJCache.taskManager = taskManager
            }
        }

        File logDir = new File(project.buildDir.absolutePath + File.separator + "outputs" + File.separator + "logs")
        if (!logDir.exists()) {
            logDir.mkdirs()
        }

        Dump.setDumpDirectory(logDir.absolutePath)
    }
}