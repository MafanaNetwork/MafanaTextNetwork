package me.tahacheji.mafanatextnetwork.logs;

import de.rapha149.signgui.SignGUI;
import de.tr7zw.nbtapi.NBTItem;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;

public class PlayerMail_GUI {

    public CompletableFuture<PaginatedGui> getPlayerMail(UUID uuid, boolean sortNewestToOldest, String playerNameFilter, String textFilter, boolean unOpenedToOpened, Player open) {
        CompletableFuture<PaginatedGui> x = new CompletableFuture<>();
        CompletableFuture<List<PlayerMail>> z = MafanaTextNetwork.getInstance().getGamePlayerMessageData().getPlayerMailAsync(uuid);
        z.thenAcceptAsync(pm -> {
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
                    getPlayerMail(uuid, newSortDirection, playerNameFilter, textFilter, unOpenedToOpened, open).thenAccept(paginatedGui -> Bukkit.getScheduler().runTask(MafanaTextNetwork.getInstance(), () -> paginatedGui.open(open)));
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
                    getPlayerMail(uuid, sortNewestToOldest, playerNameFilter, textFilter, newSortDirection, open).thenAccept(paginatedGui -> Bukkit.getScheduler().runTask(MafanaTextNetwork.getInstance(), () -> paginatedGui.open(open)));
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
                openMailFilterSign(uuid, sortNewestToOldest, playerNameFilter, textFilter, true, unOpenedToOpened, open);
            }));

            ItemStack textFilterButton = new ItemStack(Material.NAME_TAG);
            ItemMeta textFilterButtonMeta = textFilterButton.getItemMeta();
            textFilterButtonMeta.setDisplayName(ChatColor.YELLOW + "Text Filter: " + (textFilter.isEmpty() ? "None" : textFilter));
            textFilterButton.setItemMeta(textFilterButtonMeta);

            gui.setItem(50, new GuiItem(textFilterButton, event -> {
                event.getWhoClicked().closeInventory();
                openMailFilterSign(uuid, sortNewestToOldest, playerNameFilter, textFilter, false, unOpenedToOpened, open);
            }));

            List<PlayerMail> mailsToRemove = new ArrayList<>();

