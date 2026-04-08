# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 模组基本信息

- **模组名**: RelicTales（考古物语）
- **Mod ID**: `relictales`
- **MC 版本**: 26.1.1（对应 Minecraft 原版最新年份制版本）
- **Mod Loader**: NeoForge 26.1.1.14-beta
- **Java**: 25（MDK 内置 Foojay resolver 自动下载，无需手动配置 JAVA_HOME）
- **构建工具**: Gradle（通过 `gradlew` wrapper，无需预先安装 Gradle）

## 构建命令

```bash
# 完整构建（客户端 + 服务端）
./gradlew build

# 运行客户端（开发环境）
./gradlew runClient

# 运行服务端（开发环境）
./gradlew runServer

# 仅运行数据生成器（BlockStates / ItemModels / Lang / LootTables）
./gradlew runData

# 清理并重新构建
./gradlew clean build

# 查看所有可用任务
./gradlew tasks --group=build
```

## 项目架构

```
com.relictales/
├── RelicTales.java              # @Mod 主类，EventBus 入口
├── config/
│   └── RelicTalesConfig.java    # ModConfigSpec 运行时配置
├── registry/                     # ★ 所有 DeferredRegister 聚合 ★
│   ├── RelicRegisters.java      # 统一注册入口，init(bus) 调用所有子模块
│   ├── blocks/RelicBlocks.java  # DeferredRegister.Blocks
│   ├── items/ChiselItems.java   # 錾子系列（铜/铁/下界合金）
│   ├── items/BrushItems.java    # 刷子系列
│   ├── items/RelicItems.java    # 遗物/盲盒物品
│   ├── items/MiscItems.java     # 凝光树脂等杂项
│   ├── entities/RelicEntities.java
│   ├── sounds/RelicSounds.java
│   ├── blockentities/RelicBlockEntities.java
│   ├── enchantments/RelicEnchantments.java
│   ├── effects/RelicEffects.java
│   ├── potions/RelicPotions.java
│   └── CreativeTab.java
├── content/                      # ★ 业务逻辑层（对美术/音效友好）★
│   ├── chisel/ChiselTier.java       # 錾子硬度等级枚举
│   ├── chisel/ChiselInteraction.java # 剥岩判定逻辑
│   ├── brush/BrushInteraction.java   # 扫尘稀有度分流
│   ├── extraction/ResinInteraction.java  # 凝光树脂固化提取
│   ├── curse/CurseManager.java        # 诅咒值累加/衰减
│   ├── curse/CurseEffects.java        # 各阶段惩罚（幻听/视觉/追魂）
│   ├── curse/CurseLevel.java          # 诅咒等级枚举
│   ├── identification/IdentificationBehavior.java  # 鉴定桌交互
│   ├── relic/RelicItem.java           # 遗物物品基类
│   ├── relic/UnknownRelicItem.java    # 盲盒剪影逻辑
│   └── boss/AncientKingEntity.java    # 古王怨灵 Boss 实体
├── data/                          # ★ 数据驱动层 ★
│   ├── loot/RelicLootTables.java  # LootTable 修改
│   ├── recipes/RelicRecipes.java   # 合成配方
│   ├── tags/RelicTags.java         # Tag 修改
│   └── datagen/                    # DataGenerator 输出
│       ├── RelicBlockStates.java
│       ├── RelicItemModels.java
│       └── RelicLang.java
├── world/                         # 世界生成
│   ├── BiomeModifier.java          # 生物群系修改器
│   ├── features/SedimentRockFeature.java  # 沉积岩地物
│   └── structures/TombPieces.java   # Jigsaw 陵墓结构
├── client/                        # ★ 客户端专用 ★
│   ├── RelicTalesClient.java      # @ClientModArg 入口
│   ├── clientSetup/SoundRegistry.java  # 音效客户端注册
│   └── renderer/RelicRenderers.java # 实体/方块渲染器
├── network/                       # 网络同步
│   └── CurseSyncPacket.java       # 诅咒值客户端同步
└── util/RelicUtils.java
```

## Registry 设计规范

**核心原则**: DeferredRegister 按 Registry 类型分开，每类一个聚合类。

