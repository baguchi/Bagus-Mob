package baguchan.bagusmob.entity.goal;

import baguchan.bagusmob.entity.Modifiger;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

public class ConstructGoal extends Goal {
    private static final String[] STRUCTURE_LOCATION_PORTALS = new String[]{"ruined_portal/portal_1", "ruined_portal/portal_2", "ruined_portal/portal_3", "ruined_portal/portal_4", "ruined_portal/portal_5", "ruined_portal/portal_6", "ruined_portal/portal_7", "ruined_portal/portal_8", "ruined_portal/portal_9", "ruined_portal/portal_10"};

    private final Modifiger mob;
    private final float speedMultiplier;
    private boolean workOver = false;
    private BlockPos maxPos;
    private BlockPos currentBlockPos;
    private StructureTemplate template;
    private StructurePlaceSettings templateSettings;
    private BlockState blockState;
    private Rotation rotation;

    private int step;
    private int buildingTick = 3;


    public ConstructGoal(Modifiger p_25919_, float speedMultiplier) {
        this.mob = p_25919_;
        this.speedMultiplier = speedMultiplier;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    public boolean canUse() {
        return !workOver && ForgeEventFactory.getMobGriefingEvent(this.mob.level(), this.mob) && this.mob.getBuildingPos().isPresent();
    }

    public void start() {
        this.workOver = false;
        BlockPos globalPos = this.mob.blockPosition();
        int i = this.mob.getRandom().nextInt(STRUCTURE_LOCATION_PORTALS.length);
        StructureTemplateManager structuretemplatemanager = this.mob.level().getServer().getStructureManager();
        StructureTemplate structuretemplate = structuretemplatemanager.getOrCreate(ResourceLocation.tryParse(STRUCTURE_LOCATION_PORTALS[i]));

        this.template = structuretemplate;
        Rotation rotation = Rotation.getRandom(this.mob.getRandom());
        ChunkPos chunkpos = new ChunkPos(globalPos);
        BoundingBox boundingbox = new BoundingBox(chunkpos.getMinBlockX() - 16, this.mob.level().getMinBuildHeight(), chunkpos.getMinBlockZ() - 16, chunkpos.getMaxBlockX() + 16, this.mob.level().getMaxBuildHeight(), chunkpos.getMaxBlockZ() + 16);
        StructurePlaceSettings structureplacesettings = (new StructurePlaceSettings()).setRotation(rotation).setBoundingBox(boundingbox).setRandom(this.mob.getRandom());
        Vec3i vec3i = structuretemplate.getSize(rotation);
        this.templateSettings = structureplacesettings;

        BlockPos blockpos1 = new BlockPos(-vec3i.getX() / 2, 0, -vec3i.getZ() / 2);
        BlockPos blockpos2 = new BlockPos(vec3i.getX() / 2, vec3i.getY(), vec3i.getZ() / 2);

        this.maxPos = blockpos2;
        this.rotation = rotation;
    }

    public void tick() {
        Optional<BlockPos> blockPos = this.mob.getBuildingPos();


        if (blockPos.isPresent()) {
            BlockPos blockpos1 = blockPos.get().offset(-maxPos.getX(), 0, -maxPos.getZ());

            BlockPos blockpos2 = template.getZeroPositionWithTransform(blockpos1, Mirror.NONE, this.rotation);

            List<StructureTemplate.StructureBlockInfo> list = templateSettings.getRandomPalette(template.palettes, blockpos2).blocks().stream().filter(info -> {
                return !info.state().is(Blocks.STRUCTURE_VOID) && !(mob.level().getBlockState(blockPos.get().offset(info.pos())).isAir() && info.state().isAir());
            }).toList();
            if (step > list.size() - 1) {
                workOver = true;
                return;
            }
            if (this.currentBlockPos == null) {
                StructureTemplate.StructureBlockInfo structureBlockInfo = list.get(step);
                BlockPos origin = blockPos.get().offset(structureBlockInfo.pos());
                if (isReplaceable(this.mob.level().getBlockState(origin), mob.level(), mob)) {
                    blockState = structureBlockInfo.state();
                    currentBlockPos = origin;
                    step += 1;
                } else {
                    step += 1;
                }
            }
            this.mob.getNavigation().moveTo(blockPos.get().getX(), blockPos.get().getY(), blockPos.get().getZ(), this.speedMultiplier);

            if (--this.buildingTick < 0) {
                if (currentBlockPos != null) {

                    if (blockPos.get().distSqr(mob.blockPosition()) < 32F) {
                        if (isReplaceable(this.mob.level().getBlockState(currentBlockPos), this.mob.level(), mob)) {
                            if (blockState != null && !blockState.isAir() && blockState.getFluidState().isEmpty()) {
                                SoundType soundType = blockState.getSoundType();
                                this.mob.level().playSound(null, currentBlockPos, soundType.getPlaceSound(), SoundSource.BLOCKS, soundType.getVolume(), blockState.getSoundType().getPitch());
                            }
                            BlockState realState = blockState.mirror(templateSettings.getMirror()).rotate(templateSettings.getRotation());

                            Block.pushEntitiesUp(this.mob.level().getBlockState(currentBlockPos), realState, this.mob.level(), currentBlockPos);
                            this.mob.level().setBlock(currentBlockPos, realState, 3);

                            currentBlockPos = null;
                        } else {
                            currentBlockPos = null;
                        }
                    }
                }
                this.buildingTick = 4;
            }
        }
    }

    @Override
    public void stop() {
        super.stop();
        this.mob.setBuildingPos(Optional.empty());
    }

    private boolean isReplaceable(BlockState blockState, Level level, PathfinderMob mob) {
        return !blockState.is(BlockTags.FEATURES_CANNOT_REPLACE) && ForgeEventFactory.getMobGriefingEvent(level, mob);
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

}