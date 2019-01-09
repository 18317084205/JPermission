package com.liang.plugin.internal

import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class TaskScheduler {

    ExecutorService executorService
    List< ? extends Callable> tasks = new ArrayList<>()

    TaskScheduler() {
        executorService = Executors.newScheduledThreadPool(Runtime.runtime.availableProcessors() + 1)
    }

    public <T extends Callable> void addTask(T task) {
        tasks << task
    }

    void execute() {
        executorService.invokeAll(tasks)
        tasks.clear()
    }

}
