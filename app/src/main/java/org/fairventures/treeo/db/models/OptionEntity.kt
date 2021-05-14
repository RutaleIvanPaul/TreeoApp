package org.fairventures.treeo.db.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Option")
class OptionEntity(
    @PrimaryKey(autoGenerate = true)
    val optionId: Long?,
    val pageId: Long?,
    val code: String?,
    val isSelected: Boolean = false,
    @Embedded val title: Title?
)

data class Title(
    val titleEn: String?,
    val titleIn: String?
)
