package com.mochibit.defcon.save.savedata

import com.mochibit.defcon.save.AbstractSaveData
import com.mochibit.defcon.save.schemas.PlayerDataSchema
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.function.Supplier

@SaveDataInfo("player_data", "/player_data/")
class PlayerDataSave(val uuid: String) :
    AbstractSaveData<PlayerDataSchema>(PlayerDataSchema()) {
    companion object {
        private val cachedPlayerData = ConcurrentHashMap<String, PlayerDataSchema>()
    }
    init {
        setSuffixSupplier { "-$uuid" }
    }
    private fun getCacheOrLoad(): PlayerDataSchema {
        if (cachedPlayerData.containsKey(uuid)) {
            val cached = cachedPlayerData[uuid]
            if (cached != null) {
                schema = cached
                return schema
            }
        }
        load()
        cachedPlayerData[uuid] = schema

        return this.schema
    }
    fun unload() {
        getCacheOrLoad()
        save()
        cachedPlayerData.remove(uuid)
    }
    fun getRadiationLevel(): Double {
        return getCacheOrLoad().radiationLevel
    }
    fun setRadiationLevel(radiationLevel: Double) {
        getCacheOrLoad()
        this.schema.radiationLevel = radiationLevel
        save()
        cachedPlayerData.remove(uuid)
    }
}




