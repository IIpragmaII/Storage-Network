package com.lothrazar.storagenetwork.block.cableinfilter;
import com.lothrazar.storagenetwork.api.capability.IConnectableLink;
import com.lothrazar.storagenetwork.block.cablefilter.TileCableFilter;
import com.lothrazar.storagenetwork.capabilities.CapabilityConnectableAutoIO;
import com.lothrazar.storagenetwork.capabilities.CapabilityConnectableLink;
import com.lothrazar.storagenetwork.capabilities.StorageNetworkCapabilities;
import com.lothrazar.storagenetwork.gui.ContainerCable;
import com.lothrazar.storagenetwork.registry.SsnRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ContainerCableImportFilter extends ContainerCable {

  public final TileCableImportFilter tile;


  @Nullable
  public CapabilityConnectableAutoIO link;

  public ContainerCableImportFilter(int windowId, World world, BlockPos pos, PlayerInventory playerInv, PlayerEntity player) {
    super(SsnRegistry.filterimportContainer, windowId);
    tile = (TileCableImportFilter) world.getTileEntity(pos);

    this.link =(CapabilityConnectableAutoIO)
        tile.getCapability(StorageNetworkCapabilities.CONNECTABLE_AUTO_IO, null).orElse(null);

    this.bindPlayerInvo(playerInv);

  }

  @Override
  public ItemStack transferStackInSlot(PlayerEntity player, int slotIndex) {
    return ItemStack.EMPTY;
  }

  @Override public boolean canInteractWith(PlayerEntity playerIn) {
    return true;
  }
}