package com.mochibit.nuclearcraft.services

import com.mochibit.nuclearcraft.NuclearCraft
import com.mochibit.nuclearcraft.classes.CustomBlockDefinition
import com.mochibit.nuclearcraft.classes.PluginConfiguration
import com.mochibit.nuclearcraft.enums.BlockBehaviour
import com.mochibit.nuclearcraft.enums.BlockDataKey
import com.mochibit.nuclearcraft.enums.ConfigurationStorages
import com.mochibit.nuclearcraft.exceptions.BlockNotRegisteredException
import com.mochibit.nuclearcraft.interfaces.PluginBlock
import com.mochibit.nuclearcraft.utils.MetaManager
import org.bukkit.Location
import org.bukkit.plugin.java.JavaPlugin

/**
 * This class handles the registration of the custom blocks
 * All the registered items are stored and returned in a form of a HashMap(id, CustomBlock
 *
 */
class BlockRegister() {
    private val pluginInstance: JavaPlugin = JavaPlugin.getPlugin(NuclearCraft::class.java)

    /**
     *
     */
    fun registerBlocks() {
        registeredBlocks = HashMap()
        /* REGISTER THE ITEMS COMING FROM THE CONFIG */
        val blockConfiguration = PluginConfiguration(pluginInstance, ConfigurationStorages.Blocks)
        val blockConfig = blockConfiguration.config
        blockConfig!!.getList("enabled-blocks")!!.forEach { item: Any? ->

            val blockId = item.toString()
            if (registeredBlocks!![blockId] != null) return@forEach
            val blockMinecraftId = blockConfig.getString("$item.block-minecraft-id") ?: throw BlockNotRegisteredException(blockId)
            val blockDataModelId = blockConfig.getInt("$item.block-data-model-id")


            var behaviourName = blockConfig.getString("$item.behaviour")
            if (behaviourName == null) {
                behaviourName = "generic"
            }
            val behaviourValue = BlockBehaviour.fromString(behaviourName) ?: throw BlockNotRegisteredException(blockId)

            val customBlock: PluginBlock = CustomBlockDefinition(
                id = blockId,
                customModelId = blockDataModelId,
                minecraftId = blockMinecraftId,
                behaviour = behaviourValue)

            registeredBlocks!![customBlock.id] = customBlock
        }
        blockConfiguration.saveConfig()
    }

    companion object {
        /**
         * Static member to access the registered items
         */
        private var registeredBlocks: HashMap<String?, PluginBlock?>? = null

        fun getBlock(location: Location): PluginBlock? {
            val customBlockId = MetaManager.getBlockData<String>(location, BlockDataKey.CustomBlockId) ?: return null
            return getBlock(customBlockId)
        }

        fun getBlock(id: String): PluginBlock? {
            return registeredBlocks!![id]
        }

        /* Registered items getter */
        @Throws(BlockNotRegisteredException::class)
        fun getRegisteredBlocks(): HashMap<String?, PluginBlock?>? {
            if (registeredBlocks == null) {
                throw BlockNotRegisteredException("Block not registered for some reason. Verify the initialization")
            }
            return registeredBlocks
        }
    }
}