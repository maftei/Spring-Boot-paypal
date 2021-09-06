import jetbrains.buildServer.configs.kotlin.v10.toExtId
import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.freeDiskSpace
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.VcsTrigger
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.vcs

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2021.1"

project {

//    buildType(Maven("Package" ,"clean package", "-DskipTests"))
//// this is the same thing as the build configuration from the UI teamcity
//   // buildType(Package)
//    buildType(Maven("Build Spring" ,"clean compile"))
//    buildType(Maven("Fast Test" ,"clean compile","-Dmaven.test.failure.ignore=true -Dtest=*test"))
//    buildType(Maven("Slow Test" ,"clean compile","DskipTests"))



    val bts=sequential {
        buildType(Maven("Build Spring" ,"clean compile"))
        parallel {
            buildType(Maven("Fast Test" ,"clean compile","-Dmaven.test.failure.ignore=true -Dtest=*test"))
            buildType(Maven("Slow Test" ,"clean","DskipTests"))
        }
        buildType(Maven("Package" ,"clean package", "-DskipTests"))
    }.buildTypes()
    bts.forEach{buildType (it)}
    bts.last().triggers{
        vcs{

        }
    }
}


class Maven(name: String, goals: String, runnerArgs: String? = null): BuildType({
    id(name.toExtId())
    this.name=name

    vcs {
        root(DslContext.settingsRoot) //this means the VCS root for your project is the same where your settings.kts file lives
    }
    features {
        freeDiskSpace {
            failBuild = true         // default is 3gb, if you don't have 3gb the build will failed
        }

    }

    steps {
        maven {
            this.goals = goals
            this.runnerArgs = runnerArgs
        }
    }
})

//object Build : BuildType({
//    name = "Build Spring"
//
//    vcs {
//        root(DslContext.settingsRoot) //this means the VCS root for your project is the same where your settings.kts file lives
//    }
//     features {
//         freeDiskSpace {
//             failBuild = true         // default is 3gb, if you don't have 3gb the build will failed
//         }
//
//     }
//
//    steps {
//        maven {
//            name = "MyStep"
//            goals = "clean compile"
//            runnerArgs = "-Dmaven.test.failure.ignore=true"
//        }
//    }

//    triggers {
//        vcs {
//            quietPeriodMode = VcsTrigger.QuietPeriodMode.USE_CUSTOM
//            quietPeriod = 30
//        }
//        //here every commit and every branch will result in a build
//    }
//})

//object FastTest : BuildType({
//    name = "Fast Test"
//
//    vcs {
//        root(DslContext.settingsRoot) //this means the VCS root for your project is the same where your settings.kts file lives
//    }
//    features {
//        freeDiskSpace {
//            failBuild = true         // default is 3gb, if you don't have 3gb the build will failed
//        }
//
//    }
//
//    steps {
//        maven {
//            name = "MyStep"
//            goals = "clean test"
//            runnerArgs = "-Dmaven.test.failure.ignore=true -Dtest=*test"
//        }
//    }
//
////    triggers {
////        vcs {
////            quietPeriodMode = VcsTrigger.QuietPeriodMode.USE_CUSTOM
////            quietPeriod = 30
////        }
////        //here every commit and every branch will result in a build
////    }
//})

//object SlowTest : BuildType({
//    name = "Slow Test"
//
//    vcs {
//        root(DslContext.settingsRoot) //this means the VCS root for your project is the same where your settings.kts file lives
//    }
//    features {
//        freeDiskSpace {
//            failBuild = true         // default is 3gb, if you don't have 3gb the build will failed
//        }
//
//    }
//
//    steps {
//        maven {
//            name = "MyStep"
//            goals = "clean test"
//            runnerArgs = "-Dmaven.test.failure.ignore=true -DskipTests"
//        }
//    }
//
////    triggers {
////        vcs {
////            quietPeriodMode = VcsTrigger.QuietPeriodMode.USE_CUSTOM
////            quietPeriod = 30
////        }
////        //here every commit and every branch will result in a build
////    }
//})



//object Package : BuildType({
//    name = "Package"
//
//    vcs {
//        root(DslContext.settingsRoot) //this means the VCS root for your project is the same where your settings.kts file lives
//    }
//    features {
//        freeDiskSpace {
//            failBuild = true         // default is 3gb, if you don't have 3gb the build will failed
//        }
//
//    }
//
//    steps {
//        maven {
//            name = "MyLastStep"
//            goals = "clean package"
//            runnerArgs = "-Dmaven.test.failure.ignore=true -DskipTests"
//        }
//    }
//
//    //this means you have dependencies between objects Buils and Pacjkage but there is a btter way , see at the begining
//    //it is called sequential
////    dependencies{
////        snapshot(Build){
////
////        }
////    }
//
//    triggers {
//        vcs {
//            quietPeriodMode = VcsTrigger.QuietPeriodMode.USE_CUSTOM
//            quietPeriod = 30
//        }
//        //here every commit and every branch will result in a build
//    }
//})
