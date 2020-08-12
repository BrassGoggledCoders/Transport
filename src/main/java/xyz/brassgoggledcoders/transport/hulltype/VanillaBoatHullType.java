package xyz.brassgoggledcoders.transport.hulltype;

import net.minecraft.entity.item.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.NonNullSupplier;
import xyz.brassgoggledcoders.transport.api.entity.HullType;

public class VanillaBoatHullType extends HullType {

    public VanillaBoatHullType(Item boat, BoatEntity.Type type) {
        this(() -> boat, type);
    }

    public VanillaBoatHullType(NonNullSupplier<Item> boatSupplier, BoatEntity.Type type) {
        super(boatSupplier, () -> new ResourceLocation("textures/entity/boat/" + type.getName() + ".png"));
    }
}
