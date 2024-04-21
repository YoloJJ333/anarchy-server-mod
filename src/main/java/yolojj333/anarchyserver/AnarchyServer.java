package yolojj333.anarchyserver;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.item.Item;
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
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import yolojj333.anarchyserver.util.ItemInterface;

import java.util.HashMap;
import java.util.Map;

public class AnarchyServer implements ModInitializer {
    public void onInitialize() {
        Log.info("anarchy mod starting");
        NbtCompound smil = new NbtCompound();
        smil.putInt("CustomModelData", 1);
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            if (source.isBuiltin()) {
                LootPool.Builder pb = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .conditionally(RandomChanceLootCondition.builder(/*0.0001F*/1.0F))
                        .conditionally(SurvivesExplosionLootCondition.builder())
                        .with(ItemEntry.builder(Items.CARROT_ON_A_STICK))
                        .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F)))
                        .apply(SetNbtLootFunction.builder(smil))
                        .apply(SetLoreLootFunction.builder()
                                .lore(Text.of("smil")));
                tableBuilder.pool(pb);
            }
        });
        Map<String, Integer> maxCounts = new HashMap<>();
        maxCounts.put("egg", 4);
        changeMaxStackCount(maxCounts);
    }

    private void changeMaxStackCount(Map<String, Integer> maxCounts){
        for(Map.Entry<String, Integer> entry : maxCounts.entrySet()){
            Item item = Registries.ITEM.get(new Identifier(entry.getKey()));
            ((ItemInterface) item).setMaxCount(entry.getValue());
        }
    }
}
