package org.aplugin.ashop

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import net.milkbowl.vault.economy.Economy
import org.aplugin.ashop.Listeners.Listeners
import org.aplugin.ashop.System.PriceChange
import org.aplugin.ashop.commands.OpenGUI
import org.aplugin.ashop.commands.ReigisterItem
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Shop : JavaPlugin() {

    override fun onEnable() {
        if (!setupEconomy()) {
            logger.severe("Vault 플러그인을 찾을 수 없습니다.")
            server.pluginManager.disablePlugin(this)
            return
        }

        if (Instance != null) return
        Instance = this
        logger.info(
            Component.text("Made By.아포칼립스").color(TextColor.color(0xB9AAFF)).decorate(
            TextDecoration.BOLD).content())

        //=====[Commands]=====
        OpenGUI().OpenGUI(this)
        ReigisterItem().ReigisterItem(this)

        //=====[Listener]=====
        server.pluginManager.registerEvents(Listeners(),this)
        server.pluginManager.registerEvents(PriceChange(),this)
        //=====[Config]======
        saveConfig()
    }

    private fun setupEconomy(): Boolean {
        if (server.pluginManager.getPlugin("Vault") == null) {
            return false
        }
        val rsp = server.servicesManager.getRegistration(
            Economy::class.java
        )
        if (rsp == null) {
            return false
        }
        economy = rsp.provider
        return true
    }

    fun getEconomy(): Economy {
        return economy!!
    }

    companion object {
        var Instance: Shop? = null
        var economy: Economy? = null
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
