import com.google.protobuf.gradle.*

plugins {
	id("org.springframework.boot") version "2.7.3"
	id("io.spring.dependency-management") version "1.0.13.RELEASE"
	id("com.google.protobuf") version "0.8.15"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
}

group = "msa"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

apply(plugin = "com.google.protobuf")

configurations.forEach {
	if (it.name.toLowerCase().contains("proto")) {
		it.attributes.attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage::class.java, "java-runtime"))
	}
}

repositories {
	mavenCentral()
}

val protobufVersion = "3.15.6"
val grpcVersion = "1.36.0"
val grpcKotlinVersion = "1.0.0"

dependencies {
	compileOnly("javax.annotation:javax.annotation-api:1.3.2")

	// grpc
	api("com.google.protobuf:protobuf-java-util:3.14.0")
	api("io.grpc:grpc-kotlin-stub:1.0.0")
	api("io.grpc:grpc-protobuf:1.34.0")
	api("io.grpc:grpc-netty-shaded:1.34.0")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

plugins.withType<ProtobufPlugin> {
	sourceSets {
		main {
			proto {
				srcDir("proto")
			}
		}
	}
	protobuf {
		generatedFilesBaseDir = "$projectDir/build/generated/source"
		protoc {
			artifact = "com.google.protobuf:protoc:3.14.0"
		}
		plugins {
			id("grpc") {
				artifact = "io.grpc:protoc-gen-grpc-java:1.34.0"
			}
			id("grpckt") {
				artifact = "io.grpc:protoc-gen-grpc-kotlin:1.0.0:jdk7@jar"
			}
		}
		generateProtoTasks {
			ofSourceSet("main").forEach {
				it.plugins {
					id("grpc")
					id("grpckt")
				}
				it.generateDescriptorSet = true
				it.descriptorSetOptions.includeSourceInfo = true
				it.descriptorSetOptions.includeImports = true
				it.descriptorSetOptions.path = "$buildDir/resources/META-INF/armeria/grpc/service-name.dsc"
			}
		}
	}
}

sourceSets {
	main {
		java.srcDir("build/generated/source/main/grpckt")
		java.srcDir("build/generated/source/main/grpc")
		java.srcDir("build/generated/source/main/java")
	}
}