# M1: 原版遗迹注入与可疑方块原型（丛林神殿闭环）

**里程碑：** Milestone 1
**目标：** 实现最小可行性产品（MVP）的核心闭环——以丛林神殿为跑通目标，实现可疑方块注册、刷取逻辑与遗物掉落。

## 📋 任务清单

### 1.1 基础架构与注册 (MVP)
- [X] 清理所有旧版沉积岩、诅咒相关的废弃代码。
- [X] **方块注册**: 在 `RelicBlocks` 注册 `suspicious_mossy_stone_bricks`（可疑的苔石砖）。
- [X] **方块实体绑定**: 为上述方块绑定自定义 `RelicBrushableBlockEntity`，复用原版刷取音效与动画。
- [X] **遗物注册**: 在 `RelicItems` 注册 `jungle_hunter_feather`（丛林猎羽符），直接掉落完整物品。

### 1.2 数据驱动与测试
- [X] **方块状态**: `blockstates/suspicious_mossy_stone_bricks.json` + `models/block/` 引用原版苔石砖纹理。
- [X] **物品模型**: 双层结构（1.21.x 规范）：
  - `models/item/` — ItemModel JSON（几何 + 纹理）
  - `items/` — ClientItem JSON（渲染配置，引用 ItemModel）
- [X] **掉落物**: `data/relictales/loot_tables/blocks/suspicious_mossy_stone_bricks.json`。
  - 8% 概率掉落 `jungle_hunter_feather`
  - 其余掉落原版神殿杂物（骨头、线、绿宝石、钻石等）
- [X] **LootTable 注入**: `RelicsBrushableBlock.onPlace()` 在方块放置时自动设置 loot table 到 block entity（通过 `setLootTable()`）
- [X] **遗迹注入 (Mixin)**: 丛林神殿为 Legacy 结构，`processor_list` 不可用 → Mixin 拦截 `JungleTemplePiece.postProcess()` 中的 `placeBlock` 调用，20% 苔石砖替换为可疑版本（M2 已完成）

### 1.3 国际化
- [X] **英文**: `lang/en_us.json` — 所有方块/物品显示名已注册。
- [X] **中文**: `lang/zh_cn.json` — 可疑的苔石砖 / 丛林猎羽符。

## 🎨 资源需求
- [ ] `@ARTIST`: `suspicious_mossy_stone_bricks` 刷取阶段 0-3 的破损剥落贴图（当前使用原版苔石砖占位）。
- [ ] `@ARTIST`: 丛林猎羽符 (`jungle_hunter_feather`) 物品图标 (16x16，当前使用程序化占位符）。

## ⚠️ 已知限制

**丛林神殿注入需要 Mixin（属于 M2）**：
> Minecraft 中存在两种结构类型。丛林神殿属于 **Legacy Feature**（硬编码 Java 逻辑），其 `processor_list` 无法注入自定义方块。
> 替代方案为 Mixin 拦截 `JungleTemplePiece.placeBlock()`，在放置时将部分苔石砖替换为可疑版本。
> 详见 [NeoForge-开发实践勘误.md](knowledge-base/reference/neoForge/NeoForge-开发实践勘误.md) — Entry 6。

## ✅ 验收标准
| # | 标准 | 状态 |
|---|---|---|
| 1 | 进入原版丛林神殿，能自然发现带有微小不同的可疑苔石砖。 | ✅ 已实现（Mixin） |
| 2 | 使用原版刷子右键，能正常播放刷取动画。 | ⏳ 待游戏内验证 |
| 3 | 刷取完成后，有小概率直接获得完整遗物 `jungle_hunter_feather`，否则获得普通杂物。 | ⏳ 待游戏内验证 |

**M1 实际完成度：约 95%**（代码全部完成，待游戏内验证闭环）
