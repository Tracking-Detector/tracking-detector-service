package com.trackingdetector.trackingdetectorservice.testSupport

import com.trackingdetector.trackingdetectorservice.TestApplication
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.event.annotation.AfterTestClass
import org.springframework.test.context.event.annotation.BeforeTestClass
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.HttpWaitStrategy
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.junit.jupiter.Testcontainers
import java.time.Duration

@SpringJUnitConfig
@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [TestApplication::class], webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = ["server.port=8086"])
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@Testcontainers
@TestConfiguration
@ContextConfiguration(classes = [TestApplication::class])
abstract class AbstractSpringTest {

    companion object {
        private const val minioRootUser: String = "trackingDetector"
        private const val minioRootPassword: String = "StrongPassword123"
        private val mongoDbContainer = GenericContainer("mongo:4.2.5")
            .withExposedPorts(27017)
            .waitingFor(Wait.forListeningPort())
        private val minioContainer = GenericContainer("minio/minio")
            .withEnv("MINIO_ROOT_USER", minioRootUser)
            .withEnv("MINIO_ROOT_PASSWORD", minioRootPassword)
            .withCommand("server /data")
            .withExposedPorts(9000)
            .waitingFor(
                HttpWaitStrategy()
                    .forPath("/minio/health/ready")
                    .forPort(9000)
                    .withStartupTimeout(Duration.ofSeconds(10))
            )

        @BeforeTestClass
        @JvmStatic
        fun startContainers() {
            mongoDbContainer.start()
            minioContainer.start()
        }

        @JvmStatic
        @DynamicPropertySource
        fun setProperties(registry: DynamicPropertyRegistry) {
            mongoDbContainer.start()
            minioContainer.start()
            registry.add("spring.data.mongodb.host") { mongoDbContainer.host }
            registry.add("spring.data.mongodb.port") { mongoDbContainer.getMappedPort(27017) }
            registry.add("spring.data.mongodb.database") { "tracking-detector" }
            registry.add("minio.url") { minioContainer.host }
            registry.add("minio.port") { minioContainer.getMappedPort(9000) }
            registry.add("minio.accessKey") { minioRootUser }
            registry.add("minio.privateKey") { minioRootPassword }
        }

        @AfterTestClass
        @JvmStatic
        fun stopContainers() {
            mongoDbContainer.stop()
            minioContainer.stop()
        }
    }
}
