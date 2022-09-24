package net.smileycorp.hordes.common;

import java.util.Collection;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.living.LivingConversionEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.smileycorp.hordes.common.capability.IZombifyPlayer;
import net.smileycorp.hordes.common.entities.IZombiePlayer;
import net.smileycorp.hordes.common.infection.HordesInfection;

@EventBusSubscriber(modid = ModDefinitions.MODID, bus = Bus.MOD)
public class MiscEventHandler {

	//determine if zombie entity should spawn, and if so create the correct entity and set properties
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onDeath(LivingDeathEvent event) {
		LivingEntity entity = event.getEntity();
		if (entity!=null) {
			Level level = entity.level;
			if (!level.isClientSide) {
				if (entity instanceof Player &!(entity instanceof FakePlayer)) {
					if ((entity.hasEffect(HordesInfection.INFECTED.get()) && CommonConfigHandler.enableMobInfection.get()) || CommonConfigHandler.zombieGraves.get() ||
							(entity.isUnderWater() && CommonConfigHandler.drownedGraves.get())) {
						LazyOptional<IZombifyPlayer> optional = entity.getCapability(Hordes.ZOMBIFY_PLAYER, null);
						if (optional.isPresent()) {
							optional.resolve().get().createZombie((Player) entity);
						}
					}
				}
			}
		}
	}

	//move items to zombie entity and spawn if one should spawn
	@SubscribeEvent(receiveCanceled = true)
	public void onDrop(LivingDropsEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			Level level = player.level;
			if (!level.isClientSide &!(player instanceof FakePlayer)) {
				if ((player.hasEffect(HordesInfection.INFECTED.get()) && CommonConfigHandler.enableMobInfection.get()) || CommonConfigHandler.zombieGraves.get()) {
					LazyOptional<IZombifyPlayer> optional = player.getCapability(Hordes.ZOMBIFY_PLAYER, null);
					if (optional.isPresent()) {
						IZombifyPlayer cap = optional.resolve().get();
						Mob zombie = cap.getZombie();
						if (zombie!=null) {
							Collection<ItemEntity> drops = event.getDrops();
							((IZombiePlayer)zombie).setInventory(drops);
							zombie.setPersistenceRequired();
							level.addFreshEntity(zombie);
							drops.clear();
							cap.clearZombie();
							event.setCanceled(true);
						}
					}
				}
			}
		}
	}

	//attach zombie player provider to players
	@SubscribeEvent
	public void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
		Entity entity = event.getObject();
		if (entity instanceof Player &!(entity instanceof FakePlayer)) {
			event.addCapability(ModDefinitions.getResource("Zombify"), new IZombifyPlayer.Provider());
		}
	}

	//copy horse inventories if they convert to another entity, useful for copying armor and saddles to zombie horses
	@SubscribeEvent
	public void entityConvert(LivingConversionEvent.Post event) {
		LivingEntity before = event.getEntity();
		if (before.level.isClientSide) return;
		LivingEntity after = event.getOutcome();
		if (before instanceof AbstractHorse && after instanceof AbstractHorse) {
			SimpleContainer beforeInv = ((AbstractHorse)before).inventory;
			SimpleContainer afterInv = ((AbstractHorse)after).inventory;
			for (int i = 0; i < Math.min(beforeInv.getContainerSize(), afterInv.getContainerSize()); i++) {
				afterInv.setItem(i, beforeInv.getItem(i).copy());
			}
		}
	}

	//register attributes for zombie/drowned players
	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(HordesInfection.ZOMBIE_PLAYER.get(), Zombie.createAttributes().build());
		event.put(HordesInfection.DROWNED_PLAYER.get(), Drowned.createAttributes().build());
	}

}
