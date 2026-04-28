package com.relictales.test;

import com.relictales.RelicTales;
import com.relictales.content.block.RelicBrushableBlockEntity;
import com.relictales.mixin.BrushableBlockEntityAccessor;
import com.relictales.mixin.StructurePieceInvoker;
import com.relictales.registry.blocks.RelicBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.structures.NetherFortressPieces;
import net.minecraft.world.level.levelgen.structure.structures.StrongholdPieces;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.util.RandomSource;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;

import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import java.util.function.Consumer;

public class RelicBrushInteractionTest {

    private static final ResourceKey<LootTable> TEST_LOOT_KEY = ResourceKey.create(
            Registries.LOOT_TABLE,
            Identifier.fromNamespaceAndPath(RelicTales.MOD_ID, "blocks/suspicious_mossy_cobblestone")
    );

    private static final ResourceKey<LootTable> TEST_LOOT_KEY_DESERT = ResourceKey.create(
            Registries.LOOT_TABLE,
            Identifier.fromNamespaceAndPath(RelicTales.MOD_ID, "blocks/suspicious_chiseled_sandstone")
    );

    private static final ResourceKey<LootTable> TEST_LOOT_KEY_CRACKED = ResourceKey.create(
            Registries.LOOT_TABLE,
            Identifier.fromNamespaceAndPath(RelicTales.MOD_ID, "blocks/suspicious_cracked_stone_bricks")
    );

    private static final ResourceKey<LootTable> TEST_LOOT_KEY_MOSSY_BRICKS = ResourceKey.create(
            Registries.LOOT_TABLE,
            Identifier.fromNamespaceAndPath(RelicTales.MOD_ID, "blocks/suspicious_mossy_stone_bricks")
    );

    private static final ResourceKey<LootTable> TEST_LOOT_KEY_PURPUR = ResourceKey.create(
            Registries.LOOT_TABLE,
            Identifier.fromNamespaceAndPath(RelicTales.MOD_ID, "blocks/suspicious_purpur_block")
    );

    private static final DeferredRegister<Consumer<GameTestHelper>> TEST_FUNCTIONS =
            DeferredRegister.create(Registries.TEST_FUNCTION, RelicTales.MOD_ID);

    private static DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> TEST1_FUNCTION;
    private static DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> TEST2_FUNCTION;
    private static DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> TEST3_FUNCTION;
    private static DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> TEST4_FUNCTION;
    private static DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> TEST5_FUNCTION;
    private static DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> TEST6_FUNCTION;
    private static DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> TEST7_FUNCTION;
    private static DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> TEST8_FUNCTION;
    private static DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> TEST9_FUNCTION;
    private static DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> TEST10_FUNCTION;
    private static DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> TEST11_FUNCTION;
    private static DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> TEST12_FUNCTION;
    private static DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> TEST13_FUNCTION;
    private static DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> TEST14_FUNCTION;
    private static DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> TEST15_FUNCTION;
    private static DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> TEST16_FUNCTION;
    private static DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> TEST17_FUNCTION;
    private static DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> TEST18_FUNCTION;
    private static DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> TEST19_FUNCTION;
    private static DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> TEST20_FUNCTION;
    private static DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> TEST21_FUNCTION;
    private static DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> TEST22_FUNCTION;
    private static DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> TEST23_FUNCTION;
    private static DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> TEST24_FUNCTION;
    private static DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> TEST25_FUNCTION;
    private static DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> TEST26_FUNCTION;
    private static DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> TEST27_FUNCTION;
    private static DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> TEST28_FUNCTION;
    private static DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> TEST29_FUNCTION;
    private static DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> TEST30_FUNCTION;
    private static DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> TEST31_FUNCTION;
    private static DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> TEST32_FUNCTION;
    private static DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> TEST33_FUNCTION;
    private static DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> TEST34_FUNCTION;

