plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "tech.dawn.template.testdsl"
    compileSdk = 37

    defaultConfig {
        minSdk = 30
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui.test)
    implementation(libs.androidx.compose.ui.test.junit4)
    implementation(libs.androidx.test.core.ktx)
    compileOnly(libs.robolectric)
}
