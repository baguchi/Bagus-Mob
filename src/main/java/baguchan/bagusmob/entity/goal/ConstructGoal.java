package baguchan.bagusmob.entity.goal;

import baguchan.bagusmob.entity.Modifiger;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.Clearable;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import static net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.processBlockInfos;

public class ConstructGoal extends Goal {
    private final String[] structures;

    private final Modifiger mob;
    private final float speedMultiplier;
    private boolean workOver = false;
    private BlockPos maxPos;
    private BlockPos currentBlockPos;
    private StructureTemplate template;
    private StructurePlaceSettings templateSettings;
    private BlockState blockState;
    private Rotation rotation;
    private CompoundTag compoundTag;

    private int step;
    private int buildingTick = 4;


    public ConstructGoal(Modifiger p_25919_, String[] structures, float speedMultiplier) {

        this.mob = p_25919_;
        this.structures = structures;
        this.speedMultiplier = speedMultiplier;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    public boolean canUse() {
        return ForgeEventFactory.getMobGriefingEvent(this.mob.level(), this.mob) && this.mob.getBuildingPos().isPresent();
    }

    @Override
    public boolean canContinueToUse() {
        return !workOver && super.canContinueToUse();
    }

    public void start() {
        this.workOver = false;
        Optional<BlockPos> globalPos = this.mob.getBuildingPos();
        int i = this.mob.getRandom().nextInt(structures.length);
        StructureTemplateManager structuretemplatemanager = this.mob.level().getServer().getStructureManager();

        ResourceLocation resourceLocation = ResourceLocation.tryParse(structures[i]);

        if (this.mob.getBuildingStructureName() != null) {
            resourceLocation = this.mob.getBuildingStructureName();
            this.step = this.mob.getBuildingStep();
        } else {
            this.mob.setBuildingStructureName(resourceLocation);
        }

        StructureTemplate structuretemplate = structuretemplatemanager.getOrCreate(resourceLocation);

        this.template = structuretemplate;
        Rotation rotation = Rotation.getRandom(this.mob.getRandom());
        ChunkPos chunkpos = new ChunkPos(globalPos.get());
        BoundingBox boundingbox = new BoundingBox(chunkpos.getMinBlockX() - 16, this.mob.level().getMinBuildHeight(), chunkpos.getMinBlockZ() - 16, chunkpos.getMaxBlockX() + 16, this.mob.level().getMaxBuildHeight(), chunkpos.getMaxBlockZ() + 16);
        StructurePlaceSettings structureplacesettings = (new StructurePlaceSettings()).setRotation(rotation).addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK).setIgnoreEntities(false).setFinalizeEntities(true).setBoundingBox(boundingbox).setRandom(this.mob.getRandom());
        Vec3i vec3i = structuretemplate.getSize(rotation);
        this.templateSettings = structureplacesettings;

        BlockPos blockpos1 = new BlockPos(-vec3i.getX() / 2, 0, -vec3i.getZ() / 2);
        BlockPos blockpos2 = new BlockPos(vec3i.getX() / 2, vec3i.getY(), vec3i.getZ() / 2);

        this.maxPos = blockpos2;
        this.rotation = rotation;
    }

