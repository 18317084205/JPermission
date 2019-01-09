package com.liang.plugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

class AspectJPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        print("JAspectPlugin apply ...")
        if (!project.android) {
            throw new IllegalStateException("'android-application' or 'android-library' plugin required.")
        }

        def hasLib = project.plugins.withType(LibraryPlugin)
        if (hasLib) {
            return
        }

        project.dependencies {
            if (project.gradle.gradleVersion > "4.0") {
                implementation 'org.aspectj:aspectjrt:1.8.9'
            } else {
                compile 'org.aspectj:aspectjrt:1.8.9'
            }
        }

        project.getExtensions().getByType(AppExtension)
                .registerTransform(new AspectJTransform(project));
    }
}
