# M2: 遗迹注入机制

**里程碑：** Milestone 2
**依赖：** M1 完成
**状态：** 🔄 开发中

## 目标
通过 **Mixin** 将模组的可疑方块注入原版 Legacy 遗迹结构中（丛林神殿等），实现考古内容与原生世界的无缝融合。

> ⚠️ **重要**：丛林神殿和沙漠神殿属于 **Legacy Feature**（硬编码 Java 逻辑），其 `processor_list` **无法**注入自定义方块。必须使用 Mixin 拦截放置逻辑。

## 任务清单

### 2.0 Mixin 基础设施
- [X] 配置 `META-INF/neoforge.mixins.json`（Mixin 配置）
- [X] Mixin 注解处理器配置（`build.gradle` 已通过 NeoForge 插件自动处理）
- [X] 验证 Mixin 注入正常工作（`remap = false` 解决混淆映射问题）

### 2.1 丛林神殿遗迹注入
- [X] `MixinJungleTemplePiece`：拦截 `postProcess` 中的 `placeBlock` 调用，20% 苔石砖替换为可疑方块
- [X] LootTable 注入：`RelicsBrushableBlock.onPlace()` 放置时自动设置 loot table
- [ ] 配置注入比例（建议 15-25%，当前硬编码 20%）
- [ ] 验证：进入丛林神殿可自然发现可疑苔石砖

### 2.2 沙漠神殿遗迹注入
- [ ] 注册 `suspicious_chiseled_sandstone`（可疑的切削砂岩）
- [ ] 注册沙漠神殿遗物物品（太阳神徽章）
- [ ] 编写 `MixinDesertPyramidPiece`
- [ ] 注入沙漠神殿的隐藏密室

### 2.3 要塞遗迹注入（Jigsaw，可选）
- [ ] 注册 `suspicious_stone_bricks`（可疑的石砖）
- [ ] 编写 `StrongholdModifier`（StructureProcessor）

### 2.4 下界堡垒遗迹注入（Jigsaw，可选）
- [ ] 注册 `suspicious_nether_bricks`（可疑的下界砖）
- [ ] 编写 `FortressModifier`（StructureProcessor）

### 2.6 末地城遗迹注入（Jigsaw，可选）
- [ ] 注册 `suspicious_purpur_block`（可疑的紫珀方块）
- [ ] 编写 `EndCityModifier`（StructureProcessor）

### 遗迹产出定义（已选定）

| 遗迹 | 注入方块 | 遗物名称 | 遗物功能 |
|---|---|---|---|
| 丛林神殿 | `suspicious_mossy_stone_bricks` | 丛林猎羽符 | 免疫摔落 + 弓弩射速 +10% |
| 沙漠神殿 | `suspicious_chiseled_sandstone` | 太阳神徽章 | 右键 30 秒夜视（冷却 120s）+ 阳光下移速 +10% |
| 末地城 | `suspicious_purpur_block` | 虚空记录碎片 | 附魔等级 +1（额外）+ 经验值获取 +15% |
| 下界堡垒 | `suspicious_nether_bricks` | 凋零护符 | 免疫凋零效果 + 对亡灵生物伤害 +15% |
| 海底废墟 | `suspicious_polished_andesite` | 亚特兰蒂斯陶罐之心 | 水中生命恢复 ×2 + 游泳速度 +20% |
- [ ] 配置每个遗迹的可疑方块生成比例（建议 15-25%）
- [ ] 平衡各遗迹的考古内容密度

## 🔑 关键技术笔记

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

## ✅ 验收标准
| # | 标准 | 状态 |
|---|---|---|
| 1 | 进入丛林神殿，能自然发现可疑苔石砖 | ⏳ 待验证 |
| 2 | 使用原版刷子右键，能正常播放刷取动画 | ⏳ 待验证 |
| 3 | 刷取完成后，小概率掉落 `jungle_hunter_feather` | ⏳ 待验证 |
