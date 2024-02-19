package me.tahacheji.mafanatextnetwork.logs;

import de.rapha149.signgui.SignGUI;
import de.rapha149.signgui.SignGUIAction;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import me.tahacheji.mafana.MafanaNetworkCommunicator;
import me.tahacheji.mafana.data.OfflineProxyPlayer;
import me.tahacheji.mafanatextnetwork.MafanaTextNetwork;
import me.tahacheji.mafanatextnetwork.data.AllowedRecipient;
import me.tahacheji.mafanatextnetwork.data.GamePlayerPrivateMessaging;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;

public class Recipient_GUI {

    public CompletableFuture<PaginatedGui> getAllowedRecipientGUI(UUID uuidRecipient, String textFilter, Player open){
        CompletableFuture<PaginatedGui> guiCompletableFuture = new CompletableFuture<>();
        CompletableFuture<List<AllowedRecipient>> x = MafanaTextNetwork.getInstance().getGamePlayerMessageData().getAllowedRecipientsAsync(uuidRecipient);
        x.thenAcceptAsync(aR -> {
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
                openFilterSign(uuidRecipient, textFilter, open);
            }));

            List<AllowedRecipient> uuids = new ArrayList<>(aR);
            List<AllowedRecipient> filteredUUID = new ArrayList<>();
            CompletableFuture<Void> oPP = CompletableFuture.allOf(uuids.stream().map(allowedRecipient ->
                    MafanaNetworkCommunicator.getInstance().getPlayerDatabase().getOfflineProxyPlayerAsync(allowedRecipient.getPlayerUUID())
                            .thenAcceptAsync(offlineProxyPlayer -> {
                                if (offlineProxyPlayer != null && (textFilter.isEmpty() || offlineProxyPlayer.getPlayerName().contains(textFilter))) {
                                    filteredUUID.add(allowedRecipient);
                                }
                            })).toArray(CompletableFuture[]::new));
            oPP.thenRun(() -> {
                List<GuiItem> guiItems = new ArrayList<>();
                CompletableFuture<Void> future = CompletableFuture.allOf(filteredUUID.stream().map(allowedRecipient -> getItemStackR(uuidRecipient, allowedRecipient)
                        .thenAcceptAsync(itemStack -> guiItems.add(new GuiItem(itemStack, event -> {
                            if(event.getClick() == ClickType.RIGHT) {
                                event.getWhoClicked().closeInventory();
                                new PrivateLog_GUI().getPrivateMessageGUI(uuidRecipient, true, allowedRecipient.getPlayerName(), "", open).thenAccept(paginatedGui -> Bukkit.getScheduler().runTask(MafanaTextNetwork.getInstance(), () -> paginatedGui.open(open)));
                                } else if (event.getClick() == ClickType.LEFT) {
                                event.getWhoClicked().closeInventory();
                                open.sendMessage(ChatColor.GREEN + "MafanaTextNetwork: PLAYER_REMOVED");
                                MafanaTextNetwork.getInstance().getGamePlayerMessageData().removeRecipient(uuidRecipient, allowedRecipient.getPlayerUUID()).thenAccept(s -> {
                                    MafanaTextNetwork.getInstance().getLogger().log(Level.INFO, "Updated Recipient Removed: UUID: " + allowedRecipient.getPlayerUUID() + " NAME: " + allowedRecipient.getPlayerName());
                                    getAllowedRecipientGUI(uuidRecipient, textFilter, open).thenAccept(paginatedGui -> Bukkit.getScheduler().runTask(MafanaTextNetwork.getInstance(), () -> paginatedGui.open(open)));
                                });
                            }
                        })))).toArray(CompletableFuture[]::new));
                future.thenRun(() -> {
                    for (GuiItem g : guiItems) {
                        gui.addItem(g);
                    }
                    gui.update();
                    guiCompletableFuture.complete(gui);
                });
            });
        });
        return guiCompletableFuture;
    }

    private CompletableFuture<ItemStack> getItemStackR(UUID uuid, AllowedRecipient allowedRecipient) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                OfflineProxyPlayer aR = MafanaNetworkCommunicator.getInstance().getPlayerDatabase().getOfflineProxyPlayerAsync(allowedRecipient.getPlayerUUID()).get();
                List<GamePlayerPrivateMessaging> gamePlayerPrivateMessaging = MafanaTextNetwork.getInstance().getGamePlayerMessageData().getPrivateChatsWithAllowedRecipientAsync(uuid, allowedRecipient.getPlayerUUID()).get();
                ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
                SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
                skullMeta.setDisplayName(ChatColor.DARK_GRAY + "[" + aR.getPlayerName() + ChatColor.DARK_GRAY + "]");
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(allowedRecipient.getPlayerUUID());
                if(offlinePlayer != null) {
                    skullMeta.setOwningPlayer(offlinePlayer);
                }
                List<String> skullLore = new ArrayList<>();
                skullLore.add(ChatColor.DARK_GRAY + "------------------------");
                skullLore.add(ChatColor.DARK_GRAY + "Name: " + aR.getPlayerName());
                skullLore.add(ChatColor.DARK_GRAY + "Chats Together : " + gamePlayerPrivateMessaging.size());
                skullLore.add("");
                skullLore.add(ChatColor.DARK_RED + "Left Click to remove");
                skullLore.add(ChatColor.DARK_GREEN + "Right Click to view chats together");
                skullLore.add(ChatColor.DARK_GRAY + "------------------------");
                skullMeta.setLore(skullLore);
                skull.setItemMeta(skullMeta);
                return skull;
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void openFilterSign(UUID uuid, String textFilter, Player open) {
        String filterName = "Text";
        SignGUI.builder()
                .setLines(null, filterName + " Filter:", textFilter, "MafanaTextNetwork") // set lines
                .setType(Material.DARK_OAK_SIGN) // set the sign type
                .setHandler((p, result) -> {
                    String filterValue = result.getLineWithoutColor(0);
                    open.closeInventory();
                    return List.of(SignGUIAction.run(() -> {
                        try {
                            getAllowedRecipientGUI(uuid, filterValue, open).thenAccept(paginatedGui -> Bukkit.getScheduler().runTask(MafanaTextNetwork.getInstance(), () -> paginatedGui.open(open)));
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }));
                }).callHandlerSynchronously(MafanaTextNetwork.getInstance()).build().open(open);
    }

}