            CompletableFuture<Void> remove = CompletableFuture.allOf(pm.stream().map(playerMail ->
                    MafanaNetworkCommunicator.getInstance().getPlayerDatabase().getOfflineProxyPlayerAsync(UUID.fromString(playerMail.getFromUUID()))
                            .thenAcceptAsync(offlineProxyPlayer -> {
                                if (offlineProxyPlayer != null) {
                                    if (!playerNameFilter.isEmpty()) {
                                        if (!offlineProxyPlayer.getPlayerName().contains(playerNameFilter)) {
                                            mailsToRemove.add(playerMail);
                                        }
                                    }
                                }
                            }).exceptionally(ex -> {
                                ex.printStackTrace();
                                return null;
                            })).toArray(CompletableFuture[]::new));
            remove.thenRun(() -> {
                List<PlayerMail> playerMails = pm;
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
                    playerMails.sort(Comparator.comparing(PlayerMail::getTime).reversed());
                } else {
                    playerMails.sort(Comparator.comparing(PlayerMail::getTime));
                }
                List<GuiItem> i = new ArrayList<>();
                CompletableFuture<Void> allFutures = CompletableFuture.allOf(playerMails.stream()
                        .map(playerMail -> getItemStackPM(playerMail)
                                .thenAccept(itemStack -> {
                                    if (itemStack != null) {
                                        if (!playerMail.isOpened()) {
                                            if (open.getUniqueId().toString().equalsIgnoreCase(uuid.toString())) {
                                                try {
                                                    MafanaTextNetwork.getInstance().getGamePlayerMessageData().openMail(uuid, playerMail);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                        i.add(new GuiItem(itemStack, event -> {
                                            if (event.getClick() == ClickType.RIGHT) {
                                                try {
                                                    event.getWhoClicked().closeInventory();
                                                    CompletableFuture<Void> future = MafanaTextNetwork.getInstance().getGamePlayerMessageData().removeMail(uuid, playerMail);
                                                    future.thenAccept(v -> getPlayerMail(uuid, sortNewestToOldest, playerNameFilter, textFilter, unOpenedToOpened, open)
                                                            .thenAccept(paginatedGui -> Bukkit.getScheduler().runTask(MafanaTextNetwork.getInstance(), () -> paginatedGui.open(open))));
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }));
                                    }
                                })
                                .exceptionally(ex -> {
                                    ex.printStackTrace();
                                    return null;
                                }))
                        .toArray(CompletableFuture[]::new));
                allFutures.thenRun(() -> {
                    if (sortNewestToOldest) {
                        i.sort((item1, item2) -> {
                            NBTItem nbtItem1 = new NBTItem(item1.getItemStack());
                            NBTItem nbtItem2 = new NBTItem(item2.getItemStack());
                            int time1 = nbtItem1.getInteger("TIME");
                            int time2 = nbtItem2.getInteger("TIME");
                            return Integer.compare(time2, time1);
                        });
                    } else {
                        i.sort((item1, item2) -> {
                            NBTItem nbtItem1 = new NBTItem(item1.getItemStack());
                            NBTItem nbtItem2 = new NBTItem(item2.getItemStack());
                            int time1 = nbtItem1.getInteger("TIME");
                            int time2 = nbtItem2.getInteger("TIME");
                            return Integer.compare(time1, time2);
                        });
                    }
                    for (GuiItem guiItem : i) {
                        gui.addItem(guiItem);
                    }
                    gui.update();
                    x.complete(gui);
                }).exceptionally(ex -> {
                    ex.printStackTrace();
                    return null;
                });
            }).exceptionally(ex -> {
                ex.printStackTrace();
                return null;
            });
        }).exceptionally(ex -> {
            ex.printStackTrace();
            return null;
        });

        return x;
    }

    private CompletableFuture<ItemStack> getItemStackPM(PlayerMail playerMail) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                OfflineProxyPlayer offlineProxyPlayer = MafanaNetworkCommunicator.getInstance().getPlayerDatabase().getOfflineProxyPlayerAsync(UUID.fromString(playerMail.getFromUUID())).get();
                ProxyPlayer proxyPlayer = MafanaNetworkCommunicator.getInstance().getNetworkCommunicatorDatabase().getProxyPlayerAsync(UUID.fromString(offlineProxyPlayer.getPlayerUUID())).get();

                ItemStack item;
                ItemMeta itemMeta;
                if (playerMail.isOpened()) {
                    item = new ItemStack(Material.PAPER);
                    itemMeta = item.getItemMeta();
                } else {
                    item = new ItemStack(Material.LEGACY_EMPTY_MAP);
                    itemMeta = item.getItemMeta();
                    itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
                }

                itemMeta.setDisplayName(net.md_5.bungee.api.ChatColor.of(new Color(245, 245, 220)) + "Mail from: " + offlineProxyPlayer.getPlayerName() + " " + ChatColor.DARK_GRAY + playerMail.getDate());
                List<String> itemLore = new ArrayList<>();
                itemLore.add(ChatColor.DARK_GRAY + "");
                itemLore.add("------------------------");

                if (proxyPlayer == null) {
                    itemLore.add(ChatColor.DARK_GRAY + "From: " + net.md_5.bungee.api.ChatColor.of(new Color(245, 245, 220)) + offlineProxyPlayer.getPlayerName() + " " + ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + "OFFLINE" + ChatColor.DARK_GRAY + "]");
                } else {
                    itemLore.add(ChatColor.DARK_GRAY + "From: " + net.md_5.bungee.api.ChatColor.of(new Color(245, 245, 220)) + offlineProxyPlayer.getPlayerName() + " " + ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "ONLINE" + ChatColor.DARK_GRAY + "]");
                }
                itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                itemLore.add(ChatColor.DARK_GRAY + "To: " + net.md_5.bungee.api.ChatColor.of(new Color(245, 245, 220)) + offlineProxyPlayer.getPlayerName());
                itemLore.add(ChatColor.DARK_GRAY + "Date: " + net.md_5.bungee.api.ChatColor.of(new Color(245, 245, 220)) + playerMail.getDate());
                itemLore.add(ChatColor.DARK_GRAY + "");
                itemLore.add(ChatColor.DARK_GRAY + "Message: ");

                String message = playerMail.getMessage();
                List<String> messageChunks = splitMessage(message, 8);

                for (String w : messageChunks) {
                    itemLore.add(ChatColor.WHITE + w);
                }
                itemLore.add("");
                itemLore.add(ChatColor.RED + "Right Click To Delete");
                itemLore.add("------------------------");
                itemMeta.setLore(itemLore);
                item.setItemMeta(itemMeta);
                NBTItem nbtItem = new NBTItem(item);
                nbtItem.setInteger("TIME", playerMail.getTime());
                item = nbtItem.getItem();
                return item;
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return null;
            }
        });
    }


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

    public void openMailFilterSign(UUID uuid, boolean sortNewestToOldest, String playerNameFilter, String textFilter, boolean isPlayerFilter, boolean unOpenedToOpened, Player open) {
        String filterName = isPlayerFilter ? "Player" : "Text";
        SignGUI.builder()
                .setLines(null, filterName + " Filter:", isPlayerFilter ? playerNameFilter : textFilter, "MafanaTextNetwork") // set lines
                .setType(Material.DARK_OAK_SIGN) // set the sign type
                .setHandler((p, result) -> {
                    String filterValue = result.getLineWithoutColor(0);
                    open.closeInventory();
                    if (isPlayerFilter) {
                        getPlayerMail(uuid, sortNewestToOldest, filterValue, textFilter, unOpenedToOpened, open).thenAccept(paginatedGui -> Bukkit.getScheduler().runTask(MafanaTextNetwork.getInstance(), () -> paginatedGui.open(open)));
                    } else {
                        getPlayerMail(uuid, sortNewestToOldest, playerNameFilter, filterValue, unOpenedToOpened, open).thenAccept(paginatedGui -> Bukkit.getScheduler().runTask(MafanaTextNetwork.getInstance(), () -> paginatedGui.open(open)));
                    }
                    return null;
                }).callHandlerSynchronously(MafanaTextNetwork.getInstance()).build().open(open);
    }
}
