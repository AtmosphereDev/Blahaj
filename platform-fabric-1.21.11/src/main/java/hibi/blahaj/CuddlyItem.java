package hibi.blahaj;

import java.util.function.Consumer;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.state.BlockState;

public class CuddlyItem extends Item {

	public static final String OWNER_KEY = "Owner";

	private final Component subtitle;

	public CuddlyItem(Properties settings, String subtitle) {
		super(settings);
		this.subtitle = subtitle == null? null: Component.translatable(subtitle).withStyle(ChatFormatting.GRAY); // Gray
	}

	@Override
	public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, TooltipDisplay tooltipDisplay, Consumer<Component> consumer, TooltipFlag tooltipFlag) {
		if(this.subtitle != null) {
			consumer.accept(this.subtitle);
		}
		if(itemStack.has(DataComponents.CUSTOM_DATA)) {
			CustomData customData = itemStack.get(DataComponents.CUSTOM_DATA);
			CompoundTag nbt = customData.copyTag();
			String owner = nbt.getStringOr(OWNER_KEY, "");
			if(owner == "") {
				return;
			}
			if(itemStack.has(DataComponents.CUSTOM_NAME)) {
				consumer.accept(Component.translatable("tooltip.blahaj.owner.rename", this.getDescriptionId(), Component.literal(owner)).withStyle(ChatFormatting.GRAY));
			}
			else {
				consumer.accept(Component.translatable("tooltip.blahaj.owner.craft", Component.literal(owner)).withStyle(ChatFormatting.GRAY));
			}
		}
	}

	@Override
	public void onCraftedBy(ItemStack itemStack, Player player) {
		itemStack.update(DataComponents.CUSTOM_DATA, CustomData.EMPTY, existing -> {
			CompoundTag tag = existing.copyTag();
			tag.putString(OWNER_KEY, player.getName().getString());
			return CustomData.of(tag);
		});
		super.onCraftedBy(itemStack, player);
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		return 0.25f;
	}
}