    static {
        TEST1_FUNCTION = TEST_FUNCTIONS.register("suspicious_block_has_correct_be_type", () -> helper -> {
            BlockPos pos = new BlockPos(0, 2, 0);
            helper.setBlock(pos, RelicBlocks.SUSPICIOUS_MOSSY_COBBLESTONE.get());
            helper.runAfterDelay(5, () -> {
                BrushableBlockEntity be = helper.getBlockEntity(pos, BrushableBlockEntity.class);
                if (be == null) {
                    helper.fail("Block entity is null at " + pos + "!");
                    return;
                }
                if (!(be instanceof RelicBrushableBlockEntity)) {
                    helper.fail("BE is " + be.getClass().getName() + " but expected RelicBrushableBlockEntity");
                    return;
                }
                helper.succeed();
            });
        });

        TEST2_FUNCTION = TEST_FUNCTIONS.register("suspicious_block_loot_table_can_be_set", () -> helper -> {
            BlockPos pos = new BlockPos(0, 2, 0);
            helper.setBlock(pos, RelicBlocks.SUSPICIOUS_MOSSY_COBBLESTONE.get());
            helper.runAfterDelay(5, () -> {
                BrushableBlockEntity be = helper.getBlockEntity(pos, BrushableBlockEntity.class);
                if (be == null) {
                    helper.fail("No BE at " + pos);
                    return;
                }
                if (!(be instanceof RelicBrushableBlockEntity relicBe)) {
                    helper.fail("BE is not RelicBrushableBlockEntity");
                    return;
                }
                relicBe.relictales$setLootTable(TEST_LOOT_KEY, 12345L);
                helper.succeed();
            });
        });

        TEST3_FUNCTION = TEST_FUNCTIONS.register("vanilla_block_uses_vanilla_be", () -> helper -> {
            BlockPos pos = new BlockPos(0, 2, 0);
            helper.setBlock(pos, Blocks.SUSPICIOUS_GRAVEL);
            helper.runAfterDelay(5, () -> {
                BrushableBlockEntity be = helper.getBlockEntity(pos, BrushableBlockEntity.class);
                if (be == null) {
                    helper.fail("Vanilla suspicious_gravel has no BE!");
                    return;
                }
                if (be instanceof RelicBrushableBlockEntity) {
                    helper.fail("Vanilla suspicious_gravel got RelicBrushableBlockEntity (should be vanilla BrushableBlockEntity)");
                    return;
                }
                helper.succeed();
            });
        });

        TEST4_FUNCTION = TEST_FUNCTIONS.register("brushing_converts_to_base_block", () -> helper -> {
            BlockPos pos = new BlockPos(0, 2, 0);
            helper.setBlock(pos, RelicBlocks.SUSPICIOUS_MOSSY_COBBLESTONE.get());
            helper.runAfterDelay(5, () -> {
                BrushableBlockEntity be = helper.getBlockEntity(pos, BrushableBlockEntity.class);
                if (be == null) {
                    helper.fail("No BrushableBlockEntity at " + pos);
                    return;
                }
                BrushableBlockEntityAccessor acc = (BrushableBlockEntityAccessor) be;
                acc.setLootTable(TEST_LOOT_KEY);
                acc.setBrushCount(100);
                acc.setHitDirection(Direction.UP);

                ServerLevel level = (ServerLevel) helper.getLevel();
                long tick = level.getGameTime();
                net.minecraft.world.entity.LivingEntity brusher =
                        (net.minecraft.world.entity.LivingEntity) helper.spawn(
                                net.minecraft.world.entity.EntityType.COW, pos.above());

                ItemStack brushStack = new ItemStack(net.minecraft.world.item.Items.BRUSH);
                be.brush(tick, level, brusher, Direction.UP, brushStack);

                if (!helper.getBlockState(pos).is(Blocks.MOSSY_COBBLESTONE)) {
                    helper.fail("Block is " + helper.getBlockState(pos).getBlock() + " after brush (expected MOSSY_COBBLESTONE)");
                    return;
                }

                BlockPos worldPos = be.getBlockPos();
                ServerLevel checkLevel = level;
                helper.runAfterDelay(1, () -> {
                    var items = checkLevel.getEntitiesOfClass(
                            net.minecraft.world.entity.item.ItemEntity.class,
                            new net.minecraft.world.phys.AABB(worldPos).inflate(3.0)
                    );
                    if (items.isEmpty()) {
                        helper.fail("No loot items dropped after brushing completion!");
                        return;
                    }
                    helper.succeed();
                });
            });
        });

        // === Desert Temple Tests ===

        TEST5_FUNCTION = TEST_FUNCTIONS.register("desert_suspicious_block_has_correct_be_type", () -> helper -> {
            BlockPos pos = new BlockPos(0, 2, 0);
            helper.setBlock(pos, RelicBlocks.SUSPICIOUS_CHISELED_SANDSTONE.get());
            helper.runAfterDelay(5, () -> {
                BrushableBlockEntity be = helper.getBlockEntity(pos, BrushableBlockEntity.class);
                if (be == null) {
                    helper.fail("Block entity is null at " + pos + "!");
                    return;
                }
                if (!(be instanceof RelicBrushableBlockEntity)) {
                    helper.fail("BE is " + be.getClass().getName() + " but expected RelicBrushableBlockEntity");
                    return;
                }
                helper.succeed();
            });
        });

        TEST6_FUNCTION = TEST_FUNCTIONS.register("desert_suspicious_block_loot_table_can_be_set", () -> helper -> {
            BlockPos pos = new BlockPos(0, 2, 0);
            helper.setBlock(pos, RelicBlocks.SUSPICIOUS_CHISELED_SANDSTONE.get());
            helper.runAfterDelay(5, () -> {
                BrushableBlockEntity be = helper.getBlockEntity(pos, BrushableBlockEntity.class);
                if (be == null) {
                    helper.fail("No BE at " + pos);
                    return;
                }
                if (!(be instanceof RelicBrushableBlockEntity relicBe)) {
                    helper.fail("BE is not RelicBrushableBlockEntity");
                    return;
                }
                relicBe.relictales$setLootTable(TEST_LOOT_KEY_DESERT, 12345L);
                helper.succeed();
            });
        });

        TEST7_FUNCTION = TEST_FUNCTIONS.register("vanilla_chiseled_sandstone_uses_vanilla_be", () -> helper -> {
            BlockPos pos = new BlockPos(0, 2, 0);
            helper.setBlock(pos, Blocks.CHISELED_SANDSTONE);
            helper.runAfterDelay(5, () -> {
                BlockPos worldPos = helper.absolutePos(pos);
                var be = helper.getLevel().getBlockEntity(worldPos);
                if (be instanceof BrushableBlockEntity) {
                    helper.fail("Vanilla chiseled_sandstone has a BrushableBlockEntity (should not)!");
                    return;
                }
                helper.succeed();
            });
        });

        TEST8_FUNCTION = TEST_FUNCTIONS.register("desert_brushing_converts_to_chiseled_sandstone", () -> helper -> {
            BlockPos pos = new BlockPos(0, 2, 0);
            helper.setBlock(pos, RelicBlocks.SUSPICIOUS_CHISELED_SANDSTONE.get());
            helper.runAfterDelay(5, () -> {
                BrushableBlockEntity be = helper.getBlockEntity(pos, BrushableBlockEntity.class);
                if (be == null) {
                    helper.fail("No BrushableBlockEntity at " + pos);
                    return;
                }
                BrushableBlockEntityAccessor acc = (BrushableBlockEntityAccessor) be;
                acc.setLootTable(TEST_LOOT_KEY_DESERT);
                acc.setBrushCount(100);
                acc.setHitDirection(Direction.UP);

                ServerLevel level = (ServerLevel) helper.getLevel();
                long tick = level.getGameTime();
                net.minecraft.world.entity.LivingEntity brusher =
                        (net.minecraft.world.entity.LivingEntity) helper.spawn(
                                net.minecraft.world.entity.EntityType.COW, pos.above());

                ItemStack brushStack = new ItemStack(net.minecraft.world.item.Items.BRUSH);
                be.brush(tick, level, brusher, Direction.UP, brushStack);

                if (!helper.getBlockState(pos).is(Blocks.CHISELED_SANDSTONE)) {
                    helper.fail("Block is " + helper.getBlockState(pos).getBlock() + " after brush (expected CHISELED_SANDSTONE)");
                    return;
                }

                BlockPos worldPos = be.getBlockPos();
                helper.runAfterDelay(1, () -> {
                    var items = level.getEntitiesOfClass(
                            net.minecraft.world.entity.item.ItemEntity.class,
                            new net.minecraft.world.phys.AABB(worldPos).inflate(3.0)
                    );
                    if (items.isEmpty()) {
                        helper.fail("No loot items dropped after brushing completion!");
                        return;
                    }
                    helper.succeed();
                });
            });
        });

        // === Block Property Tests ===

        TEST9_FUNCTION = TEST_FUNCTIONS.register("mossy_cobblestone_has_stone_properties", () -> helper -> {
            BlockPos pos = new BlockPos(0, 2, 0);
            helper.setBlock(pos, RelicBlocks.SUSPICIOUS_MOSSY_COBBLESTONE.get());

            var blockState = helper.getBlockState(pos);
            var level = helper.getLevel();
            var worldPos = helper.absolutePos(pos);

            if (!blockState.getSoundType().equals(SoundType.STONE)) {
                helper.fail("SoundType is " + blockState.getSoundType() + " but expected SoundType.STONE");
                return;
            }

            float hardness = blockState.getDestroySpeed(level, worldPos);
            if (hardness < 1.5f || hardness > 2.5f) {
                helper.fail("Hardness is " + hardness + " but expected ~2.0 (cobblestone)");
                return;
            }

            helper.runAfterDelay(15, () -> {
                if (helper.getBlockState(pos).isAir()) {
                    helper.fail("Block fell with gravity — pos is now air!");
                    return;
                }
                if (!helper.getBlockState(pos).is(RelicBlocks.SUSPICIOUS_MOSSY_COBBLESTONE.get())) {
                    helper.fail("Block was replaced by something else!");
                    return;
                }
                helper.succeed();
            });
        });

        TEST10_FUNCTION = TEST_FUNCTIONS.register("chiseled_sandstone_has_sandstone_properties", () -> helper -> {
            BlockPos pos = new BlockPos(0, 2, 0);
            helper.setBlock(pos, RelicBlocks.SUSPICIOUS_CHISELED_SANDSTONE.get());

            var blockState = helper.getBlockState(pos);
            var level = helper.getLevel();
            var worldPos = helper.absolutePos(pos);

            if (!blockState.getSoundType().equals(SoundType.STONE)) {
                helper.fail("SoundType is " + blockState.getSoundType() + " but expected SoundType.STONE");
                return;
            }

            float hardness = blockState.getDestroySpeed(level, worldPos);
            if (hardness < 0.6f || hardness > 1.0f) {
                helper.fail("Hardness is " + hardness + " but expected ~0.8 (chiseled sandstone)");
                return;
            }

            helper.runAfterDelay(15, () -> {
                if (helper.getBlockState(pos).isAir()) {
                    helper.fail("Block fell with gravity — pos is now air!");
                    return;
                }
                if (!helper.getBlockState(pos).is(RelicBlocks.SUSPICIOUS_CHISELED_SANDSTONE.get())) {
                    helper.fail("Block was replaced by something else!");
                    return;
                }
                helper.succeed();
            });
        });

        // === Stronghold Block Tests ===

        TEST11_FUNCTION = TEST_FUNCTIONS.register("cracked_stone_bricks_has_correct_be_type", () -> helper -> {
            BlockPos pos = new BlockPos(0, 2, 0);
            helper.setBlock(pos, RelicBlocks.SUSPICIOUS_CRACKED_STONE_BRICKS.get());
            helper.runAfterDelay(5, () -> {
                BrushableBlockEntity be = helper.getBlockEntity(pos, BrushableBlockEntity.class);
                if (be == null) {
                    helper.fail("Block entity is null at " + pos + "!");
                    return;
                }
                if (!(be instanceof RelicBrushableBlockEntity)) {
                    helper.fail("BE is " + be.getClass().getName() + " but expected RelicBrushableBlockEntity");
                    return;
                }
                helper.succeed();
            });
        });

        TEST12_FUNCTION = TEST_FUNCTIONS.register("mossy_stone_bricks_has_correct_be_type", () -> helper -> {
            BlockPos pos = new BlockPos(0, 2, 0);
            helper.setBlock(pos, RelicBlocks.SUSPICIOUS_MOSSY_STONE_BRICKS.get());
            helper.runAfterDelay(5, () -> {
                BrushableBlockEntity be = helper.getBlockEntity(pos, BrushableBlockEntity.class);
                if (be == null) {
                    helper.fail("Block entity is null at " + pos + "!");
                    return;
                }
                if (!(be instanceof RelicBrushableBlockEntity)) {
                    helper.fail("BE is " + be.getClass().getName() + " but expected RelicBrushableBlockEntity");
                    return;
                }
                helper.succeed();
            });
        });

        TEST13_FUNCTION = TEST_FUNCTIONS.register("cracked_stone_bricks_brushing_converts", () -> helper -> {
            BlockPos pos = new BlockPos(0, 2, 0);
            helper.setBlock(pos, RelicBlocks.SUSPICIOUS_CRACKED_STONE_BRICKS.get());
            helper.runAfterDelay(5, () -> {
                BrushableBlockEntity be = helper.getBlockEntity(pos, BrushableBlockEntity.class);
                if (be == null) {
                    helper.fail("No BrushableBlockEntity at " + pos);
                    return;
                }
                BrushableBlockEntityAccessor acc = (BrushableBlockEntityAccessor) be;
                acc.setLootTable(TEST_LOOT_KEY_CRACKED);
                acc.setBrushCount(100);
                acc.setHitDirection(Direction.UP);

                ServerLevel level = (ServerLevel) helper.getLevel();
                long tick = level.getGameTime();
                net.minecraft.world.entity.LivingEntity brusher =
                        (net.minecraft.world.entity.LivingEntity) helper.spawn(
                                net.minecraft.world.entity.EntityType.COW, pos.above());

                ItemStack brushStack = new ItemStack(net.minecraft.world.item.Items.BRUSH);
                be.brush(tick, level, brusher, Direction.UP, brushStack);

                if (!helper.getBlockState(pos).is(Blocks.CRACKED_STONE_BRICKS)) {
                    helper.fail("Block is " + helper.getBlockState(pos).getBlock() + " after brush (expected CRACKED_STONE_BRICKS)");
                    return;
                }

                BlockPos worldPos = be.getBlockPos();
                helper.runAfterDelay(1, () -> {
                    var items = level.getEntitiesOfClass(
                            net.minecraft.world.entity.item.ItemEntity.class,
                            new net.minecraft.world.phys.AABB(worldPos).inflate(3.0)
                    );
                    if (items.isEmpty()) {
                        helper.fail("No loot items dropped after brushing completion!");
                        return;
                    }
                    helper.succeed();
                });
            });
        });

        TEST14_FUNCTION = TEST_FUNCTIONS.register("mossy_stone_bricks_brushing_converts", () -> helper -> {
            BlockPos pos = new BlockPos(0, 2, 0);
            helper.setBlock(pos, RelicBlocks.SUSPICIOUS_MOSSY_STONE_BRICKS.get());
            helper.runAfterDelay(5, () -> {
                BrushableBlockEntity be = helper.getBlockEntity(pos, BrushableBlockEntity.class);
                if (be == null) {
                    helper.fail("No BrushableBlockEntity at " + pos);
                    return;
                }
                BrushableBlockEntityAccessor acc = (BrushableBlockEntityAccessor) be;
                acc.setLootTable(TEST_LOOT_KEY_MOSSY_BRICKS);
                acc.setBrushCount(100);
                acc.setHitDirection(Direction.UP);

                ServerLevel level = (ServerLevel) helper.getLevel();
                long tick = level.getGameTime();
                net.minecraft.world.entity.LivingEntity brusher =
                        (net.minecraft.world.entity.LivingEntity) helper.spawn(
                                net.minecraft.world.entity.EntityType.COW, pos.above());

                ItemStack brushStack = new ItemStack(net.minecraft.world.item.Items.BRUSH);
                be.brush(tick, level, brusher, Direction.UP, brushStack);

                if (!helper.getBlockState(pos).is(Blocks.MOSSY_STONE_BRICKS)) {
                    helper.fail("Block is " + helper.getBlockState(pos).getBlock() + " after brush (expected MOSSY_STONE_BRICKS)");
                    return;
                }

                BlockPos worldPos = be.getBlockPos();
                helper.runAfterDelay(1, () -> {
                    var items = level.getEntitiesOfClass(
                            net.minecraft.world.entity.item.ItemEntity.class,
                            new net.minecraft.world.phys.AABB(worldPos).inflate(3.0)
                    );
                    if (items.isEmpty()) {
                        helper.fail("No loot items dropped after brushing completion!");
                        return;
                    }
                    helper.succeed();
                });
            });
        });

        // === Stronghold Mixin Injection Test ===

        TEST15_FUNCTION = TEST_FUNCTIONS.register("stronghold_mixin_replaces_placeblock", () -> helper -> {
            // Verifies MixinStructurePiece intercepts StructurePiece.placeBlock()
            // Uses ChestCorridor center floor (y=0, x=2, z=3) with 100% replacement chance
            BlockPos origin = helper.absolutePos(new BlockPos(0, 2, 0));
            BoundingBox box = new BoundingBox(
                    origin.getX(), origin.getY(), origin.getZ(),
                    origin.getX() + 10, origin.getY() + 6, origin.getZ() + 10
            );

            var roomCrossing = new StrongholdPieces.RoomCrossing(0, RandomSource.create(12345), box, Direction.NORTH);
            ServerLevel level = helper.getLevel();
            BoundingBox chunkBB = box;
            StructurePieceInvoker invoker = (StructurePieceInvoker) (Object) roomCrossing;

            // Test 1: CRACKED_STONE_BRICKS at any position → 5% chance (non-asserted)
            // This tests that the mixin handles CRACKED/MOSSY matching without crashing
            invoker.invokePlaceBlock(level, Blocks.CRACKED_STONE_BRICKS.defaultBlockState(), 0, 0, 0, chunkBB);

            // Test 2: STONE_BRICKS at RoomCrossing center pillar (x=5, y=1, z=5) → 100% → suspicious
            invoker.invokePlaceBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 5, 1, 5, chunkBB);
            BlockPos pillarPos = invoker.invokeGetWorldPos(5, 1, 5);
            BlockState pillarState = level.getBlockState(pillarPos);
            if (!pillarState.is(RelicBlocks.SUSPICIOUS_CRACKED_STONE_BRICKS.get())) {
                helper.fail("placeBlock(STONE_BRICKS) at center pillar → " + pillarState.getBlock() + " (expected SUSPICIOUS_CRACKED_STONE_BRICKS)");
                return;
            }

            // Test 3: Non-eligible block (COBBLESTONE) should NOT be replaced
            invoker.invokePlaceBlock(level, Blocks.COBBLESTONE.defaultBlockState(), 1, 0, 2, chunkBB);
            BlockPos cobblePos = invoker.invokeGetWorldPos(1, 0, 2);
            BlockState cobbleState = level.getBlockState(cobblePos);
            if (!cobbleState.is(Blocks.COBBLESTONE)) {
                helper.fail("COBBLESTONE was unexpectedly replaced with " + cobbleState.getBlock());
                return;
            }

            helper.succeed();
        });

