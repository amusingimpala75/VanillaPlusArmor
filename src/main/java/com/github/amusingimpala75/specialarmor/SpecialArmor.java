package com.github.amusingimpala75.specialarmor;

import com.github.amusingimpala75.specialarmor.item.HoneyChestplate;
import com.github.amusingimpala75.specialarmor.item.IceBoots;
import com.github.amusingimpala75.specialarmor.item.SlimeBoots;
import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SpecialArmor implements ModInitializer {
    public static String MOD_ID = "special_armor";
    public static final Item SLIME_BOOTS = new SlimeBoots();
    public static final Item HONEY_CHESTPLATE = new HoneyChestplate();
    public static final Item ICE_BOOTS = new IceBoots();
    @Override
    public void onInitialize() {
        Registry.register(Registry.ITEM, id("slime_boots"), SLIME_BOOTS);
        Registry.register(Registry.ITEM, id("honey_chestplate"), HONEY_CHESTPLATE);
        Registry.register(Registry.ITEM, id("ice_boots"), ICE_BOOTS);
    }
    public static Identifier id(String name) {
        return new Identifier(MOD_ID, name);
    }

}
