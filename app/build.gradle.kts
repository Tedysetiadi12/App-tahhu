plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.tahhu.id"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.tahhu.id"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            storeFile = file("$rootDir/release-key.jks")
            storePassword = "password_keystore"
            keyAlias = "my-key-alias"
            keyPassword = "password_key"
        }
    }

    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation ("com.google.firebase:firebase-database:20.2.2")
    implementation ("com.google.firebase:firebase-auth:22.1.1")
    implementation ("com.github.bumptech.glide:glide:4.14.2")
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.14.2")
    implementation ("pl.droidsonroids.gif:android-gif-drawable:1.2.25")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("androidx.appcompat:appcompat:1.4.0")
    implementation ("com.google.android.exoplayer:exoplayer-hls:2.19.1")
    implementation ("com.google.android.exoplayer:exoplayer:2.19.1")
    implementation("androidx.media3:media3-exoplayer:1.0.0-alpha01")
    implementation ("com.google.android.material:material:1.12.0")
    implementation("com.android.volley:volley:1.2.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    implementation("androidx.activity:activity:1.9.3")
    implementation("androidx.media3:media3-exoplayer:1.4.1")
    implementation("com.google.android.gms:play-services-location:21.3.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    //firebase
    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))
    implementation("com.google.firebase:firebase-analytics")
    implementation ("com.google.android.gms:play-services-maps:19.0.0")
    implementation ("com.github.amitshekhariitbhu.Fast-Android-Networking:android-networking:1.0.4") /* lebih baru */

    implementation("com.prolificinteractive:material-calendarview:1.4.3")  // Kalender kustom
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")         // Grafik visual
    implementation ("androidx.recyclerview:recyclerview:1.3.1")
//    implementation(files("libs/ZCustomCalendar-1.0.4.jar"))
    implementation ("androidx.lifecycle:lifecycle-viewmodel:2.6.1")
    implementation ("androidx.lifecycle:lifecycle-livedata:2.6.1")
//    implementation ("io.github.ztk1994:richeditor:2.0.1")
    implementation ("jp.wasabeef:richeditor-android:2.0.0")

}

