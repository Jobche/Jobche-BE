package bg.elsys.jobche.controller

import bg.elsys.jobche.entity.body.user.UserLoginBody
import bg.elsys.jobche.entity.body.user.UserRegisterBody
import bg.elsys.jobche.entity.response.UserResponse
import bg.elsys.jobche.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(val userService: UserService) {

    @PostMapping("/login")
    fun login(@RequestBody userLogin : UserLoginBody): ResponseEntity<UserResponse> {
        return ResponseEntity(userService.login(userLogin), HttpStatus.OK)
    }

    @PostMapping
    fun register(@RequestBody userRegister: UserRegisterBody): ResponseEntity<UserResponse> {
        return ResponseEntity(userService.register(userRegister), HttpStatus.CREATED)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) : ResponseEntity<Unit> {
        return ResponseEntity(userService.delete(id), HttpStatus.OK)
    }

}
