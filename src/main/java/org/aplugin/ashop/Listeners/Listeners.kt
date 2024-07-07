package org.aplugin.ashop.Listeners

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextColor
import org.aplugin.ashop.Shop
import org.aplugin.ashop.gui.Gui
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.ItemStack

class Listeners:Listener {
    val shop=Shop.Instance!!
    val eco= Shop.economy

    var iValue1=0
    var iValue2=0

    @EventHandler
    fun onInventoryClickEvent(e:InventoryClickEvent) {
        val i= e.clickedInventory ?: return
        if (e.view.title.contains("상점")) {
            if (!i.type.equals(InventoryType.CHEST)) return
            if (e.currentItem==null) return
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

            if (e.currentItem?.itemMeta?.hasLore()!!) {
                val itemN= e.currentItem?.type?.name ?: return
                val item=e.inventory.getItem(e.slot)!!
                val itemType = item.type.name
                val sellPrice=shop.config.getInt("page${findPage()}.$itemType.price.sell")
                val buyPrice=shop.config.getInt("page${findPage()}.$itemType.price.buy")
                if (e.click.isShiftClick) {
                    if (e.click.isRightClick) {
                        val item=Material.getMaterial(itemN) ?: Material.AIR
                        if (e.whoClicked.inventory.contains(item,item.maxStackSize)){
                            e.whoClicked.inventory.contents
                                .filterNotNull()
                                .find { it.type == item }
                                ?.let { it.amount -= item.maxStackSize }
                            e.whoClicked.sendMessage(
                                "§f[§aaShop§f]"
                                        + Component.text("아이템을 판매했습니다")
                                    .color(TextColor.color(0x96C9F4))
                                    .content()
                            )
                            eco?.depositPlayer(e.whoClicked as Player, sellPrice.toDouble()*item.maxStackSize)
                            e.whoClicked.sendMessage(
                                "§f[§aaShop§f]"
                                        + Component.text("잔고: ${eco?.getBalance(e.whoClicked as Player)}")
                                    .content()
                            )
                        } else  {
                            e.whoClicked.sendMessage(
                                "§f[§aaShop§f]"
                                        + Component.text("아이템이 충분하지 않습니다!")
                                    .color(TextColor.color(0x96C9F4))
                                    .content()
                            )
                        }
                    }
                    if (e.click.isLeftClick) {
                        if (eco?.has(e.whoClicked as Player, buyPrice.toDouble()*item.maxStackSize)!!){
                            val item = Material.getMaterial(itemN) ?: Material.AIR
                            val itemMaxStack = item.maxStackSize
                            e.whoClicked.inventory.addItem(ItemStack(item, itemMaxStack))
                            e.whoClicked.sendMessage(
                                "§f[§aaShop§f]"
                                        + Component.text("아이템을 구매했습니다")
                                    .color(TextColor.color(0xFF6969))
                                    .content()
                            )
                            eco?.withdrawPlayer(e.whoClicked as Player, buyPrice.toDouble()*64)
                            e.whoClicked.sendMessage(
                                "§f[§aaShop§f]"
                                        + Component.text("잔고: ${eco?.getBalance(e.whoClicked as Player)}")
                                    .content()
                            )
                        }
                    }
                } else {
                    if (e.click.isRightClick) {
                        val item=Material.getMaterial(itemN) ?: Material.AIR
                        if (e.whoClicked.inventory.contains(item)){
                            e.whoClicked.inventory.contents
                                .filterNotNull()
                                .find { it.type == item }
                                ?.let { it.amount -= 1 }
                            e.whoClicked.sendMessage(
                                "§f[§aaShop§f]"
                                        + Component.text("아이템을 판매했습니다")
                                    .color(TextColor.color(0x96C9F4))
                                    .content()
                            )
                            eco?.depositPlayer(e.whoClicked as Player, sellPrice.toDouble())
                            e.whoClicked.sendMessage(
                                "§f[§aaShop§f]"
                                        + Component.text("잔고: ${eco?.getBalance(e.whoClicked as Player)}")
                                    .content()
                            )
                        } else {
                            e.whoClicked.sendMessage(
                                "§f[§aaShop§f]"
                                        + Component.text("아이템이 충분하지 않습니다!")
                                    .color(TextColor.color(0x96C9F4))
                                    .content()
                            )
                        }
                    }
                    if (e.click.isLeftClick) {
                        if (eco?.has(e.whoClicked as Player, buyPrice.toDouble())!!){
                            val item = Material.getMaterial(itemN) ?: Material.AIR
                            e.whoClicked.inventory.addItem(ItemStack(item))
                            e.whoClicked.sendMessage(
                                "§f[§aaShop§f]"
                                        + Component.text("아이템을 구매했습니다")
                                    .color(TextColor.color(0xFF6969))
                                    .content()
                            )
                            eco?.withdrawPlayer(e.whoClicked as Player, buyPrice.toDouble())
                            e.whoClicked.sendMessage(
                                "§f[§aaShop§f]"
                                        + Component.text("잔고: ${eco?.getBalance(e.whoClicked as Player)}")
                                    .content()
                            )
                        } else {
                            e.whoClicked.sendMessage(
                                "§f[§aaShop§f]"
                                        + Component.text("돈이 충분하지 않습니다!")
                                    .color(TextColor.color(0x96C9F4))
                                    .content()
                            )
                        }
                    }
                }

                val lore = mutableListOf(
                    Component.text("구매 하기[좌클릭] ${buyPrice}$").color(TextColor.color(0xFF6969)),
                    Component.text("판매 하기[우클릭] $sellPrice$").color(TextColor.color(0x96C9F4)),
                    Component.text("쉬프트 클릭으로 아이템 한번에 구매/판매").color(TextColor.color(0x4ad5a))
                )
                val meta=item.itemMeta
                meta.lore(lore)
                item.itemMeta=meta
                e.inventory.setItem(e.slot, item)

                val p=e.whoClicked as Player
                p.updateInventory()

            }

            e.isCancelled = true
        }

        if (e.view.title.contains("등록")) {
            if (!i.type.equals(InventoryType.CHEST)) return
            val item= e.currentItem ?: return
            if (item.isEmpty) return
            if (item.type.equals(Material.LIME_STAINED_GLASS_PANE)) {
                val itemType = e.inventory.getItem(10)?.type?.name ?: return
                val currentPage = findPage()
                iValue1=getPrice(e.inventory.getItem(14)!!)!!
                iValue2=getPrice(e.inventory.getItem(15)!!)!!
                shop.config.set("page$currentPage.$itemType.item", itemType)
                shop.config.set("page$currentPage.$itemType.price.sell", iValue1)
                shop.config.set("page$currentPage.$itemType.price.buy", iValue2)
                shop.config.set("page$currentPage.$itemType.count.sell", 0)
                shop.config.set("page$currentPage.$itemType.count.buy", 0)
                shop.saveConfig()
                e.whoClicked.closeInventory()

                e.whoClicked.sendMessage("§f[§aaShop§f] §a아이템 등록됨: ${itemType}")
            }
            if (item.type.equals(Material.PAPER)) {
                if (e.slot == 14 || e.slot==15) {
                    val paper=e.inventory.getItem(e.slot)!!
                    val paperM=paper.itemMeta
                    val lore=paperM?.lore()
                    var price=getPrice(paper)!!
                    if (e.click.isShiftClick) {
                        if (e.click.isLeftClick) {
                            price+=10
                            lore?.set(0,Component.text("${price}$").color(TextColor.color(0xFAFFAF)))
                            paperM.lore(lore)
                            paper.itemMeta=paperM
                        }
                        if (e.click.isRightClick) {
                            if ((price-10)>=0){
                                price -= 10
                                lore?.set(0, Component.text("${price}$").color(TextColor.color(0xFAFFAF)))
                                paperM.lore(lore)
                                paper.itemMeta = paperM
                            }
                        }
                    } else {
                        if (e.click.isLeftClick) {
                            price++
                            lore?.set(0,Component.text("${price}$").color(TextColor.color(0xFAFFAF)))
                            paperM.lore(lore)
                            paper.itemMeta=paperM
                        }
                        if (e.click.isRightClick) {
                            if ((price-1)>=0){
                                price--
                                lore?.set(0, Component.text("${price}$").color(TextColor.color(0xFAFFAF)))
                                paperM.lore(lore)
                                paper.itemMeta = paperM
                            }
                        }
                    }
                }
            }
            e.isCancelled = true
        }
    }
    fun findPage(): Int {
        var pageN = 1
        while (true) {
            val page = shop.config.getConfigurationSection("page$pageN")
            if (page == null || page.getKeys(false).size < 45) {
                return pageN
            }
            pageN++
        }
    }

    fun getPrice(item: ItemStack): Int? {
        val itemMeta = item.itemMeta ?: return null
        val lore = itemMeta.lore() ?: return null

        if (lore.isNotEmpty()) {
            val priceL = (lore[0] as TextComponent).content()
            val regex = Regex("""\d+""")
            val regR = regex.find(priceL)
            return regR?.value?.toInt()
        }

        return null
    }

}
