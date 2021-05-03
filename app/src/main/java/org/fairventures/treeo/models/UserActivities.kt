package org.fairventures.treeo.models


data class UserActivities(
    var firstName: String,
    var lastName: String,
    var email: String,
    var phoneNumber: String,
    var country: String,
    var username: String,
    var plannedActivites: List<PlannedActivity>
)
