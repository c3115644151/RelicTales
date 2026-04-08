# M5: 古王挑战与遗物觉醒（Ancient King & Relic Awakening）

**里程碑：** Milestone 5
**到期日：** 2026-10-30
**依赖：** M3+M4 完成

---

## 目标

古王怨灵Boss战完整实现，遗物觉醒终局机制，Boss战诅咒强化。

---

## 技术任务

### 5.1 Boss实体

```
[T] 古王怨灵实体注册
    - ID: relictales:ancient_king
    - 实体类型: 亡灵类怨灵（类似唤魔者/女妖）
    - 属性: 高血量, 飞行能力, 诅咒攻击
    → registry/entities/RelicEntities.java
    → content/boss/AncientKingEntity.java
```

```
[T] Boss AI状态机
    - 阶段1-追击: 追踪玩家, 远程诅咒弹攻击
    - 阶段2-召唤: 召唤小怨灵（骷髅/僵尸变种）
    - 阶段3-觉醒: 血量<30%时, 攻击加剧, 召唤幻影
    - 使用NeoForge Goals + Sensors系统
    → content/boss/AncientKingAI.java
    → content/boss/AncientKingGoals.java
    → content/boss/AncientKingSensors.java
```

```
[T] Boss战斗区域
    - Boss房生成: 独立结构, 只有入口
    - 战斗开始触发: 进入Boss房
    → world/structures/AncientKingArena.java
```

### 5.2 Boss战诅咒强化

```
[T] 诅咒追击惩罚
    - Boss战期间: 诅咒值上限从100提升至150
    - 幻觉追影加剧: 多个幻影同时跟随
    - 假受伤体验触发率提高
    → content/boss/AncientKingCurseBoost.java
```

### 5.3 遗物觉醒

```
[T] 觉醒触发机制
    - 条件: Boss战胜利后
    - 效果: 背包中净化后的遗物激活功能
    → content/relic/RelicAwakening.java
```

```
[T] 觉醒特效
    - 视觉: 遗物发光 + 粒子爆发
    - 音效: 觉醒音效（空灵/力量感）
    → client/renderer/RelicAwakeningRenderer.java
    → content/relic/RelicAwakeningEffects.java
```

```
[T] 觉醒遗物功能
    - 普通遗物: 被动小buff（挖掘速度+5%等）
    - 稀有遗物: 主动技能（短距离传送等）
    - 传说遗物: 强力被动（抗性提升等）
    - 史诗遗物: 独特终极技能
    ⚠️ 遗物具体功能设计延后至M3阶段
    → content/relic/RelicFunctions.java
```

### 5.4 Boss掉落

```
[T] Boss掉落遗物
    - 必掉: 1个独特Boss遗物（不可净化，诅咒值固定极高）
    - 随机掉落: 普通遗物 × 1-2
    → data/loot/AncientKingLootTables.java
```

---

## 美术资源需求

### Boss实体贴图 #P0

```
@ARTIST: 古王怨灵实体 - 64x64png，怨灵/幽灵王形象，多形态（追击/召唤/觉醒）
  → textures/entity/ancient_king.png
  → textures/entity/ancient_king_awakened.png

@ARTIST: 古王小怨灵 - 32x32png，召唤小兵
  → textures/entity/ancient_king_minion.png
```

### Boss战特效 #P1

```
@ARTIST: Boss攻击特效 - 诅咒弹粒子，深紫色幽魂弹
@ARTIST: Boss召唤特效 - 传送门/灵魂漩涡效果
@ARTIST: 觉醒特效 - 遗物激活光效，紫色/金色光芒爆发
@ARTIST: Boss房氛围 - 特殊迷雾/光照效果
```

### 音效 #P1

```
@SOUND: ancient_king_ambient - Boss战环境音，低沉压迫
@SOUND: ancient_king_attack - Boss诅咒弹攻击
@SOUND: ancient_king_summon - 召唤小怪音效
@SOUND: ancient_king_awaken - Boss觉醒音效，力量释放
@SOUND: ancient_king_death - Boss死亡音效
@SOUND: relic_awaken - 遗物觉醒音效
```

### 觉醒遗物图标 #P2

```
@ARTIST: Boss掉落遗物图标 - 独特外观，与普通遗物区分
  → textures/item/relic_ancient_king.png
```

---

## 验收标准

1. Boss实体正常生成在Boss房
2. 三个AI阶段正常切换
3. Boss战胜利后遗物正常觉醒
4. 觉醒遗物功能激活
5. Boss掉落正确（独特遗物 + 随机普通遗物）
6. Boss战诅咒强化正常生效
7. 诅咒上限在Boss战期间提升至150
