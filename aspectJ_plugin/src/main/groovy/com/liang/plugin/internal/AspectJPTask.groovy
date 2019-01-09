package com.liang.plugin.internal

import org.aspectj.bridge.IMessage
import org.aspectj.bridge.MessageHandler
import org.aspectj.tools.ajc.Main
import org.gradle.api.logging.Logger

import java.util.concurrent.Callable

class AspectJPTask implements Callable {
    Logger logger

    ArrayList<File> inPath
    ArrayList<File> aspectPath = new ArrayList<>()
    ArrayList<File> classPath = new ArrayList<>()
    List<String> ajcArgs = new ArrayList<>()

    String encoding
    String bootClassPath
    String sourceCompatibility
    String targetCompatibility

    String outputDir
    String outputJar

    AspectJPTask(Logger logger) {
        this.logger = logger
        this.inPath = new ArrayList<>()
    }

    @Override
    Object call() throws Exception {
        def args = [
                "-showWeaveInfo",
                "-encoding", encoding,
                "-source", sourceCompatibility,
                "-target", targetCompatibility,
                "-classpath", classPath.join(File.pathSeparator),
                "-bootclasspath", bootClassPath
        ]


        if (!getInPath().isEmpty()) {
            args << '-inpath'
            args << getInPath().join(File.pathSeparator)
        }

        if (!getAspectPath().isEmpty()) {
            args << '-aspectpath'
            args << getAspectPath().join(File.pathSeparator)
        }

        if (outputDir != null && !outputDir.isEmpty()) {
            args << '-d'
            args << outputDir
        }

        if (outputJar != null && !outputJar.isEmpty()) {
            args << '-outjar'
            args << outputJar
        }

        if (ajcArgs != null && !ajcArgs.isEmpty()) {
            if (!ajcArgs.contains('-Xlint')) {
                args.add('-Xlint:ignore')
            }
            if (!ajcArgs.contains('-warn')) {
                args.add('-warn:none')
            }

            args.addAll(ajcArgs)
        } else {
            args.add('-Xlint:ignore')
            args.add('-warn:none')
        }


//        inPath.each { File file ->
//            println "~~~~~~~~~~~~~input file: ${file.absolutePath}"
//        }

        MessageHandler handler = new MessageHandler(true);

        String[] strings = args as String[]
        if (strings==null){
            return
        }
        println "=====input file: ${strings.size()}"
        new Main().run(args as String[], handler);
        for (IMessage message : handler.getMessages(null, true)) {
            switch (message.getKind()) {
                case IMessage.ABORT:
                case IMessage.ERROR:
                case IMessage.FAIL:
                    logger.error message.message, message.thrown
                    break;
                case IMessage.WARNING:
                    logger.warn message.message, message.thrown
                    break;
                case IMessage.INFO:
                    logger.info message.message, message.thrown
                    break;
                case IMessage.DEBUG:
                    logger.debug message.message, message.thrown
                    break;
            }
        }
        return null
    }
}
