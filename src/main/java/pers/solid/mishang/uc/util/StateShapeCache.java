package pers.solid.mishang.uc.util;

import net.minecraft.block.BlockState;
import net.minecraft.util.shape.VoxelShape;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * 按方块状态缓存只取决于方块状态的形状。首次查询某个方块状态时调用原有的计算方法，之后直接返回同一结果。<br>
 * Caches shapes that depend only on the block state. The original computation runs once per state; later
 * lookups return its result. Safe to use from chunk builder worker threads.
 */
public final class StateShapeCache {
  private final Map<BlockState, VoxelShape> shapes = new ConcurrentHashMap<>();
  private final Function<BlockState, VoxelShape> factory;

  public StateShapeCache(Function<BlockState, VoxelShape> factory) {
    this.factory = factory;
  }

  public VoxelShape get(BlockState state) {
    final VoxelShape shape = shapes.get(state);
    return shape != null ? shape : shapes.computeIfAbsent(state, factory);
  }
}
