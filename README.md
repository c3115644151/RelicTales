# RelicTales（考古物语）

![Minecraft 26.1.1](https://img.shields.io/badge/Minecraft-26.1.1-brightgreen)
![NeoForge 26.1.1.14-beta](https://img.shields.io/badge/NeoForge-26.1.1.14--beta-orange)
![Java 25](https://img.shields.io/badge/Java-25-blue)

> A Vanilla+ archaeology mod — seamlessly extending Minecraft's native archaeology system into existing structures.

## 模组简介

RelicTales（考古物语）是一个以"原生增强"为核心理念的考古 MOD。它不改变世界的岩层结构，也不引入复杂的工具链，而是将"可疑方块"悄悄注入玩家熟悉的原版遗迹（丛林神殿、沙漠神殿、要塞）中。玩家使用原版刷子即可参与考古，发现并收集来自失落文明的遗物碎片与钱币。

**核心理念：零诅咒、零自定义工具、零世界改动。**

## 核心特性

### 原生遗迹注入
| 目标遗迹 | 注入方块 | 注入方式 |
|---|---|---|
| 丛林神殿 | 可疑的苔石 | Mixin（Legacy 结构） |
| 沙漠神殿 | 可疑的雕纹砂岩 | Mixin（Legacy 结构） |
| 要塞 | 可疑的裂纹石砖/苔石砖/苔石 | Mixin（概率替换系统） |
| 下界要塞 | 可疑的下界砖 | Mixin（空气暴露检测 + 概率替换） |
| 末地城 | 可疑的紫珀块 | Mixin postProcess TAIL（Jigsaw → TemplateStructurePiece） |

### 智能概率系统（要塞）
不同房间类型有不同的可疑方块生成概率：
- **图书馆**: 10% | **传送门房间**: 6% | **走廊**: 6%
- **喷泉房间**: 中心柱 100%，中心 3×3 地板 50%，其余 10%
- **储藏室**: 仅替换裂纹/苔石砖（非普通石砖），中心地板 100%

### 智能概率系统（下界要塞）
- **烈焰人王座**: 30% | **烈焰人疣房**: 10% | **疣楼梯间**: 5%
- **阳台**: 4% | **熔岩井 / 十字路口 / 小走廊十字**: 3%
- **小走廊 / 转弯 / 桥**: 1% | **全覆盖兜底**: 0.2%
- **熔岩井底**: 100%（特殊位置固定替换）
- **空气暴露检测**: 仅替换至少有一面暴露在空气中的方块（排除埋墙和支柱内部）

### 智能概率系统（末地城）
- **桥梁末端**: 20% | **胖塔顶**: 12% | **第三层 2 号**: 10%
- **末地船**: 8% | **塔顶**: 6% | **底层 / 胖塔底**: 5%
- **第二层 / 胖塔中段**: 4% | **第三层 1 号**: 3%
- **屋顶 / 楼梯**: 2% | **塔基 / 桥段 / 塔板**: 0%（保留结构完整性）
- **鞘翅翼后**: 100%（末地船特殊位置固定替换）
- **5 方向空气暴露检测**: 仅替换顶面或四面至少有一面暴露在空气中的方块，排除平台底面/柱芯/屋顶外侧

### 裂纹纹理自动生成
自定义可疑方块的裂纹材质从原版可疑沙砾/沙子中提取裂纹遮罩，程序化叠加到基础方块纹理上。刷取时裂纹随 `dusted=N` 等级逐步扩散，与原版行为完全一致。

### 零额外工具负担
玩家直接使用 **Minecraft 原版刷子**（`minecraft:brush`）即可参与考古，无需合成任何自定义工具。

### 快速考古循环
```
进入遗迹 → 发现可疑方块 → 原版刷子刷取 → 获得掉落物
```

## 开发状态

| Milestone | 内容 | 状态 |
|---|---|---|
| M1 | 原型与基础注册 | ✅ 已完成 |
| M2 | 遗迹注入机制（丛林神殿/沙漠神殿/要塞/下界要塞/末地城） | ✅ 已完成 |
| M3 | 内容物扩充 | 🔄 设计讨论中 |
| M4 | 考古工具集 | ⏳ 待设计 |
| M5 | MVP 发布验收 | ⏳ 待设计 |

## 构建命令

```bash
./gradlew build               # 完整构建
./gradlew runClient           # 运行测试客户端
./gradlew runGameTestServer   # 运行自动化游戏测试（35 个测试）
./gradlew runData             # 运行数据生成器（BlockStates / LootTables / Lang）
./gradlew clean build         # 清理并重新构建
```

## 系统要求

- Minecraft: 26.1.1
- NeoForge: 26.1.1.14-beta
- Java: 25（Foojay resolver 自动管理，无需手动配置）

## 项目结构

```
src/main/java/com/relictales/
├── RelicTales.java              # @Mod 主类，EventBus 入口
│
├── registry/                    # 注册层
│   ├── RelicRegisters.java     # 统一注册入口（BLOCKS/ITEMS/PROCESSORS）
│   ├── blocks/RelicBlocks.java # 可疑方块（BrushableBlock）
│   ├── items/RelicItems.java   # 遗物物品 + BlockItem
│   └── CreativeTab.java        # 创造模式物品栏
│
├── content/                     # 业务层：纯 Java
│   ├── block/                  # RelicBrushableBlock / RelicBrushableBlockEntity
│   └── structure/              # JungleTempleProcessor
│
├── mixin/                       # Mixin 注入
│   ├── MixinStructurePiece.java      # 要塞 + 下界要塞概率替换
│   ├── MixinJungleTemplePiece.java   # 丛林神殿注入
│   ├── MixinDesertPyramidPiece.java  # 沙漠神殿注入
│   ├── MixinChestCorridor.java       # 要塞走廊安全网
│   ├── MixinEndCityProcessor.java    # 末地城方块注入（postProcess TAIL）
│   ├── StructurePieceInvoker.java    # 访问 StructurePiece 内部方法
│   └── RoomCrossingAccessor.java     # 访问 RoomCrossing.type 字段
│
├── config/                      # 配置层
│   └── RelicConfig.java         # 所有可调参数
│
├── data/datagen/                # 数据生成器
│   ├── RelicBlockStates.java
│   ├── RelicItemModels.java
│   ├── RelicLootTables.java
│   └── RelicLang.java
│
└── test/                        # 游戏测试（35 个）
    └── RelicBrushInteractionTest.java
```

详细设计文档见 [DESIGN_DOC.md](DESIGN_DOC.md)

## 许可

All Rights Reserved
