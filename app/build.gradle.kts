import com.google.protobuf.gradle.generateProtoTasks
import com.google.protobuf.gradle.id
import com.google.protobuf.gradle.ofSourceSet
import com.google.protobuf.gradle.plugins
import com.google.protobuf.gradle.protoc
import com.google.protobuf.gradle.protobuf
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("jvm")
    kotlin("plugin.spring")
    id("com.google.protobuf") // version "0.8.17"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11


val protobuf = "3.10.0"
val grpc = "1.25.0"
val grpcKotlin = "1.0.0"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // protobuf
    implementation("com.google.protobuf:protobuf-java:$protobuf")
    implementation("com.google.protobuf:protobuf-java-util:$protobuf")

//    implementation("io.grpc:grpc-all:${grpc}")
//    implementation("io.grpc:grpc-kotlin-stub:${grpcKotlin}")

    protobuf(files("$rootDir/proto"))
//    create(files("$rootDir/proto"))
}

//com.google.protobuf.gradle.ProtobufDependencyHelper()

//protobuf {
//    protoc {
//        artifact = "com.google.protobuf:protoc:${protobuf}"
//    }
//
//    plugins {
//        id("grpc") {
//            artifact = "io.grpc:protoc-gen-grpc-java:${grpc}"
//        }
//        id("grpckt") {
//            artifact = "io.grpc:protoc-gen-grpc-kotlin:${grpcKotlin}:jdk7@jar"
//        }
//    }
//
//    generateProtoTasks {
//        ofSourceSet("main").forEach {
//            it.plugins {
//                id("grpc")
//                id("grpckt")
//            }
//        }
//    }
//}


tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
