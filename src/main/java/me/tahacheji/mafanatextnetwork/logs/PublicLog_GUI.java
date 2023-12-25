package me.tahacheji.mafanatextnetwork.logs;

import de.rapha149.signgui.SignGUI;
import de.rapha149.signgui.SignGUIAction;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import me.tahacheji.mafanatextnetwork.MafanaTextNetwork;
import me.tahacheji.mafanatextnetwork.data.GamePlayerPrivateMessaging;
import me.tahacheji.mafanatextnetwork.data.GamePlayerPublicMessaging;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PublicLog_GUI {

    public PaginatedGui getPublicMessageGUI(Player player, boolean sortNewestToOldest, String textFilter) throws Exception {
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
                getPublicMessageGUI(clicker, newSortDirection, textFilter).open(player);
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
            openFilterSign((Player) event.getWhoClicked(), sortNewestToOldest, textFilter);
        }));

        List<GamePlayerPublicMessaging> privateMessages = MafanaTextNetwork.getInstance().getGamePlayerMessageData().getPublicTextList(player);
        List<GamePlayerPublicMessaging> filteredMessages = new ArrayList<>();
        for (GamePlayerPublicMessaging privateMessage : privateMessages) {
            if (textFilter.isEmpty() || privateMessage.getText().contains(textFilter)) {
                filteredMessages.add(privateMessage);
            }
        }
        if (sortNewestToOldest) {
            filteredMessages.sort(Comparator.comparing(GamePlayerPublicMessaging::getLocalDateTime).reversed());
        } else {
            filteredMessages.sort(Comparator.comparing(GamePlayerPublicMessaging::getLocalDateTime));
        }
        for (GamePlayerPublicMessaging publicMessaging : filteredMessages) {
            ItemStack item = getItemStackPM(publicMessaging);
            gui.addItem(new GuiItem(item));
        }

        return gui;
    }


    @NotNull
    private static ItemStack getItemStackPM(GamePlayerPublicMessaging publicMessaging) {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.AQUA + publicMessaging.getTime());
        List<String> itemLore = new ArrayList<>();
        itemLore.add(ChatColor.DARK_GRAY + "");
        itemLore.add("------------------------");
        itemLore.add(ChatColor.DARK_GRAY + "Sender: " + publicMessaging.getSender().getName());
        itemLore.add(ChatColor.DARK_GRAY + "");
        if (publicMessaging.getText() != null) {
            itemLore.add(ChatColor.DARK_GRAY + "Message: " + publicMessaging.getText());
        } else {
            itemLore.add(ChatColor.DARK_GRAY + "Message: " + "NULL");
        }
        itemLore.add(ChatColor.DARK_GRAY + "Time: " + publicMessaging.getTime());
        itemLore.add(ChatColor.DARK_GRAY + "");
        itemLore.add("------------------------");
        itemMeta.setLore(itemLore);
        item.setItemMeta(itemMeta);
        return item;
    }

    public void openFilterSign(Player player, boolean sortNewestToOldest, String textFilter) {
        String filterName = "Text";
        SignGUI.builder()
                .setLines(null, filterName + " Filter:", textFilter, "MafanaTextNetwork") // set lines
                .setType(Material.DARK_OAK_SIGN) // set the sign type
                .setHandler((p, result) -> {
                    String filterValue = result.getLineWithoutColor(0);
                    player.closeInventory();
                        return List.of(SignGUIAction.run(() -> {
                            try {
                                getPublicMessageGUI(player, sortNewestToOldest, filterValue).open(player);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }));
                }).callHandlerSynchronously(MafanaTextNetwork.getInstance()).build().open(player);
    }


}
