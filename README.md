
# ZarjLib 📚

**Библиотека утилит для разработки Minecraft модов** на Fabric.

ZarjLib содержит переиспользуемые компоненты и системы, вынесенные из моих модов.

## Включённые модули

### 🎲 DropChanges — Система модификации дропа

Динамическая система изменения дропа мобов на основе их характеристик.

**Возможности:**
- Регистрация кастомных предметов с модификаторами шанса
- Автоматический расчёт базового шанса на основе:
  - Максимального здоровья моба
  - Урона атаки
  - Удачи игрока (killer's luck)
- Event-driven архитектура (ServerLivingEntityEvents)

**Использование:**
```java
// Создай систему
DropChanges dropSystem = new DropChanges();

// Зарегистрируй предметы с модификаторами
dropSystem.addDrop(MyItems.RARE_GEM, 2.0);  // x2 шанс
dropSystem.addDrop(MyItems.EPIC_WEAPON, 0.5); // x0.5 шанс

// Запусти систему (один раз при инициализации мода)
dropSystem.ensureAllRegistered();

// Формула шанса;
baseChance = (mob.maxHealth * 0.05 + mob.attackDamage * 0.2) / 100.0
if (killer instanceof Player):
    baseChance += player.luck * 0.01

finalChance = baseChance * itemModifier