| Registry 类型 | 聚合类 | 命名规范 |
|---|---|---|
| `Blocks` | `RelicBlocks` | `NAME_BLOCK` |
| `Items` | `RelicItems` | `NAME_ITEM` |
| `EntityTypes` | `RelicEntities` | `NAME` |
| `SoundEvents` | `RelicSounds` | `NAME` |
| `BlockEntityTypes` | `RelicBlockEntities` | `NAME` |
| `Enchantments` | `RelicEnchantments` | `NAME` |
| `MobEffects` | `RelicEffects` | `NAME` |
| `Potions` | `RelicPotions` | `NAME` |
| `CreativeModeTabs` | `RelicCreativeTab` | `TAB_ID` |

**注册入口（RelicRegisters.java）**:
```java
public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.create(Registries.BLOCK, MOD_ID);
public static final DeferredRegister.Items  ITEMS  = DeferredRegister.create(Registries.ITEM,  MOD_ID);

public static void init(IEventBus bus) {
    BLOCKS.register(bus);
    ITEMS.register(bus);
}
```

**主类调用**:
```java
public RelicTales(IEventBus bus) {
    RelicRegisters.init(bus);
}
```

## 团队协作接口规范

### 美术（Artist）接口

所有美术资源严格通过 **BlockState JSON + ItemModel JSON** 驱动，不硬编码路径。

- 贴图文件：`src/main/resources/assets/relictales/textures/` 下按 `block/` `item/` `entity/` 子目录组织
- BlockState：`src/main/resources/assets/relictales/blockstates/` 命名即方块 ID
- 物品模型：`src/main/resources/assets/relictales/models/item/` 命名即物品 ID
- 所有新增方块/物品 → 美术提供贴图后，在 `registry/` 对应类中注册，渲染自动生效

### 音效（Sound/渲染）接口

音效和渲染器通过标准 NeoForge 事件系统注入，美术/音效无需修改核心逻辑：

- 音效：注册到 `RelicSounds.java`，在 `content/` 业务层通过 `SoundEngine` 或 `PlaySoundAtEntity` 调用
- 渲染器：放入 `client/renderer/`，通过 `EntityRendererRegistry` 或 `BlockEntityRendererProvider` 绑定
- **关键**：所有音效/渲染触发点必须有**明确的接口文档注释**（// ARTIST: play X sound here）

## 数据驱动要点

- **LootTable**: `src/main/resources/data/relictales/loot_tables/` — 地层战利品配置
- **Tag**: `src/main/resources/data/c/tags/` — 原版标签修改
- **Recipes**: DataGenerator 自动生成，无需手动编写 JSON
- **Lang**: DataGenerator 输出到 `src/main/resources/assets/relictales/lang/`

## 开发注意事项

- Java 25 由 Foojay resolver 自动管理，无需手动安装
- 所有网络包需标记 `@Networked` 并在 `RelicPacketHandler` 注册
- CurseManager 使用 Player Capability 存储诅咒值，跨维度持久化
- Boss AI 使用 goals + sensors 标准 NeoForge AI 系统

## NeoForge 工具生态

### NeoForge 官方组织工具（neoforged/）

| 仓库 | 用途 |
|------|------|
| `AccessTransformers` | 字节码访问修饰符转换器（可添加到 `build.gradle` accessTransformers） |
| `Bus` | 事件总线框架（NeoForge 事件系统基于此） |
| `GradleUtils` | Gradle 构建工具库 |
| `NeoGradle` | 新一代 Kotlin DSL 构建插件 |
| `CoreMods` | JavaScript 字节码转换系统 |
| `actions-modming` | GitHub Actions CI/CD 工具集 |

### 推荐第三方依赖库

| 库 | Maven 坐标 | 用途 |
|----|-----------|------|
| **Curios API** | `top.theillusivec4.curios:curios-neoforge` |饰品槽位 API（用于遗物佩戴系统）|
| **Jade** | `maven.modrinth:jade-324717` | 开发期调试工具（显示方块/实体信息） |
| **EMI** | `dev.emi:emi-neoforge` | 合成路线查看器（JEI 替代品） |
| **Cloth Config** | `me.shedaniel.cloth:cloth-config-neoforge` | 配置界面 UI 库 |

### Maven 仓库配置

```groovy
repositories {
    maven { url = 'https://maven.neoforged.net/releases' }  // NeoForge
    maven { url = 'https://maven.modrinth.org' }            // Modrinth 库
    maven { url = 'https://maven.architectury.dev/' }       // 第三方库（如需）
}
```

