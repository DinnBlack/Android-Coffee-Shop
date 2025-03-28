plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.thefutuscoffeeversion13"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.thefutuscoffeeversion13"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.squareup.picasso:picasso:2.71828")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.github.denzcoskun:ImageSlideshow:0.1.2")
    implementation("nl.joery.animatedbottombar:library:1.1.0")
    implementation("com.google.firebase:firebase-auth-ktx:22.1.2")
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
    implementation("com.google.firebase:firebase-firestore:24.9.1")
    implementation("com.google.firebase:firebase-database:20.3.0")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    implementation("com.google.firebase:firebase-auth:22.3.0")
    implementation("com.google.firebase:firebase-analytics")
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("com.github.dhaval2404:imagepicker:2.1")
    implementation(fileTree(mapOf(
        "dir" to "C:\\Users\\phucd\\AndroidStudioProjects\\final_project_mobile\\TheFutusCoffeeVersion13\\app\\src\\main\\libs",
        "include" to listOf("*.aar", "*.jar"),
        "exclude" to listOf("*.aar", "*.jar"))))
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("com.airbnb.android:lottie:4.2.2")

    //zalo
    implementation("com.squareup.okhttp3:okhttp:4.6.0")
    implementation("commons-codec:commons-codec:1.14")
    implementation(files("C:\\Users\\phucd\\AndroidStudioProjects\\final_project_mobile\\TheFutusCoffeeVersion13\\app\\src\\main\\libs\\zpdk-release-v3.1.aar"))

    implementation ("it.xabaras.android:recyclerview-swipedecorator:1.4")


}