package me.tahacheji.mafanatextnetwork.logs;

import de.rapha149.signgui.SignGUI;
import de.rapha149.signgui.SignGUIAction;
import de.tr7zw.nbtapi.NBTItem;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import me.tahacheji.mafana.MafanaNetworkCommunicator;
import me.tahacheji.mafana.data.OfflineProxyPlayer;
import me.tahacheji.mafana.data.ProxyPlayer;
import me.tahacheji.mafana.data.Server;
import me.tahacheji.mafanatextnetwork.MafanaTextNetwork;
import me.tahacheji.mafanatextnetwork.data.GamePlayerPrivateMessaging;
import me.tahacheji.mafanatextnetwork.data.GamePlayerPublicMessaging;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class PrivateLog_GUI {

    public CompletableFuture<PaginatedGui> getDateMessagesGUI(UUID uuid, boolean sortNewestToOldest, Player open) {
        CompletableFuture<PaginatedGui> paginatedGuiCompletableFuture = new CompletableFuture<>();
        CompletableFuture<List<GamePlayerPrivateMessaging>> x = MafanaTextNetwork.getInstance().getGamePlayerMessageData().getPrivateTextListAsync(uuid);
        x.thenAcceptAsync(gPPM -> {
            PaginatedGui gui = Gui.paginated()
                    .title(Component.text(ChatColor.GOLD + "MTN " + ChatColor.GOLD + "Private Logs"))
                    .rows(6)
                    .pageSize(28)
                    .disableAllInteractions()
                    .create();

            gui.setItem(0, ItemBuilder.from(Material.YELLOW_STAINED_GLASS_PANE).setName(" ").setLore(" ").asGuiItem());
            gui.setItem(1, ItemBuilder.from(Material.YELLOW_STAINED_GLASS_PANE).setName(" ").setLore(" ").asGuiItem());
            gui.setItem(2, ItemBuilder.from(Material.YELLOW_STAINED_GLASS_PANE).setName(" ").setLore(" ").asGuiItem());
            gui.setItem(3, ItemBuilder.from(Material.YELLOW_STAINED_GLASS_PANE).setName(" ").setLore(" ").asGuiItem());
            gui.setItem(4, ItemBuilder.from(Material.YELLOW_STAINED_GLASS_PANE).setName(" ").setLore(" ").asGuiItem());
            gui.setItem(5, ItemBuilder.from(Material.YELLOW_STAINED_GLASS_PANE).setName(" ").setLore(" ").asGuiItem());
            gui.setItem(6, ItemBuilder.from(Material.YELLOW_STAINED_GLASS_PANE).setName(" ").setLore(" ").asGuiItem());
            gui.setItem(7, ItemBuilder.from(Material.YELLOW_STAINED_GLASS_PANE).setName(" ").setLore(" ").asGuiItem());
            gui.setItem(8, ItemBuilder.from(Material.YELLOW_STAINED_GLASS_PANE).setName(" ").setLore(" ").asGuiItem());
            gui.setItem(17, ItemBuilder.from(Material.YELLOW_STAINED_GLASS_PANE).setName(" ").setLore(" ").asGuiItem());
            gui.setItem(26, ItemBuilder.from(Material.YELLOW_STAINED_GLASS_PANE).setName(" ").setLore(" ").asGuiItem());
            gui.setItem(35, ItemBuilder.from(Material.YELLOW_STAINED_GLASS_PANE).setName(" ").setLore(" ").asGuiItem());
            gui.setItem(45, ItemBuilder.from(Material.YELLOW_STAINED_GLASS_PANE).setName(" ").setLore(" ").asGuiItem());
            gui.setItem(53, ItemBuilder.from(Material.YELLOW_STAINED_GLASS_PANE).setName(" ").setLore(" ").asGuiItem());
            gui.setItem(52, ItemBuilder.from(Material.YELLOW_STAINED_GLASS_PANE).setName(" ").setLore(" ").asGuiItem());
            gui.setItem(51, ItemBuilder.from(Material.YELLOW_STAINED_GLASS_PANE).setName(" ").setLore(" ").asGuiItem());
            gui.setItem(50, ItemBuilder.from(Material.YELLOW_STAINED_GLASS_PANE).setName(" ").setLore(" ").asGuiItem());
            gui.setItem(48, ItemBuilder.from(Material.YELLOW_STAINED_GLASS_PANE).setName(" ").setLore(" ").asGuiItem());
            gui.setItem(47, ItemBuilder.from(Material.YELLOW_STAINED_GLASS_PANE).setName(" ").setLore(" ").asGuiItem());
            gui.setItem(46, ItemBuilder.from(Material.YELLOW_STAINED_GLASS_PANE).setName(" ").setLore(" ").asGuiItem());
            gui.setItem(44, ItemBuilder.from(Material.YELLOW_STAINED_GLASS_PANE).setName(" ").setLore(" ").asGuiItem());
            gui.setItem(36, ItemBuilder.from(Material.YELLOW_STAINED_GLASS_PANE).setName(" ").setLore(" ").asGuiItem());
            gui.setItem(27, ItemBuilder.from(Material.YELLOW_STAINED_GLASS_PANE).setName(" ").setLore(" ").asGuiItem());
            gui.setItem(18, ItemBuilder.from(Material.YELLOW_STAINED_GLASS_PANE).setName(" ").setLore(" ").asGuiItem());
            gui.setItem(9, ItemBuilder.from(Material.YELLOW_STAINED_GLASS_PANE).setName(" ").setLore(" ").asGuiItem());
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

            gui.setItem(49, ItemBuilder.from(Material.BARRIER).setName(ChatColor.RED + "Close").asGuiItem(event -> {
                event.getWhoClicked().closeInventory();
            }));

            gui.setItem(53, new GuiItem(sortButton, event -> {
                boolean newSortDirection = !sortNewestToOldest;
                Player clicker = (Player) event.getWhoClicked();
                try {
                    clicker.closeInventory();
                    getDateMessagesGUI(uuid, newSortDirection, open).thenAccept(paginatedGui -> Bukkit.getScheduler().runTask(MafanaTextNetwork.getInstance(), () -> paginatedGui.open(open)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }));

            Map<String, List<GamePlayerPrivateMessaging>> messagesByDate = new LinkedHashMap<>();
            for (GamePlayerPrivateMessaging message : gPPM) {
                String[] components = message.getDate().replaceAll("[\\[\\]]", "").split("[ /:]");
                if (components.length >= 6) {
                    int month = Integer.parseInt(components[0]);
                    int day = Integer.parseInt(components[1]);
                    int year = Integer.parseInt(components[2]);
                    String d = month + "/" + day + "/" + year;
                    messagesByDate.computeIfAbsent(d, k -> new ArrayList<>()).add(message);
                }
            }

            List<String> sortedDates = new ArrayList<>(messagesByDate.keySet());
            if (sortNewestToOldest) {
                sortedDates.sort(Comparator.reverseOrder());
            } else {
                sortedDates.sort(Comparator.naturalOrder());
            }


            sortedDates.forEach(date -> {
                ItemStack dateItem = new ItemStack(Material.PAINTING);
                ItemMeta dateItemMeta = dateItem.getItemMeta();
                dateItemMeta.setDisplayName(net.md_5.bungee.api.ChatColor.of(new Color(245, 245, 220)) +  "[" + date + "]");
                dateItemMeta.setLore(Collections.singletonList(ChatColor.YELLOW + "Click to view messages for [" + date + "]"));
                dateItem.setItemMeta(dateItemMeta);
                gui.addItem(new GuiItem(dateItem, event -> {
                    event.getWhoClicked().closeInventory();
                    getPrivateMessageGUI(uuid, date, true, "", "", open).thenAccept(paginatedGui -> Bukkit.getScheduler().runTask(MafanaTextNetwork.getInstance(), () -> paginatedGui.open(open)));
                }));
            });


            paginatedGuiCompletableFuture.complete(gui);
        });
        return paginatedGuiCompletableFuture;
    }

    public CompletableFuture<PaginatedGui> getPrivateMessageGUI(UUID uuid, String date, boolean sortNewestToOldest, String playerNameFilter, String textFilter, Player open) {
        CompletableFuture<PaginatedGui> paginatedGuiCompletableFuture = new CompletableFuture<>();
        CompletableFuture<List<GamePlayerPrivateMessaging>> x = MafanaTextNetwork.getInstance().getGamePlayerMessageData().getPrivateTextListAsync(uuid);
        x.thenAcceptAsync(gPPM -> {
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
            gui.setItem(49, ItemBuilder.from(Material.BARRIER).setName(ChatColor.RED + "Back").asGuiItem(event -> {
                getDateMessagesGUI(uuid, sortNewestToOldest, open).thenAccept(paginatedGui -> Bukkit.getScheduler().runTask(MafanaTextNetwork.getInstance(), () -> paginatedGui.open(open)));
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
                    getPrivateMessageGUI(uuid, date, newSortDirection, playerNameFilter, textFilter, open).thenAccept(paginatedGui -> Bukkit.getScheduler().runTask(MafanaTextNetwork.getInstance(), () -> paginatedGui.open(open)));
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
                openFilterSign(uuid, date, sortNewestToOldest, playerNameFilter, textFilter, true, open);
            }));

            ItemStack textFilterButton = new ItemStack(Material.NAME_TAG);
            ItemMeta textFilterButtonMeta = textFilterButton.getItemMeta();
            textFilterButtonMeta.setDisplayName(ChatColor.YELLOW + "Text Filter: " + (textFilter.isEmpty() ? "None" : textFilter));
            textFilterButton.setItemMeta(textFilterButtonMeta);

            gui.setItem(50, new GuiItem(textFilterButton, event -> {
                event.getWhoClicked().closeInventory();
                openFilterSign(uuid, date, sortNewestToOldest, playerNameFilter, textFilter, false, open);
            }));
            List<GamePlayerPrivateMessaging> privateMessages = new ArrayList<>(gPPM);
            List<GamePlayerPrivateMessaging> filteredMessages = new ArrayList<>();
            CompletableFuture<Void> filter = CompletableFuture.allOf(privateMessages.stream().map(privateMessage ->
                    MafanaNetworkCommunicator.getInstance().getPlayerDatabase().getOfflineProxyPlayerAsync(privateMessage.getReceiver())
                            .thenAcceptAsync(offlineProxyPlayer -> {
                                if (offlineProxyPlayer != null) {
                                    if (playerNameFilter.isEmpty() || offlineProxyPlayer.getPlayerName().equalsIgnoreCase(playerNameFilter)) {
                                        if (textFilter.isEmpty() || privateMessage.getText().contains(textFilter)) {
                                            if(date.isEmpty()) {
                                                filteredMessages.add(privateMessage);
                                            } else {
                                                String[] components = privateMessage.getDate().replaceAll("[\\[\\]]", "").split("[ /:]");
                                                if (components.length >= 6) {
                                                    int month = Integer.parseInt(components[0]);
                                                    int day = Integer.parseInt(components[1]);
                                                    int year = Integer.parseInt(components[2]);
                                                    String d = month + "/" + day + "/" + year;
                                                    if (d.equalsIgnoreCase(date)) {
                                                        filteredMessages.add(privateMessage);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            })).toArray(CompletableFuture[]::new));
            filter.thenRun(() -> {
                List<GuiItem> guiItems = new ArrayList<>();
                CompletableFuture<Void> guiItem = CompletableFuture.allOf(filteredMessages.stream().map(privateMessaging -> getItemStackPM(privateMessaging)
                        .thenAccept(itemStack -> guiItems.add(new GuiItem(itemStack)))).toArray(CompletableFuture[]::new));
                guiItem.thenRun(() -> {
                    if (sortNewestToOldest) {
                        guiItems.sort((item1, item2) -> {
                            NBTItem nbtItem1 = new NBTItem(item1.getItemStack());
                            NBTItem nbtItem2 = new NBTItem(item2.getItemStack());
                            int time1 = nbtItem1.getInteger("TIME");
                            int time2 = nbtItem2.getInteger("TIME");
                            return Integer.compare(time2, time1);
                        });
                    } else {
                        guiItems.sort((item1, item2) -> {
                            NBTItem nbtItem1 = new NBTItem(item1.getItemStack());
                            NBTItem nbtItem2 = new NBTItem(item2.getItemStack());
                            int time1 = nbtItem1.getInteger("TIME");
                            int time2 = nbtItem2.getInteger("TIME");
                            return Integer.compare(time1, time2);
                        });
                    }
                    for (GuiItem g : guiItems) {
                        gui.addItem(g);
                    }
                    gui.update();
                    paginatedGuiCompletableFuture.complete(gui);
                });
            });
        });
        return paginatedGuiCompletableFuture;
    }


    @NotNull
    private CompletableFuture<ItemStack> getItemStackPM(GamePlayerPrivateMessaging privateMessaging) {
        return CompletableFuture.supplyAsync(() -> {
            OfflineProxyPlayer receiverOfflinePlayer = MafanaNetworkCommunicator.getInstance().getPlayerDatabase().getOfflineProxyPlayerAsync(privateMessaging.getReceiver()).join();
            ProxyPlayer receiverProxyPlayer = MafanaNetworkCommunicator.getInstance().getNetworkCommunicatorDatabase().getProxyPlayerAsync(UUID.fromString(receiverOfflinePlayer.getPlayerUUID())).join();
            Server receiverServer = MafanaNetworkCommunicator.getInstance().getNetworkCommunicatorDatabase().getServerFromUUIDAsync(UUID.fromString(privateMessaging.getServerReceiverID())).join();


            OfflineProxyPlayer senderOfflinePlayer = MafanaNetworkCommunicator.getInstance().getPlayerDatabase().getOfflineProxyPlayerAsync(privateMessaging.getSender()).join();
            ProxyPlayer senderProxyPlayer = MafanaNetworkCommunicator.getInstance().getNetworkCommunicatorDatabase().getProxyPlayerAsync(UUID.fromString(senderOfflinePlayer.getPlayerUUID())).join();
            Server senderServer = MafanaNetworkCommunicator.getInstance().getNetworkCommunicatorDatabase().getServerFromUUIDAsync(UUID.fromString(privateMessaging.getServerSenderID())).join();


            ItemStack item = new ItemStack(Material.PAPER);
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(net.md_5.bungee.api.ChatColor.of(new Color(245, 245, 220)) + senderOfflinePlayer.getPlayerName() + " " + privateMessaging.getDate());
            List<String> itemLore = new ArrayList<>();
            itemLore.add("------------------------");
            if (senderProxyPlayer != null) {
                itemLore.add(ChatColor.DARK_GRAY + "Sender: " + senderOfflinePlayer.getPlayerName() + " " + ChatColor.GREEN + "[ONLINE]");
                if (senderServer != null) {
                    itemLore.add(ChatColor.DARK_GRAY + "Sender Server: " + senderServer.getServerID());
                }
            } else {
                itemLore.add(ChatColor.DARK_GRAY + "Sender: " + senderOfflinePlayer.getPlayerName() + " " + ChatColor.RED + "[OFFLINE]");
                if (senderServer != null) {
                    itemLore.add(ChatColor.DARK_GRAY + "Sender Server: " + senderServer.getServerID());
                }
            }
            itemLore.add(ChatColor.DARK_GRAY + "");
            if (receiverProxyPlayer != null) {
                itemLore.add(ChatColor.DARK_GRAY + "Receiver: " + receiverOfflinePlayer.getPlayerName() + " " + ChatColor.GREEN + "[ONLINE]");
                if (receiverServer != null) {
                    itemLore.add(ChatColor.DARK_GRAY + "Receiver Server: " + receiverServer.getServerID());
                }
            } else {
                itemLore.add(ChatColor.DARK_GRAY + "Receiver: " + receiverOfflinePlayer.getPlayerName() + " " + ChatColor.RED + "[OFFLINE]");
                if (receiverServer != null) {
                    itemLore.add(ChatColor.DARK_GRAY + "Receiver Server: " + receiverServer.getServerID());
                }
            }
            itemLore.add(ChatColor.DARK_GRAY + "");
            if (privateMessaging.getText() != null) {
                itemLore.add(ChatColor.DARK_GRAY + "Message: " + ChatColor.WHITE + privateMessaging.getText());
            } else {
                itemLore.add(ChatColor.DARK_GRAY + "Message: " + ChatColor.WHITE + "NULL");
            }
            itemLore.add(ChatColor.DARK_GRAY + "Time: " + ChatColor.WHITE + privateMessaging.getDate());
            itemLore.add("------------------------");
            itemMeta.setLore(itemLore);
            item.setItemMeta(itemMeta);
            NBTItem nbtItem = new NBTItem(item);
            nbtItem.setInteger("TIME", privateMessaging.getTime());
            item = nbtItem.getItem();
            return item;
        });
    }

    public void openFilterSign(UUID uuid, String date, boolean sortNewestToOldest, String playerNameFilter, String textFilter, boolean isPlayerFilter, Player open) {
        String filterName = isPlayerFilter ? "Player" : "Text";
        SignGUI.builder()
                .setLines(null, filterName + " Filter:", isPlayerFilter ? playerNameFilter : textFilter, "MafanaTextNetwork") // set lines
                .setType(Material.DARK_OAK_SIGN) // set the sign type
                .setHandler((p, result) -> {
                    String filterValue = result.getLineWithoutColor(0);
                    open.closeInventory();
                    if (isPlayerFilter) {
                        return List.of(SignGUIAction.run(() -> {
                            try {
                                getPrivateMessageGUI(uuid, date, sortNewestToOldest, filterValue, textFilter, open).thenAccept(paginatedGui -> Bukkit.getScheduler().runTask(MafanaTextNetwork.getInstance(), () -> paginatedGui.open(open)));
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }));
                    } else {
                        return List.of(SignGUIAction.run(() -> {
                            try {
                                getPrivateMessageGUI(uuid, date, sortNewestToOldest, playerNameFilter, filterValue, open).thenAccept(paginatedGui -> Bukkit.getScheduler().runTask(MafanaTextNetwork.getInstance(), () -> paginatedGui.open(open)));
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }));
                    }
                }).callHandlerSynchronously(MafanaTextNetwork.getInstance()).build().open(open);
    }


}
