# CLAUDE.md

This file provides guidance to Claude Code when working with the RelicTales mod.
All decisions must align with the MVP-first philosophy documented in DESIGN_DOC.md.

---

## 一、模组基本信息

| 属性 | 值 |
|---|---|
| 模组名称 | RelicTales（考古物语） |
| Mod ID | `relictales` |
| Minecraft 版本 | 1.21.4 |
| Mod Loader | NeoForge 26.1.1.14-beta |
| Java | 25（Foojay resolver 自动管理） |
| 核心理念 | Vanilla+ 原版增强考古，零诅咒，零自定义工具分级 |

---

## 二、构建命令

```bash
./gradlew build           # 完整构建
./gradlew runClient       # 运行测试客户端
./gradlew runData         # 运行数据生成器（BlockStates / LootTables / Lang）
./gradlew clean build     # 清理并重新构建
```

---

## 三、项目架构（扁平三层）

```
com.relictales/
├── RelicTales.java              # @Mod 主类，EventBus 入口
│
├── registry/                     # ★ 注册层 ★
│   ├── RelicRegisters.java      # 统一注册入口（BLOCKS/ITEMS/PROCESSORS）
│   ├── blocks/
│   │   └── RelicBlocks.java     # 可疑方块（BrushableBlock）
│   ├── items/
│   │   └── RelicItems.java      # 遗物物品 + BlockItem
│   ├── processors/
│   │   └── ModStructureProcessors.java  # StructureProcessorType 注册
│   └── CreativeTab.java         # 创造模式物品栏
│
├── content/                      # ★ 业务层：纯 Java，无 Loader 专属 API ★
│   └── structure/
│       └── JungleTempleProcessor.java  # 丛林神殿方块注入处理器
│
├── config/                        # ★ 配置层 ★
│   └── RelicConfig.java          # 所有可调参数的配置文件
│
└── data/                         # ★ 数据驱动层 ★
    └── datagen/
        ├── RelicBlockStates.java
        ├── RelicItemModels.java
        ├── RelicLootTables.java
        └── RelicLang.java
```

---

## 四、知识库查阅规范（强制执行）

**在任何新功能实现前，必须先查阅知识库中的 NeoForge 官方文档。严禁基于旧版本经验或记忆编写代码。**

### 4.1 必读文档清单

所有文档位于 `knowledge-base/reference/neoForge/` 目录：

| 实现场景 | 必须查阅的文档 | 关键内容 |
|---|---|---|
| **所有 API 疑问** | `NeoForge-开发实践勘误.md` | ⚠️ **开发前必读**，包含实际遇到的卡点与修正 |
| 遗迹注入（仅 Jigsaw 结构） | `NeoForge-世界生成-结构.md` | StructureProcessor 注册、Legacy 结构说明 |
| 方块注册（BrushableBlock） | `NeoForge-方块.md` | ✅ 双参数 register 语法 |
| 考古音效 | `NeoForge-资源-音效.md` | `SoundEvents.BRUSH_GENERIC`（1.21 名称） |
| 战利品表配置 | `NeoForge-服务端-战利品表.md` | LootTable 数据结构与注入 |

### 4.2 查询优先级

```
遇到任何 NeoForge API 疑问 → 先查 knowledge-base/reference/neoForge/ → 再查官方文档 docs.neoforged.net → 最后才用网络搜索
```

### 4.3 版本规范

- MC 版本格式：`YY.Drop.Hotfix`（如 26.1.1 = 2026年第1个大版本第1个热修）
- 本项目：Minecraft 26.1.1 / NeoForge 26.1.x
- **严禁混用旧版本（1.20.x / 1.21.x）的 API 认知**

---

## 五、Registry 设计规范

**核心原则**: DeferredRegister 按 Registry 类型分开，每类一个聚合类。

| Registry 类型 | 聚合类 | 命名规范 |
|---|---|---|
| `Blocks` | `RelicBlocks` | `NAME_BLOCK` |
| `Items` | `RelicItems` | `NAME_ITEM` |
| `CreativeModeTabs` | `RelicCreativeTab` | `TAB_ID` |

