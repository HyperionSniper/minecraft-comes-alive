package net.mca.client.render.layer;

import net.mca.client.gui.immersive_library.SkinCache;
import net.mca.client.model.CommonVillagerModel;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public class ClothingLayer<T extends LivingEntity, M extends BipedEntityModel<T>> extends VillagerLayer<T, M> {
    private final String variant;

    public ClothingLayer(FeatureRendererContext<T, M> renderer, M model, String variant) {
        super(renderer, model);
        this.variant = variant;
    }

    @Override
    public Identifier getSkin(T villager) {
        String v = CommonVillagerModel.getVillager(villager).isBurned() ? "burnt" : variant;
        String identifier = CommonVillagerModel.getVillager(villager).getClothes();
        if (identifier.startsWith("immersive_library:")) {
            return SkinCache.getTextureIdentifier(Integer.parseInt(identifier.substring(18)));
        }
        return cached(identifier + v, clothes -> {
            Identifier id = new Identifier(CommonVillagerModel.getVillager(villager).getClothes());

            Identifier idNew = new Identifier(id.getNamespace(), id.getPath().replace("normal", v));
            if (canUse(idNew)) {
                return idNew;
            }

            return id;
        });
    }
}
