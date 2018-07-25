package selim.geyser.items.forge;

import java.lang.reflect.Field;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import selim.geyser.core.shared.EnumComponent;
import selim.geyser.core.shared.GeyserCoreInfo;
import selim.geyser.items.shared.GeyserItemsInfo;

@Mod(modid = GeyserItemsInfo.ID, name = GeyserItemsInfo.NAME,
		dependencies = "required-after:" + GeyserCoreInfo.ID, version = GeyserItemsInfo.VERSION,
		clientSideOnly = true)
public class GeyserItemsForge {

	@Mod.Instance(value = GeyserItemsInfo.ID)
	public static GeyserItemsForge instance;
	public static final Logger LOGGER = LogManager.getLogger(GeyserCoreInfo.ID);
	public static SimpleNetworkWrapper network;

	static {
		try {
			Field lockedField = Item.REGISTRY.getClass().getDeclaredField("locked");
			lockedField.setAccessible(true);
			lockedField.set(Item.REGISTRY, false);
			Item.REGISTRY.register(GeyserItemsInfo.ITEM_ID,
					new ResourceLocation(GeyserItemsInfo.ID, "geyser_item"), new Item() {});
			lockedField.set(Item.REGISTRY, true);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException
				| IllegalAccessException e) {
			System.out.println("FAILED");
			e.printStackTrace();
		}
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		network = NetworkRegistry.INSTANCE.newSimpleChannel(GeyserItemsInfo.CHANNEL);

		FMLInterModComms.sendMessage(GeyserCoreInfo.ID, GeyserCoreInfo.IMC_SEND_KEY,
				EnumComponent.ITEMS.toString());
	}

}
