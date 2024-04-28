package com.natamus.recast.neoforge.events;

import com.natamus.recast.events.RecastEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemFishedEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber
public class NeoForgeRecastEvent {
	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent e) {
		Player player = e.getEntity();
		Level level = player.level();
		if (level.isClientSide) {
			return;
		}

		RecastEvent.onPlayerTick((ServerLevel)level, (ServerPlayer)player);
	}
	
	@SubscribeEvent
	public static void onFishingCatch(ItemFishedEvent e) {
		RecastEvent.onFishingCatch(e.getDrops(), e.getHookEntity());
	}
}
