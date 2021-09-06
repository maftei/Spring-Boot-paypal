package patches.buildTypes

import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.MavenBuildStep
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.v2019_2.ui.*

/*
This patch script was generated by TeamCity on settings change in UI.
To apply the patch, change the buildType with id = 'Package'
accordingly, and delete the patch script.
*/
changeBuildType(RelativeId("Package")) {
    expectSteps {
        maven {
            goals = "clean package"
            runnerArgs = "-DskipTests"
        }
    }
    steps {
        update<MavenBuildStep>(0) {
            clearConditions()
            jdkHome = "%env.JDK_18%"
        }
    }
}