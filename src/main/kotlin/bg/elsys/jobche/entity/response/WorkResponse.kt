package bg.elsys.jobche.entity.response

import bg.elsys.jobche.entity.model.user.User
import bg.elsys.jobche.entity.model.task.Task
import bg.elsys.jobche.entity.model.work.WorkStatus
import java.util.*

data class WorkResponse(val id: Long?, val task: Task?, val workers: List<User>?, val createdAt: Date?, val status: WorkStatus)
