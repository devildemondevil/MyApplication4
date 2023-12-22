plugins {
    id("com.android.library")  version "8.0.0"
    id("org.jetbrains.kotlin.android") version "1.7.20"
}

android {
    namespace = "com.aspsine.fragmentnavigator"
    compileSdk = 34

    defaultConfig {
//        applicationId = "com.aspsine.fragmentnavigator"
        minSdk = 24
        targetSdk = 33
//        versionName = "1.0"

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
        buildConfig=true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.0-alpha01"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    testImplementation ("junit:junit:4.13.2")
    implementation ("androidx.fragment:fragment:1.6.2")
    implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.7.20"))
}