package com.mochibit.defcon.save.manager

import com.mochibit.defcon.radiation.RadiationArea
import com.mochibit.defcon.radiation.RadiationAreaFactory
import com.mochibit.defcon.save.savedata.RadiationAreaSave
import com.mochibit.defcon.save.strategy.SaveStrategy
import java.util.concurrent.atomic.AtomicInteger

// TODO: Implement a DAO pattern for entities which are saved to the database/flat file (depending on save strategy)
class RadiationAreaManager : SaveStrategy<RadiationArea> {
    companion object {
        private val cachedNextId: AtomicInteger = AtomicInteger(0)
        fun getNextId(): Int {
            if (cachedNextId.get() == 0) {
                val saveData = RadiationAreaSave().load()
                val currSize = saveData.saveData.radiationAreas.size
                cachedNextId.set(currSize)
            }
            return cachedNextId.get() + 1
        }
    }

    override fun save(data: RadiationArea) {
        val saveData = RadiationAreaSave().load()
        saveData.saveData.radiationAreas.add(data)
        saveData.save()
        cachedNextId.getAndIncrement()
    }

    override fun getAll(): HashSet<RadiationArea> { // Load the data
        val saveData = RadiationAreaSave().load()
        return saveData.saveData.radiationAreas
    }

    override fun get(id: Int): RadiationArea {
        val saveData = RadiationAreaSave().load()
        return saveData.saveData.radiationAreas.find { it.id == id }!!
    }

    override fun delete(data: RadiationArea) {
        val saveData = RadiationAreaSave().load()
        saveData.saveData.radiationAreas.remove(data)
        saveData.save()
    }

}