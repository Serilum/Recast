package com.natamus.recast.forge.events;

import com.natamus.recast.events.RecastEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class ForgeRecastEvent {
	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent e) {
		Player player = e.player;
		Level level = player.getCommandSenderWorld();
		if (level.isClientSide || !e.phase.equals(TickEvent.Phase.END)) {
			return;
		}

		RecastEvent.onPlayerTick((ServerLevel)level, (ServerPlayer)player);
	}
	
	@SubscribeEvent
	public void onFishingCatch(ItemFishedEvent e) {
		RecastEvent.onFishingCatch(e.getDrops(), e.getHookEntity());
	}
}