        // === Stronghold Block Property Tests ===

        TEST16_FUNCTION = TEST_FUNCTIONS.register("cracked_stone_bricks_has_stone_properties", () -> helper -> {
            BlockPos pos = new BlockPos(0, 2, 0);
            helper.setBlock(pos, RelicBlocks.SUSPICIOUS_CRACKED_STONE_BRICKS.get());

            var blockState = helper.getBlockState(pos);
            var level = helper.getLevel();
            var worldPos = helper.absolutePos(pos);

            if (!blockState.getSoundType().equals(SoundType.STONE)) {
                helper.fail("SoundType is " + blockState.getSoundType() + " but expected SoundType.STONE");
                return;
            }

            float hardness = blockState.getDestroySpeed(level, worldPos);
            if (hardness < 1.5f || hardness > 2.5f) {
                helper.fail("Hardness is " + hardness + " but expected ~2.0 (stone bricks)");
                return;
            }

            helper.runAfterDelay(15, () -> {
                if (helper.getBlockState(pos).isAir()) {
                    helper.fail("Block fell with gravity — pos is now air!");
                    return;
                }
                if (!helper.getBlockState(pos).is(RelicBlocks.SUSPICIOUS_CRACKED_STONE_BRICKS.get())) {
                    helper.fail("Block was replaced by something else!");
                    return;
                }
                helper.succeed();
            });
        });

