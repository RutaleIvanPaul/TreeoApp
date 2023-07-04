package org.fairventures.treeo.util.mappers

import org.fairventures.treeo.db.models.ActivityEntity
import org.fairventures.treeo.db.models.OptionEntity
import org.fairventures.treeo.db.models.PageEntity
import org.fairventures.treeo.db.models.Title
import org.fairventures.treeo.models.Activity
import org.fairventures.treeo.models.ActivityTemplate
import org.fairventures.treeo.models.Option
import org.fairventures.treeo.models.Page
import javax.inject.Inject

class ModelEntityMapper @Inject constructor() {

    fun getActivityFromEntity(entity: ActivityEntity): Activity {
        return Activity(
            entity.activityId!!,
            entity.activityRemoteId!!,
            entity.dueDate!!,
            entity.isComplete,
            entity.title!!,
            entity.description!!,
            entity.plot,
            getActivityTemplateFromEntity(entity)
        )
    }

    fun getActivityTemplateFromEntity(entity: ActivityEntity): ActivityTemplate {
        return ActivityTemplate(
            entity.template.templateRemoteId!!,
            entity.template.activityType!!,
            entity.template.code!!,
            entity.template.preQuestionnaireId!!,
            entity.template.postQuestionnaireId!!
        )
    }

    fun getListOfActivitiesFromEntities(entities: List<ActivityEntity>): List<Activity> {
        return entities.map { getActivityFromEntity(it) }
    }

    fun getPageFromEntity(entity: PageEntity): Page {
        return Page(
            entity.pageId!!,
            entity.pageType!!,
            entity.questionCode!!,
            getPageHeaderFromEntity(entity),
            getPageDescriptionFromEntity(entity),
            null
        )
    }

    fun getListOfPageFromEntities(entities: List<PageEntity>): List<Page> {
        return entities.map { getPageFromEntity(it) }
    }

    fun getPageHeaderFromEntity(entity: PageEntity): Map<String, String> {
        return mapOf(
            "en" to entity.header?.headerEn!!,
            "in" to entity.header.headerIn!!
        )
    }

    fun getPageDescriptionFromEntity(entity: PageEntity): Map<String, String> {
        return mapOf(
            "en" to entity.description!!.descEn!!,
            "in" to entity.description.descIn!!
        )
    }

    fun getOptionFromEntity(entity: OptionEntity): Option {
        return Option(
            entity.optionId!!,
            entity.pageId!!,
            entity.code!!,
            entity.isSelected,
            getOptionTitleFromEntity(entity)
        )
    }

    fun getOptionTitleFromEntity(entity: OptionEntity): Map<String, String> {
        return mapOf(
            "en" to entity.title!!.titleEn!!,
            "in" to entity.title.titleIn!!
        )
    }

    fun getListOfOptionsFromEntities(entities: List<OptionEntity>): List<Option> {
        return entities.map { getOptionFromEntity(it) }
    }

    fun getOptionEntityFromModel(option: Option): OptionEntity {
        return OptionEntity(
            option.optionId,
            option.pageId,
            option.code,
            option.isSelected,
            getOptionEntityTitleFromModel(option)
        )
    }

    fun getOptionEntityTitleFromModel(option: Option): Title {
        return Title(option.title["en"], option.title["in"])
    }
}