**注册入口（RelicRegisters.java）**:
```java
public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MOD_ID);
public static final DeferredRegister.Items  ITEMS  = DeferredRegister.createItems(MOD_ID);

public static void init(IEventBus bus) {
    BLOCKS.register(bus);
    ITEMS.register(bus);
    CreativeTab.CREATIVE_TABS.register(bus);
    ModStructureProcessors.PROCESSORS.register(bus);
}
```

**BrushableBlock 注册（双参数 register）**:
```java
// ✅ 正确：props 由 NeoForge 自动注入 lambda
public static final DeferredBlock<BrushableBlock> SUSPICIOUS_BLOCK = BLOCKS.register(
    "suspicious_block",
    props -> new BrushableBlock(Blocks.STONE, SoundEvents.BRUSH_GENERIC,
        SoundEvents.BRUSH_GRAVEL_COMPLETED, props)
);
```

**主类调用**:
```java
public RelicTales(IEventBus bus) {
    RelicRegisters.init(bus);
}
```

---

## 六、原版资源优先原则（强制执行）

### 6.1 零自定义工具

> **Minecraft 原版 1.20+ 已有完整考古系统和刷子。本模组不注册任何自定义考古工具。**
> 玩家直接使用 `minecraft:brush` 参与考古。模组仅负责注入方块和配置掉落。

### 6.2 方块系统：继承原版

> 所有可疑方块必须继承 `BrushableBlock`。
> 禁止创建独立的敲击状态机或自定义硬度系统。

### 6.3 物品系统：优先复用原版

> 所有物品优先查找原版中功能相近的资源进行扩展或替换。
> 自定义物品仅在原版无替代品时创建。

---

## 七、配置驱动原则（强制执行）

**所有可变数值必须进入配置文件，硬核逻辑留在 Java 文件中。**

### 7.1 配置层级（优先级从高到低）

| 层级 | 文件位置 | 说明 |
|---|---|---|
| **L1 模组配置** | `config/relictales-common.toml` | 运行时可调参数（通过 NeoForge ConfigSpec） |
| **L2 Datagen 生成的 JSON** | `src/main/resources/data/` | 掉落表、配方等数据文件 |
| **L3 Java 硬逻辑** | `src/main/java/com/relictales/` | 仅放不可变的核心算法 |

### 7.2 必须配置化的内容

以下数值**严禁硬编码**在 Java 文件中，必须移至 `config/RelicConfig.java`：

```java
// 示例：所有可调参数必须在此配置
public static class ArchaeologyConfig {
    // 遗迹注入比例
    public static final ForgeConfigSpec.IntValue SUSPICIOUS_BLOCK_CHANCE;

    // 各遗迹刷取难度（次数）
    public static final ForgeConfigSpec.IntValue BRUSH_HITS_JUNGLE_TEMPLE;
    public static final ForgeConfigSpec.IntValue BRUSH_HITS_DESERT_TEMPLE;
    public static final ForgeConfigSpec.IntValue BRUSH_HITS_STRONGHOLD;
    public static final ForgeConfigSpec.IntValue BRUSH_HITS_NETHER_FORTRESS;

    // 掉落物权重
    public static final ForgeConfigSpec.DoubleValue LEGENDARY_DROP_CHANCE;
    public static final ForgeConfigSpec.DoubleValue RARE_DROP_CHANCE;

    // 遗迹生成密度
    public static final ForgeConfigSpec.IntValue BLOCKS_PER_CHUNK;
}
```

### 7.3 配置化检查清单

**在提交任何新功能前，必须确认：**
- [ ] 所有数值（概率、比例、次数）是否已移入配置？
- [ ] 所有新增的方块/物品是否已注册到正确的 DeferredRegister？
- [ ] 所有 JSON 资源是否通过 Datagen 生成而非手动编写？

---

## 八、核心开发规范（红线）

