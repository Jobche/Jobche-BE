package bg.elsys.jobche.entity.response

import java.time.LocalDateTime

data class TaskResponse(val id: Long?,
                   val title: String?,
                   val description: String?,
                   val payment: Int?,
                   val numberOfWorkers: Int?,
                   val dateTime: LocalDateTime?)