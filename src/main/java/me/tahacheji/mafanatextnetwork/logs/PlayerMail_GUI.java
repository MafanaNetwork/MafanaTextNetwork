package me.tahacheji.mafanatextnetwork.logs;

import de.rapha149.signgui.SignGUI;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import me.tahacheji.mafana.MafanaNetworkCommunicator;
import me.tahacheji.mafana.data.OfflineProxyPlayer;
import me.tahacheji.mafana.data.ProxyPlayer;
import me.tahacheji.mafanatextnetwork.MafanaTextNetwork;
import me.tahacheji.mafanatextnetwork.data.GamePlayerPrivateMessaging;
import me.tahacheji.mafanatextnetwork.data.PlayerMail;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.*;
import java.util.List;

public class PlayerMail_GUI {

    public PaginatedGui getPlayerMail(Player player, boolean sortNewestToOldest, String playerNameFilter, String textFilter, boolean unOpenedToOpened) {
        PaginatedGui gui = Gui.paginated()
                .title(Component.text(ChatColor.GOLD + "MTN: " + ChatColor.YELLOW + "Player Mail"))
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

        ItemStack sortButton = new ItemStack(Material.CLOCK);
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
                getPlayerMail(clicker, newSortDirection, playerNameFilter, textFilter, unOpenedToOpened).open(player);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));

        ItemStack openSort = new ItemStack(Material.COMPARATOR);
        ItemMeta openSortMeta = openSort.getItemMeta();

        if (unOpenedToOpened) {
            openSortMeta.setDisplayName(ChatColor.YELLOW + "Sort: UnOpened to Opened");
        } else {
            openSortMeta.setDisplayName(ChatColor.YELLOW + "Sort: Opened to UnOpened");
        }
        openSort.setItemMeta(openSortMeta);

        gui.setItem(52, new GuiItem(openSort, event -> {
            boolean newSortDirection = !unOpenedToOpened;
            Player clicker = (Player) event.getWhoClicked();
            try {
                clicker.closeInventory();
                getPlayerMail(clicker, sortNewestToOldest, playerNameFilter, textFilter, newSortDirection).open(player);
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
            openMailFilterSign((Player) event.getWhoClicked(), sortNewestToOldest, playerNameFilter, textFilter, true , unOpenedToOpened);
        }));

        ItemStack textFilterButton = new ItemStack(Material.NAME_TAG);
        ItemMeta textFilterButtonMeta = textFilterButton.getItemMeta();
        textFilterButtonMeta.setDisplayName(ChatColor.YELLOW + "Text Filter: " + (textFilter.isEmpty() ? "None" : textFilter));
        textFilterButton.setItemMeta(textFilterButtonMeta);

        gui.setItem(50, new GuiItem(textFilterButton, event -> {
            event.getWhoClicked().closeInventory();
            openMailFilterSign((Player) event.getWhoClicked(), sortNewestToOldest, playerNameFilter, textFilter, false, unOpenedToOpened);
        }));

        List<PlayerMail> playerMails = MafanaTextNetwork.getInstance().getGamePlayerMessageData().getPlayerMail(player.getUniqueId());

        List<PlayerMail> mailsToRemove = new ArrayList<>();

        if (!playerNameFilter.isEmpty()) {
            for (PlayerMail playerMail : playerMails) {
                OfflineProxyPlayer offlineProxyPlayer = MafanaNetworkCommunicator.getInstance().getPlayerDatabase().getOfflineProxyPlayer(UUID.fromString(playerMail.getFromUUID()));
                if (!offlineProxyPlayer.getPlayerName().contains(playerNameFilter)) {
                    mailsToRemove.add(playerMail);
                }
            }
        }

        for (PlayerMail mailToRemove : mailsToRemove) {
            playerMails.remove(mailToRemove);
        }

        mailsToRemove.clear();

        if (!textFilter.isEmpty()) {
            playerMails.removeIf(playerMail -> !playerMail.getMessage().contains(textFilter));
        }

        if (unOpenedToOpened) {
            playerMails.sort(Comparator.comparing(PlayerMail::isOpened).reversed());
        } else {
            playerMails.sort(Comparator.comparing(PlayerMail::isOpened));
        }
        if (sortNewestToOldest) {
            playerMails.sort(Comparator.comparing(PlayerMail::getLocalDateTime).reversed());
        } else {
            playerMails.sort(Comparator.comparing(PlayerMail::getLocalDateTime));
        }

        for (PlayerMail playerMail : playerMails) {
            ItemStack item = getItemStackPM(playerMail);
            if(!playerMail.isOpened()) {
                MafanaTextNetwork.getInstance().getGamePlayerMessageData().openMail(player.getUniqueId(), playerMail);
            }
            gui.addItem(new GuiItem(item, event -> {
                if(event.getClick() == ClickType.RIGHT) {
                    MafanaTextNetwork.getInstance().getGamePlayerMessageData().removeMail(player.getUniqueId(), playerMail);
                    event.getWhoClicked().closeInventory();
                    getPlayerMail(player, sortNewestToOldest, playerNameFilter, textFilter, unOpenedToOpened).open(player);
                }
            }));
        }

        return gui;
    }

    @NotNull
    private static ItemStack getItemStackPM(PlayerMail playerMail) {
        ItemStack item;
        ItemMeta itemMeta;
        if(playerMail.isOpened()) {
            item = new ItemStack(Material.PAPER);
            itemMeta = item.getItemMeta();
        } else {
            item = new ItemStack(Material.LEGACY_EMPTY_MAP);
            itemMeta = item.getItemMeta();
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
        }
        OfflineProxyPlayer offlineProxyPlayer = MafanaNetworkCommunicator.getInstance().getPlayerDatabase().getOfflineProxyPlayer(UUID.fromString(playerMail.getFromUUID()));


        itemMeta.setDisplayName(net.md_5.bungee.api.ChatColor.of(new Color(245, 245, 220)) + "Mail from: " + offlineProxyPlayer.getPlayerName() + " " + ChatColor.DARK_GRAY +  playerMail.getDate());
        List<String> itemLore = new ArrayList<>();
        itemLore.add(ChatColor.DARK_GRAY + "");
        itemLore.add("------------------------");

        ProxyPlayer proxyPlayer = MafanaNetworkCommunicator.getInstance().getNetworkCommunicatorDatabase().getProxyPlayer(Bukkit.getOfflinePlayer(UUID.fromString(offlineProxyPlayer.getPlayerUUID())));

        if(proxyPlayer == null) {
            itemLore.add(ChatColor.DARK_GRAY + "From: " + net.md_5.bungee.api.ChatColor.of(new Color(245, 245, 220)) + offlineProxyPlayer.getPlayerName() + " " + ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + "OFFLINE" + ChatColor.DARK_GRAY + "]");
        } else {
            itemLore.add(ChatColor.DARK_GRAY + "From: " + net.md_5.bungee.api.ChatColor.of(new Color(245, 245, 220)) + offlineProxyPlayer.getPlayerName() + " " + ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "ONLINE" + ChatColor.DARK_GRAY + "]");
        }
        itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemLore.add(ChatColor.DARK_GRAY + "To: " + net.md_5.bungee.api.ChatColor.of(new Color(245, 245, 220)) + Bukkit.getPlayer(UUID.fromString(playerMail.getToUUID())).getName());
        itemLore.add(ChatColor.DARK_GRAY + "Date: " + net.md_5.bungee.api.ChatColor.of(new Color(245, 245, 220)) + playerMail.getDate());
        itemLore.add(ChatColor.DARK_GRAY + "");
        itemLore.add(ChatColor.DARK_GRAY + "Message: ");

        // Split the message into chunks of 8 words each
        String message = playerMail.getMessage();
        List<String> messageChunks = splitMessage(message, 8);

        for(String s : messageChunks) {
            itemLore.add(ChatColor.WHITE + s);
        }
        itemLore.add("");
        itemLore.add(ChatColor.RED + "Right Click To Delete");
        itemLore.add("------------------------");
        itemMeta.setLore(itemLore);
        item.setItemMeta(itemMeta);
        return item;
    }

    // Additional utility method for splitting a string into chunks of words
    private static List<String> splitMessage(String message, int chunkSize) {
        List<String> words = Arrays.asList(message.split("\\s+"));
        List<String> chunks = new ArrayList<>();

        for (int i = 0; i < words.size(); i += chunkSize) {
            int end = Math.min(i + chunkSize, words.size());
            List<String> subList = words.subList(i, end);
            chunks.add(String.join(" ", subList));
        }

        return chunks;
    }

    public void openMailFilterSign(Player player, boolean sortNewestToOldest, String playerNameFilter, String textFilter, boolean isPlayerFilter, boolean unOpenedToOpened) {
        String filterName = isPlayerFilter ? "Player" : "Text";
        SignGUI.builder()
                .setLines(null, filterName + " Filter:", isPlayerFilter ? playerNameFilter : textFilter, "MafanaTextNetwork") // set lines
                .setType(Material.DARK_OAK_SIGN) // set the sign type
                .setHandler((p, result) -> {
                    String filterValue = result.getLineWithoutColor(0);
                    player.closeInventory();
                    if (isPlayerFilter) {
                        getPlayerMail(player, sortNewestToOldest, filterValue, textFilter, unOpenedToOpened).open(player);
                    } else {
                        getPlayerMail(player, sortNewestToOldest, playerNameFilter, filterValue, unOpenedToOpened).open(player);
                    }
                    return null;
                }).callHandlerSynchronously(MafanaTextNetwork.getInstance()).build().open(player);
    }
}
