package com.linngdu664.bsf3lite.misc;

import com.linngdu664.bsf3lite.Main;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Items {
        public static final TagKey<Item> EMERALD_TOOL_MATERIALS = ItemTags.create(Main.makeMyIdentifier("emerald_tool_materials"));
        public static final TagKey<Item> NONE = ItemTags.create(Main.makeMyIdentifier("item_none"));
    }

    public static class Blocks {
        public static final TagKey<Block> NONE = BlockTags.create(Main.makeMyIdentifier("block_none"));
    }
}
