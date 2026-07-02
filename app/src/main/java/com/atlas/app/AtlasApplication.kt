package com.atlas.app

import android.app.Application
import com.atlas.app.data.AtlasRepository
import com.atlas.app.data.db.AtlasDatabase

class AtlasApplication : Application() {

    val database: AtlasDatabase by lazy {
        AtlasDatabase.getInstance(this)
    }

    val repository: AtlasRepository by lazy {
        AtlasRepository(database)
    }
}
