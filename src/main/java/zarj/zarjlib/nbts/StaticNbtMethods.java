package zarj.zarjlib.nbts;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class StaticNbtMethods {

    static public NbtCompound AttributeToNBT(EntityAttribute attribute, EntityAttributeModifier modifier, EquipmentSlot slot){
        NbtCompound mod = new NbtCompound();

        // AttributeName: "minecraft:generic.attack_damage"
        Identifier attrId = Registries.ATTRIBUTE.getId(attribute);
        mod.putString("AttributeName", attrId.toString());

        mod.putString("Name", modifier.getName());
        mod.putUuid("UUID", modifier.getId());
        mod.putDouble("Amount", modifier.getValue());

        // Operation: 0=ADDITION, 1=MULTIPLY_BASE, 2=MULTIPLY_TOTAL
        int opId;
        if (modifier.getOperation() == EntityAttributeModifier.Operation.ADDITION) opId = 0;
        else if (modifier.getOperation() == EntityAttributeModifier.Operation.MULTIPLY_BASE) opId = 1;
        else opId = 2;
        mod.putInt("Operation", opId);

        // Slot: "mainhand", "chest" и т.д.
        mod.putString("Slot", slot.getName());
        return mod;
    }
    static public double getTotalBaseStatFromAttr(ItemStack stack, EquipmentSlot slot, EntityAttribute attr) {
        double total = attr == EntityAttributes.GENERIC_ATTACK_DAMAGE ? 1 : 0;
        var modifiers = stack.getAttributeModifiers(slot).get(attr);
        for (EntityAttributeModifier mod : modifiers) {
            total += mod.getValue();
        }
        return total;
    }
    static public void testNOTMethod(){

    }
}
