package hibi.blahaj;

import java.util.function.Consumer;

import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

public class ItemContainerCuddlyItem extends CuddlyItem {

	public static final String STORED_ITEM_KEY = "Item";

	public static final DataComponentType<ItemStack> STORED_ITEM = Registry.register(
		BuiltInRegistries.DATA_COMPONENT_TYPE,
		Identifier.fromNamespaceAndPath(Common.NAMESPACE, STORED_ITEM_KEY),
		DataComponentType.<ItemStack>builder().persistent(ItemStack.CODEC).build()
	);

	public ItemContainerCuddlyItem(Properties settings, String subtitle) {
		super(settings, subtitle);
	}

	@Override
	public boolean overrideStackedOnOther(ItemStack thisStack, Slot otherSlot, ClickAction clickType, Player player) {
		if (clickType != ClickAction.SECONDARY) {
			return false;
		}
		ItemStack otherStack = otherSlot.getItem();
		ItemStack storedStack = thisStack.get(STORED_ITEM);
		if (storedStack != null) {
			if (!otherStack.isEmpty()) {
				return false;
			}
			if (!otherSlot.mayPlace(storedStack)) {
				return false;
			}
			otherSlot.safeInsert(storedStack.copy(), storedStack.getMaxStackSize());
			ItemContainerCuddlyItem.storeItemStack(thisStack, null);
			return true;
		} else {
			if (otherStack.isEmpty()) {
				return false;
			}
			if (!ItemContainerCuddlyItem.canHold(otherStack)) {
				return false;
			}
			ItemContainerCuddlyItem.storeItemStack(thisStack, otherStack);
			return true;
		}
	}

	@Override
	public boolean overrideOtherStackedOnMe(ItemStack thisStack, ItemStack otherStack, Slot thisSlot, ClickAction clickType, Player player, SlotAccess cursorStackReference) {
		if (clickType != ClickAction.SECONDARY || otherStack.isEmpty()) {
			return false;
		}

		ItemStack storedStack = thisStack.get(STORED_ITEM);
		if (storedStack != null) {
			return false;
		} else {
			if (!ItemContainerCuddlyItem.canHold(otherStack)) {
				return false;
			}
			ItemContainerCuddlyItem.storeItemStack(thisStack, otherStack);
			return true;
		}
	}

	@Override
	public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, TooltipDisplay tooltipDisplay, Consumer<Component> consumer, TooltipFlag tooltipFlag) {
		super.appendHoverText(itemStack, tooltipContext, tooltipDisplay, consumer, tooltipFlag);
		ItemStack storedStack = itemStack.get(STORED_ITEM);
		if (storedStack == null || storedStack.isEmpty()) {
			return;
		}
		MutableComponent text = storedStack.getHoverName().copy();
		text.append(" x").append(String.valueOf(storedStack.getCount()));
		consumer.accept(text);
	}

	protected static boolean canHold(ItemStack otherStack) {
		if (!otherStack.getItem().canFitInsideContainerItems()
		|| otherStack.getItem() instanceof ItemContainerCuddlyItem
		|| otherStack.getItem() instanceof BundleItem) {
			return false;
		}
		return true;
	}

	protected static void storeItemStack(ItemStack thisStack, @Nullable ItemStack otherStack) {
		if (otherStack == null || otherStack.isEmpty()) {
			thisStack.remove(STORED_ITEM);
		} else {
			thisStack.set(STORED_ITEM, otherStack.copyWithCount(otherStack.getCount()));
			otherStack.setCount(0);
		}
	}

	protected static boolean mergeStacks(ItemStack dest, ItemStack source) {
		if (!ItemStack.isSameItem(dest, source)) {
			return false;
		}
		int destCount = dest.getCount();
		int sourceCount = source.getCount();
		int destMax = dest.getMaxStackSize();
		dest.grow(destCount + sourceCount);
		int surplus = destCount + sourceCount - destMax;
		source.setCount(surplus);
		return source.isEmpty();
	}
}
