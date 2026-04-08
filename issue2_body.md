# M2: 沉浸式发掘与提取（Immersive Excavation）

**里程碑：** Milestone 2
**到期日：** 2026-07-30
**依赖：** M1 完成

---

## 目标

深化发掘体验，添加沉浸式视觉/音效反馈，完善镇魂祭坛净化系统。

---

## 技术任务

### 2.1 敲击阶段视觉

```
[T] 各敲击阶段独立BlockState
    - 每个岩石有 N 个敲击状态（N = 该岩石敲击次数）
    - 每个状态有独特视觉: 裂纹逐渐加深
    - 例: 暗曜岩4次敲击 = 4个独立BlockState variants
    ⚠️ P0.2确认后实施: 独立JSON模型 或 动态纹理替换
    → data/datagen/RelicBlockStates.java（DataGenerator输出）
    → resources/assets/relictales/blockstates/suspicious_limestone.json
    → resources/assets/relictales/blockstates/suspicious_darkglimmer.json
```

```
[T] 可刷状态视觉
    - 方块进入"可刷"状态时有独特视觉效果
    - 建议: 微弱发光 + 更密集粒子
    → BlockState JSON variant: "brushable"
```

### 2.2 刷取阶段视觉

```
[T] 各刷取阶段的独立视觉
    - 刷取进度可视化: 方块表面尘土逐渐减少
    - 进度阶段数 = 矿物刷取次数（石灰岩2次/暗曜岩3次）
    → BlockState JSON variants: "brush_stage_0" ~ "brush_stage_N"
```

### 2.3 第三形态视觉

```
[T] 第三形态（高价值产出）独特视觉
    - 更强光芒效果
    - 核心物品已露出的视觉效果
    - 持续性聚集粒子
    → BlockState JSON variant: "revealed"
```

### 2.4 音效系统

```
[T] 注册敲击音效
    @SOUND: chisel_hit_limestone_1~3 - 沉积石灰岩敲击3阶段递进音效，各<1s
    @SOUND: chisel_hit_darkglimmer_1~4 - 暗曜岩敲击4阶段递进音效，各<1s
    → registry/sounds/RelicSounds.java
    → content/chisel/ChiselInteraction.java（触发点）
```

```
[T] 注册刷取音效
    @SOUND: brush_sweep_limestone - 沉积石灰岩刷取音效，循环1-2s
    @SOUND: brush_sweep_darkglimmer - 暗曜岩刷取音效，循环1-2s
    → content/brush/BrushInteraction.java（触发点）
```

```
[T] 注册产出发现音效
    @SOUND: relic_discover_high - 发现高价值遗物，神秘庄重感，<2s
    @SOUND: relic_discover_low - 发现低价值物品，轻微失望感，<1s
    → content/brush/BrushInteraction.java（触发点）
```

### 2.5 镇魂祭坛

```
[T] 注册镇魂祭坛方块
    - ID: relictales:soul_altar
    - 合成: 3石头 + 1紫水晶 + 1灵魂土（类似灵魂营火）
    - 外观: 紫色/蓝色火焰 + 祭坛底座
    → registry/blocks/SoulAltarBlock.java
    → registry/blockentities/SoulAltarBlockEntity.java
    → registry/RelicRegisters.java
```

```
[T] 净化交互逻辑
    - 放置遗物 + 放置紫水晶 → 自动开始
    - 净化时间: 30秒（600 ticks）
    - 净化过程动画: 火焰变色 + 粒子聚集 + 紫水晶逐渐消耗
    - 可取消: 净化中玩家右键取出 → 紫水晶消失 → 遗物保持诅咒
    → content/altar/SoulAltarInteraction.java
```

```
[T] 祭坛音效
    @SOUND: soul_altar_activate - 激活音效，火焰变化+低鸣，<3s
    @SOUND: soul_altar_complete - 净化完成，空灵净化感，<2s
```

### 2.6 蠹虫惩罚

```
[T] 蛮力取物→蠹虫生成
    - 触发条件: 无slimeball破坏第三形态方块
    - 概率: 30%
    - 蠹虫按原版AI主动攻击玩家
    → content/brush/BrushInteraction.java
```

---

## 美术资源需求

### 方块贴图 #P0

```
@ARTIST: 镇魂祭坛 - 16x16png，4面各不同（底座+火焰），顶部紫色/蓝色火焰效果
  → textures/block/soul_altar.png
```

### 音效 #P1

```
@SOUND: chisel_hit_limestone_1~3 - 沉积石灰岩敲击3阶段递进（轻微裂纹→密集裂纹→濒临破碎）
@SOUND: chisel_hit_darkglimmer_1~4 - 暗曜岩敲击4阶段递进（深色递进）
@SOUND: brush_sweep_limestone - 刷子扫取沉积石灰岩，砂砾摩擦感
@SOUND: brush_sweep_darkglimmer - 刷子扫取暗曜岩，略带回响
@SOUND: relic_discover_high - 高价值产出发现，神秘/庄重感
@SOUND: relic_discover_low - 低价值产出，轻微失望感
@SOUND: soul_altar_activate - 祭坛激活，火焰燃烧变化+低频共鸣
@SOUND: soul_altar_complete - 净化完成，空灵/净化感
```

### BlockState JSON #P0

```
@ARTIST + @TECH: 需要为以下方块设计BlockState variants:
  - suspicious_limestone: 2敲击状态 + 2刷取进度 + 可刷 + 第三形态 = ~6 variants
  - suspicious_darkglimmer: 4敲击状态 + 3刷取进度 + 可刷 + 第三形态 = ~9 variants
  ⚠️ 每个variant需要对应的模型json和物品模型json
```

---

## 验收标准

1. 每个敲击阶段有独特的视觉效果（裂纹递进）
2. 刷取进度可视化（尘土逐渐减少）
3. 第三形态有明显的"核心露出"视觉
4. 敲击/刷取/产出音效与环境匹配
5. 镇魂祭坛净化流程完整，动画流畅，可取消机制生效
6. 蠹虫惩罚正常触发
