package bg.elsys.jobche.entity.model.work

import bg.elsys.jobche.entity.BaseEntity
import bg.elsys.jobche.entity.model.task.Task
import bg.elsys.jobche.entity.model.user.Review
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.*

@Entity
@Table(name = "works")
@JsonIgnoreProperties("participations")
data class Work(

        @OneToOne(fetch = FetchType.EAGER)
        val task: Task,

        @OneToMany(fetch = FetchType.LAZY, mappedBy = "work", cascade = [CascadeType.REMOVE])
        var participations: List<Participation> = emptyList(),

        @Enumerated(EnumType.STRING)
        var status: WorkStatus = WorkStatus.IN_PROGRESS,

        @OneToMany(fetch = FetchType.LAZY, mappedBy = "work", cascade = [CascadeType.REMOVE])
        var reviews: List<Review> = emptyList()) : BaseEntity() {

    override fun toString(): String {
        return ""
    }
}

enum class WorkStatus {
    IN_PROGRESS, ENDED
}
