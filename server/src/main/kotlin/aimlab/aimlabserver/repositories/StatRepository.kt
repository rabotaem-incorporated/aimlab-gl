package aimlab.aimlabserver.repositories

import aimlab.aimlabserver.models.Stat
import org.springframework.data.mongodb.repository.MongoRepository


interface StatRepository : MongoRepository<Stat, String> {
}