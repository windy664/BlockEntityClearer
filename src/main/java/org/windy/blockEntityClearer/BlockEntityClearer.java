package org.windy.blockEntityClearer;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class BlockEntityClearer extends JavaPlugin implements Listener {

    // 配置项：每个区块最大实体数量和掉落物数量
    private int maxEntitiesPerChunk = 10;
    private int maxItemsPerChunk = 20;

    // 存储每个区块的实体和掉落物数量
    private final Map<String, Integer> chunkEntityCount = new HashMap<>();
    private final Map<String, Integer> chunkItemCount = new HashMap<>();

    @Override
    public void onEnable() {
        // 加载配置文件
        saveDefaultConfig();
        maxEntitiesPerChunk = getConfig().getInt("max-entities-per-chunk", 10);
        maxItemsPerChunk = getConfig().getInt("max-items-per-chunk", 20);

        // 注册事件
        getServer().getPluginManager().registerEvents(this, this);

        // 定时清理多余的实体和掉落物
        new BukkitRunnable() {
            @Override
            public void run() {
                clearExcessEntitiesAndItems();
            }
        }.runTaskTimer(this, 20L, 200L); // 每10秒检查一次
    }

    // 清理掉落物和实体超过限制的区块
    private void clearExcessEntitiesAndItems() {
        for (String chunkKey : chunkEntityCount.keySet()) {
            String[] chunkCoords = chunkKey.split(",");
            int chunkX = Integer.parseInt(chunkCoords[0]);
            int chunkZ = Integer.parseInt(chunkCoords[1]);

            // 获取区块对象
            org.bukkit.Chunk chunk = Bukkit.getWorld("world").getChunkAt(chunkX, chunkZ);  // 假设是世界 "world"，可以替换为实际的世界名称

            int entityCount = chunkEntityCount.get(chunkKey);
            int itemCount = chunkItemCount.get(chunkKey);

            // 如果超过最大实体或掉落物数量，则清除并提示
            if (entityCount > maxEntitiesPerChunk) {
                // 使用 for 循环遍历区块中的实体
                for (Entity entity : chunk.getEntities()) {
                    if (entity instanceof Item) {
                        entity.remove();  // 删除掉落物实体
                    }
                }
                Bukkit.getServer().getLogger().info("区块 " + chunkKey + " 的实体数量超过 " + maxEntitiesPerChunk + "，已清除过多的实体。");
            }

            if (itemCount > maxItemsPerChunk) {
                // 使用 for 循环遍历区块中的实体
                for (Entity entity : chunk.getEntities()) {
                    if (entity instanceof Item) {
                        entity.remove();  // 删除掉落物实体
                    }
                }
                Bukkit.getServer().getLogger().info("区块 " + chunkKey + " 的掉落物数量超过 " + maxItemsPerChunk + "，已清除过多的掉落物。");
            }
        }
    }

    // 监听掉落物的生成
    @EventHandler
    public void onItemSpawn(EntitySpawnEvent event) {
        if (event.getEntity() instanceof Item) {
            String chunkKey = event.getLocation().getChunk().getX() + "," + event.getLocation().getChunk().getZ();
            chunkItemCount.put(chunkKey, chunkItemCount.getOrDefault(chunkKey, 0) + 1);
        }
    }

    // 监听实体的生成
    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        String chunkKey = event.getLocation().getChunk().getX() + "," + event.getLocation().getChunk().getZ();
        chunkEntityCount.put(chunkKey, chunkEntityCount.getOrDefault(chunkKey, 0) + 1);
    }

    // 监听玩家聊天，清空所有区块的统计数据
    @EventHandler
    public void onPlayerChat(PlayerChatEvent event) {
        if (event.getMessage().equalsIgnoreCase("/clearstats")) {
            chunkEntityCount.clear();
            chunkItemCount.clear();
            event.getPlayer().sendMessage("区块统计已重置。");
        }
    }
}