### 8.1 API 1.21 正确规范（⚠️ 必须严格遵守）

> 以下为在 26.1 版本开发中实际遇到的卡点及错误经验，**严禁凭旧版本记忆编写代码**。

| 错误写法 | 正确写法 | 原因 |
|---|---|---|
| `SoundEvents.BRUSH_GENERIC_COMPLETED` | `SoundEvents.BRUSH_GRAVEL_COMPLETED` 或 `BRUSH_SAND_COMPLETED` | 1.21 中无 `GENERIC_COMPLETED` |
| `BLOCKS.registerBlock(name, lambda, props)` 三参数 | `BLOCKS.register(name, props -> new Block(...))` 双参数 | 三参数重载在 1.21.10 已废弃 |
| `Registries.STRUCTURE_PROCESSOR_SERIALIZER` | `Registries.STRUCTURE_PROCESSOR` | SERIALIZER 后缀不存在 |
| `processBlock(ServerLevelAccessor level, ...)` | `processBlock(LevelReader level, ...)` | 1.21 官方映射使用 `LevelReader` |
| `processBlock(..., originalInfo, currentInfo)` | ✅ 参数名正确但语义变了 | `currentBlockInfo` = 经前面处理器处理后的状态 |
| `DeferredHolder<StructureProcessorType<?>, T>` 嵌套泛型 | `DeferredHolder<StructureProcessorType<?>, StructureProcessorType<T>>` | `DeferredHolder<T, V>` 中 V 是包装类型本身 |
| 仅有 `models/item/<name>.json`（ItemModel） | 同时需要 `items/<name>.json`（ClientItem）引用 ItemModel | 1.21.x 物品模型需双层：ItemModel 定义几何 + ClientItem 配置渲染 |
| Mixin JSON 放在 `META-INF/` 子目录 | Mixin JSON 放在 resources **根目录** | `FolderJarContents` Windows 路径解析 bug，classes 根目录才能正确读取 |
| `@Inject(method = "...", at = @At("HEAD"))` 默认 remap | Mixin dev 环境需 `remap = false` | ModDevGradle 缺少 SRG 映射，`remap = false` 跳过混淆查找 |
| `@Inject(method = "placeBlock")` 拦截 JungleTemplePiece | 注入父类方法无效 → 改用 `postProcess` TAIL 后置扫描 | `placeBlock` 定义在 `StructurePiece`，`JungleTemplePiece` 未 override |

**1.21 考古音效正确名称**：
```
SoundEvents.BRUSH_GENERIC         ✅（不是 ITEM_BRUSH_BRUSHING_GENERIC）
SoundEvents.BRUSH_GRAVEL_COMPLETED ✅（不是 BRUSH_GENERIC_COMPLETED）
SoundEvents.BRUSH_SAND_COMPLETED   ✅
```

### 8.2 诅咒系统：彻底禁止

> 任何代码中不得出现 `curse`、`CurseManager`、`CurseLevel` 等关键词。

### 8.2 内容层纯度

> `content/` 包下所有代码必须为纯 Java，不得引用任何 `net.neoforged` 专属 API。

### 8.3 数据驱动

> 所有模型（BlockState / ItemModel）和 LootTable 必须通过 Datagen 自动生成。

### 8.4 客户端/服务端分离

> 渲染、粒子、特效属于客户端，逻辑、数据属于服务端。客户端代码不得进入 `content/` 层。

---

## 九、考古流程（MVP）

```
玩家进入原版遗迹结构
        ↓
发现模组注入的可疑方块
        ↓
使用原版刷子（minecraft:brush）右键刷取
        ↓
刷取完成 → 方块变回普通方块 + 掉落遗物碎片/钱币
        ↓
玩家收集遗物，考古循环完成
```

---

## 十、遗迹注入机制（核心方案）

> ⚠️ **重要**：并非所有遗迹都能通过 `processor_list` 注入方块。

