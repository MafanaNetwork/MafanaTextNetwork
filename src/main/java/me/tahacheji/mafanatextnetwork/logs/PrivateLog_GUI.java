package me.tahacheji.mafanatextnetwork.logs;

import de.rapha149.signgui.SignGUI;
import de.rapha149.signgui.SignGUIAction;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import me.tahacheji.mafana.MafanaNetworkCommunicator;
import me.tahacheji.mafana.data.OfflineProxyPlayer;
import me.tahacheji.mafana.data.ProxyPlayer;
import me.tahacheji.mafanatextnetwork.MafanaTextNetwork;
import me.tahacheji.mafanatextnetwork.data.GamePlayerPrivateMessaging;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class PrivateLog_GUI {


    public PaginatedGui getPrivateMessageGUI(Player player, boolean sortNewestToOldest, String playerNameFilter, String textFilter) throws Exception {
        PaginatedGui gui = Gui.paginated()
                .title(Component.text(ChatColor.GOLD + "MTN " + ChatColor.GOLD + "Private Logs"))
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

        ItemStack sortButton = new ItemStack(Material.COMPARATOR);
        ItemMeta sortButtonMeta = sortButton.getItemMeta();

        if (sortNewestToOldest) {
            sortButtonMeta.setDisplayName(ChatColor.YELLOW + "Sort: Newest to Oldest");
        } else {
            sortButtonMeta.setDisplayName(ChatColor.YELLOW + "Sort: Oldest to Newest");
        }
        sortButton.setItemMeta(sortButtonMeta);

        gui.setItem(53, new GuiItem(sortButton, event -> {
            boolean newSortDirection = !sortNewestToOldest;
            Player clicker = (Player) event.getWhoClicked();
            try {
                clicker.closeInventory();
                getPrivateMessageGUI(clicker, newSortDirection, playerNameFilter, textFilter).open(player);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));

        ItemStack playerFilterButton = new ItemStack(Material.WITHER_SKELETON_SKULL);
        ItemMeta playerFilterButtonMeta = playerFilterButton.getItemMeta();
        playerFilterButtonMeta.setDisplayName(ChatColor.YELLOW + "Player Filter: " + (playerNameFilter.isEmpty() ? "None" : playerNameFilter));
        playerFilterButton.setItemMeta(playerFilterButtonMeta);

        gui.setItem(48, new GuiItem(playerFilterButton, event -> {
            event.getWhoClicked().closeInventory();
            openFilterSign((Player) event.getWhoClicked(), sortNewestToOldest, playerNameFilter, textFilter, true);
        }));

        ItemStack textFilterButton = new ItemStack(Material.NAME_TAG);
        ItemMeta textFilterButtonMeta = textFilterButton.getItemMeta();
        textFilterButtonMeta.setDisplayName(ChatColor.YELLOW + "Text Filter: " + (textFilter.isEmpty() ? "None" : textFilter));
        textFilterButton.setItemMeta(textFilterButtonMeta);

        gui.setItem(50, new GuiItem(textFilterButton, event -> {
            event.getWhoClicked().closeInventory();
            openFilterSign((Player) event.getWhoClicked(), sortNewestToOldest, playerNameFilter, textFilter, false);
        }));

        List<GamePlayerPrivateMessaging> privateMessages = new ArrayList<>();
        if(MafanaTextNetwork.getInstance().getGamePlayerMessageData().getPrivateTextList(player.getUniqueId()) != null) {
            privateMessages.addAll(MafanaTextNetwork.getInstance().getGamePlayerMessageData().getPrivateTextList(player.getUniqueId()));
        }
        List<GamePlayerPrivateMessaging> filteredMessages = new ArrayList<>();
        for (GamePlayerPrivateMessaging privateMessage : privateMessages) {
            if(privateMessage.getReceiver() != null) {
                OfflineProxyPlayer offlineProxyPlayer = MafanaNetworkCommunicator.getInstance().getPlayerDatabase().getOfflineProxyPlayer(privateMessage.getReceiver());
                if(offlineProxyPlayer.getPlayerName() != null) {
                    if (playerNameFilter.isEmpty() || offlineProxyPlayer.getPlayerName().equalsIgnoreCase(playerNameFilter)) {
                        if (textFilter.isEmpty() || privateMessage.getText().contains(textFilter)) {
                            filteredMessages.add(privateMessage);
                        }
                    }
                }
            }
        }
        if (sortNewestToOldest) {
            filteredMessages.sort(Comparator.comparing(GamePlayerPrivateMessaging::getLocalDateTime).reversed());
        } else {
            filteredMessages.sort(Comparator.comparing(GamePlayerPrivateMessaging::getLocalDateTime));
        }
        for (GamePlayerPrivateMessaging privateMessaging : filteredMessages) {
            ItemStack item = getItemStackPM(privateMessaging);
            gui.addItem(new GuiItem(item));
        }

        return gui;
    }


    @NotNull
    private static ItemStack getItemStackPM(GamePlayerPrivateMessaging privateMessaging) {
        OfflineProxyPlayer offlineProxyPlayer = MafanaNetworkCommunicator.getInstance().getPlayerDatabase().getOfflineProxyPlayer(privateMessaging.getReceiver());
        ProxyPlayer proxyPlayer = MafanaNetworkCommunicator.getInstance().getNetworkCommunicatorDatabase().getProxyPlayer(UUID.fromString(offlineProxyPlayer.getPlayerUUID()));
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.AQUA + offlineProxyPlayer.getPlayerName() + " " + privateMessaging.getTime());
        List<String> itemLore = new ArrayList<>();
        itemLore.add(ChatColor.DARK_GRAY + "");
        itemLore.add("------------------------");
        itemLore.add(ChatColor.DARK_GRAY + "Sender: " + offlineProxyPlayer.getPlayerName());
        if (proxyPlayer != null) {
            itemLore.add(ChatColor.DARK_GRAY + "Receiver: " + offlineProxyPlayer.getPlayerDisplayName() + " " + ChatColor.GREEN + "[ONLINE]");
        } else {
            itemLore.add(ChatColor.DARK_GRAY + "Receiver: " + offlineProxyPlayer.getPlayerDisplayName() + " " + ChatColor.RED + "[OFFLINE]");
        }
        itemLore.add(ChatColor.DARK_GRAY + "");
        if (privateMessaging.getText() != null) {
            itemLore.add(ChatColor.DARK_GRAY + "Message: " + privateMessaging.getText());
        } else {
            itemLore.add(ChatColor.DARK_GRAY + "Message: " + "NULL");
        }
        if (privateMessaging.getItem() != null) {
            itemLore.add(ChatColor.DARK_GRAY + "Item: " + privateMessaging.getItem().getItemMeta().getDisplayName());
        } else {
            itemLore.add(ChatColor.DARK_GRAY + "Item: " + "NULL");
        }
        itemLore.add(ChatColor.DARK_GRAY + "Time: " + privateMessaging.getTime());
        itemLore.add(ChatColor.DARK_GRAY + "");
        itemLore.add("------------------------");
        itemMeta.setLore(itemLore);
        item.setItemMeta(itemMeta);
        return item;
    }

    public void openFilterSign(Player player, boolean sortNewestToOldest, String playerNameFilter, String textFilter, boolean isPlayerFilter) {
        String filterName = isPlayerFilter ? "Player" : "Text";
        SignGUI.builder()
                .setLines(null, filterName + " Filter:", isPlayerFilter ? playerNameFilter : textFilter, "MafanaTextNetwork") // set lines
                .setType(Material.DARK_OAK_SIGN) // set the sign type
                .setHandler((p, result) -> {
                    String filterValue = result.getLineWithoutColor(0);
                    player.closeInventory();
                    if (isPlayerFilter) {
                        return List.of(SignGUIAction.run(() -> {
                            try {
                                getPrivateMessageGUI(player, sortNewestToOldest, filterValue, textFilter).open(player);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }));
                    } else {
                        return List.of(SignGUIAction.run(() -> {
                            try {
                                getPrivateMessageGUI(player, sortNewestToOldest, playerNameFilter, filterValue).open(player);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }));
                    }
                }).callHandlerSynchronously(MafanaTextNetwork.getInstance()).build().open(player);
    }


}
