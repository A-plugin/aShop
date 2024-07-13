package org.aplugin.ashop.commands

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.aplugin.ashop.Shop
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Price: TabExecutor {
    fun Price(plugin: JavaPlugin) {
        plugin.getCommand("가격변동")?.setExecutor(this)
        plugin.getCommand("가격변동")?.tabCompleter = this
    }
    val shop=Shop.Instance!!

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) return false
        if (!sender.hasPermission("ashop.price")) return true

        if (args.isEmpty()) {
            sender.sendMessage("${ChatColor.RED}사용법: /가격변동 [가격변동비율/가격변동켜기/가격변동끄기/가격변동간격/가격변동기준]")
            sender.sendMessage(Component.text("가격 변동 정보" +
                    "가격변동 on/off: ${shop.config.getBoolean("PriceC")}"
                    +"가격변동 비율: ${shop.config.getInt("PriceP")}"+
                    "가격벼동간격: ${shop.config.get("PriceT")}"
            ))
            return true
        }

        when (args[0]) {
            "가격변동켜기" -> {
                shop.config.set("PriceC", true)
                sender.sendMessage(Component.text("[aShop] 가격변동을 활성화했습니다")
                    .color(TextColor.color(0x508D4E)).decorate(TextDecoration.BOLD))
                shop.saveConfig()
            }
            "가격변동끄기" -> {
                shop.config.set("PriceC", false)
                sender.sendMessage(Component.text("[aShop] 가격변동을 비활성화했습니다")
                    .color(TextColor.color(0x973131)).decorate(TextDecoration.BOLD))
                shop.saveConfig()
            }
            "가격변동비율" -> {
                if (args.size > 1) {
                    val rate = args[1].toIntOrNull()
                    if (rate != null) {
                        shop.config.set("PriceP", rate)
                        shop.saveConfig()
                        sender.sendMessage(Component.text("[aShop] 가격변동비율을 $rate%로 설정했습니다.")
                            .color(TextColor.color(0xBBE9FF)).decorate(TextDecoration.BOLD))
                    } else {
                        sender.sendMessage("유효한 숫자를 입력하세요.")
                    }
                } else {
                    sender.sendMessage("가격변동비율을 입력하세요.")
                }
            }
            "가격변동간격"->{
                if (args.size > 1) {
                    val time = args[1].toIntOrNull()
                    if (time != null) {
                        shop.config.set("PriceT", time*20)
                        shop.saveConfig()
                        sender.sendMessage(Component.text("[aShop] 가격변동간격(s)을 $time(s)로 설정했습니다.")
                            .color(TextColor.color(0xBBE9FF)).decorate(TextDecoration.BOLD))
                    } else {
                        sender.sendMessage("유효한 숫자를 입력하세요.")
                    }
                } else {
                    sender.sendMessage("가격변동간격(s)을 입력하세요.")
                }
            }
            "가격변동기준"-> {
                if (args.size > 1) {
                    val count = args[1].toIntOrNull()
                    if (count != null) {
                        shop.config.set("PriceH", count)
                        shop.saveConfig()
                        sender.sendMessage(Component.text("[aShop] 가격변동기준(정수)을 $count(s)로 설정했습니다.")
                            .color(TextColor.color(0xBBE9FF)).decorate(TextDecoration.BOLD))
                    } else {
                        sender.sendMessage("유효한 숫자를 입력하세요.")
                    }
                } else {
                    sender.sendMessage("가격변동기준(정수)을 입력하세요.")
                }
            }
            else -> {
                sender.sendMessage("사용법: /가격변동 [가격변동비율/가격변동켜기/가격변동끄기/가격변동기준]")
            }
        }
        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): MutableList<String> {
        val tab = mutableListOf<String>()
        if (args.isEmpty() || args.size == 1) {
            tab.add("가격변동켜기")
            tab.add("가격변동끄기")
            tab.add("가격변동비율")
            tab.add("가격변동간격")
            tab.add("가격변동기준")
        } else if (args.size == 2) {
            if (args[0] == "가격변동비율"){
                tab.add("10")
                tab.add("20")
                tab.add("30")
            }
            if (args[0] == "가격변동간격"){
                tab.add("900")
                tab.add("1800")
                tab.add("60")
            }
            if (args[0] == "가격변동기준"){
                tab.add("300")
            }
        }
        return tab
    }
}