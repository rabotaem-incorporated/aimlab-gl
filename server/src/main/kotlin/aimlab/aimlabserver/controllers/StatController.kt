package aimlab.aimlabserver.controllers

import aimlab.aimlabserver.models.Stat
import aimlab.aimlabserver.repositories.StatRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class StatController(var statRepository : StatRepository) {
    @CrossOrigin()
    @PostMapping("/")
    fun saveStat(@RequestBody stat: Stat): ResponseEntity<Stat> = ResponseEntity.ok(statRepository.save(stat))

    @GetMapping("/")
    fun getAllStats(): ResponseEntity<List<Stat>> = ResponseEntity.ok(statRepository.findAll())
}