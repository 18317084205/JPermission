package com.liang.plugin.internal

import java.util.concurrent.Callable

class TaskManager {
    TaskScheduler taskScheduler = new TaskScheduler()
    ArrayList<File> aspectPath = new ArrayList<>()
    ArrayList<File> classPath = new ArrayList<>()
    List<String> ajcArgs = new ArrayList<>()
    String encoding
    String bootClassPath
    String sourceCompatibility
    String targetCompatibility

    void addTask(Callable task) {
        taskScheduler.tasks << task
    }

    void execute() {
        taskScheduler.tasks.each { AspectJPTask task ->
            task.encoding = encoding
            task.aspectPath = aspectPath
            task.classPath = classPath
            task.targetCompatibility = targetCompatibility
            task.sourceCompatibility = sourceCompatibility
            task.bootClassPath = bootClassPath
            task.ajcArgs = ajcArgs
        }

        taskScheduler.execute()
    }
}