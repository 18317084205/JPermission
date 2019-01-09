package com.liang.plugin

import com.android.build.api.transform.*
import com.google.common.io.ByteStreams
import com.liang.plugin.cache.AspectJCache
import com.liang.plugin.internal.TaskScheduler
import org.apache.commons.io.FileUtils

import java.util.concurrent.Callable
import java.util.jar.JarEntry
import java.util.jar.JarFile

class AspectJFileProcess {
    AspectJCache aspectJCache
    TransformInvocation transformInvocation

    AspectJFileProcess(AspectJCache aspectJCache, TransformInvocation transformInvocation) {
        this.aspectJCache = aspectJCache
        this.transformInvocation = transformInvocation
    }

    void proceed() {
        TaskScheduler taskScheduler = new TaskScheduler()
        transformInvocation.inputs.each { TransformInput input ->
            input.directoryInputs.each { DirectoryInput dirInput ->
                taskScheduler.addTask(new Callable() {
                    @Override
                    Object call() throws Exception {
                        dirInput.file.eachFileRecurse { File item ->
                            if (AspectClassUtils.isClassFile(item)) {
                                String path = item.absolutePath
                                String subPath = path.substring(dirInput.file.absolutePath.length())
                                aspectJCache.add(item, new File(aspectJCache.includeFilePath, subPath))
                                if (AspectClassUtils.isAspectClass(item)) {
                                    File cacheFile = new File(aspectJCache.aspectPath, subPath)
                                    aspectJCache.add(item, cacheFile)
                                }
                            }
                        }
                        return null
                    }
                })
            }

            input.jarInputs.each { JarInput jarInput ->
                taskScheduler.addTask(new Callable() {
                    @Override
                    Object call() throws Exception {
                        JarFile jarFile = new JarFile(jarInput.file)
                        Enumeration<JarEntry> entries = jarFile.entries()
                        while (entries.hasMoreElements()) {
                            JarEntry jarEntry = entries.nextElement()
                            String entryName = jarEntry.getName()
                            if (!jarEntry.isDirectory() && AspectClassUtils.isClassFile(entryName)) {
                                byte[] bytes = ByteStreams.toByteArray(jarFile.getInputStream(jarEntry))
                                File cacheFile = new File(aspectJCache.aspectPath + File.separator + entryName)
                                if (AspectClassUtils.isAspectClass(bytes)) {
                                    aspectJCache.add(bytes, cacheFile)
                                }
                            }
                        }

                        jarFile.close()
                        return null
                    }
                })
            }

        }

        taskScheduler.execute()
    }

}
