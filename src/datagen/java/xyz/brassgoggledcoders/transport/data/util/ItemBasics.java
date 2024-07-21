package xyz.brassgoggledcoders.transport.data.util;

import net.minecraft.world.item.Item;
import xyz.brassgoggledcoders.shadyskies.dataregistering.DataRegistering;
import xyz.brassgoggledcoders.shadyskies.dataregistering.language.TranslationProvider;
import xyz.brassgoggledcoders.shadyskies.dataregistering.provider.ProviderTypes;
import xyz.brassgoggledcoders.shadyskies.dataregistering.registering.DataRegisteringEntry;
import xyz.brassgoggledcoders.shadyskies.dataregistering.util.StringHelper;
import xyz.brassgoggledcoders.shadyskies.registering.item.ItemLikeEntry;

public class ItemBasics {

    public static <T extends ItemLikeEntry<U, Item>, U extends Item> void defaultItem(
            DataRegistering dataRegistering,
            DataRegisteringEntry<T, U, Item> dataEntry
    ) {
        defaultLang(dataRegistering, dataEntry);
        defaultModel(dataRegistering, dataEntry);
    }

    public static <T extends ItemLikeEntry<U, Item>, U extends Item> void defaultLang(
            DataRegistering dataRegistering,
            DataRegisteringEntry<T, U, Item> dataEntry
    ) {
        dataRegistering.getProvider(TranslationProvider.TYPE)
                .addTranslation(
                        "item",
                        dataEntry.getRegisteringEntry()
                                .getId(),
                        StringHelper.createName(dataEntry.getRegisteringEntry()
                                .getId()
                                .getPath()
                        )
                );
    }

    public static <T extends ItemLikeEntry<U, Item>, U extends Item> void defaultModel(
            DataRegistering dataRegistering,
            DataRegisteringEntry<T, U, Item> dataEntry
    ) {
        dataEntry.withDeferredProvider(
                ProviderTypes.ITEM_MODELS,
                (entry, provider) -> provider.basicItem(entry.getId())
        );
    }
}
