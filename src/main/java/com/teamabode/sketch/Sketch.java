package com.teamabode.sketch;

import com.teamabode.sketch.common.entity.boat.SketchBoatType;
import com.teamabode.sketch.core.api.advancement.AdvancementEvents;
import com.teamabode.sketch.core.api.advancement.modifier.AddElementModifier;
import com.teamabode.sketch.core.api.advancement.modifier.AddListModifier;
import com.teamabode.sketch.core.api.advancement.modifier.AdvancementModifier;
import com.teamabode.sketch.core.api.advancement.modifier.RequirementModifier;
import com.teamabode.sketch.core.registry.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.advancements.critereon.ItemUsedOnLocationTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Sketch implements ModInitializer {
    public static final String MOD_ID = "sketch";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public void onInitialize() {
        LOGGER.info("Initializing Sketch");

        SketchRegistries.init();
        SketchBuiltInRegistries.init();
        SketchDataSerializers.init();

        SketchFeatures.init();
        SketchItems.init();
        SketchBoatType.init();
        SketchEntities.init();

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.OP_BLOCKS).register(entries -> {
            if (entries.getContext().hasPermissions()) {
                entries.addAfter(Items.DEBUG_STICK, SketchItems.DEFAULT_BOAT, SketchItems.DEFAULT_CHEST_BOAT);
            }
        });

        AdvancementEvents.MODIFY.register((resourceManager, advancementManager, id, builder) -> {

            if (Objects.equals(id, new ResourceLocation("husbandry/plant_seed"))) {
                builder.addCriterion("placed_stone", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(Blocks.STONE));

                String[][] lists = {
                        {"placed_stone"}
                };

                RequirementModifier modifier = new AddListModifier(lists);
                modifier.modify(builder);
                LOGGER.info(Arrays.deepToString(modifier.getRequirements(builder)));
            }
        });
    }

    public static ResourceLocation id(String name) {
        return new ResourceLocation(MOD_ID, name);
    }
}
