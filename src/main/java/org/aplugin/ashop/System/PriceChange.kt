package org.aplugin.ashop.System

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.TextColor
import org.aplugin.ashop.Listeners.Listeners
import org.aplugin.ashop.Shop
import org.aplugin.ashop.gui.Gui
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class PriceChange: Listener {
    var sellCount=0
    var buyCount=0
    val shop=Shop.Instance!!
    val fndpg=Listeners().findPage()
    val lst=Listeners()

    @EventHandler
    fun ClickEvent(e:InventoryClickEvent) {
        val i= e.clickedInventory ?: return
        if (e.view.title.contains("상점")) {
            if (e.slot>44) return
            val itemType = e.inventory.getItem(e.slot)?.type?.name ?: return
            val item=e.inventory.getItem(e.slot)!!
            sellCount=shop.config.getInt("page$fndpg.$itemType.count.sell")
            buyCount=shop.config.getInt("page$fndpg.$itemType.count.buy")

            if (e.click.isRightClick) {
                val m=Material.getMaterial(itemType) ?: Material.AIR
                if (e.click.isShiftClick) {
                    if (!e.whoClicked.inventory.contains(m,64)) return
                } else {
                    if (!e.whoClicked.inventory.contains(m)) return
                }
            }

            if (sellCount>=50) {
                sellCount=0
                SetPriceSell(item)
            }
            if (buyCount>=50) {
                buyCount=0
                SetPriceBuy(item)
            }

            if (e.click.isShiftClick) {
                if (e.click.isLeftClick) {
                    buyCount+=10
                }
                if (e.click.isRightClick) {
                    sellCount+=10
                }
            } else {
                if (e.click.isLeftClick) {
                    buyCount++
                }
                if (e.click.isRightClick) {
                    sellCount++
                }
            }
            shop.config.set("page$fndpg.$itemType.count.sell", sellCount)
            shop.config.set("page$fndpg.$itemType.count.buy",buyCount)
            shop.saveConfig()

        }
    }

    fun SetPriceSell(item: ItemStack) {
        val buyPrice=shop.config.getInt("page${fndpg}.${item.type.name}.price.buy")
        val price= Math.round(buyPrice * 0.9)
        val itemType = item.type.name
        shop.config.set("page$fndpg.$itemType.price.buy", price)
        shop.saveConfig()
    }

    fun SetPriceBuy(item: ItemStack) {
        val itemType = item.type.name
        val sellPrice=shop.config.getInt("page${fndpg}.${item.type.name}.price.sell")
        val price= Math.round(sellPrice * 1.1)
        shop.config.set("page$fndpg.$itemType.price.sell", price)
        shop.saveConfig()
    }

}