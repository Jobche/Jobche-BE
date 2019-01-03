package bg.elsys.jobche.service

import bg.elsys.jobche.entity.body.user.UserLoginBody
import bg.elsys.jobche.entity.body.user.UserRegisterBody
import bg.elsys.jobche.entity.model.User
import bg.elsys.jobche.entity.response.UserResponse
import bg.elsys.jobche.exceptions.UserNotFoundException
import bg.elsys.jobche.repositories.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(val userRepository: UserRepository,
                  val passwordEncoder: PasswordEncoder) {

    fun login(userLogin: UserLoginBody): UserResponse {
        if (userRepository.existsByEmail(userLogin.email)) {
            val user = userRepository.findByEmail(userLogin.email)
            return UserResponse(user?.id, user?.firstName, user?.lastName)
        } else throw UserNotFoundException()
    }

    fun create(userRegister: UserRegisterBody): UserResponse {
        val userDTO = User(userRegister.firstName,
                userRegister.lastName,
                userRegister.email,
                passwordEncoder.encode(userRegister.password))

        val savedUser = userRepository.save(userDTO)
        return UserResponse(savedUser.id, savedUser.firstName, savedUser.lastName)
    }

    fun delete(id: Long) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id)
        } else throw UserNotFoundException()
    }

    fun update(id: Long, updatedUser: UserRegisterBody) {
        if (userRepository.existsById(id)) {
            val user = userRepository.getOne(id)
            user.firstName = updatedUser.firstName
            user.lastName = updatedUser.lastName
            user.email = updatedUser.email
            user.password = updatedUser.password
            userRepository.save(user)
        } else throw UserNotFoundException()
    }

    fun read(id: Long): UserResponse {
        if( userRepository.existsById(id) ) {
            val user = userRepository.findById(id).get()
            return UserResponse(user.id, user.firstName, user.lastName)
        } else throw UserNotFoundException()
    }

}