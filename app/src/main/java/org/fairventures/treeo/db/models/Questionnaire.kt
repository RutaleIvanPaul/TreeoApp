package org.fairventures.treeo.db.models


data class Questionnaire (
    var activity_id_from_remoteDB: Long,
    var questionnaire_id_from_remote: Long,
    var questionnaire_title: Map<String,String>,
    var pages: Array<Page>
)