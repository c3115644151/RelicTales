package com.relictales.test;

import com.relictales.RelicTales;
import com.relictales.content.block.RelicBrushableBlockEntity;
import com.relictales.mixin.BrushableBlockEntityAccessor;
import com.relictales.registry.blocks.RelicBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.FunctionGameTestInstance;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;

import java.util.function.Consumer;

public class RelicBrushInteractionTest {

    private static final ResourceKey<LootTable> TEST_LOOT_KEY = ResourceKey.create(
            Registries.LOOT_TABLE,
            Identifier.fromNamespaceAndPath(RelicTales.MOD_ID, "blocks/suspicious_mossy_cobblestone")
    );

    private static final DeferredRegister<Consumer<GameTestHelper>> TEST_FUNCTIONS =
            DeferredRegister.create(Registries.TEST_FUNCTION, RelicTales.MOD_ID);

    private static DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> TEST1_FUNCTION;
    private static DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> TEST2_FUNCTION;
    private static DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> TEST3_FUNCTION;
    private static DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> TEST4_FUNCTION;

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
    }

    public static void register(IEventBus bus) {
        RelicTales.LOGGER.info("[RelicTales] Registering game test functions");
        TEST_FUNCTIONS.register(bus);
        bus.addListener(net.neoforged.neoforge.event.RegisterGameTestsEvent.class,
                RelicBrushInteractionTest::onRegisterGameTests);
    }

    private static void onRegisterGameTests(net.neoforged.neoforge.event.RegisterGameTestsEvent event) {
        Holder<TestEnvironmentDefinition<?>> defaultEnv =
                event.registerEnvironment(
                        Identifier.fromNamespaceAndPath(RelicTales.MOD_ID, "default"),
                        new TestEnvironmentDefinition.AllOf()
                );

        Identifier emptyStructure = Identifier.fromNamespaceAndPath("minecraft", "empty");
        TestData<Holder<TestEnvironmentDefinition<?>>> test1Data = new TestData<>(
                defaultEnv,
                emptyStructure,
                100, 5, true
        );
        FunctionGameTestInstance test1Instance = new FunctionGameTestInstance(
                TEST1_FUNCTION.getKey(),
                test1Data
        );
        event.registerTest(
                Identifier.fromNamespaceAndPath(RelicTales.MOD_ID, "suspicious_block_has_correct_be_type"),
                test1Instance
        );

        TestData<Holder<TestEnvironmentDefinition<?>>> test2Data = new TestData<>(
                defaultEnv,
                emptyStructure,
                100, 5, true
        );
        FunctionGameTestInstance test2Instance = new FunctionGameTestInstance(
                TEST2_FUNCTION.getKey(),
                test2Data
        );
        event.registerTest(
                Identifier.fromNamespaceAndPath(RelicTales.MOD_ID, "suspicious_block_loot_table_can_be_set"),
                test2Instance
        );

        TestData<Holder<TestEnvironmentDefinition<?>>> test3Data = new TestData<>(
                defaultEnv,
                emptyStructure,
                100, 5, true
        );
        FunctionGameTestInstance test3Instance = new FunctionGameTestInstance(
                TEST3_FUNCTION.getKey(),
                test3Data
        );
        event.registerTest(
                Identifier.fromNamespaceAndPath(RelicTales.MOD_ID, "vanilla_block_uses_vanilla_be"),
                test3Instance
        );

        TestData<Holder<TestEnvironmentDefinition<?>>> test4Data = new TestData<>(
                defaultEnv,
                emptyStructure,
                120, 5, true
        );
        FunctionGameTestInstance test4Instance = new FunctionGameTestInstance(
                TEST4_FUNCTION.getKey(),
                test4Data
        );
        event.registerTest(
                Identifier.fromNamespaceAndPath(RelicTales.MOD_ID, "brushing_converts_to_base_block"),
                test4Instance
        );
    }
}
