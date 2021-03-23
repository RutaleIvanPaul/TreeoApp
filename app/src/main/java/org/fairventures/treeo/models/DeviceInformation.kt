package org.fairventures.treeo.models

data class DeviceInformation(
    val advertisingID: String,
    val androidVersion: String,
    val cameraInformation: String,
    val card: String,
    val freeRAM: String,
    val installedApps: List<String>,
    val manufacturer: String,
    val model: String,
    val screenResolution: String,
    val sensors: List<String>,
    val storage: String,
    val totalRAM: String
)
