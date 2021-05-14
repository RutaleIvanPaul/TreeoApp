package org.fairventures.treeo.db.models.mappers

import org.fairventures.treeo.db.models.*
import org.fairventures.treeo.services.models.*
import org.fairventures.treeo.util.EntityMapper
import javax.inject.Inject

class ActivityDtoToEntityMapper @Inject constructor() :
    EntityMapper<ActivityDTO, ActivityEntity> {
    override fun toEntity(obj: ActivityDTO): ActivityEntity {
        return ActivityEntity(
            null,
            obj.id,
            obj.dueDate,
            obj.completedByActivity,
            obj.title,
            obj.description,
            obj.plot,
            getActivityTemplate(obj.activityTemplate),
        )
    }

    override fun fromEntity(obj: ActivityEntity): ActivityDTO {
        TODO("Not yet implemented")
    }

    private fun getActivityTemplate(dto: ActivityTemplateDTO): ActivityTemplate {
        return ActivityTemplate(
            dto.id,
            dto.activityType,
            dto.code,
            dto.preQuestionnaireId,
            dto.postQuestionnaireId
        )
    }

    fun getQuestionnaireEntity(activityId: Long, dto: QuestionnaireDTO): QuestionnaireEntity {
        return QuestionnaireEntity(
            null,
            activityId,
            dto.id,
            dto.projectId
        )
    }

    fun getPageEntity(questionnaireId: Long, dto: PageDTO): PageEntity {
        return PageEntity(
            null,
            questionnaireId,
            dto.pageType,
            dto.questionCode,
            getPageEntityHeader(dto),
            getPageEntityDescription(dto)
        )
    }

    private fun getPageEntityHeader(dto: PageDTO): Header {
        return Header(
            dto.header["en"],
            dto.header["in"]
        )
    }

    private fun getPageEntityDescription(dto: PageDTO): Description {
        return Description(
            dto.description["en"],
            dto.description["in"]
        )
    }

    fun getOptionEntity(pageId: Long, dto: OptionDTO): OptionEntity {
        return OptionEntity(
            null,
            pageId,
            dto.code,
            false,
            getOptionEntityTitle(dto)
        )
    }

    private fun getOptionEntityTitle(dto: OptionDTO): Title {
        return Title(
            dto.title["en"],
            dto.title["in"]
        )
    }
}
