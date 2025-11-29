plugins {
  id("com.android.application")
  id("com.chaquo.python")
}

android {
  namespace = "com.xirion.localinter"
  compileSdk = 35

  defaultConfig {
    applicationId = "com.xirion.localinter"
    minSdk = 26
    targetSdk = 35
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    externalNativeBuild {
      cmake {
        cppFlags += "-std=c++17"
      }
    }

    ndk {
      // Chaquopy needs ABIs declared
      abiFilters += listOf("arm64-v8a", "x86_64")
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

  externalNativeBuild {
    cmake {
      path = file("src/main/cpp/CMakeLists.txt")
      version = "3.22.1"
    }
  }

  buildFeatures {
    viewBinding = true
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
}

// 🐍 Chaquopy config: Kotlin must use `chaquopy {}` DSL, not `python {}`
chaquopy {
  defaultConfig {
    // Use system python3 on your dev machine
    buildPython("python3")

    pip {
      install("requests")
      // add more here if you want
    }
  }
}

dependencies {
  implementation(libs.appcompat)
  implementation(libs.material)
  implementation(libs.constraintlayout)
  testImplementation(libs.junit)
  androidTestImplementation(libs.ext.junit)
  androidTestImplementation(libs.espresso.core)
}
