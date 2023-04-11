package com.trackingdetector.trackingdetectorservice.acceptanceTest

import com.trackingdetector.trackingdetectorservice.domain.RequestData
import com.trackingdetector.trackingdetectorservice.repository.RequestDataRepository
import com.trackingdetector.trackingdetectorservice.testSupport.AbstractSpringTest
import com.trackingdetector.trackingdetectorservice.testSupport.JobTest
import com.trackingdetector.trackingdetectorservice.testSupport.MinioTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value


class Export204JobAcceptanceTest : AbstractSpringTest() {

    @Value("\${buckets.trainingDataBucket}")
    private lateinit var trainingBucket: String

    @Autowired
    private lateinit var jobTest: JobTest

    @Autowired
    private lateinit var minioTest: MinioTest

    @Autowired
    private lateinit var requestDataRepository: RequestDataRepository

    @BeforeEach
    fun setUp() {
        this.requestDataRepository.deleteAll()
    }

    @Test
    fun should_be_skipped_when_no_data_available() {
        // given
        // when
        jobTest.startRequestDataExport204Job()
            .and()
            .waitForJobCompletion()
            .and()
            .jobFinishedWithStatusSkipped()
        // then

    }

    @Test
    fun should_generate_csv_file_for_given_request_data() {
        // given
        requestDataRepository.save(RequestData(
            documentId = "CFB05D1A2E1B7E6B44813CCBB3ED7638",
            documentLifecycle= "active",
            frameId= 0,
            frameType= "outermost_frame",
            initiator= "https://www.sportsnet.ca",
            method= "GET",
            parentFrameId= -1,
            requestId= "979320",
            tabId= 19381,
            timeStamp= "1660340377939.461",
            type= "image",
            url= "https://dpm.demdex.net/ibs:dpid=411&dpuuid=YvYavQAF2aAqsAA0&d_uuid=78854490201948989640033682043832183304",
            requestHeaders= listOf(mapOf("name" to "sec-ch-ua-mobile", "value" to "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"101\", \"Google Chrome\";v=\"101\"")),
            success= true,
            label= true,
        ))
        // when
        jobTest.startRequestDataExport204Job()
            .and()
            .waitForJobCompletion()
            .and()
            .jobFinishedWithStatusOk()

        // then
        val content = minioTest.getContentOfFile(trainingBucket, "training-data.csv.gz")
        val expectedCsvRow = "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0," +
                "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0," +
                "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0," +
                "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,16,28,28,24,27,59,48,48,12,24,21" +
                ",47,12,13,21,12,13,32,47,22,13,28,48,17,10,27,59,12,24,17,12,62,53," +
                "50,50,39,12,24,29,29,17,12,62,1,30,1,9,30,82,66,71,51,9,66,25,27,66,66," +
                "49,39,12,7,29,29,17,12,62,56,57,57,54,53,53,58,49,51,49,50,58,53,57,58,57," +
                "58,55,53,49,49,52,52,55,57,51,49,53,52,57,52,51,50,57,52,52,49,53,1,1,2,0,1"
        assertEquals(205, content.split(",").size)
        assertEquals(expectedCsvRow, content.trim())

    }
}