## 跨平台架构（未来迁移规划）

### 分层原则：四层架构

代码严格按职责分为四层，**从下到上依赖，不能反向**：

```
┌─────────────────────────────────────────────┐
│  表现层（client/）                          │  ← 客户端渲染器、音效、GUI
│  仅在特定 Loader（如 NeoForge/Fabric）存在   │
├─────────────────────────────────────────────┤
│  业务逻辑层（content/）                     │  ★ 核心玩法，所有 Loader 共用
│  纯 Java，不引用任何 Loader 专属 API         │
├─────────────────────────────────────────────┤
│  注册层（registry/）                       │  ★ DeferredRegister 等 Loader 专属
│  负责将 content 层的逻辑注册到游戏中        │
├─────────────────────────────────────────────┤
│  数据驱动层（data/ + resources/）           │  ★ JSON，天然跨版本
│  LootTable、Tag、Lang、ItemModel JSON       │
└─────────────────────────────────────────────┘
```

### 业务逻辑层编写规范（content/）

这是**最容易在未来迁移时复用的代码**，编写时严格遵守：

```java
// ✓ 正确：纯 Java 逻辑，无 Loader 依赖
public enum ChiselTier {
    COPPER(1), IRON(2), NETHERITE(3);

    public final int hardness;
    ChiselTier(int hardness) { this.hardness = hardness; }

    public boolean canBreak(Block block) {
        return block.getHardness() <= this.hardness * 2;
    }
}

// ✗ 错误：在 content 层使用 NeoForge 专属 API
// public class CurseManager extends NeoForgeHooks { ... }
```

**以下逻辑必须写在 content/ 层（纯 Java）**：
- 枚举定义（ChiselTier、CurseLevel、RelicRarity）
- 算法和判定逻辑（canBreak 判断、稀有度过滤概率）
- 数据模型（RelicData NBT 结构）
- 状态机（boss AI 状态转换逻辑）

**以下逻辑写在 registry/ 层（Loader 专属）**：
- `DeferredRegister` 创建和注册调用
- 事件监听器注册到 `IEventBus`
- Capability 注册

### 数据驱动原则（跨版本关键）

**数据包（JSON）天然跨版本兼容**，尽量把逻辑往这里放：

| 数据类型 | 路径 | 跨版本兼容性 |
|---------|------|------------|
| LootTable | `data/relictales/loot_tables/` | ★★★★★ |
| Tags | `data/c/tags/` | ★★★★★ |
| ItemModel | `assets/relictales/models/item/` | ★★★★☆ |
| BlockState | `assets/relictales/blockstates/` | ★★★★☆ |
| Lang | `assets/relictales/lang/` | ★★★★☆ |
| Recipes | DataGenerator 自动生成 | ★★★★★ |

### 未来迁移路径

```
当前阶段（NeoForge 26.1.1）
    │
    ▼  引入 MultiLoader-Template 时
    │  将 com.relictales/ 拆分为 common/ + neoforge/ + fabric/
    │
    ▼  Fabric 移植
    │  common/ 逻辑直接复用
    │  neoforge/ → fabric/ 编写胶水层
    │
    ▼  向下版本兼容
    │  主要工作量在 registry/ 层适配
    │  content/ 纯 Java 逻辑极少改动
    │  data/ JSON 层几乎不改
```

### 第三方 Mod 兼容性策略

通过 **`optional compileOnly`** 声明可选依赖：

```groovy
// JEI 兼容（行业标准，几乎所有整合包都带）
compileOnly("mezz.jei:jei-$minecraft_version-neoforge-api:$jei_version")
runtimeOnly("mezz.jei:jei-$minecraft_version-neoforge:$jei_version") { transitive = false }

// EMI 兼容（JEI 替代品）
compileOnly("dev.emi:emi-neoforge:$emi_version")

// Curios API（核心依赖，必须）
implementation("top.theillusivec4.curios:curios-neoforge:$curios_version+$minecraft_version")
```

### 不需要的技术

- **Architectury API**：仅在同时需要 Fabric + NeoForge 双平台时才需要
- **Registrate**：NeoForge 内置 DeferredRegister 已足够，无需额外注册库
