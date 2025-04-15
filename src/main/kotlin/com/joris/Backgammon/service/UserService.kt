package com.joris.Backgammon.service
import com.joris.Backgammon.CustomExceptions.UserAlreadyExistException
import com.joris.Backgammon.dto.User
import com.mongodb.MongoException
import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.firstOrNull
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64



class UserService (
    private val database : MongoDatabase,
    @Value("\${mongo.users}")
    val usersCollections : String
)
{

    private val logger = LoggerFactory.getLogger(this::class.java);
    private val md = MessageDigest.getInstance("SHA-256");

    suspend fun registerUser(user : User) : String?{
        try{
            val userCol =  database.getCollection<User>(usersCollections);
            user.password = hashPassword(user.password)
            val res = userCol.insertOne(user);
            logger.info("user created with id: ${res.insertedId.toString()}")
            return user.userName;
        }catch( e : MongoException){
            logger.error("Could not register user!")
            return null
        }
    }

    private fun hashPassword(password: String): String {
        val salt = generateSalt()
        this.md.update(salt)
        val hashedBytes = md.digest(password.toByteArray())
        return Base64.getEncoder().encodeToString(hashedBytes)
    }

    private fun generateSalt(): ByteArray {
        val salt = ByteArray(16) // 128-bit salt
        SecureRandom().nextBytes(salt)
        return salt
    }

    private fun generateSession(){

    }

    suspend fun checkUserExists(userName : String, email : String) : Boolean{
        val userCol = database.getCollection<User>(usersCollections);
        val filter = Filters.or(
            Filters.eq(User::userName.name, userName),
            Filters.eq(User::email.name, email)
        )
        val res = userCol.find(filter);
        return res.firstOrNull() == null
    }

    suspend fun loginUser(userName : String, password : String) : Unit {
        val userCol = database.getCollection<User>(usersCollections);
        val userNameFilter = Filters.eq(User::userName.name, userName);
        val res = userCol.find(userNameFilter)


        val passwordHash = hashPassword(password);
        val filter = Filters.eq(User::password.name, passwordHash);
        userCol.find(filter)
    }

    suspend fun logoutUser(): Unit{

    }



}