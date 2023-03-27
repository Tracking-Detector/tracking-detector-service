package com.trackingdetector.trackingdetectorservice.testSupport

import com.trackingdetector.trackingdetectorservice.TestApplication
import org.junit.jupiter.api.*
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.HttpWaitStrategy
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.junit.jupiter.Testcontainers
import java.time.Duration

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = [TestApplication::class], webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@Testcontainers
@TestConfiguration
@ContextConfiguration
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
            .waitingFor(HttpWaitStrategy()
                .forPath("/minio/health/ready")
                .forPort(9000)
                .withStartupTimeout(Duration.ofSeconds(10)))
        @BeforeAll
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

        @AfterAll
        @JvmStatic
        fun stopContainers() {
            mongoDbContainer.stop()
            minioContainer.stop()
        }
    }
}