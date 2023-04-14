package com.trackingdetector.trackingdetectorservice.representation

import java.time.Instant

data class TrainingResultRepresentation(val accuracy: Double,
                                        val trainingRun: Instant,)
