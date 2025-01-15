import com.google.protobuf.gradle.*

plugins {
    id("java")
    id("idea")
    id("com.google.protobuf") version "0.9.4"
}

group = "com.konsec.vtx"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
val vertxVersion: String by extra("4.5.11")
val protobufVersion: String by extra("4.28.3")
val grpcVersion: String by extra("1.68.0")

dependencies {
    implementation("io.vertx:vertx-core:$vertxVersion")
    implementation("io.vertx:vertx-grpc:$vertxVersion")
    implementation("com.google.guava:guava:33.3.1-jre")
    implementation("com.google.protobuf:protobuf-java:$protobufVersion")

    if (JavaVersion.current().isJava9Compatible()) {
        // Workaround for @javax.annotation.Generated
        // see: https://github.com/grpc/grpc-java/issues/3633
        implementation("javax.annotation:javax.annotation-api:1.3.1")
    }

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

configure<JavaPluginExtension> {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}


tasks.test {
    useJUnitPlatform()
}

protobuf {
    protoc {
        // The artifact spec for the Protobuf Compiler
        artifact = "com.google.protobuf:protoc:${protobufVersion}"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:${grpcVersion}"
        }
        id("vertx") {
            artifact = "io.vertx:vertx-grpc-protoc-plugin:${vertxVersion}"
        }
    }
    generateProtoTasks {
        ofSourceSet("main").forEach {
            it.plugins {
                // Apply the "grpc" plugin whose spec is defined above, without
                // options. Note the braces cannot be omitted, otherwise the
                // plugin will not be added. This is because of the implicit way
                // NamedDomainObjectContainer binds the methods.
                id("grpc") { }
                id("vertx") { }
            }
        }
    }
}
