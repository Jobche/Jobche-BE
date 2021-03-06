package bg.elsys.jobche.UserTests

import bg.elsys.jobche.BaseUnitTest
import bg.elsys.jobche.DefaultValues
import bg.elsys.jobche.config.security.AuthenticationDetails
import bg.elsys.jobche.converter.Converters
import bg.elsys.jobche.entity.body.user.DateOfBirth
import bg.elsys.jobche.entity.body.user.UserBody
import bg.elsys.jobche.entity.model.user.User
import bg.elsys.jobche.entity.response.user.UserResponse
import bg.elsys.jobche.exception.EmailExistsException
import bg.elsys.jobche.exception.PhoneNumberExistsException
import bg.elsys.jobche.repository.UserRepository
import bg.elsys.jobche.service.AmazonStorageService
import bg.elsys.jobche.service.UserService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyAll
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.ArgumentMatchers.anyString
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.*

class UserServiceTest: BaseUnitTest() {

    companion object {
        const val FIRST_NAME = "Radoslav"
        const val LAST_NAME = "Hubenov"
        const val EMAIL = "rrhubenov@gmail.com"
        const val PASSWORD = "password"
        val DATE_OF_BIRTH = DateOfBirth(1, 1, 2000)
        const val PHONE_NUM = "0878555373"
        private val user = User(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, DATE_OF_BIRTH.toString(), PHONE_NUM)
        private val userRegister = DefaultValues.creatorUserBody()
        private val userResponse = DefaultValues.creatorUserResponse()
    }

    private val repository: UserRepository = mockk()
    private val authenticationDetails: AuthenticationDetails = mockk()
    private val passwordEncoder = BCryptPasswordEncoder()
    private val storageService: AmazonStorageService = mockk()
    private val converters: Converters = Converters(storageService)

    private val userService: UserService

    init {
        userService = UserService(repository, passwordEncoder, authenticationDetails, converters, storageService)
    }


    @Nested
    inner class create {
        @Test
        fun `create should return valid user response`() {
            every { repository.existsByEmail(userRegister.email) } returns false
            every { repository.existsByPhoneNum(userRegister.phoneNum) } returns false
            every { repository.save(any<User>()) } returns user

            val result = userService.create(userRegister)

            assertThat(result).isEqualTo(userResponse)
        }

        @Test
        fun `create with already existing phone should throw exception`() {
            every { repository.existsByEmail(userRegister.email) } returns false
            every { repository.existsByPhoneNum(userRegister.phoneNum) } returns true
            every { repository.save(any<User>()) } returns user

            assertThatExceptionOfType(PhoneNumberExistsException::class.java).isThrownBy {
                userService.create(userRegister)
            }
        }

        @Test
        fun `create with already existing email should throw exception`() {
            every { repository.existsByEmail(userRegister.email) } returns true
            every { repository.existsByPhoneNum(userRegister.phoneNum) } returns true
            every { repository.save(any<User>()) } returns user

            assertThatExceptionOfType(EmailExistsException::class.java).isThrownBy {
                userService.create(userRegister)
            }
        }
    }

    @Test
    fun `delete user`() {
        every { authenticationDetails.getUser() } returns user
        every { repository.delete(any()) } returns Unit

        userService.delete()

        verifyAll {
            authenticationDetails.getUser()
            repository.delete(any())
        }
    }

    @Test
    fun `update user`() {
        every { authenticationDetails.getUser() } returns user
        every { repository.save(user) } returns user

        userService.update(UserBody(user.firstName, user.lastName, user.email, user.password, DATE_OF_BIRTH, user.phoneNum))

        verify {
            authenticationDetails.getUser()
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

    @Test
    fun `user exists should return true`() {
        every { repository.existsById(anyLong()) } returns true

        val result = userService.existsById(anyLong())

        assertThat(result).isEqualTo(true)
    }


}