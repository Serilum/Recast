package com.natamus.recast;

import com.natamus.collective.check.RegisterMod;
import com.natamus.collective.check.ShouldLoadCheck;
import com.natamus.collective.fabric.callbacks.CollectiveItemEvents;
import com.natamus.collective.fabric.callbacks.CollectivePlayerEvents;
import com.natamus.recast.events.RecastEvent;
import com.natamus.recast.util.Reference;
import net.fabricmc.api.ModInitializer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class ModFabric implements ModInitializer {
	
	@Override
	public void onInitialize() {
		if (!ShouldLoadCheck.shouldLoad(Reference.MOD_ID)) {
			return;
		}

		setGlobalConstants();
		ModCommon.init();

		loadEvents();

		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}

	private void loadEvents() {
		CollectivePlayerEvents.PLAYER_TICK.register((ServerLevel world, ServerPlayer player) -> {
			RecastEvent.onPlayerTick(world, player);
		});

		CollectiveItemEvents.ON_ITEM_FISHED.register((List<ItemStack> loot, FishingHook hook) -> {
			RecastEvent.onFishingCatch(loot, hook);
		});
	}

	private static void setGlobalConstants() {

	}
}
