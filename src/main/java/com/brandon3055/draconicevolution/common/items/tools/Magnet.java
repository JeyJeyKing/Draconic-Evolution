package com.brandon3055.draconicevolution.common.items.tools;

import com.brandon3055.brandonscore.common.utills.InfoHelper;
import com.brandon3055.brandonscore.common.utills.ItemNBTHelper;
import com.brandon3055.draconicevolution.DraconicEvolution;
import com.brandon3055.draconicevolution.common.ModItems;
import com.brandon3055.draconicevolution.common.items.ItemDE;
import com.brandon3055.draconicevolution.common.lib.References;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by brandon3055 on 9/3/2016.
 */
public class Magnet extends ItemDE {
	private IIcon draconium;
	private IIcon awakened;

	public Magnet(){
		this.setUnlocalizedName("magnet");
		this.setCreativeTab(DraconicEvolution.tabBlocksItems);
		ModItems.register(this);
	}

	@Override
	public void registerIcons(IIconRegister iconRegister) {
		draconium = iconRegister.registerIcon(References.RESOURCESPREFIX+"magnetWyvern");
		awakened = iconRegister.registerIcon(References.RESOURCESPREFIX+"magnetDraconic");
	}

	@Override
	public IIcon getIconFromDamage(int dmg) {
		return dmg == 0 ? draconium : awakened;
	}

	@Override
	public boolean getHasSubtypes() {
		return true;
	}

	@SideOnly(Side.CLIENT)
	@SuppressWarnings("unchecked")
	@Override
	public void getSubItems(Item item, CreativeTabs p_150895_2_, List list) {
		list.add(new ItemStack(item, 1, 0));
		list.add(new ItemStack(item, 1, 1));
	}

	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		return super.getUnlocalizedName(itemStack)+(itemStack.getItemDamage() == 0 ? ".wyvern" : ".draconic");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean hasEffect(ItemStack stack, int pass) {
		return ItemNBTHelper.getBoolean(stack, "MagnetEnabled", false);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int damage, boolean hotbar) {
		if (!entity.isSneaking() && entity.ticksExisted % 5 == 0 && ItemNBTHelper.getBoolean(stack, "MagnetEnabled", false)){
			int range = damage == 0 ? 8 : 32;

			List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(entity.posX, entity.posY, entity.posZ, entity.posX, entity.posY, entity.posZ).expand(range, range, range));

			boolean flag = false;

			for (EntityItem item : items) {
				flag = true;
				if (item.delayBeforeCanPickup > 0) item.delayBeforeCanPickup = 0;
				item.motionX = item.motionY = item.motionZ = 0;
				item.setPosition(entity.posX-0.2+(world.rand.nextDouble()*0.4), entity.posY-0.6, entity.posZ-0.2+(world.rand.nextDouble()*0.4));
			}
			if (flag) world.playSoundAtEntity(entity, "random.orb", 0.1F, 0.5F * ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 2F));
		}
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if (player.isSneaking()) ItemNBTHelper.setBoolean(stack, "MagnetEnabled", !ItemNBTHelper.getBoolean(stack, "MagnetEnabled", false));
		return stack;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addInformation(ItemStack stack, EntityPlayer p_77624_2_, List list, boolean p_77624_4_) {
		list.add(StatCollector.translateToLocal("info.de.shiftRightClickToActivate.txt"));
		int range = stack.getItemDamage() == 0 ? 8 : 32;
		list.add(InfoHelper.HITC()+range+InfoHelper.ITC()+" "+StatCollector.translateToLocal("info.de.blockRange.txt"));
	}
}
