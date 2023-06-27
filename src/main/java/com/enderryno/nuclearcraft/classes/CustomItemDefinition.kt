package com.enderryno.nuclearcraft.classes

import com.enderryno.nuclearcraft.NuclearCraft
import com.enderryno.nuclearcraft.enums.ItemBehaviour
import com.enderryno.nuclearcraft.enums.ItemDataKey
import com.enderryno.nuclearcraft.interfaces.PluginItem
import com.enderryno.nuclearcraft.utils.ColorParser
import com.enderryno.nuclearcraft.utils.MetaManager
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

/**
 * This class defines a custom item
 * It provides a way to generate effective minecraft items from this definition
 */
class CustomItemDefinition(
    override val id: String,
    override val name: String,
    override val description: String?,
    override val minecraftId: String,
    override val modelId: Int,
    override val customBlockId: String?,
    override val isUsable: Boolean,
    override val isEquipable: Boolean,
    override val isDroppable: Boolean,
    override val stackSize: Int,
    override val isTransportable: Boolean,
    override val behaviour: ItemBehaviour
) : PluginItem {
    /*Getting instance of the minecraft plugin (in theory O(1) complexity) */
    private val plugin: JavaPlugin = JavaPlugin.getPlugin(NuclearCraft::class.java)

    override val displayName: String
        get() {
            // Strip color codes from the name
            return ColorParser.stripColor(name)
        }

    override val itemStack: ItemStack
        /* Instantiation */ // Suppressing deprecation warning for the ItemStack constructor (Paper api is slightly different)
        get() {
            val material = Material.getMaterial(minecraftId)
                ?: throw IllegalArgumentException("Material $minecraftId does not exist")
            val customItem = ItemStack(material)

            /* Meta assignment */
            val itemMeta = customItem.itemMeta
            itemMeta.setDisplayName(ColorParser.parseColor(name))
            itemMeta.lore =
                ColorParser.parseColor(Arrays.asList(*description!!.split("\n".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()))

            MetaManager.setItemData(itemMeta, ItemDataKey.ItemID, id)
            MetaManager.setItemData(itemMeta, ItemDataKey.StackSize, stackSize)
            MetaManager.setItemData(itemMeta, ItemDataKey.Usable, isUsable)
            MetaManager.setItemData(itemMeta, ItemDataKey.Equipable, isEquipable)
            MetaManager.setItemData(itemMeta, ItemDataKey.Droppable, isDroppable)
            MetaManager.setItemData(itemMeta, ItemDataKey.Transportable, isTransportable)
            MetaManager.setItemData(itemMeta, ItemDataKey.Behaviour, behaviour.name)
            if (customBlockId != null) {
                MetaManager.setItemData(itemMeta, ItemDataKey.CustomBlockId, customBlockId)
            }

            itemMeta.setCustomModelData(modelId)
            customItem.setItemMeta(itemMeta)
            /* Properties assignment */return customItem
        }
}
