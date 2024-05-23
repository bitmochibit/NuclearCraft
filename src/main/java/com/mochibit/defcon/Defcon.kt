package com.mochibit.defcon

import com.mochibit.defcon.Defcon.Companion.Logger.info
import com.mochibit.defcon.events.radiationarea.RadiationSuffocationEvent
import com.mochibit.defcon.extensions.getRadiationLevel
import com.mochibit.defcon.extensions.increaseRadiationLevel
import com.mochibit.defcon.radiation.RadiationArea
import com.mochibit.defcon.radiation.RadiationAreaFactory
import com.mochibit.defcon.registers.*
import com.mochibit.defcon.save.savedata.PlayerDataSave
import com.mochibit.defcon.threading.runnables.ScheduledRunnable
import io.papermc.lib.PaperLib
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class Defcon : JavaPlugin() {

    // This whole stuff will be moved, this is just for testing
    val scheduledRunnable: ScheduledRunnable = ScheduledRunnable()
    val asyncRunnable: ScheduledRunnable = ScheduledRunnable()

    override fun onLoad() {
        instance = this

        // Register datapack
        DatapackRegister.get.registerPack()
    }

    override fun onEnable() {

        PaperLib.suggestPaper(this)

        getLogger().info("[Defcon] has been enabled!")

        info("Registering resource pack")
        ResourcePackRegister.get.registerPack()


        /* Register all plugin's listeners */
        EventRegister().registerEvents()

        /* Register definitions items */
        if (!ItemRegister().registerItems()) {
            getLogger().warning("[Defcon] Some items were not registered!")
        }

        /* Register definitions blocks */
        BlockRegister().registerBlocks()

        /* Register commands */
        CommandRegister().registerCommands()

        /* Register structures */
        StructureRegister().registerStructures()

        // TODO: Thread pool for the scheduledRunnable, so there's a static class and there are a dynamic number of scheduledRunnable instances.
        // For example, if we drop 5 nukes, we want to have 5 scheduledRunnable instances running at the same time, not just one, so we can process the nukes in parallel
        // (for now they are processed sequentially)


        Bukkit.getScheduler().runTaskTimer(this, this.scheduledRunnable, 0L, 1L);
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, this.asyncRunnable, 0L, 1L);


        //TODO: Refactor
        Bukkit.getScheduler().runTaskTimer(
            this, Runnable {
                // Loop through every player and check if they are in a radiation area
                for (player in Bukkit.getOnlinePlayers()) {
                    val radCheck = RadiationArea.shouldSuffocate( player.location.add(0.0, 1.0, 0.0))
                    if (!radCheck.first) {
                        continue;
                    }

                    for (radiationArea in radCheck.second) {
                        val radSuffocateEvent = RadiationSuffocationEvent(player, radiationArea)
                        Bukkit.getPluginManager().callEvent(radSuffocateEvent)

                        if (radSuffocateEvent.isCancelled()) {
                            continue
                        }

                        player.damage(1.0)
                        player.increaseRadiationLevel(0.5)
                    }

                    player.increaseRadiationLevel(0.1)
                }
            },
            0, 20
        )

        Bukkit.getScheduler().runTaskTimer(
            this, Runnable {

                for (player in Bukkit.getOnlinePlayers()) {
                    val radLevel = player.getRadiationLevel()
                    // Decrease max health
                    player.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH)?.baseValue = 20.0 - radLevel.coerceAtMost(20.0)

                    if (radLevel > 5.0) {
                        // Give nausea effect
                        player.addPotionEffect(org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.GLOWING, 100, 1))
                    }
                    if (radLevel > 10.0) {
                        // Give poison effect
                        player.addPotionEffect(org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.CONFUSION, 100, 1))
                    }

                    if (radLevel > 15.0) {
                        // Give poison effect
                        player.addPotionEffect(org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.SLOW, 100, 1))
                        player.addPotionEffect(org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.SLOW_DIGGING, 100, 1))
                    }
                }
            },
            0, 20
        )

    }

    override fun onDisable() {
        getLogger().info("[Defcon] has been disabled!")
        // Disconnect from all databases
        //Database.disconnectAll()
    }

    companion object {
        lateinit var instance: Defcon;

        var namespace = "defcon"

        object Logger {
            fun info(message: String) {
                instance.getLogger().info(message)
            }

            fun warning(message: String) {
                instance.getLogger().warning(message)
            }

            fun severe(message: String) {
                instance.getLogger().severe(message)
            }
        }
    }
}
