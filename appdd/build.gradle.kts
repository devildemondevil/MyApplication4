

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.aspsine.fragmentnavigator.demo"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.aspsine.fragmentnavigator.demo"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    dependencies {
//        implementation(fileTree(mapOf("include" to listOf("*.jar"), "dir" to "libs")))
        implementation(project(":library"))
        //implementation("com.github.Aspsine:FragmentNavigator:1.0.2")

        implementation(project(":serialhelperlibrary"))

        testImplementation("junit:junit:4.13.2")
//        implementation("androidx.localbroadcastmanager:localbroadcastmanager:1.1.0")
        implementation("androidx.appcompat:appcompat:1.6.1")
        implementation("org.jetbrains.kotlin:kotlin-stdlib:1.8.22")
//        implementation("androidx.legacy:legacy-support-v4:1.0.0")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.8.22")
        implementation("androidx.core:core-ktx:1.9.0")
        implementation("androidx.constraintlayout:constraintlayout:2.1.4")
//        implementation("com.google.android.material:material:1.9.0")
//        androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

        // 注释掉冗余的依赖项
        // implementation(project(":serialhelperlibrary"))
        // implementation("com.swallowsonny:serialhelper:2.0.3")
//        implementation("com.swallowsonny:convert-ext:1.0.4")
        implementation("org.greenrobot:eventbus:3.3.0")
//        implementation("com.github.CymChad:BaseRecyclerViewAdapterHelper:3.0.4")
    }


}