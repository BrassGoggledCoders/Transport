package xyz.brassgoggledcoders.transport.menu.jobsite;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.transport.recipe.IJobSiteRecipe;

public class JobSiteResultSlot<T extends IJobSiteRecipe<T>> extends Slot {
    private final JobSiteMenu<T> jobSiteMenu;

    public JobSiteResultSlot(Container pContainer, int pIndex, int pX, int pY, JobSiteMenu<T> jobSiteMenu) {
        super(pContainer, pIndex, pX, pY);
        this.jobSiteMenu = jobSiteMenu;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack pStack) {
        return false;
    }

    @Override
    public void onTake(@NotNull Player pPlayer, ItemStack pStack) {
        pStack.onCraftedBy(pPlayer.level, pPlayer, pStack.getCount());
        jobSiteMenu.getResultContainer().awardUsedRecipes(pPlayer);
        if (jobSiteMenu.removeInputs()) {
            jobSiteMenu.setupResultSlot();

            jobSiteMenu.getLevelAccess().execute((level, pos) -> {
                long l = level.getGameTime();
                if (jobSiteMenu.getLastSoundTime() != l) {
                    level.playSound(null, pos, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundSource.BLOCKS, 1.0F, 1.0F);
                    jobSiteMenu.setLastSoundTime(l);
                }

            });
            super.onTake(pPlayer, pStack);
        }


    }

}
