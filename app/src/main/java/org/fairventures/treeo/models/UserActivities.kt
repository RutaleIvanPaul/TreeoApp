package org.fairventures.treeo.models

import com.google.gson.annotations.SerializedName
import org.fairventures.treeo.services.models.ActivityDTO


data class UserActivities(
    @SerializedName("firstName")
    var firstName: String,
    @SerializedName("lastName")
    var lastName: String,
    @SerializedName("email")
    var email: String,
    @SerializedName("phoneNumber")
    var phoneNumber: String,
    @SerializedName("country")
    var country: String,
    @SerializedName("username")
    var username: String,
    @SerializedName("plannedActivites")
    var plannedActivities: List<ActivityDTO>
)
