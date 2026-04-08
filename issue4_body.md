# M4: 陵墓、机关与开棺（Tomb & Trap）

**里程碑：** Milestone 4
**到期日：** 2026-09-29
**依赖：** M1+M2 完成

---

## 目标

扩展岩石系统至全5层，完善世界生成结构，陵墓结构与机关系统。

---

## 技术任务

### 4.1 扩展岩石系统

```
[T] 注册另外三种考古岩石
    - 沉积页岩: Y -10~20, 敲3次+刷3次
      ID: relictales:shale / relictales:suspicious_shale
    - 沉积板岩: Y -30~0, 敲4次+刷3次
      ID: relictales:slate / relictales:suspicious_slate
    - 幽晶岩: Y -40~-25, 敲5次+刷4次, 高价值30%
      ID: relictales:phantomite / relictales:suspicious_phantomite
      ⚠️ P0.4 Y轴边界待最终确认
    → registry/blocks/RelicBlocks.java（新增3种岩石）
    → content/rock/RockType.java（新增3个枚举）
    → content/rock/RockDefinition.java（新增3个定义）
```

```
[T] 更新斑状生成配置
    - 5种岩石各自Y轴区间的生成配置
    - 每个chunk生成数量调优
    → world/features/SedimentRockFeature.java
```

### 4.2 陵墓结构（可选，规模较大）

```
[T] Jigsaw陵墓结构
    - 结构组成: 入口走廊 → 主墓室 → 侧室 × N → 棺材间
    - 使用NeoForge Jigsaw系统注册
    → world/structures/TombPieces.java
    → world/structures/TombStructure.java
```

```
[T] 陵墓生成配置
    - 生成条件: Y < 20，在岩石层中生成
    - 生成频率: 较低（稀有结构）
    - 生物群系偏好: 恶地/沙漠（未来扩展）
    → world/biome/TombBiomeModifier.java
```

### 4.3 机关系统

```
[T] 陵墓机关触发机制
    - 压力板机关: 踩上触发，发送箭矢
    - 绊线机关: 经过时触发，激活陷阱
    - 连体沉积岩坍塌: 破坏相邻方块，模拟墓室坍塌
    → content/trap/TombTrapRegistry.java
    → content/trap/PressurePlateTrap.java
    → content/trap/CollapsingRockTrap.java
```

```
[T] 光敏飞箭陷阱
    - 触发条件: 光照变化（玩家持火把经过）
    - 效果: 箭矢从墙壁射出
    → content/trap/ArrowTrap.java
```

### 4.4 开棺系统

```
[T] 棺材交互逻辑
    - 使用錾子撬开棺材（非破坏）
    - 触发密室战斗（如果陵墓有敌意生成配置）
    → content/interaction/CoffinInteraction.java
```

```
[T] 开棺奖励
    - 保底稀有遗物掉落
    - 特殊奖励（金钱/经验/独特物品）
    → data/loot/TombLootTables.java
```

### 4.5 结构内稀有遗物保底

```
[T] 陵墓内遗物生成
    - 保底机制: 每个陵墓至少生成1个稀有+遗物
    - 位置: 棺材间或主墓室
    → content/loot/TombLootModifier.java
```

---

## 美术资源需求

### 方块贴图 #P1

```
@ARTIST: 沉积页岩 - 16x16png，中层页状沉积岩，纹理清晰分层
  → textures/block/shale.png

@ARTIST: 可疑的沉积页岩 - 同上，需有可疑暗示
  → textures/block/suspicious_shale.png

@ARTIST: 沉积板岩 - 16x16png，中深层板岩，压实度高
  → textures/block/slate.png

@ARTIST: 可疑的沉积板岩 - 同上，需有可疑暗示
  → textures/block/suspicious_slate.png

@ARTIST: 幽晶岩 - 16x16png，幽蓝色晶体纹理，极深层级感
  → textures/block/phantomite.png

@ARTIST: 可疑的幽晶岩 - 同上，需有可疑暗示
  → textures/block/suspicious_phantomite.png
```

### 陵墓结构贴图 #P2

```
@ARTIST: 陵墓石砖 - 16x16png，古旧风化石砖
  → textures/block/tomb_brick.png

@ARTIST: 陵墓装饰砖 - 16x16png，带壁画/雕刻纹理
  → textures/block/tomb_decorative.png

@ARTIST: 石棺 - 16x16png，石制棺材外观
  → textures/block/coffin.png

@ARTIST: 青铜灯 - 16x16png，古旧青铜灯笼
  → textures/block/bronze_lamp.png
```

### 机关视觉效果 #P2

```
@ARTIST: 压力板机关 - 触发前/触发后两状态
@ARTIST: 绊线 - 隐形绊线贴图
@ARTIST: 箭矢陷阱 - 发射口贴图
```

---

## 验收标准

1. 5种岩石在各自Y轴区间正常生成，有粒子标识
2. 敲击/刷取次数与各岩石设计匹配
3. 陵墓结构可生成（如果实现）
4. 机关触发机制正常运作
5. 开棺获得保底稀有遗物
6. 蠹虫惩罚与原版一致攻击玩家