| 原版遗迹 | 结构类型 | 可用注入方式 |
|---|---|---|
| 丛林神殿 | **Legacy（硬编码）** | ❌ `processor_list` 不可用 → 需要 **Mixin** |
| 沙漠神殿 | **Legacy（硬编码）** | ❌ `processor_list` 不可用 → 需要 **Mixin** |
| 要塞 | Jigsaw ✅ | ✅ StructureProcessor + processor_list JSON |
| 下界堡垒 | Jigsaw ✅ | ✅ StructureProcessor + processor_list JSON |
| 末地城 | Jigsaw ✅ | ✅ StructureProcessor + processor_list JSON |

**详见**：`knowledge-base/reference/neoForge/NeoForge-世界生成-结构.md` → "Legacy 结构与注入方案"章节

**当前实现状态**：
- ✅ `suspicious_mossy_stone_bricks` 已注册（BrushableBlock）
- ✅ `jungle_hunter_feather` 已注册（物品）
- ✅ LootTable / BlockState / ItemModel / Lang 已配置
- ✅ `JungleTempleProcessor` 已创建（但丛林神殿为 Legacy，processor_list 无法生效）
- ⏳ **下一步**：通过 **Mixin** 实现丛林神殿的方块注入（见 issue2_body.md）

---

## 十一、开发工作流

```
1. 查阅知识库文档（knowledge-base/reference/neoForge/）
2. 确认遗迹类型：Legacy → Mixin / Jigsaw → StructureProcessor
3. 编写 Java 注册代码（RelicBlocks / RelicItems / ModStructureProcessors）
4. 编写配置类（RelicConfig.java），将所有数值移入配置
5. 编写 Datagen（BlockStates / LootTables / Lang）
6. 编写遗迹注入逻辑（Jigsaw 用 StructureProcessor + JSON；Legacy 用 Mixin）
7. ./gradlew build      ← 验证编译
8. ./gradlew runClient ← 游戏内验证
9. 开发者反馈感受 → 进入下一轮迭代
```

---

## 十二、已废弃系统（禁止回归）

| 废弃项 | 旧路径 | 废弃原因 |
|---|---|---|
| 诅咒系统 | `content/curse/` | 与 MVP 无关 |
| 錾子系统 | `content/chisel/` | 复用原版刷子即可 |
| 沉积岩地层 | `world/features/` | 破坏世界平衡 |
| 多级刷子 | `content/brush/` | 无差异化价值 |
| 岩石枚举 | `content/rock/` | 被遗迹注入替代 |

---

## 十四、反编译认知修正流程（强制执行）

> **背景**：NeoForge 官方文档存在大量缺失和错误，1.21.x 的 API 与旧版本（1.20.x）差异显著。
> 仅靠文档无法确定官方行为时，必须通过**反编译 Minecraft 源码**来验证。
> 本项目已多次通过反编译发现文档错误并纠正认知。

### 14.1 何时需要反编译

| 场景 | 触发条件 |
|---|---|
| 文档行为与实际不符 | 官方文档示例代码无法编译、行为描述与运行时不符 |
| 找不到 API 文档 | NeoForge 文档缺失某 API 的用法说明 |
| 继承/覆盖基类行为 | 需要了解父类 `BrushableBlock`、`BlockEntity` 等的内部逻辑 |
| 注册机制时序问题 | `DeferredRegister` / `RegisterEvent` 时序导致 crash |
| 新版本（26.1）迁移 | 从旧版本迁移时 API 变化无文档记录 |
| 官方映射名称不确定 | 不确定 `SoundEvents`、`Registries` 等的字段名 |

**核心原则**：遇到任何无法通过知识库或文档解决的卡点，立即反编译验证，不要猜测。

### 14.2 反编译工具

**推荐工具**：
- **IntelliJ IDEA** 内置的反编译器（右键 `.class` → "Decompile"）
- **FernFlower**（Minecraft 配套的反编译器，Gradle 自动集成）
- **Minecraft Source**（通过 `./gradlew decompile` 生成，可直接在 IDEA 中浏览）

```bash
./gradlew decompile  # 生成反编译源码到 build/decompiled/
```

