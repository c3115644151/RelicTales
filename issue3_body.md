# M3: 盲盒、鉴定与诅咒（Relic Mystery & Curse）

**里程碑：** Milestone 3
**到期日：** 2026-08-30
**依赖：** M1+M2 完成

---

## 目标

遗物系统 + 诅咒系统完整实现，鉴定桌揭示遗物信息，诅咒视觉/音效反馈完整。

---

## 技术任务

### 3.1 遗物物品系统

```
[T] 遗物基类
    - 属性: relicId, rarity, isCleansed, relicData(NBT)
    - 稀有度: 普通/稀有/传说/史诗（决定诅咒积累速度）
    → content/relic/RelicRarity.java
    → content/relic/RelicItem.java
```

```
[T] 盲盒遗物剪影
    - 未知遗物显示为模糊剪影，不显示具体物品
    - 持有时根据稀有度显示对应颜色光晕
    → content/relic/UnknownRelicItem.java
```

```
[T] 遗物NBT数据结构
    - 存储: 遗物ID, 稀有度, 净化状态, 诅咒值
    - 用于跨维度持久化和网络同步
    → content/relic/RelicData.java
```

### 3.2 诅咒系统

```
[T] Player Capability 存储诅咒值
    - 诅咒值范围: 0~100
    - 跨维度持久化
    → content/curse/CurseManager.java
    → registry/RelicRegisters.java（Capability注册）
```

```
[T] 诅咒积累/衰减逻辑
    - 持有遗物时积累: 普通+0.05/秒, 稀有+0.1/秒, 传说+0.15/秒, 史诗+0.2/秒
    - 无遗物时衰减: -0.05/秒
    - 持有净化后遗物: -0.01/秒
    → content/curse/CurseManager.java（积累衰减逻辑）
```

```
[T] 诅咒阶梯惩罚（5阶段）
    - 0-30: 轻微暗角 + 微弱低频嗡鸣（安静可闻）
    - 31-50: 暗角加深 + 偶尔粒子 + 随机方向低语
    - 51-70: 暗角脉动 + 清晰耳语 + 隐约脚步 + 5%/分钟假敌意检测
    - 71-85: 深色暗角 + 持续脚步 + 幽灵化物品图标 + 幻觉追影
    - 86-100: 几乎全屏暗角 + 尖锐耳鸣 + 假受伤体验
    → content/curse/CurseLevel.java
    → content/curse/CurseEffects.java
```

```
[T] 客户端诅咒特效（client-only）
    - 屏幕边缘暗角shader（强度随诅咒值变化）
    - 幻觉追影（71-85区间，左肩方向暗影幻觉）
    - 粒子飘过视野
    → client/RelicTalesClient.java
    → client/renderer/CurseEffectsRenderer.java
    ⚠️ 客户端特效，不在content/层
```

```
[T] 诅咒音效
    @SOUND: curse_whisper_common - 普通诅咒低语，隐约耳语，循环低音量
    @SOUND: curse_whisper_rare - 稀有诅咒低语，更清晰
    @SOUND: curse_footsteps - 诅咒足音，跟随感，循环
    @SOUND: curse_phantom_call - 呼唤名字幻觉音效，触发性<1s
    → client/clientSetup/SoundRegistry.java
    → content/curse/CurseEffects.java（触发点）
```

### 3.3 鉴定系统

```
[T] 鉴定桌方块
    - ID: relictales:identification_table
    - 合成配方: 待设计（建议: 2钻石 + 1红石 + 4木板等）
    → registry/blocks/IdentificationTableBlock.java
    → registry/blockentities/IdentificationTableBlockEntity.java
```

```
[T] 鉴定逻辑
    - 右键鉴定桌: 显示遗物信息（名称/稀有度/当前诅咒值）
    - 鉴定不解除诅咒
    - 鉴定有成本（建议: 1绿宝石）
    → content/identification/IdentificationBehavior.java
```

### 3.4 整合包兼容

```
[T] EMI/REI 合成路线支持
    - 工具合成配方注册
    - 祭坛净化配方注册
    → registry/recipes/RelicRecipes.java
```

```
[T] Curios API 集成
    - 遗物可装备到饰品栏
    - 诅咒值与饰品栏遗物关联
    → build.gradle 添加 Curios 依赖
    → content/relic/RelicItem.java（Curios插槽集成）
```

---

## 美术资源需求

### 方块贴图 #P1

```
@ARTIST: 鉴定桌 - 16x16png，专业感设计，带透镜/天平/试剂暗示
  → textures/block/identification_table.png
```

### 物品图标 #P1

```
@ARTIST: 普通遗物图标 - 16x16png，灰色标记基础遗物
@ARTIST: 稀有遗物图标 - 16x16png，蓝色标记
@ARTIST: 传说遗物图标 - 16x16png，橙色/金色标记
@ARTIST: 史诗遗物图标 - 16x16png，紫色标记
  → textures/item/relic_common.png
  → textures/item/relic_rare.png
  → textures/item/relic_legendary.png
  → textures/item/relic_epic.png
```

### 诅咒特效 #P1

```
@ARTIST: 诅咒视觉暗角shader - GLSL或混合模式，实现屏幕边缘渐变暗角
@ARTIST: 幻觉追影粒子 - 飘浮暗影碎片粒子效果
```

### 音效 #P2

```
@SOUND: curse_whisper_common - 普通诅咒环境低语
@SOUND: curse_whisper_rare - 稀有诅咒环境低语（更清晰）
@SOUND: curse_footsteps - 诅咒足音（持续跟随）
@SOUND: curse_phantom_call - 幻觉呼唤名字（随机触发）
```

---

## 验收标准

1. 遗物有4级稀有度视觉标识
2. 盲盒遗物显示剪影，不可见具体信息
3. 持有遗物时诅咒值持续积累，屏幕暗角随诅咒加深
4. 5个诅咒阶段有明确的视觉/音效/机制差异
5. 幻觉追影在71-85诅咒值区间正常触发
6. 鉴定桌正确揭示遗物信息
7. 镇魂祭坛净化成功移除诅咒状态
8. EMI/REI显示相关合成路线
