// Root build file = the coordinator for the multi-module build.
// Each `alias(...)` pulls a plugin id + version from gradle/libs.versions.toml.
// `apply false` = make the plugin available at that version, but DON'T apply it to the
// root project (the root is not a KMP/Android module)
plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose.compiler) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
}
