package net.mcreator.dimensionswicher.procedures;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.common.MinecraftForge;

import net.minecraft.world.IWorld;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.Entity;

import net.mcreator.dimensionswicher.DimensionSwicherModVariables;
import net.mcreator.dimensionswicher.DimensionSwicherMod;

import java.util.Map;

public class DimensionOnKeyPressedProcedure {

	public static void executeProcedure(Map<String, Object> dependencies) {
		if (dependencies.get("world") == null) {
			if (!dependencies.containsKey("world"))
				DimensionSwicherMod.LOGGER.warn("Failed to load dependency world for procedure DimensionOnKeyPressed!");
			return;
		}
		if (dependencies.get("entity") == null) {
			if (!dependencies.containsKey("entity"))
				DimensionSwicherMod.LOGGER.warn("Failed to load dependency entity for procedure DimensionOnKeyPressed!");
			return;
		}
		IWorld world = (IWorld) dependencies.get("world");
		Entity entity = (Entity) dependencies.get("entity");
		boolean not_finised = false;
		DimensionSwicherModVariables.WorldVariables.get(world).key_pressed = (true);
		DimensionSwicherModVariables.WorldVariables.get(world).syncData(world);
		if (!not_finised) {
			not_finised = (true);
			new Object() {
				private int ticks = 0;
				private float waitTicks;
				private IWorld world;

				public void start(IWorld world, int waitTicks) {
					this.waitTicks = waitTicks;
					MinecraftForge.EVENT_BUS.register(this);
					this.world = world;
				}

				@SubscribeEvent
				public void tick(TickEvent.ServerTickEvent event) {
					if (event.phase == TickEvent.Phase.END) {
						this.ticks += 1;
						if (this.ticks >= this.waitTicks)
							run();
					}
				}

				private void run() {
					if (entity instanceof PlayerEntity && !entity.world.isRemote()) {
						((PlayerEntity) entity).sendStatusMessage(new StringTextComponent("Overworld"), (true));
					}
					DimensionSwicherModVariables.WorldVariables.get(world).dimension = 0;
					DimensionSwicherModVariables.WorldVariables.get(world).syncData(world);
					new Object() {
						private int ticks = 0;
						private float waitTicks;
						private IWorld world;

						public void start(IWorld world, int waitTicks) {
							this.waitTicks = waitTicks;
							MinecraftForge.EVENT_BUS.register(this);
							this.world = world;
						}

						@SubscribeEvent
						public void tick(TickEvent.ServerTickEvent event) {
							if (event.phase == TickEvent.Phase.END) {
								this.ticks += 1;
								if (this.ticks >= this.waitTicks)
									run();
							}
						}

						private void run() {
							if (DimensionSwicherModVariables.WorldVariables.get(world).key_pressed) {
								if (entity instanceof PlayerEntity && !entity.world.isRemote()) {
									((PlayerEntity) entity).sendStatusMessage(new StringTextComponent("Nether"), (true));
								}
								DimensionSwicherModVariables.WorldVariables.get(world).dimension = (-1);
								DimensionSwicherModVariables.WorldVariables.get(world).syncData(world);
								new Object() {
									private int ticks = 0;
									private float waitTicks;
									private IWorld world;

									public void start(IWorld world, int waitTicks) {
										this.waitTicks = waitTicks;
										MinecraftForge.EVENT_BUS.register(this);
										this.world = world;
									}

									@SubscribeEvent
									public void tick(TickEvent.ServerTickEvent event) {
										if (event.phase == TickEvent.Phase.END) {
											this.ticks += 1;
											if (this.ticks >= this.waitTicks)
												run();
										}
									}

									private void run() {
										if (DimensionSwicherModVariables.WorldVariables.get(world).key_pressed) {
											if (entity instanceof PlayerEntity && !entity.world.isRemote()) {
												((PlayerEntity) entity).sendStatusMessage(new StringTextComponent("End"), (true));
											}
											DimensionSwicherModVariables.WorldVariables.get(world).dimension = 1;
											DimensionSwicherModVariables.WorldVariables.get(world).syncData(world);
										}
										MinecraftForge.EVENT_BUS.unregister(this);
									}
								}.start(world, (int) 20);
							}
							MinecraftForge.EVENT_BUS.unregister(this);
						}
					}.start(world, (int) 20);
					MinecraftForge.EVENT_BUS.unregister(this);
				}
			}.start(world, (int) 20);
			not_finised = (false);
		}
	}
}