### 14.3 常见反编译目标

| 类/文件 | 反编译目的 | 关键学习方法 |
|---|---|---|
| `BrushableBlock` | 理解刷取流程、`newBlockEntity()` 硬编码问题 | 看 `createBlockEntity()` 调用的具体类 |
| `BrushableBlockEntity` | 理解刷取状态机、`lootTable` 字段、`spawnLoot()` 时机 | 看 `brushingState` 枚举和 `checkComplete()` |
| `BlockItem` | 理解 `descriptionId` 前缀规则（`item.` vs `block.`） | 看 `getName()` 源码 |
| `LootTable` | 理解 `spawnLoot()` 完整链路 | 看 `getRandomItems()` 参数构建 |
| `DeferredRegister` | 理解 `register()` 在类加载时执行的时机 | 看 `register()` 方法是否调用 `factory.apply()` |
| `Registries` | 确定 26.1 中的正确键名 | 看 `STRUCTURE_PROCESSOR` 而非 `STRUCTURE_PROCESSOR_SERIALIZER` |

### 14.4 卡点沉淀规范

**每次通过反编译解决卡点后，必须完成以下两步**：

**Step 1 — 更新 `NeoForge-开发实践勘误.md`**
新增 Entry，格式：
```markdown
## Entry N — <简短标题>

**根本原因**：<一句话描述根因>

**症状**：<崩溃信息或错误表现>

**错误认知**：<错误写法/理解>

**正确认知**：<正确写法 + 解释>

**反编译证据**：<来自哪个类/方法，例如 "BrushableBlock.newBlockEntity() L82"`
```

**Step 2 — 判断是否需要更新 CLAUDE.md**
- 如果是 API 使用规范（如 SoundEvents 名称）→ 更新 "八、核心开发规范" 的表格
- 如果是注册机制时序 → 更新 "五、Registry 设计规范"
- 如果是文档缺失的系统性认知 → 在本节补充新的反编译目标

### 14.5 反编译认知验证 Checklist

每次反编译找到答案后，验证以下问题：

- [ ] 我找到的类/方法在哪个包？是否与我的理解一致？
- [ ] 这个行为在 1.21.11 和 26.1 之间是否有变化？
- [ ] 是否需要在 `NeoForge-开发实践勘误.md` 中记录？
- [ ] 是否有相关的 CLAUDE.md 规范需要更新？
- [ ] 这个认知是否可能误导未来的开发？需要写入规范吗？

### 14.6 已沉淀的卡点（参考）

以下问题均通过反编译确认并记录在 `NeoForge-开发实践勘误.md` 中：

| Entry | 主题 | 来源 |
|---|---|---|
| Entry 1 | DeferredRegister `register()` 时序陷阱 | 反编译 DeferredRegister 源码 |
| Entry 1b | BrushableBlock 自定义子类 Placement Crash | 反编译 `BrushableBlock.newBlockEntity()` |
| Entry 1c | BlockEntityType 循环依赖 | 反编译 `BlockEntityType` 构造逻辑 |
| Entry 8 | BlockItem `item.` 前缀 descriptionId | 反编译 `BlockItem.getName()` |
| Entry 9 | BrushableBlock 自定义 loot 表注入 | 反编译 `BrushableBlockEntity.spawnLoot()` |
| Entry 10 | Item 模型引用 `minecraft:` 纹理导致紫黑贴图 | 游戏内验证 |

---

## 十五、issue 文档规范

| 文件 | 对应 Milestone |
|---|---|
| `issue1_body.md` | M1: 原型与基础注册 |
| `issue2_body.md` | M2: 遗迹注入机制 |
| `issue3_body.md` | M3: 内容物扩充 |
| `issue4_body.md` | M4: 考古工具集与功能遗物 |
| `issue5_body.md` | M5: MVP 发布验收 |

**任务状态标记：**
- `[ ]` = 未开始
- `[T]` = 技术实现中
- `[A]` = 等待美术资源
- `[X]` = 已完成
