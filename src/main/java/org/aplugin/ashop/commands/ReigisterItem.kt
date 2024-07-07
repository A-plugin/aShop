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

class ReigisterItem:TabExecutor {
    fun ReigisterItem(plugin: JavaPlugin) {
        plugin.getCommand("아이템등록")?.setExecutor(this)
        plugin.getCommand("아이템등록")?.setTabCompleter(this)
    }

    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<out String>?): Boolean {
        if (p0 !is Player) return true
        if (!p0.hasPermission("ashop.item"))
        if (p0.inventory.itemInMainHand.isEmpty) {
            p0.sendMessage(
                Component.text("[aShop] 등록하려는 아이템이 존재하지 않습니다.")
                    .color(TextColor.color(255, 100, 100))
                    .decorate(TextDecoration.BOLD)
            )
            return false
        }
        p0.openInventory(Gui().ItemS(p0))
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