package org.aplugin.ashop.commands

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.aplugin.ashop.Shop
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.checkerframework.checker.index.qual.IndexFor

class Price: TabExecutor {
    fun Price(plugin: JavaPlugin) {
        plugin.getCommand("가격변동")?.setExecutor(this)
        plugin.getCommand("가격변동")?.tabCompleter = this
    }
    val shop=Shop.Instance!!

    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<out String>?): Boolean {
        if (p0 !is Player) return false
        if (!p0.hasPermission("ashop.price")) return true
        if (p3==null) {
            p0.sendMessage("사용법: /가격변동 [가격변동비율/가격변동켜기/가격변동끄기]")
            return true
        }
        if (p3.size <=1) {
            p0.sendMessage("사용법: /가격변동 [가격변동비율/가격변동켜기/가격변동끄기]")
            return true
        }
        if (p3[0].contains("켜기")) {
            shop.config.set("PriceC", true)
            p0.sendMessage(Component.text("[aShop] 가격변동을 비활성화했습니다")
                .color(TextColor.color(0x508D4E)).decorate(TextDecoration.BOLD).content())
            shop.saveConfig()
        }
        if (p3[0].contains("끄기")) {
            shop.config.set("PriceC", false)
            p0.sendMessage(Component.text("[aShop] 가격변동을 활성화했습니다")
                .color(TextColor.color(0x973131)).decorate(TextDecoration.BOLD).content())
            shop.saveConfig()
        }
        if (p3[0].contains("비율")) {
            if (p3[1]!=null) {
                shop.config.set("PriceP", p3[1])
                shop.saveConfig()
                p0.sendMessage(Component.text("[aShop] 가격변동비율을 ${p3[1]}%로 설정했습니다.")
                    .color(TextColor.color(0xBBE9FF)).decorate(TextDecoration.BOLD).content())
            }
        }

        return true
    }

    override fun onTabComplete(
        p0: CommandSender,
        p1: Command,
        p2: String,
        p3: Array<out String>?
    ): MutableList<String> {
        val tab= mutableListOf<String>()
        if (p3==null || p3.isEmpty()){
            tab.add("가격변동켜기")
            tab.add("가격변동끄기")
            tab.add("가격변동비율")
        } else  {
            when (p3[0]) {
                "가격변동비율"-> {
                    tab.add("10")
                    tab.add("20")
                    tab.add("30")
                }
            }
        }
        return tab
    }
}