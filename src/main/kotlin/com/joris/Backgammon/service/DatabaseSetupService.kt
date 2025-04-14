package com.joris.Backgammon.service


import com.mongodb.kotlin.client.coroutine.MongoClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitExchange

val REQUIREDCOLLECTIONS = listOf("users", "games")

@Service
class SetupService(
    @Value("\${mongo.host}") val mongoHost : String,
    @Value("\${mongo.port}") val  mongoPort : String,
    @Value("\${mongo.name}") val  dbName : String,
    @Value("\${backgammon.service.host}")  val backgammonServiceHost : String,
    @Value("\${backgammon.service.port}")  val backgammonServicePort : String

)
{
    private val logger = LoggerFactory.getLogger(this::class.java)

    @EventListener(ApplicationReadyEvent::class)
    suspend fun initApplication(){
        initDatabase()
        checkBackgammonService()
    }

    suspend fun checkBackgammonService(){
        val client = WebClient.create("${this.backgammonServiceHost}:$${this.backgammonServicePort}");
        client.get()
            .uri("/health")
            .accept(MediaType.APPLICATION_JSON)
            .awaitExchange { response ->
                if (response.statusCode() != HttpStatus.OK){
                    throw Exception("Backgammon Service not reachable")
                }
            }

    }

    suspend fun initDatabase(){
        logger.info("start initializing mongoDB")
        val mongoClient = MongoClient.create("mongo://${this.mongoHost}${this.mongoPort}")
        val db = mongoClient.getDatabase(dbName)
        val collectionNames: Flow<String> = db.listCollectionNames()
        val collectionsList = collectionNames.toList()
        logger.info("Found collections in the db : ${collectionsList}")
        if (collectionsList == REQUIREDCOLLECTIONS) {
            logger.info("All collections already available")
        }
        else {
                db.createCollection(REQUIREDCOLLECTIONS[0])
                logger.info("successfully created the ${REQUIREDCOLLECTIONS[0]}")
                db.createCollection(REQUIREDCOLLECTIONS[1])
                logger.info("successfully created the ${REQUIREDCOLLECTIONS[1]}")
        }
    }

}