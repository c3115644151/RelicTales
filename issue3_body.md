# M3: 遗物系统与奖励池扩充

**里程碑：** Milestone 3
**依赖：** M1+M2 完成

## 目标
将模组从"1种方块"扩充为"全遗迹覆盖"，并建立丰富多样的遗物与战利品体系。

## 任务清单

### 3.1 遗迹方块全量注入
- [ ] `suspicious_chiseled_sandstone` → 沙漠神殿遗迹
- [ ] `suspicious_stone_bricks` → 要塞遗迹
- [ ] `suspicious_nether_bricks` → 下界堡垒遗迹
- [ ] `suspicious_polished_andesite` → 海底废墟遗迹

### 3.2 遗物物品注册（已选定）

| 遗迹 | 遗物 ID | 遗物名称 | 核心功能 |
|---|---|---|---|
| 丛林神殿 | `jungle_hunter_feather` | 丛林猎羽符 | 免疫摔落 + 弓弩射速 +10% |
| 沙漠神殿 | `sun_god_badge` | 太阳神徽章 | 右键 30 秒夜视（CD 120s）+ 阳光下移速 +10% |
| 末地城 | `void_record_fragment` | 虚空记录碎片 | 附魔等级 +1 + 经验值获取 +15% |
| 下界堡垒 | `wither_charm` | 凋零护符 | 免疫凋零效果 + 对亡灵伤害 +15% |
| 海底废墟 | `atlantis_pot_heart` | 亚特兰蒂斯陶罐之心 | 水中生命恢复 ×2 + 游泳速度 +20% |

**技术要求：** 每个遗物必须实现以下接口：
- [ ] `Holder<Item>` 注册到 `RelicItems`
- [ ] 实现 `EquipmentSupplier` 或 `CuriosSlot` 饰品栏装备能力
- [ ] 通过 `AttributeModifier` 实现数值加成
- [ ] 通过 `Item#use` 实现右键激活功能（夜视等）
- [ ] 注册 20+ 种遗物碎片（按遗迹主题分类）
- [ ] 注册 5 种古老钱币（可交易物品）
- [ ] 注册 3 种稀有遗物（功能性待 M4）

### 3.3 奖励池配置
- [ ] 为每个遗迹定制 LootTable（DataGen 自动生成）
- [ ] 配置稀有度权重（普通/稀有/传说/史诗）

## ✅ 验收标准
每个遗迹刷取出的物品主题鲜明，数量分布合理。
