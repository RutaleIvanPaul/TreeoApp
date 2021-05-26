package org.fairventures.treeo.db.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Page")
data class PageEntity(
    @PrimaryKey(autoGenerate = true)
    val pageId: Long?,
    val questionnaireId: Long?,
    val pageType: String?,
    val questionCode: String?,
    @Embedded val header: Header?,
    @Embedded val description: Description?
)

data class Header(
    val headerEn: String?,
    val headerIn: String?,
)

data class Description(
    val descEn: String?,
    val descIn: String?
)
