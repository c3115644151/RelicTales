# RelicTales（考古物语）

![Minecraft 1.21.4](https://img.shields.io/badge/Minecraft-1.21.4-brightgreen)
![NeoForge 26.1.1.14-beta](https://img.shields.io/badge/NeoForge-26.1.1.14--beta-orange)
![Java 25](https://img.shields.io/badge/Java-25-blue)

> A Vanilla+ archaeology mod — seamlessly extending Minecraft's native archaeology system into existing structures.

## 模组简介

RelicTales（考古物语）是一个以"原生增强"为核心理念的考古 MOD。它不改变世界的岩层结构，也不引入复杂的工具链，而是将"可疑方块"悄悄注入玩家熟悉的原版遗迹（丛林神殿、沙漠神殿、要塞、下界堡垒、海底废墟）中。玩家使用原版刷子即可参与考古，发现并收集来自失落文明的遗物碎片与钱币。

**核心理念：零诅咒、零自定义工具、零世界改动。**

## 核心特性

### 原生遗迹注入
| 目标遗迹 | 注入方块 | 产出主题 |
|---|---|---|
| 丛林神殿 | 可疑的苔石砖 | 自然力量/图腾碎片 |
| 沙漠神殿 | 可疑的切削砂岩 | 太阳文明/黄金饰品 |
| 要塞 | 可疑的石砖 | 虚空记录/末影痕迹 |
| 下界堡垒 | 可疑的下界砖 | 凋零历史/远古合金 |
| 海底废墟 | 可疑的安山岩 | 亚特兰蒂斯/陶罐碎片 |

### 零额外工具负担
玩家直接使用 **Minecraft 原版刷子**（`minecraft:brush`）即可参与考古，无需合成任何自定义工具。

### 快速考古循环
```
进入遗迹 → 发现可疑方块 → 原版刷子刷取 → 获得遗物碎片/钱币
```

## 开发状态

| Milestone | 内容 | 状态 |
|---|---|---|
| M1 | 原型与基础注册 | 🔨 开发中 |
| M2 | 遗迹注入机制 | ⏳ 待开始 |
| M3 | 内容物扩充 | ⏳ 待开始 |
| M4 | 考古工具集与功能遗物 | ⏳ 待开始 |
| M5 | MVP 发布验收 | ⏳ 待开始 |

## 构建命令

```bash
./gradlew build           # 完整构建
./gradlew runClient       # 运行测试客户端
./gradlew runData         # 运行数据生成器（BlockStates / LootTables / Lang）
./gradlew clean build     # 清理并重新构建
```

## 系统要求

- Minecraft: 1.21.4
- NeoForge: 26.1.1.14-beta
- Java: 25（Foojay resolver 自动管理，无需手动配置）

## 项目结构

```
src/main/java/com/relictales/
├── RelicTales.java              # @Mod 主类，EventBus 入口
├── registry/
│   ├── RelicRegisters.java     # 统一注册入口
│   ├── blocks/RelicBlocks.java # 可疑方块（继承 BrushableBlock）
│   ├── items/RelicItems.java   # 遗物/碎片/钱币
│   └── CreativeTab.java         # 创造模式标签
├── content/
│   ├── brushable/              # 刷取回调与掉落判定
│   ├── structure/              # 遗迹注入（StructureModifier）
│   └── loot/                   # 各遗迹专属掉落池定义
├── data/
│   └── datagen/                # 数据生成器
└── client/
    └── RelicTalesClient.java  # 客户端入口（粒子特效）
```

详细设计文档见 [DESIGN_DOC.md](DESIGN_DOC.md)

## 许可

All Rights Reserved
