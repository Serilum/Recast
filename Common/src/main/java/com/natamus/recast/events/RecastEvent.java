package com.natamus.recast.events;

import com.natamus.collective.data.GlobalVariables;
import com.natamus.collective.functions.BlockPosFunctions;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.List;

public class RecastEvent {
	private static final HashMap<Player, Vec3> recasting = new HashMap<Player, Vec3>();
	private static final HashMap<FishingHook, Vec3> lastcastlocation = new HashMap<FishingHook, Vec3>();
	
	public static void onPlayerTick(ServerLevel world, ServerPlayer player) {
		if (!recasting.containsKey(player)) {
			return;
		}
		
		ItemStack activestack;
		ItemStack mainhand = player.getMainHandItem();
		if (!(mainhand.getItem() instanceof FishingRodItem)) {
			ItemStack offhand = player.getOffhandItem();
			if (!(offhand.getItem() instanceof FishingRodItem)) {
				recasting.remove(player);
				return;
			}
			activestack = offhand;
		}
		else {
			activestack = mainhand;
		}
		
		Vec3 fbvec = recasting.get(player);
		
		world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FISHING_BOBBER_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (GlobalVariables.random.nextFloat() * 0.4F + 0.8F));
		
		int k = EnchantmentHelper.getFishingSpeedBonus(activestack);
		int j = EnchantmentHelper.getFishingLuckBonus(activestack);
		
		FishingHook fbe = new FishingHook(player, world, j, k);
		fbe.teleportTo(fbvec.x, fbvec.y, fbvec.z);
		world.addFreshEntity(fbe);
		
		player.awardStat(Stats.ITEM_USED.get(Items.FISHING_ROD));
		
		recasting.remove(player);
		lastcastlocation.put(fbe, fbvec);
	}
	
	public static void onFishingCatch(List<ItemStack> loot, FishingHook fbe) {
		Player player = fbe.getPlayerOwner();
		Level world = player.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		
		Vec3 fbvec = fbe.position();
		if (lastcastlocation.containsKey(fbe)) {
			Vec3 lastvec = lastcastlocation.get(fbe);
			if (BlockPosFunctions.withinDistance(new BlockPos(fbvec.x, fbvec.y, fbvec.z), new BlockPos(lastvec.x, lastvec.y, lastvec.z), 5)) {
				fbvec = lastvec;
			}
		}
		
		recasting.put(player, fbvec);
		lastcastlocation.remove(fbe);
	}
}
