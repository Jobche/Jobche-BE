package bg.elsys.jobche.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus


@ResponseStatus(HttpStatus.NOT_FOUND)
class UserNotFoundException(message: String = "Exception: User was not found") : RuntimeException(message) {

}
