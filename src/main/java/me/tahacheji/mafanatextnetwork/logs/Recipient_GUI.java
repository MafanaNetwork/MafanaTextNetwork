package me.tahacheji.mafanatextnetwork.logs;

import de.rapha149.signgui.SignGUI;
import de.rapha149.signgui.SignGUIAction;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import me.tahacheji.mafanatextnetwork.MafanaTextNetwork;
import me.tahacheji.mafanatextnetwork.data.AllowedRecipient;
import me.tahacheji.mafanatextnetwork.data.GamePlayerPublicMessaging;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Recipient_GUI {


    public PaginatedGui getAllowedRecipientGUI(Player player, String textFilter) throws Exception {
        PaginatedGui gui = Gui.paginated()
                .title(Component.text(ChatColor.GOLD + "MTN " + ChatColor.GOLD + "Allowed Recipient"))
                .rows(6)
                .pageSize(28)
                .disableAllInteractions()
                .create();
        List<String> lore = new ArrayList<>();
        ItemStack greystainedglass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta newmeta = greystainedglass.getItemMeta();
        newmeta.setDisplayName(ChatColor.GRAY + " ");
        newmeta.setLore(lore);
        greystainedglass.setItemMeta(newmeta);

        ItemStack closeShop = new ItemStack(Material.BARRIER);
        ItemMeta closeShopeta = closeShop.getItemMeta();
        closeShopeta.setDisplayName(ChatColor.GRAY + "Close Page");
        closeShopeta.setLore(lore);
        closeShop.setItemMeta(closeShopeta);

        gui.setItem(0, new GuiItem(greystainedglass));
        gui.setItem(1, new GuiItem(greystainedglass));
        gui.setItem(2, new GuiItem(greystainedglass));
        gui.setItem(3, new GuiItem(greystainedglass));
        gui.setItem(4, new GuiItem(greystainedglass));
        gui.setItem(5, new GuiItem(greystainedglass));
        gui.setItem(6, new GuiItem(greystainedglass));
        gui.setItem(7, new GuiItem(greystainedglass));
        gui.setItem(8, new GuiItem(greystainedglass));
        gui.setItem(17, new GuiItem(greystainedglass));
        gui.setItem(26, new GuiItem(greystainedglass));
        gui.setItem(35, new GuiItem(greystainedglass));
        gui.setItem(45, new GuiItem(greystainedglass));
        gui.setItem(53, new GuiItem(greystainedglass));
        gui.setItem(52, new GuiItem(greystainedglass));
        gui.setItem(51, new GuiItem(greystainedglass));
        gui.setItem(50, new GuiItem(greystainedglass));
        gui.setItem(48, new GuiItem(greystainedglass));
        gui.setItem(47, new GuiItem(greystainedglass));
        gui.setItem(46, new GuiItem(greystainedglass));
        gui.setItem(44, new GuiItem(greystainedglass));
        gui.setItem(36, new GuiItem(greystainedglass));
        gui.setItem(27, new GuiItem(greystainedglass));
        gui.setItem(18, new GuiItem(greystainedglass));
        gui.setItem(9, new GuiItem(greystainedglass));
        gui.setItem(49, new GuiItem(closeShop, event -> {
            event.getWhoClicked().closeInventory();
        }));
        gui.setItem(6, 3, ItemBuilder.from(Material.PAPER).setName(ChatColor.DARK_GRAY + "Previous")
                .asGuiItem(event -> gui.previous()));
        gui.setItem(6, 7, ItemBuilder.from(Material.PAPER).setName(ChatColor.DARK_GRAY + "Next")
                .asGuiItem(event -> gui.next()));

        ItemStack textFilterButton = new ItemStack(Material.NAME_TAG);
        ItemMeta textFilterButtonMeta = textFilterButton.getItemMeta();
        textFilterButtonMeta.setDisplayName(ChatColor.YELLOW + "Text Filter: " + (textFilter.isEmpty() ? "None" : textFilter));
        textFilterButton.setItemMeta(textFilterButtonMeta);

        gui.setItem(50, new GuiItem(textFilterButton, event -> {
            event.getWhoClicked().closeInventory();
            openFilterSign((Player) event.getWhoClicked(), textFilter);
        }));

        List<AllowedRecipient> uuids = MafanaTextNetwork.getInstance().getGamePlayerMessageData().getAllowedRecipients(player.getUniqueId());
        List<UUID> filteredUUID = new ArrayList<>();

        for (AllowedRecipient uuid : uuids) {
            OfflinePlayer oPlayer = Bukkit.getOfflinePlayer(uuid.getPlayerUUID());

            if (oPlayer != null && (textFilter.isEmpty() || oPlayer.getName().contains(textFilter))) {
                filteredUUID.add(uuid.getPlayerUUID());
            }
        }

        for (UUID uuid : filteredUUID) {
            ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
            skullMeta.setDisplayName(ChatColor.DARK_GRAY + "[" + Bukkit.getOfflinePlayer(uuid).getName() + ChatColor.DARK_GRAY + "]");
            skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(uuid));
            List<String> skullLore = new ArrayList<>();
            skullLore.add(ChatColor.DARK_GRAY + "------------------------");
            skullLore.add(ChatColor.DARK_GRAY + "NAME: " + Bukkit.getOfflinePlayer(uuid).getName());
            skullLore.add(ChatColor.DARK_GRAY + "UUID: " + uuid.toString());
            skullLore.add(ChatColor.DARK_GRAY + "Chats Together : " + MafanaTextNetwork.getInstance().getGamePlayerMessageData().getPrivateChatsWithAllowedRecipient(player.getUniqueId(), uuid).size());
            skullLore.add("");
            skullLore.add(ChatColor.DARK_RED + "Left Click to remove");
            skullLore.add(ChatColor.DARK_GREEN + "Right Click to view chats together");
            skullLore.add(ChatColor.DARK_GRAY + "------------------------");
            skullMeta.setLore(skullLore);
            skull.setItemMeta(skullMeta);
            gui.addItem(new GuiItem(skull, event -> {
                if(event.getClick() == ClickType.RIGHT) {
                    event.getWhoClicked().closeInventory();
                    try {
                        new PrivateLog_GUI().getPrivateMessageGUI(player, true, Bukkit.getOfflinePlayer(uuid).getName(), "").open(player);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return;
                }
                event.getWhoClicked().closeInventory();
                MafanaTextNetwork.getInstance().getGamePlayerMessageData().removeRecipient(player.getUniqueId(), uuid);
                player.sendMessage(ChatColor.GREEN + "MafanaTextNetwork: PLAYER_REMOVED");
            }));
        }
        return gui;
    }

    public void openFilterSign(Player player, String textFilter) {
        String filterName = "Text";
        SignGUI.builder()
                .setLines(null, filterName + " Filter:", textFilter, "MafanaTextNetwork") // set lines
                .setType(Material.DARK_OAK_SIGN) // set the sign type
                .setHandler((p, result) -> {
                    String filterValue = result.getLineWithoutColor(0);
                    player.closeInventory();
                    return List.of(SignGUIAction.run(() -> {
                        try {
                            getAllowedRecipientGUI(player, filterValue).open(player);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }));
                }).callHandlerSynchronously(MafanaTextNetwork.getInstance()).build().open(player);
    }
}
