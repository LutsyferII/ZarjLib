package zarj.zarjlib.drops;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;

public class DropChanges {


    private final Map<Item, Double> ALL_ENTRIES = new HashMap<>();
    private boolean registered = false;

    public DropChanges(){

    }
    public DropChanges(Map<Item, Double> baseMap){
        ALL_ENTRIES.putAll(baseMap);
    }

    public void addDrop(Item item, double bonusChance) {
        if (registered) {
            System.err.println("[ZarjLib] ОШИБКА: Попытка добавить предмет " + item.toString() + " ПОСЛЕ того, как регистрация дропа уже завершена!");
            return; // Просто уходим, не трогая мапу
        }
        ALL_ENTRIES.put(item, bonusChance);
    }
    public void ensureAllRegistered() {
        if (registered) return;
        registered = true;

        ServerLivingEntityEvents.AFTER_DEATH.register((entity, source) -> {
            if (entity.getWorld().isClient) return; // Только сервер
            if (entity instanceof PlayerEntity) return;

            // Характеристики моба
            double maxHealth = entity.getMaxHealth();
            // double armor = entity.getAttributeValue(EntityAttributes.GENERIC_ARMOR);
            double attackDamage = 0.0;
            if (entity.getAttributes().hasAttribute(EntityAttributes.GENERIC_ATTACK_DAMAGE)) {
                attackDamage = entity.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
            }

            double baseChance = (maxHealth * 0.05 + attackDamage * 0.2) / 100.0;
            // Убийца — игрок?
            if (source.getAttacker() instanceof ServerPlayerEntity player) {
                baseChance += player.getLuck() * 0.01;
            }
            for (Map.Entry<Item, Double> entry : ALL_ENTRIES.entrySet()) {
                if (entity.getWorld().random.nextDouble() < baseChance + entry.getValue()) {
                    ItemStack dropStack = new ItemStack(entry.getKey()); // или что там дропается
                    ItemEntity itemEntity = new ItemEntity(
                            entity.getWorld(),
                            entity.getX(), entity.getY() + 0.5, entity.getZ(),
                            dropStack
                    );
                    itemEntity.setPickupDelay(10); // Небольшая задержка на подбор
                    entity.getWorld().spawnEntity(itemEntity);
                }
            }
        });
    }
}
