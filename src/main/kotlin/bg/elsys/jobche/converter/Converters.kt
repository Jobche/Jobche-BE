package bg.elsys.jobche.converter

import bg.elsys.jobche.entity.body.user.DateOfBirth
import bg.elsys.jobche.entity.model.picture.ProfilePicture
import bg.elsys.jobche.entity.model.picture.TaskPicture
import bg.elsys.jobche.entity.model.task.Application
import bg.elsys.jobche.entity.model.task.Task
import bg.elsys.jobche.entity.model.user.Review
import bg.elsys.jobche.entity.model.user.User
import bg.elsys.jobche.entity.model.work.Participation
import bg.elsys.jobche.entity.model.work.Work
import bg.elsys.jobche.entity.response.WorkResponse
import bg.elsys.jobche.entity.response.application.ApplicationResponse
import bg.elsys.jobche.entity.response.picture.PictureResponse
import bg.elsys.jobche.entity.response.task.TaskResponse
import bg.elsys.jobche.entity.response.user.ReviewResponse
import bg.elsys.jobche.entity.response.user.UserResponse
import bg.elsys.jobche.exception.NoContentException
import bg.elsys.jobche.service.AmazonStorageService
import org.springframework.stereotype.Component

@Component
class Converters(val storageService: AmazonStorageService) {
    val Work.response
        get() = WorkResponse(id, task.response,
                participations.map { it.userResponse }, createdAt, status)

    val Task.response
        get() = TaskResponse(id, title, description, payment,
                numberOfWorkers, dateTime, city, creator.id, acceptedWorkersCount
                ,getPicturesTask(pictures))

    val User.response
        get() = UserResponse(id, firstName, lastName,
                toDateOfBirth(dateOfBirth), phoneNum, reviews.map { it.response }, getPicture(picture))

    val Application.response
        get() = ApplicationResponse(id, user.response, task?.response, accepted)

    val Review.response
        get() = ReviewResponse(id, work.id, reviewGrade)

    val Participation.userResponse
        get() = UserResponse(id, user.firstName, user.lastName,
                toDateOfBirth(user.dateOfBirth), user.phoneNum, user.reviews.map { it.response })

    val ProfilePicture.response
        get() = PictureResponse(id)

    val TaskPicture.response
        get() = PictureResponse(id)

    fun toDateOfBirth(date: String): DateOfBirth {
        val values = date.split("-")
        return DateOfBirth(values.get(0).toInt(), values.get(1).toInt(), values.get(2).toInt())
    }

    private fun getPicture(picture: ProfilePicture?): String? {
        if (picture != null) {
            return storageService.url(picture.pictureId)
        } else return null
    }

    private fun getPicturesTask(pictures: List<TaskPicture>?): List<String>? {
        if (pictures != null && !pictures.isEmpty()) {
            return pictures.map { storageService.url(it.pictureId) }
        } else return null
    }
}