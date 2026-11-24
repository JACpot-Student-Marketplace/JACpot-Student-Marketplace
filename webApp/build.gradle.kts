import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    js {
        browser {
            commonWebpackConfig {
                devServer =
                    (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                        port = 8080
                    }
            }
        }
        binaries.executable()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser {
            commonWebpackConfig {
                devServer =
                    (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                        port = 8080
                    }
            }
        }
        binaries.executable()
    }

    sourceSets {
        webMain.dependencies {
            implementation(libs.wrappers.browser)
        }
        commonMain.dependencies {
            implementation(project(":shared"))

            implementation(libs.compose.ui)
        }
    }
}
