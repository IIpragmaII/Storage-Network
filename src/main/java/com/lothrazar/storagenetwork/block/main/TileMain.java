package com.lothrazar.storagenetwork.block.main;

import java.util.Set;

import com.lothrazar.storagenetwork.StorageNetworkMod;
import com.lothrazar.storagenetwork.api.DimPos;
import com.lothrazar.storagenetwork.api.EnumStorageDirection;
import com.lothrazar.storagenetwork.api.IConnectable;
import com.lothrazar.storagenetwork.api.IConnectableItemAutoIO;
import com.lothrazar.storagenetwork.api.IItemStackMatcher;
import com.lothrazar.storagenetwork.capability.handler.ItemStackMatcher;
import com.lothrazar.storagenetwork.registry.SsnRegistry;
import com.lothrazar.storagenetwork.registry.StorageNetworkCapabilities;
import com.lothrazar.storagenetwork.util.UtilInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class TileMain extends BlockEntity {

  //currently this has one network
  public NetworkModule nw = new NetworkModule();

  public TileMain(BlockPos pos, BlockState state) {
    super(SsnRegistry.Tiles.MASTER.get(), pos, state);
  }

  @Override
  public CompoundTag getUpdateTag() {
    CompoundTag nbt = new CompoundTag();
    this.saveAdditional(nbt);
    return nbt;
  }

  @Override
  public ClientboundBlockEntityDataPacket getUpdatePacket() {
    saveWithFullMetadata();
    return ClientboundBlockEntityDataPacket.create(this);
  }

  @Override
  public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
    load(pkt.getTag() == null ? new CompoundTag() : pkt.getTag());
    super.onDataPacket(net, pkt);
  }

  /**
   * insert into my network
   */
  public int insertStack(ItemStack stack, boolean simulate) {
    int totalInserted = nw.insertStack(stack, simulate);
    //subnetwork ?
    return totalInserted;
  }

  /**
   * request from my network
   */
  public ItemStack request(ItemStackMatcher matcher, int size, boolean simulate) {
    ItemStack result = nw.request(matcher, size, simulate);
    //if not found then ?
    return result;
  }

  public void executeRequestBatch(RequestBatch batch) {
    nw.executeRequestBatch(batch);
  }

  private DimPos getDimPos() {
    return new DimPos(level, worldPosition);
  }

  public void clearCache() {
    nw.ch.clearCache();
  }

  /**
   * Pull into the network from the relevant linked cables
   */
  private void updateImports() {
    for (IConnectable connectable : nw.getConnectables()) {
      if (connectable == null || connectable.getPos() == null) {
        continue;
      }
      IConnectableItemAutoIO storage = connectable.getPos().getCapability(StorageNetworkCapabilities.CONNECTABLE_AUTO_IO, null);
      if (storage == null) {
        continue;
      }
      //
      // We explicitely don't want to check whether this can do BOTH, because we don't
      // want to import what we've just exported in updateExports().
      if (storage.ioDirection() != EnumStorageDirection.IN) {
        continue;
      }
      // Give the storage a chance to have a cooldown or other conditions that prevent it from running
      if (!storage.runNow(connectable.getPos(), this)) {
        continue;
      }
      IItemHandler itemHandler = storage.getItemHandler();
      if (itemHandler == null) {
        continue;
      }
      if (storage.needsRedstone()) {
        boolean power = level.hasNeighborSignal(connectable.getPos().getBlockPos());
        if (power == false) {
          continue;
        }
      }
      for (int slot = 0; slot < itemHandler.getSlots(); slot++) {
        if (itemHandler.getStackInSlot(slot).isEmpty()) {
          continue;
        }
        ItemStack stackCurrent = itemHandler.getStackInSlot(slot).copy();
        // Ignore stacks that are filtered
        if (storage.getFilters() == null || !storage.getFilters().isStackFiltered(stackCurrent)) {
          if (storage.isStockMode()) {
            int filterSize = storage.getFilters().getStackCount(stackCurrent);
            BlockEntity tileEntity = level.getBlockEntity(connectable.getPos().getBlockPos().relative(storage.facingInventory()));
            IItemHandler targetInventory = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).orElse(null);
            //request with false to see how many even exist in there.
            int chestHowMany = UtilInventory.countHowMany(targetInventory, stackCurrent);
            //so if chest=37 items of that kind
            //and the filter is say filterSize == 20
            //we SHOULD import 37
            //as we want the STOCK of the chest to not go less than the filter number , just down to it
            if (chestHowMany > filterSize) {
              int realSize = Math.min(chestHowMany - filterSize, 64);
              StorageNetworkMod.log(" : stock mode import  realSize = " + realSize);
              stackCurrent.setCount(realSize);
            }
            else {
              StorageNetworkMod.log(" : stock mode CANCEL: ITS NOT ENOUGH chestHowMany <= filter size ");
              continue;
            }
          }
          int extractSize = Math.min(storage.getTransferRate(), stackCurrent.getCount());
          ItemStack stackToImport = itemHandler.extractItem(slot, extractSize, true); //simulate to grab a reference
          if (stackToImport.isEmpty()) {
            continue; //continue back to itemHandler
          }
          // Then try to insert the stack into this masters network and store the number of remaining items in the stack
          int countUnmoved = this.insertStack(stackToImport, true);
          // Calculate how many items in the stack actually got moved
          int countMoved = stackToImport.getCount() - countUnmoved;
          if (countMoved <= 0) {
            continue; //continue back to itemHandler
          }
          // Alright, simulation says we're good, let's do it!
          // First extract from the storage
          ItemStack actuallyExtracted = itemHandler.extractItem(slot, countMoved, false);
          // Then insert into our network
          this.insertStack(actuallyExtracted, false);
          break; // break out of itemHandler loop, done processing this cable, so move to next
        } //end of checking on filter for this stack
      }
    }
  }

  private void updateProcess() {
    //    for (IConnectable connectable : getConnectables()) {
    //    if (connectable == null || connectable.getPos() == null) {
    //      //        StorageNetwork.log("null connectable or pos : updateProcess() ");
    //      continue;
    //    }
    //      TileCableProcess cableProcess = connectable.getPos().getTileEntity(TileCableProcess.class);
    //      if (cableProcess == null) {
    //        continue;
    //      }
    //      cableProcess.run();
    //    }
  }

  /**
   * push OUT of the network to attached export cables.
   * 
   */
  private void updateExports() {
    Set<IConnectable> conSet = nw.getConnectables();
    RequestBatch requstBatch = new RequestBatch();

    for (IConnectable connectable : conSet) {
      if (!connectable.hasStorage()) {
        continue;
      }
      // We explicitely don't want to check whether this can do BOTH, because we don't
      // want to import what we've just exported in updateExports().
      if (!connectable.isDirection(EnumStorageDirection.OUT)) {
        continue;
      }
      // Give the storage a chance to have a cooldown
      if (!connectable.runNow(this)) {
        continue;
      }
      if (connectable.needsRedstone()) {
        boolean power = level.hasNeighborSignal(connectable.getPos().getBlockPos());
        if (power == false) {
          // StorageNetwork.log(power + " Export pow here ; needs yes skip me");
          continue;
        }
      }
      for (IItemStackMatcher matcher : connectable.getAutoExportList(level)) {
        if (matcher.getStack().isEmpty()) {
          continue;
        }
        requstBatch.add(matcher.getStack().getTag(), connectable);
      }
      executeRequestBatch(requstBatch);
    }
  }

  public static void clientTick(Level level, BlockPos blockPos, BlockState blockState, TileMain tile) {}

  public static <E extends BlockEntity> void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileMain tile) {
    tile.tick();
  }

  private void tick() {
    if (level == null || level.isClientSide) {
      return;
    }
    //refresh time in config, default 200 ticks aka 10 seconds
    if ((level.getGameTime() % StorageNetworkMod.CONFIG.refreshTicks() == 0)
        || nw.shouldRefresh()) {
      nw.doRefresh(this.getDimPos());
    }
    updateImports();
    updateExports();
    updateProcess();
  }
}
