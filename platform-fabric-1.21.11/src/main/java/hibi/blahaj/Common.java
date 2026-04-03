package hibi.blahaj;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;

import java.util.function.Function;

public class Common {
	public static final String NAMESPACE = "blahaj";

	public static final Identifier BLAHAJ_ID;
	public static final Identifier KLAPPAR_HAJ_ID;
	public static final Identifier BLAVINGAD_ID;
	public static final Identifier BREAD_ID;
	public static final Identifier BROWN_BEAR_ID;

	public void onInitializeQuilt(Object _mod) {
		// NOTE: Cast `_mod` to `ModContainer` before using it.
		this.onInitialize();
	}

	public void onInitialize() {
		Item blueShark = register("gray_shark", "item.blahaj.gray_shark.tooltip");
		Item grayShark = register("blue_shark", "item.blahaj.blue_shark.tooltip");
		Item blueWhale = register("blue_whale", "item.blahaj.blue_whale.tooltip");
		Item breadPillow = register("bread", null);
	    Item brownBear = register("brown_bear", "item.blahaj.brown_bear.tooltip");

		// Register items to item group
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register((content) -> {
			content.accept(blueShark);
			content.accept(grayShark);
			content.accept(blueWhale);
			content.accept(breadPillow);
			content.accept(brownBear);
		});

//		LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
		LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
			if(!source.isBuiltin()) return;
			if(BuiltInLootTables.STRONGHOLD_CROSSING.equals(key) // Stronghold crossing
				|| BuiltInLootTables.STRONGHOLD_CORRIDOR.equals(key)) { // Stronghold corridor
				LootPool.Builder pb = LootPool.lootPool()
					.add(LootItem.lootTableItem(grayShark)
						.setWeight(5))
					.add(LootItem.lootTableItem(Items.AIR) // Air
						.setWeight(100));
				tableBuilder.withPool(pb);
			}
			else if(key.equals(BuiltInLootTables.VILLAGE_PLAINS_HOUSE)) {
				LootPool.Builder pb = LootPool.lootPool()
					.add(LootItem.lootTableItem(grayShark))
					.add(LootItem.lootTableItem(Items.AIR)
						.setWeight(43));
				tableBuilder.withPool(pb);
			}
			else if(key.equals(BuiltInLootTables.VILLAGE_TAIGA_HOUSE)
				|| key.equals(BuiltInLootTables.VILLAGE_SNOWY_HOUSE)) {
				LootPool.Builder pb = LootPool.lootPool()
					.add(LootItem.lootTableItem(grayShark)
						.setWeight(5))
					.add(LootItem.lootTableItem(Items.AIR)
						.setWeight(54));
				tableBuilder.withPool(pb);
			}
		});

		TradeOfferHelper.registerVillagerOffers(VillagerProfession.SHEPHERD, 5,
			factories -> {
			    factories.add((serverLevel, entity, random) -> new MerchantOffer(
				    new ItemCost(Items.EMERALD, 15),
					new ItemStack(grayShark), // Emerald
				    2, 30, 0.1f));
			}); // Shepherd
	}

	static {
		BLAHAJ_ID = Identifier.fromNamespaceAndPath(NAMESPACE, "blue_shark");
		KLAPPAR_HAJ_ID = Identifier.fromNamespaceAndPath(NAMESPACE, "gray_shark");
		BLAVINGAD_ID = Identifier.fromNamespaceAndPath(NAMESPACE, "blue_whale");
		BREAD_ID = Identifier.fromNamespaceAndPath(NAMESPACE, "bread");
		BROWN_BEAR_ID = Identifier.fromNamespaceAndPath(NAMESPACE, "brown_bear");
	}

	public static Item register(String name, String subtitle) {
		// Create the item key.
		ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(NAMESPACE, name));

		// Create the item instance.
		// T item = itemFactory.apply(settings.setId(itemKey));
		Item tCuddlyItem = new CuddlyItem(new Item.Properties().setId(itemKey).stacksTo(1), subtitle);

		// Register the item.
		Registry.register(BuiltInRegistries.ITEM, itemKey, tCuddlyItem);

		return tCuddlyItem;
	}
}
