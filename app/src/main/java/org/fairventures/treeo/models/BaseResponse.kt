package org.fairventures.treeo.models

data class BaseResponse<T>(var data: T, var message: String)