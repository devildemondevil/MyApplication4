plugins {
    id("com.android.library")  version "8.0.0"
    id("org.jetbrains.kotlin.android") version "1.7.20"
//    id("com.novoda.bintray-release")
}

android {
    compileSdkVersion(33)
    namespace="com.swallowsonny.convertextlibrary"
    defaultConfig {
        minSdkVersion(19)
        targetSdkVersion(33)
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
//    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.8.22")
}

repositories {
//    mavenCentral()
}

// 从这里开始配置
//bintray {
//    repoName = "ext" // 仓库名
//    userOrg = "swallowsonny" // Bintray 注册的用户名
//    groupId = "com.swallowsonny" // compile 引用时的第1部分 groupId
//    artifactId = "convert-ext" // compile 引用时的第2部分项目名
//    publishVersion = "1.0.4" // compile 引用时的第3部分版本号
//    desc = "Conversion between ByteArray and Basic Types" // 项目描述
//    website = "https://github.com/swallowsonny/ConvertExt" // GitHub 托管地址
//}

allprojects {
    repositories {
//        jcenter()
    }

    tasks.withType(Javadoc::class) {
        options {
            encoding = "UTF-8"
//            charSet = "UTF-8"
//            links("http://docs.oracle.com/javase/7/docs/api")
        }
    }
}
