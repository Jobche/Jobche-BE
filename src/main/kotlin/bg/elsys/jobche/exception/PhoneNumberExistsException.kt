package bg.elsys.jobche.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.CONFLICT)
class PhoneNumberExistsException(message: String = "Exception: An account is already registered with this phone number") : RuntimeException(message)
