# RelicTales（考古物语）

![Minecraft 26.1.1](https://img.shields.io/badge/Minecraft-26.1.1-brightgreen)
![NeoForge 26.1.1.14-beta](https://img.shields.io/badge/NeoForge-26.1.1.14--beta-orange)
![Java 25](https://img.shields.io/badge/Java-25-blue)

> A Vanilla+ archaeology mod featuring excavation, curse dynamics, and relic awakening.

## 模组简介

RelicTales（考古物语）是一个沉浸式考古主题 MOD。玩家在世界深层古老地层中，通过錾子与刷子的传统工具组合，发掘埋藏的遗物与化石。每件遗物都承载着古老的诅咒，持有越久诅咒越深，最终需要通过镇魂祭坛净化。

## 核心特性

### 工具系统
- **錾子**（单一类型）- 敲击硬质考古方块，揭示内部遗物
- **刷子**（五级：铜/铁/金/钻石/下界合金）- 刷去尘土，露出珍贵产出

### 岩石地层（5层）
| 岩石 | 深度 | 难度 |
|------|------|------|
| 沉积石灰岩 | Y 5~30 | ★★☆☆☆ |
| 沉积页岩 | Y -10~20 | ★★★☆☆（未来） |
| 沉积板岩 | Y -30~0 | ★★★☆☆（未来） |
| 暗曜岩 | Y -60~-40 | ★★★★☆ |
| 幽晶岩 | Y -40~-25 | ★★★★★（未来） |

### 诅咒系统
- 持有遗物时诅咒值持续积累
- 五阶段递进惩罚（视觉暗角 → 幻觉追影 → 假受伤体验）
- 镇魂祭坛净化系统

## 开发状态

| Milestone | 状态 |
|-----------|------|
| M1: 基石与分级系统 | 🔨 开发中 |
| M2: 沉浸式发掘与提取 | ⏳ 待开始 |
| M3: 盲盒、鉴定与诅咒 | ⏳ 待开始 |
| M4: 陵墓、机关与开棺 | ⏳ 待开始 |
| M5: 古王挑战与遗物觉醒 | ⏳ 待开始 |

## 构建命令

```bash
# 完整构建
./gradlew build

# 开发环境运行客户端
./gradlew runClient

# 快速编译检查
./gradlew build --offline

# 数据生成（BlockStates/ItemModels/Lang）
./gradlew runData
```

## 系统要求

- Minecraft: 26.1.1
- NeoForge: 26.1.1.14-beta
- Java: 25（由 Foojay resolver 自动管理）

## 项目结构

```
src/main/java/com/relictales/
├── RelicTales.java          # @Mod 主类
├── registry/                # DeferredRegister 聚合
│   └── RelicRegisters.java  # 统一注册入口
├── content/                  # 业务逻辑层（纯Java）
│   ├── chisel/              # 錾子系统
│   ├── brush/               # 刷子系统
│   ├── rock/                # 岩石类型定义
│   ├── curse/               # 诅咒系统（M3）
│   └── relic/               # 遗物系统（M3）
└── client/                   # 客户端渲染/音效
```

详细设计文档见 [DESIGN_DOC.md](DESIGN_DOC.md)

## 团队

- **制作人**: 考古物语制作组
- **美术**: 待定
- **音效/渲染**: 待定

## 许可

All Rights Reserved
