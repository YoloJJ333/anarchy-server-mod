package yolojj333.mymod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.condition.SurvivesExplosionLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.function.SetLoreLootFunction;
import net.minecraft.loot.function.SetNbtLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;

public class MyMod implements ModInitializer {
    public void onInitialize() {
        Log.info("anarchy mod starting");
        NbtCompound smil = new NbtCompound();
        smil.putInt("CustomModelData", 1);
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            if (source.isBuiltin()) {
                LootPool.Builder pb = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .conditionally(RandomChanceLootCondition.builder(0.0001F))
                        .conditionally(SurvivesExplosionLootCondition.builder())
                        .with(ItemEntry.builder(Items.CARROT_ON_A_STICK))
                        .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F)))
                        .apply(SetNbtLootFunction.builder(smil))
                        .apply(SetLoreLootFunction.builder()
                                .lore(Text.of("smil")));
                tableBuilder.pool(pb);
            }
        });
    }
}
