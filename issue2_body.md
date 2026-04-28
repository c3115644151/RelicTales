# M2: 遗迹注入机制

**里程碑：** Milestone 2
**依赖：** M1 完成
**状态：** ✅ 已完成

## 目标
通过 **Mixin** 将模组的可疑方块注入原版 Legacy 遗迹结构中（丛林神殿等），实现考古内容与原生世界的无缝融合。

> ⚠️ **重要**：丛林神殿和沙漠神殿属于 **Legacy Feature**（硬编码 Java 逻辑），其 `processor_list` **无法**注入自定义方块。必须使用 Mixin 拦截放置逻辑。

## 任务清单

### 2.0 Mixin 基础设施
- [X] 配置 `META-INF/neoforge.mixins.json`（Mixin 配置）
- [X] Mixin 注解处理器配置（`build.gradle` 已通过 NeoForge 插件自动处理）
- [X] 验证 Mixin 注入正常工作（`remap = false` 解决混淆映射问题）

### 2.1 丛林神殿遗迹注入
- [X] `MixinJungleTemplePiece`：拦截 `postProcess` TAIL，扫描并替换所有 MOSSY_STONE_BRICKS 为可疑方块
- [X] LootTable 注入：`RelicsBrushableBlock.onPlace()` 放置时自动设置 loot table
- [X] 配置注入比例（100% 替换，Mixin TAIL 扫描）
- [X] 验证：Mixin 加载成功，无 Critical injection errors（见 2026-04-21 测试日志）

### 2.2 沙漠神殿遗迹注入
- [X] 注册 `suspicious_chiseled_sandstone`（可疑的雕纹砂岩）
- [X] 注册沙漠神殿遗物物品（太阳神徽章）
- [X] 编写 `MixinDesertPyramidPiece`
- [X] 注入沙漠神殿的隐藏密室

### 2.3 要塞遗迹注入（Mixin）
- [X] 注册 `suspicious_cracked_stone_bricks`（可疑的裂纹石砖）
- [X] 注册 `suspicious_mossy_stone_bricks`（可疑的苔石砖）
- [X] 注册 `suspicious_mossy_cobblestone`（可疑的苔石）
- [X] 编写 `MixinStructurePiece`：拦截 `StructurePiece.placeBlock()` HEAD，概率替换要塞内的裂纹石砖/苔石砖为可疑方块
- [X] 编写 `RoomCrossingAccessor`：访问 `RoomCrossing.type` 字段区分喷泉/储藏室
- [X] 编写 `MixinChestCorridor`：安全网，在 ChestCorridor 中心地板注入可疑方块
- [X] 实现分房间概率系统：
  - Library: 10% | RoomCrossing 中心柱: 100% | 中心 3×3 地板: 50% | 通用: 10%
  - FiveCrossing: 10% | ChestCorridor 中心: 100% | 通用: 6%
  - PortalRoom: 6% | 其他房间: 1%
- [X] 使用 `level.getLevel()` 解决 WorldGenLevel 战利品表注入问题
- [X] 19 个自动化测试全部通过

### 2.4 下界堡垒遗迹注入（✅ 已完成）
- [X] 注册 `suspicious_nether_bricks`（可疑的下界砖）
- [X] BlockItem + BlockEntityType 注册
- [X] 裂纹纹理自动生成（复用 `tools/generate_suspicious_textures.py`）
- [X] blockstate `dusted=N` 变体 + 方块模型 + loot table
- [X] `MixinStructurePiece` 扩展：下界要塞分支，与要塞逻辑共存
- [X] 空气暴露检测（6 方向邻居检查，排除埋墙/支柱内部方块）
- [X] 熔岩井底 100% 替换（CastleEntrance local(6,0,6)）
- [X] 分房间概率系统：
  - MonsterThrone: 30% | CastleStalkRoom: 10%
  - StairsRoom / CastleCorridorStairs: 5% | CastleCorridorTBalcony: 4%
  - CastleEntrance / RoomCrossing / BridgeCrossing / 小走廊十字: 3%
  - 小走廊/转弯/BridgeStraight: 1% | 全覆盖兜底: 0.2%
- [X] 5 个自动化测试全部通过（共 24 项测试）

### 2.6 末地城遗迹注入（Jigsaw，可选）
- [ ] 注册 `suspicious_purpur_block`（可疑的紫珀方块）
- [ ] 编写 `EndCityModifier`（StructureProcessor）

