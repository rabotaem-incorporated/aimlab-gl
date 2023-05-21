package aimlab.aimlabclient.models

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import java.util.*

@Serializable
class Stat (
    var id: String = "",
    var score: Double = 0.0,
    var combo: UInt = 0u,
    var accuracy: Double = 0.0,
    var datetime: String = ""
)
