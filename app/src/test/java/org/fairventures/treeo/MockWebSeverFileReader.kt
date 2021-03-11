package org.fairventures.treeo

import java.io.InputStreamReader

class MockWebSeverFileReader(path: String) {
    var content: String

    init {
        val reader = InputStreamReader(this.javaClass.classLoader!!.getResourceAsStream(path))
        content = reader.readText()
        reader.close()
    }
}

