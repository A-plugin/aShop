package org.aplugin.ashop.System

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.TextColor
import org.aplugin.ashop.Listeners.Listeners
import org.aplugin.ashop.Shop
import org.aplugin.ashop.gui.Gui
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

class PriceChange: Listener {
    var sellCount=0
    var buyCount=0
    val shop=Shop.Instance!!
    val fndpg=Listeners().findPage()
    val lst=Listeners()


    @EventHandler
    fun ClickEvent(e:InventoryClickEvent) {
        val i= e.clickedInventory ?: return
        if (!shop.config.getBoolean("PriceC")) return
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

            if (sellCount>=300) {
                //SetPriceSell(item)
            }
            if (buyCount>=300) {
                //SetPriceBuy(item)
            }

            if (e.click.isShiftClick) {
                if (e.click.isLeftClick) {
                    sellCount+=10
                }
                if (e.click.isRightClick) {
                    sellCount+=10
                }
            } else {
                if (e.click.isLeftClick) {
                    sellCount++
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

    fun PriceC() {
        var priceP = shop.config.getInt("PriceP")
        var priceH= shop.config.getInt("PriceH")
        var time = shop.config.getLong("PriceT")


        if (shop.config.getBoolean("PriceC")) {
            Bukkit.getScheduler().runTaskTimer(shop, Runnable {
                priceP = shop.config.getInt("PriceP")
                time = shop.config.getLong("PriceT")
                priceH= shop.config.getInt("PriceH")
                for (i in 0..1000) {
                    val pageN = shop.config.getConfigurationSection("page$i") ?: continue
                    for (key in pageN.getKeys(false)) {
                        val itemName = pageN.getString("$key.item") ?: continue
                        val material = Material.getMaterial(itemName) ?: continue

                        val item = ItemStack(material)

                        var sellPrice = pageN.getInt("$key.price.sell")
                        var buyPrice = pageN.getInt("$key.price.buy")

                        var sellCount = pageN.getInt("$key.count.sell")
                        var buyCount = sellCount

                        if (buyCount < priceH) {
                            buyPrice = Math.round((buyPrice * (1 - (priceP / 100.0))).toFloat())
                            sellPrice = Math.round((sellPrice * (1 + (priceP / 100.0))).toFloat())
                        } else {
                            buyCount=0
                            sellCount=0
                            buyPrice = Math.round((buyPrice * (1 + (priceP / 100.0))).toFloat())
                            sellPrice = Math.round((sellPrice * (1 - (priceP / 100.0))).toFloat())
                        }

                        if (sellPrice<=0) sellPrice=0
                        if (buyPrice<=0) buyPrice=0

                        shop.config.set("page$i.$key.price.sell",sellPrice)
                        shop.config.set("page$i.$key.price.buy",buyPrice)
                        shop.config.set("page$i.$key.count.sell",sellCount)
                        shop.config.set("page$i.$key.count.buy",buyCount)
                        shop.saveConfig()
                    }
                }
            }, time, time)
        }
    }



    /*
    안쓰는 코드
    fun SetPriceSell(item: ItemStack) {
        val itemType = item.type.name
        val sellPrice=shop.config.getInt("page${fndpg}.${item.type.name}.price.sell")
        var price= Math.round((sellPrice * (100-priceP/100)).toDouble()) //가격 하강
        shop.config.set("page$fndpg.$itemType.price.sell", price)

        val buyPrice=shop.config.getInt("page${fndpg}.${item.type.name}.price.buy")
        price= Math.round(buyPrice * 0.7)
        shop.config.set("page$fndpg.$itemType.price.buy", price)
        shop.saveConfig()
    }

    fun SetPriceBuy(item: ItemStack) {
        val itemType = item.type.name
        val sellPrice=shop.config.getInt("page${fndpg}.${item.type.name}.price.sell")
        var price= Math.round(sellPrice * 1.1)
        shop.config.set("page$fndpg.$itemType.price.sell", price)

        val buyPrice=shop.config.getInt("page${fndpg}.${item.type.name}.price.buy")
        price= Math.round(buyPrice * 1.2)
        shop.config.set("page$fndpg.$itemType.price.buy", price)
        shop.saveConfig()
    }

     */

}