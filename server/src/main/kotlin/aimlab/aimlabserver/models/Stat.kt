package aimlab.aimlabserver.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document("stats")
class Stat {
    @Id
    var id: String = ""
    var username: String = ""
    var score: Double = 0.0
    var combo: UInt = 0u
    var accuracy: Double = 0.0
    var datetime: String = ""
}