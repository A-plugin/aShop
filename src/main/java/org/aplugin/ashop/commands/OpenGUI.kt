package org.aplugin.ashop.commands

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.aplugin.ashop.gui.Gui
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

class OpenGUI:TabExecutor {
    fun OpenGUI(plugin: JavaPlugin) {
        plugin.getCommand("open")?.setExecutor(this)
        plugin.getCommand("open")?.setTabCompleter(this)
    }

    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<out String>?): Boolean {
        if (p0 !is Player) return false
        if (p0.hasPermission("ashop.open")){
            val i=Gui().openS()
            if (p0.hasPermission("ashop.edit")) {
                val edit= ItemStack(Material.RED_STAINED_GLASS_PANE)
                val editM=edit.itemMeta
                editM.displayName(Component.text("아이템 배치 수정").color(TextColor.color(0xC80036)).decorate(TextDecoration.BOLD))
                edit.itemMeta=editM
                //i.setItem(49,edit)
            }
            p0.openInventory(i)
            return true
        }
        p0.sendMessage("§f[§aaShop§f] §c권한이 없습니다!")
        return true
    }

    override fun onTabComplete(
        p0: CommandSender,
        p1: Command,
        p2: String,
        p3: Array<out String>?
    ): MutableList<String> {
        return mutableListOf()
    }
}