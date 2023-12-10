plugins {
    id("com.android.application") version "8.1.1" apply false
    id("com.android.library")  version "8.0.0" apply false
    id("org.jetbrains.kotlin.android") version "1.7.20" apply false
}
buildscript {

    repositories {
        maven {
            url = uri("https://maven.aliyun.com/repository/google")
        }
        maven {
            url = uri("https://maven.aliyun.com/repository/public")
        }
        maven {
            url = uri("https://maven.aliyun.com/repository/jcenter")
        }
    }

    dependencies {
        classpath ("com.android.tools.build:gradle:8.1.1")
    }
}
// 自动生成BuildConfig
allprojects {
    repositories {
//        maven {
//            url = uri("https://maven.aliyun.com/repository/google")
//        }
//        maven {
//            url = uri("https://maven.aliyun.com/repository/public")
//        }
//        maven {
//            url = uri("https://maven.aliyun.com/repository/jcenter")
//        }
    }
}
