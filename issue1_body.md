# M1: 基石与分级系统（Foundation & Tiers）

**里程碑：** Milestone 1
**到期日：** 2026-06-29
**依赖：** 无

---

## 目标

建立考古核心循环的基础骨架：工具系统、岩石方块体系、发掘交互核心逻辑。

---

## 技术任务

### 1.1 錾子系统

```
[T] 注册錾子物品
    - ID: relictales:chisel
    - 合成配方: 2铜锭 + 1木棍（工作台，V形）
    - 耐久: 120（铜工具等级）
    - 可附魔: 耐久I/II/III（不可附魔效率）
    → registry/items/ChiselItem.java
    → registry/RelicRegisters.java
```

```
[T] 实现敲击交互逻辑
    - 敲击状态机: STATE_0 → STATE_1 → ... → STATE_N → 可刷状态
    - 矿物决定敲击次数: 石灰岩2次, 暗曜岩4次
    - 硬直时间: 0.4秒（原版铁砧延迟）
    - 每次敲击: 粒子效果 + 音效变化
    → content/chisel/ChiselTier.java
    → content/chisel/ChiselInteraction.java
    ⚠️ P0.2 待确认: BlockState实现方式（独立JSON vs 动态纹理）
```

### 1.2 刷子系统

```
[T] 注册五种刷子
    - copper_brush: 1铜锭+1木棍, 耐久64, 效率1.0x
    - iron_brush: 1铁锭+1木棍, 耐久250, 效率1.5x
    - gold_brush: 1金锭+1木棍, 耐久32, 效率2.0x
    - diamond_brush: 1钻石+1木棍, 耐久512, 效率2.5x
    - netherite_brush: 钻石刷子+1下界合金锭（锻造台升级）, 耐久2031, 效率3.0x
    → registry/items/BrushItems.java
```

```
[T] 实现刷取交互逻辑
    - 效率倍率影响刷取次数: ceil(矿物基础点数 / 刷子效率)
    - 刷取状态机: 矿物进度点逐次累进 → 到达终点
    → content/brush/BrushTier.java
    → content/brush/BrushInteraction.java
```

### 1.3 岩石方块系统

```
[T] 注册两种考古岩石 + 可疑变种
    - 沉积石灰岩: Y 5~30, 敲2次+刷2次
      ID: relictales:limestone / relictales:suspicious_limestone
    - 暗曜岩: Y -60~-40, 敲4次+刷3次 ⚠️ P0.4 Y轴边界待确认
      ID: relictales:darkglimmer / relictales:suspicious_darkglimmer
    → registry/blocks/RelicBlocks.java
    → content/rock/RockType.java
    → content/rock/RockDefinition.java
```

```
[T] 实现斑状生成逻辑
    - 斑块大小: 10~30方块
    - 每个chunk生成: 1~3个斑块
    - 可疑比例: 15~20%
    - 替代石头/深板岩生成
    - 允许暴露表面生成（矿洞/峡谷岩壁）
    → world/features/SedimentRockFeature.java
    → data/loot/RelicLootTables.java
    ⚠️ P1.4 待确认: 斑块参数初估值待测试调整
```

```
[T] 可疑方块识别机制
    - 粒子特效标识（持续性飘浮粒子）
    - 石灰岩: 米黄色尘土粒子
    - 暗曜岩: 深紫色发光碎片粒子
    → client/renderer/RelicRenderers.java（粒子注册）
```

### 1.4 发掘交互核心

```
[T] 高价值产出判定
    - 刷取完成后 25% 概率进入第三形态（高价值）
    - 75% 概率直接掉落低价值物品
    → content/brush/BrushInteraction.java（判定逻辑）
```

```
[T] 第三形态 + slimeball取出
    - 第三形态视觉: 更强光芒/粒子
    - slimeball右键: 安全取出遗物
    - 无slimeball蛮力破坏: 30%蠹虫生成 + 50%遗物损坏
    → content/brush/BrushInteraction.java（第三形态逻辑）
```

---

## 美术资源需求

### 方块贴图 #P0

```
@ARTIST: 沉积石灰岩 - 16x16png，浅褐灰色沉积纹理，带化石痕迹暗示
  → textures/block/limestone.png

@ARTIST: 可疑的沉积石灰岩 - 同上，但需有"可疑"暗示（可用粒子代码实现，贴图可共用）
  → textures/block/suspicious_limestone.png

@ARTIST: 暗曜岩 - 16x16png，深紫/蓝色岩面，带微光效果（使用emissive通道）
  → textures/block/darkglimmer.png

@ARTIST: 可疑的暗曜岩 - 同上，但需有"可疑"暗示
  → textures/block/suspicious_darkglimmer.png
```

### 物品模型贴图 #P0

```
@ARTIST: 錾子 - 16x16手持视角，金属质感凿状工具
  → textures/item/chisel.png

@ARTIST: 铜刷子 - 16x16手持视角，铜质锥形毛刷
  → textures/item/copper_brush.png

@ARTIST: 铁刷子 - 16x16手持视角，铁质更密集刷头
  → textures/item/iron_brush.png

@ARTIST: 金刷子 - 16x16手持视角，金色高光泽刷头
  → textures/item/gold_brush.png

@ARTIST: 钻石刷子 - 16x16手持视角，蓝色晶体质感刷头
  → textures/item/diamond_brush.png

@ARTIST: 下界合金刷子 - 16x16手持视角，深灰+黑色光泽顶级质感
  → textures/item/netherite_brush.png
```

---

## 阻塞项

| # | 问题 | 状态 |
|---|------|------|
| P0.2 | 敲击状态BlockState实现方式（4个独立JSON vs 动态纹理） | ⚠️ 待确认 |
| P0.4 | 暗曜岩Y轴边界（提案Y=-60~-40）和幽晶岩Y轴边界（提案Y=-40~-25） | ⚠️ 待确认 |

---

## 验收标准

1. 錾子敲击任意可疑岩石，状态逐次更新，4-5次后进入可刷状态
2. 刷子刷取可刷岩石，进度逐次累进，最终触发产出判定
3. 25%概率进入第三形态（需slimeball取出），75%直接掉落
4. 两种岩石在各自Y轴区间自然生成，带粒子标识
5. 所有物品可正常合成（錾子/五种刷子）
