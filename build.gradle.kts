import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.google.protobuf.gradle.*

plugins {
	id("org.springframework.boot") version "2.7.3"
	id("io.spring.dependency-management") version "1.0.13.RELEASE"
	id("com.google.protobuf") version "0.8.14"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
}

group = "msa"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
}

val protobufVersion = "3.15.6"
val grpcVersion = "1.36.0"
val grpcKotlinVersion = "1.0.0"

dependencies {
	// Armeria
	implementation("com.linecorp.armeria:armeria-spring-boot-webflux-starter")
	implementation("com.linecorp.armeria:armeria-grpc")

	// reactor
	implementation("com.salesforce.servicelibs:reactor-grpc-stub:1.0.0")
}

dependencyManagement {
	imports {
		mavenBom("com.linecorp.armeria:armeria-bom:0.99.5")
	}
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
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
		protoc {
			artifact = "com.google.protobuf:protoc:3.10.1"
		}
		plugins {
			id("grpc") {
				artifact = "io.grpc:protoc-gen-grpc-java:1.25.0"
			}
			id("reactorGrpc") {
				artifact = "com.salesforce.servicelibs:reactor-grpc:1.0.0"
			}
		}
		generateProtoTasks {
			ofSourceSet("main").forEach {
				it.plugins {
					id("grpc")
					id("reactorGrpc")
				}
			}
		}
	}
}