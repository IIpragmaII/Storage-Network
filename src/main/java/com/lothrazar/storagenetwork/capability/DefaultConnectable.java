package com.lothrazar.storagenetwork.capability;

import java.util.ArrayList;
import java.util.List;

import com.lothrazar.storagenetwork.StorageNetworkMod;
import com.lothrazar.storagenetwork.api.DimPos;
import com.lothrazar.storagenetwork.api.EnumStorageDirection;
import com.lothrazar.storagenetwork.api.IConnectable;
import com.lothrazar.storagenetwork.api.IConnectableItemAutoIO;
import com.lothrazar.storagenetwork.api.IItemStackMatcher;
import com.lothrazar.storagenetwork.block.main.TileMain;
import com.lothrazar.storagenetwork.capability.handler.FilterItemStackHandler;
import com.lothrazar.storagenetwork.registry.StorageNetworkCapabilities;
import com.lothrazar.storagenetwork.util.UtilInventory;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraft.world.level.Level;

public class DefaultConnectable implements IConnectable {

  FilterItemStackHandler filters = new FilterItemStackHandler();
  DimPos main;
  DimPos self;
  private boolean needsRedstone = false;
  private Integer count = 0;

  public IConnectableItemAutoIO getStorage() {
    DimPos pos = getPos();
    if (pos == null)
      return null;
    return pos.getCapability(StorageNetworkCapabilities.CONNECTABLE_AUTO_IO, null);
  }

  public List<IItemStackMatcher> getAutoExportList(Level level) {
    IConnectableItemAutoIO storage = getStorage();
    List<IItemStackMatcher> filteredExportList = new ArrayList<IItemStackMatcher>();
    for (IItemStackMatcher matcher : storage.getAutoExportList()) {
      if (matcher.getStack().isEmpty()) {
        continue;
      }
      // default amt to request. can be overriden by other upgrades
      // check operations upgrade for export
      boolean stockMode = storage.isStockMode();
      if (stockMode) {
        StorageNetworkMod.log("stockMode == TRUE ; updateExports: attempt " + matcher.getStack());
        // STOCK upgrade means
        try {
          BlockEntity tileEntity = level
              .getBlockEntity(getPos().getBlockPos().relative(storage.facingInventory()));
          IItemHandler targetInventory = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)
              .orElse(null);
          // request with false to see how many even exist in there.
          int stillNeeds = UtilInventory.containsAtLeastHowManyNeeded(targetInventory, matcher.getStack(),
              matcher.getStack().getCount());
          if (stillNeeds == 0) {
            // they dont need any more, they have the stock they need
            StorageNetworkMod.log("stockMode continnue; canc");
            continue;
          }
        } catch (Throwable e) {
          StorageNetworkMod.LOGGER.error("Error thrown from a connected block" + e);
        }
      }
      filteredExportList.add(matcher);
    }
    return filteredExportList;
  }

  public Boolean insertStack(ItemStack stack) {
    IConnectableItemAutoIO storage = getStorage();
    ItemStack insertedStack = storage.insertStack(stack, false);
    // Determine the amount of items moved in the stack
    int movedItems = stack.getCount() - insertedStack.getCount();
    stack.setCount(movedItems);
    if (stack.isEmpty()) {
      return false;
    }
    return true;
  }

  public Integer getCount() {
    return count;
  }

  @Override
  public void toggleNeedsRedstone() {
    needsRedstone = !needsRedstone;
  }

  @Override
  public boolean needsRedstone() {
    return this.needsRedstone;
  }

  @Override
  public void needsRedstone(boolean in) {
    this.needsRedstone = in;
  }

  @Override
  public FilterItemStackHandler getFilter() {
    return filters;
  }

  @Override
  public void setFilter(int value, ItemStack stack) {
    filters.setStackInSlot(value, stack);
    filters.getStacks().set(value, stack);
  }

  @Override
  public DimPos getMainPos() {
    return main;
  }

  @Override
  public DimPos getPos() {
    return self;
  }

  @Override
  public void setMainPos(DimPos mainIn) {
    this.main = mainIn;
  }

  @Override
  public void setPos(DimPos pos) {
    this.self = pos;
  }
}
