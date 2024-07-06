package org.aplugin.ashop.Listeners

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.aplugin.ashop.Shop
import org.aplugin.ashop.gui.Gui
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType

class Listeners:Listener {
    val shop=Shop.Instance!!
    var iValue1=0
    var iValue2=0

    @EventHandler
    fun onInventoryClickEvent(e:InventoryClickEvent) {
        val i= e.clickedInventory ?: return
        if (e.view.title.contains("상점")) {
            if (!i.type.equals(InventoryType.CHEST)) return
            when {
                e.currentItem?.type==Material.LIME_STAINED_GLASS_PANE -> {
                    val currentPage = e.view.title.substringAfterLast(" ").toIntOrNull() ?: return
                    val nextPage = currentPage + 1
                    if (shop.config.contains("page$nextPage")) {
                        e.whoClicked.openInventory(Gui().openS(nextPage))
                    }
                }
                e.currentItem?.type==Material.ORANGE_STAINED_GLASS_PANE -> {
                    val currentPage = e.view.title.substringAfterLast(" ").toIntOrNull() ?: return
                    if (currentPage > 1) {
                        e.whoClicked.openInventory(Gui().openS(currentPage - 1))
                    }
                }
            }
            if (e.currentItem?.type?.equals(Material.RED_STAINED_GLASS_PANE) == true) {
                Bukkit.broadcast(Component.text("EDIT SHOP ITEM"))
            }



            e.isCancelled = true
        }

        if (e.view.title.contains("등록")) {
            if (!i.type.equals(InventoryType.CHEST)) return
            val item= e.currentItem ?: return
            if (item.isEmpty) return
            if (item.type.equals(Material.LIME_STAINED_GLASS_PANE)) {
                val itemType = e.inventory.getItem(10)?.type?.name ?: return
                val currentPage = findAvailablePage()
                shop.config.set("page$currentPage.$itemType.item", itemType)
                shop.config.set("page$currentPage.$itemType.price.sell", iValue1)
                shop.config.set("page$currentPage.$itemType.price.buy", iValue2)
                shop.config.set("page$currentPage.$itemType.author", e.whoClicked.name)
                shop.saveConfig()
                e.whoClicked.closeInventory()
            }
            if (item.type.equals(Material.PAPER)) {
                e.whoClicked.closeInventory()
            }
            e.isCancelled = true
        }

        if (e.view.title.contains("가격")) {
            // 이거 작동 안됨
            if (!i.type.equals(InventoryType.ANVIL)) return
            if (e.rawSlot!=2) return
            val item=e.currentItem ?: return
            val input=item.itemMeta.displayName

            val reg = "\\d+"
            if (!input.matches(Regex(reg))) {
                e.view.player.sendMessage(
                    Component.text("[aShop] 숫자만 입력 가능합니다.")
                        .color(TextColor.color(255,100,100))
                        .decorate(TextDecoration.BOLD)
                )
            }
            e.whoClicked.closeInventory()
            if (e.view.title.contains("판매")) {
                iValue1=input.toInt()
            }
            if (e.view.title.contains("구매")) {
                iValue2=input.toInt()
            }
            e.whoClicked.openInventory(Gui().ItemS(e.whoClicked as Player))
            e.isCancelled = true
        }
    }
    fun findAvailablePage(): Int {
        var pageN = 1
        while (true) {
            val page = shop.config.getConfigurationSection("page$pageN")
            if (page == null || page.getKeys(false).size < 45) {
                return pageN
            }
            pageN++
        }
    }
}