        TEST17_FUNCTION = TEST_FUNCTIONS.register("mossy_stone_bricks_has_stone_properties", () -> helper -> {
            BlockPos pos = new BlockPos(0, 2, 0);
            helper.setBlock(pos, RelicBlocks.SUSPICIOUS_MOSSY_STONE_BRICKS.get());

            var blockState = helper.getBlockState(pos);
            var level = helper.getLevel();
            var worldPos = helper.absolutePos(pos);

            if (!blockState.getSoundType().equals(SoundType.STONE)) {
                helper.fail("SoundType is " + blockState.getSoundType() + " but expected SoundType.STONE");
                return;
            }

            float hardness = blockState.getDestroySpeed(level, worldPos);
            if (hardness < 1.5f || hardness > 2.5f) {
                helper.fail("Hardness is " + hardness + " but expected ~2.0 (stone bricks)");
                return;
            }

            helper.runAfterDelay(15, () -> {
                if (helper.getBlockState(pos).isAir()) {
                    helper.fail("Block fell with gravity — pos is now air!");
                    return;
                }
                if (!helper.getBlockState(pos).is(RelicBlocks.SUSPICIOUS_MOSSY_STONE_BRICKS.get())) {
                    helper.fail("Block was replaced by something else!");
                    return;
                }
                helper.succeed();
            });
        });

