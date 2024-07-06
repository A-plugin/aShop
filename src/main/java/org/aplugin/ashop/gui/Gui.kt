package org.aplugin.ashop.gui

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.aplugin.ashop.Shop
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class Gui {
    val shop= Shop.Instance!!

    fun openS(pageNum: Int = 1): Inventory {
        val i = Bukkit.createInventory(null, 54, Component.text("상점 - 페이지 $pageNum").color(TextColor.color(0xEEEDEB)).decorate(TextDecoration.BOLD))

        val next = ItemStack(Material.LIME_STAINED_GLASS_PANE)
        val nextM = next.itemMeta
        nextM.displayName(Component.text("다음").color(TextColor.color(0x9BEC00)).decorate(TextDecoration.BOLD))
        next.itemMeta = nextM

        val before = ItemStack(Material.ORANGE_STAINED_GLASS_PANE)
        val beforeM = before.itemMeta
        beforeM.displayName(Component.text("이전").color(TextColor.color(0xFF6969)).decorate(TextDecoration.BOLD))
        before.itemMeta = beforeM

        /*
        val player =i.viewers as Player
        if (player.hasPermission("ashop.edit")) {
            val edit= ItemStack(Material.RED_STAINED_GLASS_PANE)
            val editM=edit.itemMeta
            editM.displayName(Component.text("아이템 배치 수정").color(TextColor.color(0xC80036)).decorate(TextDecoration.BOLD))
            edit.itemMeta=editM
            i.setItem(49,edit)
        }
         */


        i.setItem(53, next)
        i.setItem(45, before)

        val page = shop.config.getConfigurationSection("page$pageNum") ?: return i
        var slot = 0
        for (key in page.getKeys(false)) {
            if (slot >= 45) break

            val itemName = page.getString("$key.item") ?: continue
            val material = Material.getMaterial(itemName) ?: continue

            val item = ItemStack(material)
            val meta = item.itemMeta

            val sellPrice = page.getInt("$key.price.sell")
            val buyPrice = page.getInt("$key.price.buy")

            meta.displayName(Component.text(itemName).color(TextColor.color(0xFFFFFF)))

            val lore = mutableListOf(
                Component.text("판매 하기[우클릭] $sellPrice$").color(TextColor.color(0x96C9F4)),
                Component.text("구매 하기[좌클릭] ${buyPrice}$").color(TextColor.color(0xFF6969)),
            )
            meta.lore(lore)

            item.itemMeta = meta
            i.setItem(slot, item)
            slot++
        }

        return i
    }


    fun ItemS(p: Player): Inventory {
        if (p.inventory.itemInMainHand.isEmpty) {
            p.sendMessage(
                Component.text("[aShop] 등록하려는 아이템이 존재하지 않습니다.")
                    .color(TextColor.color(255, 100, 100))
                    .decorate(TextDecoration.BOLD)
            )
        }

        val mainItem=p.inventory.itemInMainHand.clone()

        val i= Bukkit.createInventory(null, 36, Component.text("아이템 등록").color(TextColor.color(0xEEEDEB)).decorate(
            TextDecoration.BOLD))

        val confirm= ItemStack(Material.LIME_STAINED_GLASS_PANE)
        val confirmM=confirm.itemMeta
        confirmM.displayName(Component.text("확인").color(TextColor.color(0x50B498)).decorate(TextDecoration.BOLD))
        confirm.itemMeta=confirmM

        val value1= ItemStack(Material.PAPER)
        val valueM1=value1.itemMeta
        valueM1.displayName(Component.text("판매 가격설정").color(TextColor.color(0x96C9F4)).decorate(TextDecoration.BOLD))
        value1.itemMeta=valueM1

        val value2= ItemStack(Material.PAPER)
        val valueM2=value2.itemMeta
        valueM2.displayName(Component.text("구매 가격설정").color(TextColor.color(0xFF6969)).decorate(TextDecoration.BOLD))
        value2.itemMeta=valueM2


        i.setItem(10,mainItem)
        i.setItem(15,value2)
        i.setItem(14,value1)
        i.setItem(31,confirm)

        return i
    }

    fun sell(): Inventory {
        val i=Bukkit.createInventory(null, InventoryType.ANVIL,Component.text("판매 가격 입력").color(TextColor.color(0xEEEDEB)).decorate(
            TextDecoration.BOLD))
        val paper= ItemStack(Material.PAPER)
        val nextM=paper.itemMeta
        nextM.displayName(Component.text("여기에 가격 입력").color(TextColor.color(0xFFFFFF)).decorate(TextDecoration.BOLD))
        paper.itemMeta=nextM
        i.setItem(0,paper)
        return i
    }

    fun buy(): Inventory {
        val i=Bukkit.createInventory(null, InventoryType.ANVIL,Component.text("구매 가격 입력").color(TextColor.color(0xEEEDEB)).decorate(
            TextDecoration.BOLD))
        val paper= ItemStack(Material.PAPER)
        val nextM=paper.itemMeta
        nextM.displayName(Component.text("여기에 가격 입력").color(TextColor.color(0xFFFFFF)).decorate(TextDecoration.BOLD))
        paper.itemMeta=nextM
        i.setItem(0,paper)
        return i
    }

}