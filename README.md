<div align="center">

<img src="assets/banner.svg" alt="BlockEntityClearer" width="100%">

# BlockEntityClearer

**区块实体清理** —— 按区块统计方块实体与掉落物数量，超出上限自动清理，给Mod 服一个简单粗暴的卡服保险丝。

![Platform](https://img.shields.io/badge/Platform-Spigot%20%2F%20Paper-62bda8?style=flat-square&logo=minecraft&logoColor=white)
![Minecraft](https://img.shields.io/badge/MC-1.20.x-8a9a5b?style=flat-square)
![Java](https://img.shields.io/badge/Java-17-f89820?style=flat-square&logo=openjdk&logoColor=white)

[功能](#-功能) · [配置](#️-配置)

</div>

## ✨ 功能

- **按区块计数**：实体放置/生成、物品掉落时按区块 key 统计（`chunkX,chunkZ`），不做全服扫描
- **双上限**：`max-entities-per-chunk` 与 `max-items-per-chunk` 分别控制方块实体和掉落物
- **超限处理**：超出上限的生成/掉落被拦截或清理，防止单区块堆积拖垮 TPS
- **统计重置**：管理操作可清零区块统计（收到"区块统计已重置"确认）

## ⚙️ 配置

```yaml
max-entities-per-chunk: 10
max-items-per-chunk: 20
```

## 🔨 构建

```bash
./gradlew build
```