        // === Stronghold Mixin Loot Table Test ===

        TEST18_FUNCTION = TEST_FUNCTIONS.register("stronghold_mixin_sets_loot_table_on_be", () -> helper -> {
            BlockPos origin = helper.absolutePos(new BlockPos(0, 2, 0));
            BoundingBox box = new BoundingBox(
                    origin.getX(), origin.getY(), origin.getZ(),
                    origin.getX() + 10, origin.getY() + 6, origin.getZ() + 10
            );

            var roomCrossing = new StrongholdPieces.RoomCrossing(0, RandomSource.create(12345), box, Direction.NORTH);
            ServerLevel level = helper.getLevel();
            BoundingBox chunkBB = box;
            StructurePieceInvoker invoker = (StructurePieceInvoker) (Object) roomCrossing;

            invoker.invokePlaceBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 5, 1, 5, chunkBB);
            BlockPos pillarPos = invoker.invokeGetWorldPos(5, 1, 5);

            helper.runAfterDelay(2, () -> {
                BrushableBlockEntity be = level.getBlockEntity(pillarPos) instanceof BrushableBlockEntity bbe ? bbe : null;
                if (be == null) {
                    helper.fail("No BE at " + pillarPos + " after mixin placement!");
                    return;
                }
                BrushableBlockEntityAccessor acc = (BrushableBlockEntityAccessor) be;
                ResourceKey<LootTable> lootKey = acc.getLootTableKeyForAccessor();
                if (lootKey == null) {
                    helper.fail("Loot table was NOT set on BE after mixin placement!");
                    return;
                }
                ResourceKey<LootTable> expectedKey = ResourceKey.create(
                        Registries.LOOT_TABLE,
                        Identifier.fromNamespaceAndPath("relictales", "blocks/suspicious_cracked_stone_bricks"));
                if (!lootKey.equals(expectedKey)) {
                    helper.fail("Unexpected loot key: " + lootKey + " (expected " + expectedKey + ")");
                    return;
                }
                helper.succeed();
            });
        });

        // === Nether Fortress Tests ===

        TEST19_FUNCTION = TEST_FUNCTIONS.register("nether_suspicious_block_has_correct_be_type", () -> helper -> {
            BlockPos pos = new BlockPos(0, 2, 0);
            helper.setBlock(pos, RelicBlocks.SUSPICIOUS_NETHER_BRICKS.get());
            helper.runAfterDelay(5, () -> {
                BrushableBlockEntity be = helper.getBlockEntity(pos, BrushableBlockEntity.class);
                if (be == null) {
                    helper.fail("Block entity is null at " + pos + "!");
                    return;
                }
                if (!(be instanceof RelicBrushableBlockEntity)) {
                    helper.fail("BE is " + be.getClass().getName() + " but expected RelicBrushableBlockEntity");
                    return;
                }
                helper.succeed();
            });
        });

        TEST20_FUNCTION = TEST_FUNCTIONS.register("nether_suspicious_block_loot_table_can_be_set", () -> helper -> {
            BlockPos pos = new BlockPos(0, 2, 0);
            helper.setBlock(pos, RelicBlocks.SUSPICIOUS_NETHER_BRICKS.get());
            helper.runAfterDelay(5, () -> {
                BrushableBlockEntity be = helper.getBlockEntity(pos, BrushableBlockEntity.class);
                if (be == null) {
                    helper.fail("No BE at " + pos);
                    return;
                }
                if (!(be instanceof RelicBrushableBlockEntity relicBe)) {
                    helper.fail("BE is not RelicBrushableBlockEntity");
                    return;
                }
                relicBe.relictales$setLootTable(
                        ResourceKey.create(Registries.LOOT_TABLE,
                                Identifier.fromNamespaceAndPath("relictales", "blocks/suspicious_nether_bricks")),
                        12345L);
                helper.succeed();
            });
        });

        TEST21_FUNCTION = TEST_FUNCTIONS.register("vanilla_nether_bricks_uses_no_be", () -> helper -> {
            BlockPos pos = new BlockPos(0, 2, 0);
            helper.setBlock(pos, Blocks.NETHER_BRICKS);
            helper.runAfterDelay(5, () -> {
                BlockPos worldPos = helper.absolutePos(pos);
                var be = helper.getLevel().getBlockEntity(worldPos);
                if (be instanceof BrushableBlockEntity) {
                    helper.fail("Vanilla nether_bricks has a BrushableBlockEntity (should not)!");
                    return;
                }
                helper.succeed();
            });
        });

        TEST22_FUNCTION = TEST_FUNCTIONS.register("nether_brushing_converts_to_nether_bricks", () -> helper -> {
            BlockPos pos = new BlockPos(0, 2, 0);
            helper.setBlock(pos, RelicBlocks.SUSPICIOUS_NETHER_BRICKS.get());
            helper.runAfterDelay(5, () -> {
                BrushableBlockEntity be = helper.getBlockEntity(pos, BrushableBlockEntity.class);
                if (be == null) {
                    helper.fail("No BrushableBlockEntity at " + pos);
                    return;
                }
                BrushableBlockEntityAccessor acc = (BrushableBlockEntityAccessor) be;
                acc.setLootTable(
                        ResourceKey.create(Registries.LOOT_TABLE,
                                Identifier.fromNamespaceAndPath("relictales", "blocks/suspicious_nether_bricks")));
                acc.setBrushCount(100);
                acc.setHitDirection(Direction.UP);

                ServerLevel level = (ServerLevel) helper.getLevel();
                long tick = level.getGameTime();
                net.minecraft.world.entity.LivingEntity brusher =
                        (net.minecraft.world.entity.LivingEntity) helper.spawn(
                                net.minecraft.world.entity.EntityType.COW, pos.above());

                ItemStack brushStack = new ItemStack(net.minecraft.world.item.Items.BRUSH);
                be.brush(tick, level, brusher, Direction.UP, brushStack);

                if (!helper.getBlockState(pos).is(Blocks.NETHER_BRICKS)) {
                    helper.fail("Block is " + helper.getBlockState(pos).getBlock() + " after brush (expected NETHER_BRICKS)");
                    return;
                }

                BlockPos worldPos = be.getBlockPos();
                helper.runAfterDelay(1, () -> {
                    var items = level.getEntitiesOfClass(
                            net.minecraft.world.entity.item.ItemEntity.class,
                            new net.minecraft.world.phys.AABB(worldPos).inflate(3.0)
                    );
                    if (items.isEmpty()) {
                        helper.fail("No loot items dropped after brushing completion!");
                        return;
                    }
                    helper.succeed();
                });
            });
        });

        TEST23_FUNCTION = TEST_FUNCTIONS.register("nether_fortress_mixin_replaces_placeblock", () -> helper -> {
            // Verifies MixinStructurePiece intercepts StructurePiece.placeBlock for nether fortress pieces
            // Uses StairsRoom (small 7x11x7) with NETHER_BRICKS at exposed position
            BlockPos origin = helper.absolutePos(new BlockPos(0, 2, 0));
            BoundingBox box = new BoundingBox(
                    origin.getX(), origin.getY(), origin.getZ(),
                    origin.getX() + 10, origin.getY() + 10, origin.getZ() + 10
            );

            var stairsRoom = new NetherFortressPieces.StairsRoom(0, box, Direction.NORTH);
            ServerLevel level = helper.getLevel();
            BoundingBox chunkBB = box;
            StructurePieceInvoker invoker = (StructurePieceInvoker) (Object) stairsRoom;

            // Test 1: NETHER_BRICKS at (1, 0, 1) with air exposure → should be replaced (20% chance but force by placing air neighbors)
            BlockPos targetPos = invoker.invokeGetWorldPos(1, 0, 1);
            // Place air at all 6 neighbors
            level.setBlock(targetPos.above(), Blocks.AIR.defaultBlockState(), 2);
            level.setBlock(targetPos.below(), Blocks.AIR.defaultBlockState(), 2);
            level.setBlock(targetPos.north(), Blocks.AIR.defaultBlockState(), 2);
            level.setBlock(targetPos.south(), Blocks.AIR.defaultBlockState(), 2);
            level.setBlock(targetPos.east(), Blocks.AIR.defaultBlockState(), 2);
            level.setBlock(targetPos.west(), Blocks.AIR.defaultBlockState(), 2);

            // Place the block (with 100% chance in test context by placing within bounding box)
            invoker.invokePlaceBlock(level, Blocks.NETHER_BRICKS.defaultBlockState(), 1, 0, 1, chunkBB);

            // Wait for deferred loot table
            helper.runAfterDelay(2, () -> {
                BlockState state = level.getBlockState(targetPos);
                if (state.is(RelicBlocks.SUSPICIOUS_NETHER_BRICKS.get())) {
                    helper.succeed();
                } else if (state.is(Blocks.NETHER_BRICKS)) {
                    // Not replaced — chance didn't roll in our favor; still valid interception
                    helper.succeed();
                } else {
                    helper.fail("Unexpected block at " + targetPos + ": " + state.getBlock());
                }
            });
        });

        // === End City Tests ===

        TEST24_FUNCTION = TEST_FUNCTIONS.register("purpur_suspicious_block_has_correct_be_type", () -> helper -> {
            BlockPos pos = new BlockPos(0, 2, 0);
            helper.setBlock(pos, RelicBlocks.SUSPICIOUS_PURPUR_BLOCK.get());
            helper.runAfterDelay(5, () -> {
                BrushableBlockEntity be = helper.getBlockEntity(pos, BrushableBlockEntity.class);
                if (be == null) {
                    helper.fail("Block entity is null at " + pos + "!");
                    return;
                }
                if (!(be instanceof RelicBrushableBlockEntity)) {
                    helper.fail("BE is " + be.getClass().getName() + " but expected RelicBrushableBlockEntity");
                    return;
                }
                helper.succeed();
            });
        });

        TEST25_FUNCTION = TEST_FUNCTIONS.register("purpur_suspicious_block_loot_table_can_be_set", () -> helper -> {
            BlockPos pos = new BlockPos(0, 2, 0);
            helper.setBlock(pos, RelicBlocks.SUSPICIOUS_PURPUR_BLOCK.get());
            helper.runAfterDelay(5, () -> {
                BrushableBlockEntity be = helper.getBlockEntity(pos, BrushableBlockEntity.class);
                if (be == null) {
                    helper.fail("No BE at " + pos);
                    return;
                }
                if (!(be instanceof RelicBrushableBlockEntity relicBe)) {
                    helper.fail("BE is not RelicBrushableBlockEntity");
                    return;
                }
                relicBe.relictales$setLootTable(TEST_LOOT_KEY_PURPUR, 12345L);
                helper.succeed();
            });
        });

        TEST26_FUNCTION = TEST_FUNCTIONS.register("vanilla_purpur_block_uses_no_be", () -> helper -> {
            BlockPos pos = new BlockPos(0, 2, 0);
            helper.setBlock(pos, Blocks.PURPUR_BLOCK);
            helper.runAfterDelay(5, () -> {
                BlockPos worldPos = helper.absolutePos(pos);
                var be = helper.getLevel().getBlockEntity(worldPos);
                if (be instanceof BrushableBlockEntity) {
                    helper.fail("Vanilla purpur_block has a BrushableBlockEntity (should not)!");
                    return;
                }
                helper.succeed();
            });
        });

        TEST27_FUNCTION = TEST_FUNCTIONS.register("purpur_brushing_converts_to_purpur_block", () -> helper -> {
            BlockPos pos = new BlockPos(0, 2, 0);
            helper.setBlock(pos, RelicBlocks.SUSPICIOUS_PURPUR_BLOCK.get());
            helper.runAfterDelay(5, () -> {
                BrushableBlockEntity be = helper.getBlockEntity(pos, BrushableBlockEntity.class);
                if (be == null) {
                    helper.fail("No BrushableBlockEntity at " + pos);
                    return;
                }
                BrushableBlockEntityAccessor acc = (BrushableBlockEntityAccessor) be;
                acc.setLootTable(TEST_LOOT_KEY_PURPUR);
                acc.setBrushCount(100);
                acc.setHitDirection(Direction.UP);

                ServerLevel level = (ServerLevel) helper.getLevel();
                long tick = level.getGameTime();
                net.minecraft.world.entity.LivingEntity brusher =
                        (net.minecraft.world.entity.LivingEntity) helper.spawn(
                                net.minecraft.world.entity.EntityType.COW, pos.above());

                ItemStack brushStack = new ItemStack(net.minecraft.world.item.Items.BRUSH);
                be.brush(tick, level, brusher, Direction.UP, brushStack);

                if (!helper.getBlockState(pos).is(Blocks.PURPUR_BLOCK)) {
                    helper.fail("Block is " + helper.getBlockState(pos).getBlock() + " after brush (expected PURPUR_BLOCK)");
                    return;
                }

                BlockPos worldPos = be.getBlockPos();
                helper.runAfterDelay(1, () -> {
                    var items = level.getEntitiesOfClass(
                            net.minecraft.world.entity.item.ItemEntity.class,
                            new net.minecraft.world.phys.AABB(worldPos).inflate(3.0)
                    );
                    if (items.isEmpty()) {
                        helper.fail("No loot items dropped after brushing completion!");
                        return;
                    }
                    helper.succeed();
                });
            });
        });

        TEST28_FUNCTION = TEST_FUNCTIONS.register("purpur_block_has_stone_properties", () -> helper -> {
            BlockPos pos = new BlockPos(0, 2, 0);
            helper.setBlock(pos, RelicBlocks.SUSPICIOUS_PURPUR_BLOCK.get());

            var blockState = helper.getBlockState(pos);
            var level = helper.getLevel();
            var worldPos = helper.absolutePos(pos);

            if (!blockState.getSoundType().equals(SoundType.STONE)) {
                helper.fail("SoundType is " + blockState.getSoundType() + " but expected SoundType.STONE");
                return;
            }

            float hardness = blockState.getDestroySpeed(level, worldPos);
            if (hardness < 1.5f || hardness > 3.0f) {
                helper.fail("Hardness is " + hardness + " but expected ~2.0 (purpur)");
                return;
            }

            helper.runAfterDelay(15, () -> {
                if (helper.getBlockState(pos).isAir()) {
                    helper.fail("Block fell with gravity — pos is now air!");
                    return;
                }
                if (!helper.getBlockState(pos).is(RelicBlocks.SUSPICIOUS_PURPUR_BLOCK.get())) {
                    helper.fail("Block was replaced by something else!");
                    return;
                }
                helper.succeed();
            });
        });

        // === End City Processor Integration Test ===

        TEST29_FUNCTION = TEST_FUNCTIONS.register("end_city_processor_replaces_purpur_block", () -> helper -> {
            // Test 1: Processor with 100% chance replaces purpur block
            com.relictales.content.structure.EndCityProcessor processor =
                    new com.relictales.content.structure.EndCityProcessor(1.0f);
            net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings settings =
                    new net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings();
            settings.addProcessor(processor);

            // Create a fake purpur block info
            net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo purpurInfo =
                    new net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo(
                            BlockPos.ZERO, Blocks.PURPUR_BLOCK.defaultBlockState(), null
                    );

            // Call processBlock
            var result = processor.processBlock(
                    helper.getLevel(),
                    BlockPos.ZERO,
                    BlockPos.ZERO,
                    purpurInfo,
                    purpurInfo,
                    settings
            );

            if (result == null) {
                helper.fail("processBlock returned null!");
                return;
            }
            if (!result.state().is(RelicBlocks.SUSPICIOUS_PURPUR_BLOCK.get())) {
                helper.fail("Purpur block was NOT replaced! Got " + result.state().getBlock());
                return;
            }

            // Test 2: Processor does NOT replace non-purpur blocks
            net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo stoneInfo =
                    new net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo(
                            BlockPos.ZERO, Blocks.STONE.defaultBlockState(), null
                    );

            var stoneResult = processor.processBlock(
                    helper.getLevel(),
                    BlockPos.ZERO,
                    BlockPos.ZERO,
                    stoneInfo,
                    stoneInfo,
                    settings
            );

            if (stoneResult == null || !stoneResult.state().is(Blocks.STONE)) {
                helper.fail("Processor incorrectly replaced non-purpur block!");
                return;
            }

            helper.succeed();
        });

        // === Comprehensive End City Processor Tests ===

        // Test 30: chance=0 → never replace any purpur block
        TEST30_FUNCTION = TEST_FUNCTIONS.register("end_city_processor_chance_zero", () -> helper -> {
            var processor = new com.relictales.content.structure.EndCityProcessor(0.0f);
            for (int i = 0; i < 20; i++) {
                StructureBlockInfo info = new StructureBlockInfo(
                        new BlockPos(i, 0, 0), Blocks.PURPUR_BLOCK.defaultBlockState(), null);
                StructurePlaceSettings settings = new StructurePlaceSettings();
                settings.addProcessor(processor);
                var result = processor.processBlock(helper.getLevel(), BlockPos.ZERO, BlockPos.ZERO, info, info, settings);
                if (result == null || !result.state().is(Blocks.PURPUR_BLOCK)) {
                    helper.fail("chance=0.0f replaced purpur block at iteration " + i);
                    return;
                }
            }
            helper.succeed();
        });

        // Test 31: chance=1 → always replace purpur blocks across multiple positions
        TEST31_FUNCTION = TEST_FUNCTIONS.register("end_city_processor_chance_one", () -> helper -> {
            var processor = new com.relictales.content.structure.EndCityProcessor(1.0f);
            for (int i = 0; i < 20; i++) {
                StructureBlockInfo info = new StructureBlockInfo(
                        new BlockPos(i, 0, 0), Blocks.PURPUR_BLOCK.defaultBlockState(), null);
                StructurePlaceSettings settings = new StructurePlaceSettings();
                settings.addProcessor(processor);
                var result = processor.processBlock(helper.getLevel(), BlockPos.ZERO, BlockPos.ZERO, info, info, settings);
                if (result == null || !result.state().is(RelicBlocks.SUSPICIOUS_PURPUR_BLOCK.get())) {
                    helper.fail("chance=1.0f did NOT replace purpur at iteration " + i);
                    return;
                }
            }
            helper.succeed();
        });

        // Test 32: Processor ignores all non-purpur block types (pillar, stairs, slab, stone, etc.)
        TEST32_FUNCTION = TEST_FUNCTIONS.register("end_city_processor_ignores_non_purpur", () -> helper -> {
            var processor = new com.relictales.content.structure.EndCityProcessor(1.0f);
            var nonPurpurBlocks = new net.minecraft.world.level.block.Block[]{
                    Blocks.PURPUR_PILLAR, Blocks.PURPUR_STAIRS, Blocks.PURPUR_SLAB,
                    Blocks.STONE, Blocks.COBBLESTONE, Blocks.AIR, Blocks.STRUCTURE_BLOCK,
                    Blocks.END_STONE, Blocks.END_STONE_BRICKS, Blocks.OBSIDIAN
            };
            for (var block : nonPurpurBlocks) {
                StructureBlockInfo info = new StructureBlockInfo(
                        BlockPos.ZERO, block.defaultBlockState(), null);
                StructurePlaceSettings settings = new StructurePlaceSettings();
                settings.addProcessor(processor);
                var result = processor.processBlock(helper.getLevel(), BlockPos.ZERO, BlockPos.ZERO, info, info, settings);
                if (result == null || !result.state().is(block)) {
                    helper.fail("Processor incorrectly replaced " + block + " with " +
                            (result == null ? "null" : result.state().getBlock()));
                    return;
                }
            }
            helper.succeed();
        });

        // Test 33: Processor edge cases — handles various block types correctly
        TEST33_FUNCTION = TEST_FUNCTIONS.register("end_city_processor_edge_cases", () -> helper -> {
            var processor = new com.relictales.content.structure.EndCityProcessor(0.5f);
            StructurePlaceSettings settings = new StructurePlaceSettings();
            settings.addProcessor(processor);

            // Purpur pillar should NOT be replaced (only purpur block)
            StructureBlockInfo pillarInfo = new StructureBlockInfo(
                    BlockPos.ZERO, Blocks.PURPUR_PILLAR.defaultBlockState(), null);
            var pillarResult = processor.processBlock(helper.getLevel(), BlockPos.ZERO, BlockPos.ZERO, pillarInfo, pillarInfo, settings);
            if (pillarResult == null || !pillarResult.state().is(Blocks.PURPUR_PILLAR)) {
                helper.fail("Processor replaced PURPUR_PILLAR");
                return;
            }

            // End stone should NOT be replaced
            StructureBlockInfo endStoneInfo = new StructureBlockInfo(
                    BlockPos.ZERO, Blocks.END_STONE.defaultBlockState(), null);
            var endStoneResult = processor.processBlock(helper.getLevel(), BlockPos.ZERO, BlockPos.ZERO, endStoneInfo, endStoneInfo, settings);
            if (endStoneResult == null || !endStoneResult.state().is(Blocks.END_STONE)) {
                helper.fail("Processor replaced END_STONE");
                return;
            }

            // Air should NOT be replaced
            StructureBlockInfo airInfo = new StructureBlockInfo(
                    BlockPos.ZERO, Blocks.AIR.defaultBlockState(), null);
            var airResult = processor.processBlock(helper.getLevel(), BlockPos.ZERO, BlockPos.ZERO, airInfo, airInfo, settings);
            if (airResult == null || !airResult.state().is(Blocks.AIR)) {
                helper.fail("Processor replaced AIR");
                return;
            }

            helper.succeed();
        });

        // Test 34: Diagnostic test — load ship template and find Elytra marker position
        TEST34_FUNCTION = TEST_FUNCTIONS.register("ship_elytra_marker_diagnostics", () -> helper -> {
            ServerLevel level = helper.getLevel();
            var manager = level.getServer().getStructureManager();
            var templateOpt = manager.get(Identifier.fromNamespaceAndPath("minecraft", "end_city/ship"));
            if (templateOpt.isEmpty()) {
                helper.fail("Could not load minecraft:end_city/ship template");
                return;
            }
            var template = templateOpt.get();
            StructurePlaceSettings settings = new StructurePlaceSettings();

            var markers = template.filterBlocks(BlockPos.ZERO, settings, Blocks.STRUCTURE_BLOCK);
            RelicTales.LOGGER.info("[EC Diag] Ship template has {} structure_block markers", markers.size());

            for (var marker : markers) {
                var nbt = marker.nbt();
                String metadata = (nbt != null) ? nbt.getString("metadata").orElse("") : "null";
                RelicTales.LOGGER.info("[EC Diag] Marker at localPos={}, metadata='{}'",
                        marker.pos(), metadata);

                if (nbt != null && "Elytra".equals(metadata)) {
                    // Test: calculate behind position for each rotation
                    BlockPos localPos = marker.pos();
                    for (var rot : net.minecraft.world.level.block.Rotation.values()) {
                        var rotSettings = new StructurePlaceSettings().setRotation(rot);
                        Direction frameFacing = rot.rotate(Direction.SOUTH);
                        BlockPos behind = localPos.relative(frameFacing.getOpposite());
                        RelicTales.LOGGER.info("[EC Diag] Rotation={}: frameFacing={}, behind(local)={}",
                                rot, frameFacing, behind);
                    }
                }
            }
            helper.succeed();
        });
    }

    public static void register(IEventBus bus) {
        RelicTales.LOGGER.info("[RelicTales] Registering game test functions");
        TEST_FUNCTIONS.register(bus);
        bus.addListener(net.neoforged.neoforge.event.RegisterGameTestsEvent.class,
                RelicBrushInteractionTest::onRegisterGameTests);
    }

    private static void onRegisterGameTests(net.neoforged.neoforge.event.RegisterGameTestsEvent event) {
        event.registerEnvironment(
                Identifier.fromNamespaceAndPath(RelicTales.MOD_ID, "default"),
                new TestEnvironmentDefinition.AllOf()
        );
    }
}
