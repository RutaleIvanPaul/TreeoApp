package org.fairventures.treeo.db.models.relations

import androidx.room.Embedded
import androidx.room.Relation
import org.fairventures.treeo.db.models.OptionEntity
import org.fairventures.treeo.db.models.PageEntity

data class PageWithOptions(
    @Embedded val page: PageEntity,
    @Relation(parentColumn = "pageId", entityColumn = "pageId")
    val options: List<OptionEntity>
)