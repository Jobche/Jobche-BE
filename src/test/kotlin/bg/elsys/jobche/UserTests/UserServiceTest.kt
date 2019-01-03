package bg.elsys.jobche.UserTests

import bg.elsys.jobche.entity.body.user.UserLoginBody
import bg.elsys.jobche.entity.body.user.UserRegisterBody
import bg.elsys.jobche.entity.model.User
import bg.elsys.jobche.entity.response.UserResponse
import bg.elsys.jobche.repositories.UserRepository
import bg.elsys.jobche.service.UserService
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyAll
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyLong
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.*

@ExtendWith(MockKExtension::class)
class UserServiceTest {

    companion object {
        const val FIRST_NAME = "Radoslav"
        const val LAST_NAME = "Hubenov"
        const val EMAIL = "rrhubenov@gmail.com"
        const val PASSWORD = "password"
        private val user = User(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD)
        private val userRegister = UserRegisterBody(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD)
        private val userLogin = UserLoginBody(EMAIL, PASSWORD)
    }

    private val repository: UserRepository = mockk()
    private val passwordEncoder = BCryptPasswordEncoder()

    private val userService: UserService

    init {
        userService = UserService(repository, passwordEncoder)
    }

    @Test
    fun `login should return valid user response`() {
        every { repository.existsByEmail(EMAIL) } returns true
        every { repository.findByEmail(EMAIL) } returns user

        val result = userService.login(userLogin)
        val expectedResult = UserResponse(0, "Radoslav", "Hubenov")
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `create should return valid user response`() {
        every { repository.save(any<User>()) } returns user

        val userResponse = userService.create(userRegister)

        assertThat(userResponse).isEqualTo(UserResponse(anyLong(), userRegister.firstName, userRegister.lastName))
    }

    @Test
    fun `delete user`() {
        every { repository.existsById(anyLong()) } returns true
        every { repository.deleteById(anyLong()) } returns Unit

        userService.delete(anyLong())

        verifyAll {
            repository.existsById(anyLong())
            repository.deleteById(anyLong())  }
    }

    @Test
    fun `update user`() {
        every { repository.existsById(anyLong()) } returns true
        every { repository.getOne(anyLong()) } returns user
        every { repository.save(user) } returns user

        userService.update(anyLong(), UserRegisterBody(user.firstName, user.lastName, user.email, user.password))

        verify {
            repository.existsById(anyLong())
            repository.getOne(anyLong())
            repository.save(user)
        }
    }

    @Test
    fun `read user`() {
        every { repository.existsById(anyLong()) } returns true
        every { repository.findById(anyLong()) } returns Optional.of(user)

        userService.read(anyLong())

        verify {
            repository.existsById(anyLong())
            repository.findById(anyLong())
        }
    }
}