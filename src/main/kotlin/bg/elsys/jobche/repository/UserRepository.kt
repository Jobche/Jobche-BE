package bg.elsys.jobche.repository

import bg.elsys.jobche.entity.model.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): User?
    fun existsByEmail(email: String): Boolean

    fun existsByPhoneNum(phoneNum: String): Boolean
}