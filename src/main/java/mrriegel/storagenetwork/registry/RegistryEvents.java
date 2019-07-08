package mrriegel.storagenetwork.registry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class RegistryEvents {

  //  @SubscribeEvent
  public static void onRegistryBlock(RegistryEvent.Register<Block> event) {
    IForgeRegistry<Block> reg = event.getRegistry();
    //    reg.register(new BlockMaster("master"));
    //    reg.register(new BlockRequest("request"));
    //    reg.register(new BlockCable("kabel"));
    //    reg.register(new BlockCableLink("storage_kabel"));
    //    reg.register(new BlockCableIO("ex_kabel", EnumStorageDirection.OUT));
    //    reg.register(new BlockCableIO("im_kabel", EnumStorageDirection.IN));
    //    reg.register(new BlockCableProcessing("process_kabel"));
    //    reg.register(new BlockControl("controller"));
    //     GameRegistry.registerTileEntity(TileCable.class, new ResourceLocation(StorageNetwork.MODID, "tileKabel"));
    //    GameRegistry.registerTileEntity(TileCableLink.class, new ResourceLocation(StorageNetwork.MODID, "tileKabelLink"));
    //    GameRegistry.registerTileEntity(TileCableIO.class, new ResourceLocation(StorageNetwork.MODID, "tileKabelIO"));
    //    GameRegistry.registerTileEntity(TileCableProcess.class, new ResourceLocation(StorageNetwork.MODID, "tileKabelProcess"));
    //    GameRegistry.registerTileEntity(TileMaster.class, new ResourceLocation(StorageNetwork.MODID, "tileMaster"));
    //    GameRegistry.registerTileEntity(TileRequest.class, new ResourceLocation(StorageNetwork.MODID, "tileRequest"));
    //    GameRegistry.registerTileEntity(TileControl.class, new ResourceLocation(StorageNetwork.MODID, "tileControl"));
  }

  //  @SubscribeEvent
  public static void onRegistryEvent(RegistryEvent.Register<Item> event) {
    IForgeRegistry<Item> registry = event.getRegistry();
    //    registry.register(new ItemBlock(ModBlocks.master).setRegistryName(ModBlocks.master.getRegistryName()));
    //    registry.register(new ItemBlock(ModBlocks.request).setRegistryName(ModBlocks.request.getRegistryName()));
    //    registry.register(new ItemBlock(ModBlocks.kabel).setRegistryName(ModBlocks.kabel.getRegistryName()));
    //    registry.register(new ItemBlock(ModBlocks.storageKabel).setRegistryName(ModBlocks.storageKabel.getRegistryName()));
    //    registry.register(new ItemBlock(ModBlocks.exKabel).setRegistryName(ModBlocks.exKabel.getRegistryName()));
    //    registry.register(new ItemBlock(ModBlocks.imKabel).setRegistryName(ModBlocks.imKabel.getRegistryName()));
    //    registry.register(new ItemBlock(ModBlocks.processKabel).setRegistryName(ModBlocks.processKabel.getRegistryName()));
    //    registry.register(new ItemBlock(ModBlocks.controller).setRegistryName(ModBlocks.controller.getRegistryName()));
    //     registry.register(new ItemUpgrade());
    //    registry.register(new ItemRemote());
  }

  //  @SubscribeEvent
  public void registerModels(ModelRegistryEvent event) {
    //    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.kabel), 0, new ModelResourceLocation(StorageNetwork.MODID + ":kabel", "inventory"));
    //    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.exKabel), 0, new ModelResourceLocation(StorageNetwork.MODID + ":ex_kabel", "inventory"));
    //    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.storageKabel), 0, new ModelResourceLocation(StorageNetwork.MODID + ":storage_kabel", "inventory"));
    //    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.imKabel), 0, new ModelResourceLocation(StorageNetwork.MODID + ":im_kabel", "inventory"));
    //    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.processKabel), 0, new ModelResourceLocation(StorageNetwork.MODID + ":process_kabel", "inventory"));
    //    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.master), 0, new ModelResourceLocation(StorageNetwork.MODID + ":master", "inventory"));
    //    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.request), 0, new ModelResourceLocation(StorageNetwork.MODID + ":request", "inventory"));
    //    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.controller), 0, new ModelResourceLocation(StorageNetwork.MODID + ":controller", "inventory"));
    //     for (EnumUpgradeType type : EnumUpgradeType.values()) {
    //      ModelLoader.setCustomModelResourceLocation(ModItems.upgrade, type.getId(), new ModelResourceLocation(StorageNetwork.MODID + ":upgrade_" + type.getId(), "inventory"));
    //    }
    //    for (RemoteType type : RemoteType.values()) {
    //      ModelLoader.setCustomModelResourceLocation(ModItems.remote, type.ordinal(), new ModelResourceLocation(StorageNetwork.MODID + ":remote_" + type.ordinal(), "inventory"));
    //    }
  }
}
