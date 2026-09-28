package pers.solid.mishang.uc.blockentity;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import pers.solid.mishang.uc.MishangUtils;

/**
 * 染色的悬挂告示牌方块。
 */
public class ColoredHungSignBlockEntity extends HungSignBlockEntity implements ColoredBlockEntity {
  public int color = 0;
  /**
   * 客户端是否已经在读取颜色后通知过重新渲染区块。
   */
  private transient boolean colorRenderNotified = false;

  public ColoredHungSignBlockEntity(BlockPos pos, BlockState state) {
    super(MishangucBlockEntities.COLORED_HUNG_SIGN_BLOCK_ENTITY, pos, state);
  }

  @Override
  public void readNbt(NbtCompound nbt) {
    super.readNbt(nbt);
    final int oldColor = color;
    color = MishangUtils.readColorFromNbtElement(nbt.get("color"));
    // 颜色只影响方块的着色（区块网格），因此只在首次读取以及颜色改变时才通知重新渲染区块。
    if (world != null && world.isClient && (!colorRenderNotified || color != oldColor)) {
      colorRenderNotified = true;
      world.updateListeners(pos, this.getCachedState(), this.getCachedState(), 3);
    }
  }

  @Override
  public void writeNbt(NbtCompound nbt) {
    super.writeNbt(nbt);
    nbt.putInt("color", color);
  }

  @Override
  public int getColor() {
    return color;
  }

  @Override
  public void setColor(int color) {
    this.color = color;
  }
}
