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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class PublicLog_GUI {

    public CompletableFuture<PaginatedGui> getPublicMessageGUI(UUID uuid, boolean sortNewestToOldest, String textFilter, Player open) {
        CompletableFuture<PaginatedGui> paginatedGuiCompletableFuture = new CompletableFuture<>();
        CompletableFuture<List<GamePlayerPublicMessaging>> x = MafanaTextNetwork.getInstance().getGamePlayerMessageData().getPublicTextListAsync(uuid);
        x.thenAcceptAsync(gPPM -> {
            PaginatedGui gui = Gui.paginated()
                    .title(Component.text(ChatColor.GOLD + "MTN " + ChatColor.GOLD + "Public Logs"))
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
                    getPublicMessageGUI(uuid, newSortDirection, textFilter, open).thenAccept(paginatedGui -> Bukkit.getScheduler().runTask(MafanaTextNetwork.getInstance(), () -> paginatedGui.open(open)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }));

            ItemStack textFilterButton = new ItemStack(Material.NAME_TAG);
            ItemMeta textFilterButtonMeta = textFilterButton.getItemMeta();
            textFilterButtonMeta.setDisplayName(ChatColor.YELLOW + "Text Filter: " + (textFilter.isEmpty() ? "None" : textFilter));
            textFilterButton.setItemMeta(textFilterButtonMeta);

            gui.setItem(50, new GuiItem(textFilterButton, event -> {
                event.getWhoClicked().closeInventory();
                openFilterSign(uuid, sortNewestToOldest, textFilter, open);
            }));

            List<GamePlayerPublicMessaging> privateMessages = new ArrayList<>(gPPM);
            List<GamePlayerPublicMessaging> filteredMessages = new ArrayList<>();
            for (GamePlayerPublicMessaging privateMessage : privateMessages) {
                if (textFilter.isEmpty() || privateMessage.getText().contains(textFilter)) {
                    filteredMessages.add(privateMessage);
                }
            }
            List<GuiItem> guiItems = new ArrayList<>();
            CompletableFuture<Void> future = CompletableFuture.allOf(filteredMessages.stream().map(filteredMessage -> getItemStackPM(filteredMessage)
                    .thenAccept(itemStack -> guiItems.add(new GuiItem(itemStack)))).toArray(CompletableFuture[]::new));
            future.thenRun(() -> {
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
        return paginatedGuiCompletableFuture;
    }


    @NotNull
    private CompletableFuture<ItemStack> getItemStackPM(GamePlayerPublicMessaging publicMessaging) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                OfflineProxyPlayer offlineProxyPlayer = MafanaNetworkCommunicator.getInstance().getPlayerDatabase().getOfflineProxyPlayerAsync(publicMessaging.getSender()).get();
                ItemStack item = new ItemStack(Material.PAPER);
                ItemMeta itemMeta = item.getItemMeta();
                itemMeta.setDisplayName(net.md_5.bungee.api.ChatColor.of(new Color(245, 245, 220)) + publicMessaging.getDate());
                List<String> itemLore = new ArrayList<>();
                itemLore.add("------------------------");
                itemLore.add(ChatColor.DARK_GRAY + "Sender: " + offlineProxyPlayer.getPlayerDisplayName());
                itemLore.add(ChatColor.DARK_GRAY + "");
                if (publicMessaging.getText() != null) {
                    itemLore.add(ChatColor.DARK_GRAY + "Message: " + ChatColor.WHITE + publicMessaging.getText());
                } else {
                    itemLore.add(ChatColor.DARK_GRAY + "Message: " + ChatColor.WHITE + "NULL");
                }
                itemLore.add(ChatColor.DARK_GRAY + "Time: " + ChatColor.WHITE + publicMessaging.getDate());
                itemLore.add("------------------------");
                itemMeta.setLore(itemLore);
                item.setItemMeta(itemMeta);
                return item;
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void openFilterSign(UUID uuid, boolean sortNewestToOldest, String textFilter, Player open) {
        String filterName = "Text";
        SignGUI.builder()
                .setLines(null, filterName + " Filter:", textFilter, "MafanaTextNetwork") // set lines
                .setType(Material.DARK_OAK_SIGN) // set the sign type
                .setHandler((p, result) -> {
                    String filterValue = result.getLineWithoutColor(0);
                    open.closeInventory();
                        return List.of(SignGUIAction.run(() -> {
                            try {
                                getPublicMessageGUI(uuid, sortNewestToOldest, filterValue, open).thenAccept(paginatedGui -> Bukkit.getScheduler().runTask(MafanaTextNetwork.getInstance(), () -> paginatedGui.open(open)));
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }));
                }).callHandlerSynchronously(MafanaTextNetwork.getInstance()).build().open(open);
    }

}