    public void tick() {
        Optional<BlockPos> blockPos = this.mob.getBuildingPos();

        if (this.mob.level() instanceof ServerLevel serverLevel) {
            if (blockPos.isPresent()) {
                BlockPos blockpos1 = blockPos.get().offset(-maxPos.getX(), 0, -maxPos.getZ());

                BlockPos blockpos2 = template.getZeroPositionWithTransform(blockpos1, Mirror.NONE, this.rotation);

                List<StructureTemplate.StructureBlockInfo> list = templateSettings.getRandomPalette(template.palettes, blockpos2).blocks();

                List<StructureTemplate.StructureBlockInfo> list2 = processBlockInfos(serverLevel, blockpos2, blockpos2, this.templateSettings, list, this.template).stream().filter(info -> !info.state().is(Blocks.JIGSAW) && (!(mob.level().getBlockState(blockPos.get().offset(info.pos())).isAir() && info.state().isAir())) || compoundTag != null).toList();
                List<Pair<BlockPos, CompoundTag>> list3 = Lists.newArrayListWithCapacity(list.size());

                if (step > list2.size() - 1) {
                    workOver = true;
                    return;
                }
                if (this.currentBlockPos == null) {
                    StructureTemplate.StructureBlockInfo structureBlockInfo = list2.get(step);
                    BlockPos origin = structureBlockInfo.pos();
                    if (isReplaceable(serverLevel.getBlockState(origin), serverLevel, mob)) {
                        blockState = structureBlockInfo.state();
                        currentBlockPos = origin;
                        compoundTag = structureBlockInfo.nbt();
                        step += 1;
                    } else {
                        step += 1;
                    }
                }
                this.mob.getNavigation().moveTo(blockPos.get().getX(), blockPos.get().getY(), blockPos.get().getZ(), this.speedMultiplier);

                if (--this.buildingTick < 0) {
                    if (currentBlockPos != null) {

                        if (blockPos.get().distSqr(mob.blockPosition()) < 32F) {
                            if (isReplaceable(serverLevel.getBlockState(currentBlockPos), serverLevel, mob)) {
                                if (blockState != null && !blockState.isAir() && blockState.getFluidState().isEmpty()) {
                                    SoundType soundType = blockState.getSoundType();
                                    serverLevel.playSound(null, currentBlockPos, soundType.getPlaceSound(), SoundSource.BLOCKS, soundType.getVolume(), blockState.getSoundType().getPitch());
                                }
                                BlockState realState = blockState.mirror(templateSettings.getMirror()).rotate(templateSettings.getRotation());

                                Block.pushEntitiesUp(serverLevel.getBlockState(currentBlockPos), realState, serverLevel, currentBlockPos);

                                if (compoundTag != null) {
                                    BlockEntity blockentity = serverLevel.getBlockEntity(currentBlockPos);
                                    Clearable.tryClear(blockentity);
                                    serverLevel.setBlock(currentBlockPos, Blocks.BARRIER.defaultBlockState(), 20);
                                }

                                if (serverLevel.setBlock(currentBlockPos, realState, 3)) {
                                    list3.add(Pair.of(currentBlockPos, compoundTag));
                                    if (compoundTag != null) {
                                        BlockEntity blockentity1 = serverLevel.getBlockEntity(currentBlockPos);

                                        if (blockentity1 != null) {
                                            if (blockentity1 instanceof RandomizableContainerBlockEntity) {
                                                compoundTag.putLong("LootTableSeed", serverLevel.random.nextLong());
                                            }

                                            blockentity1.load(compoundTag);
                                            blockentity1.setChanged();
                                        }
                                    }

                                    for (Pair<BlockPos, CompoundTag> pair : list3) {
                                        BlockPos blockpos4 = pair.getFirst();
                                        BlockState blockstate2 = serverLevel.getBlockState(blockpos4);
                                        BlockState blockstate3 = Block.updateFromNeighbourShapes(blockstate2, serverLevel, blockpos4);
                                        if (blockstate2 != blockstate3) {
                                            serverLevel.setBlock(blockpos4, blockstate3, 3 & -2 | 16);
                                        }
                                        serverLevel.blockUpdated(blockpos4, blockstate3.getBlock());


                                        if (pair.getSecond() != null) {
                                            BlockEntity blockentity2 = serverLevel.getBlockEntity(blockpos4);
                                            if (blockentity2 != null) {
                                                blockentity2.setChanged();
                                            }
                                        }
                                    }
                                }


                                currentBlockPos = null;
                            } else {
                                currentBlockPos = null;
                            }
                        }
                    }
                    this.buildingTick = 1;

                }
                this.mob.getNavigation().moveTo(blockPos.get().getX(), blockPos.get().getY(), blockPos.get().getZ(), this.speedMultiplier);

            }
        }
        this.mob.setBuildingStep(this.step);
    }

    @Override
    public void stop() {
        super.stop();
        if (this.workOver) {
            this.mob.setBuildingPos(Optional.empty());
            this.mob.setBuildingStructureName(null);
            this.mob.setBuildingStep(0);
        }
    }

    private boolean isReplaceable(BlockState blockState, ServerLevel level, PathfinderMob mob) {
        return !blockState.is(BlockTags.FEATURES_CANNOT_REPLACE) && ForgeEventFactory.getMobGriefingEvent(level, mob);
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

}