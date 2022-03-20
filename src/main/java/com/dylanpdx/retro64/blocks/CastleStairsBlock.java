package com.dylanpdx.retro64.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
// import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.stream.Stream;

public class CastleStairsBlock extends Block {

    public static final DirectionProperty HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;

    private static final VoxelShape SHAPE_N = Stream.of(
            Block.box(0, 0, 3, 16, 4, 4),
            Block.box(0, 0, 15, 16, 16, 16),
            Block.box(0, 0, 14, 16, 15, 15),
            Block.box(0, 0, 12, 16, 13, 13),
            Block.box(0, 0, 13, 16, 14, 14),
            Block.box(0, 0, 11, 16, 12, 12),
            Block.box(0, 0, 10, 16, 11, 11),
            Block.box(0, 0, 8, 16, 9, 9),
            Block.box(0, 0, 9, 16, 10, 10),
            Block.box(0, 0, 7, 16, 8, 8),
            Block.box(0, 0, 6, 16, 7, 7),
            Block.box(0, 0, 5, 16, 6, 6),
            Block.box(0, 0, 4, 16, 5, 5),
            Block.box(0, 0, 2, 16, 3, 3),
            Block.box(0, 0, 1, 16, 2, 2),
            Block.box(0, 0, 0, 16, 1, 1)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    private static final VoxelShape SHAPE_E=Stream.of(
            Block.box(12, 0, 0, 13, 4, 16),
            Block.box(0, 0, 0, 1, 16, 16),
            Block.box(1, 0, 0, 2, 15, 16),
            Block.box(3, 0, 0, 4, 13, 16),
            Block.box(2, 0, 0, 3, 14, 16),
            Block.box(4, 0, 0, 5, 12, 16),
            Block.box(5, 0, 0, 6, 11, 16),
            Block.box(7, 0, 0, 8, 9, 16),
            Block.box(6, 0, 0, 7, 10, 16),
            Block.box(8, 0, 0, 9, 8, 16),
            Block.box(9, 0, 0, 10, 7, 16),
            Block.box(10, 0, 0, 11, 6, 16),
            Block.box(11, 0, 0, 12, 5, 16),
            Block.box(13, 0, 0, 14, 3, 16),
            Block.box(14, 0, 0, 15, 2, 16),
            Block.box(15, 0, 0, 16, 1, 16)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    private static final VoxelShape SHAPE_S=Stream.of(
            Block.box(0, 0, 12, 16, 4, 13),
            Block.box(0, 0, 0, 16, 16, 1),
            Block.box(0, 0, 1, 16, 15, 2),
            Block.box(0, 0, 3, 16, 13, 4),
            Block.box(0, 0, 2, 16, 14, 3),
            Block.box(0, 0, 4, 16, 12, 5),
            Block.box(0, 0, 5, 16, 11, 6),
            Block.box(0, 0, 7, 16, 9, 8),
            Block.box(0, 0, 6, 16, 10, 7),
            Block.box(0, 0, 8, 16, 8, 9),
            Block.box(0, 0, 9, 16, 7, 10),
            Block.box(0, 0, 10, 16, 6, 11),
            Block.box(0, 0, 11, 16, 5, 12),
            Block.box(0, 0, 13, 16, 3, 14),
            Block.box(0, 0, 14, 16, 2, 15),
            Block.box(0, 0, 15, 16, 1, 16)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    private static final VoxelShape SHAPE_W=Stream.of(
            Block.box(3, 0, 0, 4, 4, 16),
            Block.box(15, 0, 0, 16, 16, 16),
            Block.box(14, 0, 0, 15, 15, 16),
            Block.box(12, 0, 0, 13, 13, 16),
            Block.box(13, 0, 0, 14, 14, 16),
            Block.box(11, 0, 0, 12, 12, 16),
            Block.box(10, 0, 0, 11, 11, 16),
            Block.box(8, 0, 0, 9, 9, 16),
            Block.box(9, 0, 0, 10, 10, 16),
            Block.box(7, 0, 0, 8, 8, 16),
            Block.box(6, 0, 0, 7, 7, 16),
            Block.box(5, 0, 0, 6, 6, 16),
            Block.box(4, 0, 0, 5, 5, 16),
            Block.box(2, 0, 0, 3, 3, 16),
            Block.box(1, 0, 0, 2, 2, 16),
            Block.box(0, 0, 0, 1, 1, 16)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public CastleStairsBlock() {
        super(BlockBehaviour.Properties.of(Material.STONE)
                .strength(3.5f,4.8f)
                .sound(SoundType.STONE));
        this.registerDefaultState(this.defaultBlockState().setValue(HORIZONTAL_FACING, Direction.NORTH));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(HORIZONTAL_FACING,pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(HORIZONTAL_FACING,pRotation.rotate(pState.getValue(HORIZONTAL_FACING)));
    }

    /*@Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(HORIZONTAL_FACING));
    }*/

    VoxelShape getShape(BlockState state){
        switch (state.getValue(HORIZONTAL_FACING)){
            case NORTH:
                return SHAPE_N;
            case SOUTH:
                return SHAPE_S;
            case EAST:
                return SHAPE_E;
            case WEST:
                return SHAPE_W;
            default:
                return SHAPE_N;
        }
    }

    @Override
    public VoxelShape getInteractionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return getShape(pState);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return getShape(pState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(HORIZONTAL_FACING);
    }
}
