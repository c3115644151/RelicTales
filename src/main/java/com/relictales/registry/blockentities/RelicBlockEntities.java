package com.relictales.registry.blockentities;

// RelicBrushableBlockEntity is created by MixinBrushableBlock's redirect,
// not via BlockEntityType factory. LevelChunk.createBlockEntity() calls
// block.newBlockEntity() directly, so no custom BET registration is needed.
//
// BET creation path (verified via bytecode):
//   LevelChunk.createBlockEntity(pos)
//     → state.hasBlockEntity()  [checks if block implements EntityBlock]
//     → block.newBlockEntity(pos, state)  [MixinBrushableBlock redirects here]
//     → new RelicBrushableBlockEntity(pos, state)
// BE type (BlockEntityType.BRUSHABLE_BLOCK) is set in RelicBrushableBlockEntity constructor.
public class RelicBlockEntities {
    // No BET registration needed — mixin handles BE creation
}
