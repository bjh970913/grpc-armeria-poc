import com.google.protobuf.gradle.generateProtoTasks
import com.google.protobuf.gradle.id
import com.google.protobuf.gradle.ofSourceSet
import com.google.protobuf.gradle.plugins
import com.google.protobuf.gradle.proto
import com.google.protobuf.gradle.protobuf
import com.google.protobuf.gradle.protoc

plugins {
    java
    idea
    id("org.springframework.boot") version "2.6.2"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.spring") version "1.6.10"
    id("com.google.protobuf") version "0.8.17"
}
repositories {
    jcenter()
    mavenCentral()
}
sourceSets {
    create("sample") {
        proto {
            srcDir("src/sample/protobuf")
        }
    }
}


val protobufV = "3.19.1"
val grpc = "1.25.0"
val grpcKotlin = "1.0.0"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation("com.google.protobuf:protobuf-java:${protobufV}")
    implementation("com.google.protobuf:protobuf-java-util:${protobufV}")
    implementation("io.grpc:grpc-stub:1.42.1")
    implementation("io.grpc:grpc-protobuf:1.42.1")
    implementation("io.grpc:grpc-netty:1.42.1")
    implementation("com.linecorp.armeria:armeria:1.13.4")
    implementation("com.linecorp.armeria:armeria-grpc:1.13.4")

    if (JavaVersion.current().isJava9Compatible) {
        // Workaround for @javax.annotation.Generated
        // see: https://github.com/grpc/grpc-java/issues/3633
        implementation("javax.annotation:javax.annotation-api:1.3.1")
    }
    // Extra proto source files besides the ones residing under
    // "src/main".
    protobuf(files("${rootProject.projectDir}/proto/"))
    // https://mvnrepository.com/artifact/com.google.api.grpc/grpc-google-common-protos
    implementation("com.google.api.grpc:grpc-google-common-protos:2.7.0")
}

protobuf {
    protoc {
        // The artifact spec for the Protobuf Compiler
        artifact = "com.google.protobuf:protoc:${protobufV}"
    }
    plugins {
        // Optional: an artifact spec for a protoc plugin, with "grpc" as
        // the identifier, which can be referred to in the "plugins"
        // container of the "generateProtoTasks" closure.
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:${grpc}"
        }
    }
    generateProtoTasks {
        ofSourceSet("main").forEach {
            it.plugins {
                // Apply the "grpc" plugin whose spec is defined above, without options.
                id("grpc")
            }
        }
    }
}