### 遗迹产出定义（已废弃 — 待重新设计）

> 旧版遗物设计（丛林猎羽符、太阳神徽章、凋零护符等）已全部清理。这些数值化 RPG 物品不符合模组的新设计哲学。
> 新的考古产出方案待 M3 讨论确定。

- [X] 配置每个遗迹的可疑方块生成比例（要塞已完成概率配置）
- [X] 裂纹纹理自动生成（从原版可疑方块提取裂纹遮罩，应用到自定义方块）

## 🔑 关键技术笔记

### WorldGenLevel 战利品表注入问题

**错误**：使用 `level instanceof ServerLevel` 检查世界生成阶段，但 WorldGenLevel 不继承 ServerLevel，导致战利品表从不注入。

**解决**：使用 `level.getLevel()` 获取底层 ServerLevel：
```java
ServerLevel serverLevel = level.getLevel();
if (serverLevel != null) {
    serverLevel.getServer().execute(() -> {
        // 设置 loot table
    });
}
```

### Mixin 混淆映射问题

**错误**：`Unable to locate obfuscation mapping for @Inject target postProcess`

**原因**：NeoForge dev 环境下，类已有 MCP 名称（`postProcess`）但缺少 SRG→MCP 映射文件，Mixin 注解处理器找不到方法。

**解决**：在 `@Inject` 添加 `remap = false`：
```java
@Inject(method = "postProcess", at = @At("HEAD"), remap = false)
```

### LootTable 注入（无需 Mixin）

在 `RelicsBrushableBlock.onPlace()` 中直接设置：
```java
BlockEntity be = level.getBlockEntity(pos);
if (be instanceof RelicBrushableBlockEntity relicBe) {
    relicBe.setLootTable(lootKey, 0L);
}
```

### 纹理自动生成（裂纹遮罩方案）

所有自定义可疑方块缺少美术资源，通过从原版可疑方块提取裂纹遮罩解决：

1. 从原版可疑沙砾/沙子纹理逐像素对比基础纹理，提取 151 个裂纹像素位置
2. 裂纹按等级分布：L0=72, L1=16, L2=27, L3=36（累积）
3. 对每个自定义基础纹理（裂纹石砖、苔石砖、苔石、雕纹砂岩），在每个等级对裂纹像素 RGB(-20) 暗化
4. 配合 `dusted=N` blockstate 变体，刷取时自动切换纹理,实现裂纹扩散效果
5. 工具脚本：`tools/generate_suspicious_textures.py`

**生成的纹理**：20 个 PNG（3 个 cube_all × 4 等级 + 1 个 cube_column × 2 纹理 × 4 等级）

## ✅ 验收标准
| # | 标准 | 状态 |
|---|---|---|
| 1 | 进入丛林神殿，能自然发现可疑苔石 | ✅ 已验证 |
| 2 | 使用原版刷子右键，能正常播放刷取动画 | ✅ 已验证 |
| 3 | 刷取完成后，小概率掉落 `jungle_hunter_feather` | ✅ 已验证 |
| 4 | 进入沙漠神殿，可在雕纹砂岩位置发现可疑雕纹砂岩 | ✅ 已验证 |
| 5 | 刷取可疑雕纹砂岩后掉落 `sun_god_badge` 或其他沙漠主题物品 | ✅ 已验证 |
| 6 | 进入要塞，裂纹石砖/苔石砖有概率被替换为可疑方块 | ✅ 已验证（19/19 测试通过） |
| 7 | 喷泉房间中心 3×3 范围为 50% 替换，非 100% | ✅ 已验证 |
| 8 | 储藏室仅替换裂纹/苔石砖（非普通石砖），中心地板 100% | ✅ 已验证 |
| 9 | 自定义可疑方块有裂纹纹理，刷取时裂纹逐级扩散 | ✅ 已验证 |
| 10 | 进入下界要塞，下界砖有概率被替换为可疑的下界砖 | ✅ 已验证（24/24 测试通过） |
| 11 | 可疑的下界砖刷取完成后掉落正确的下界主题物品 | ✅ 已验证 |
| 12 | 熔岩井底浮空下界砖 100% 替换为可疑方块 | ✅ 已验证 |
| 13 | 埋墙和支柱内部的下界砖不会被替换（空气暴露检测） | ✅ 已验证 |
