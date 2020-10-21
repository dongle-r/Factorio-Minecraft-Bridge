package com.dongle.command;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

public class ItemCommand implements ICommand{

	@Override
	public int compareTo(ICommand o) {
		return 0;
	}

	@Override
	public String getName() {
		return "fmc_item_data";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/fmc_item_data";
	}

	@Override
	public List<String> getAliases() {
		List<String> aliases = Lists.<String>newArrayList();
		aliases.add("/fmc_item_data");
		aliases.add("/fmc_item");
		return aliases;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		// TODO Auto-generated method stub
		Entity ent = sender.getCommandSenderEntity();
		if(args.length == 0){
			if(ent instanceof EntityPlayer){
				Item tempItem = ((EntityPlayer) ent).getHeldItemMainhand().getItem();
				String retString = ((EntityPlayer) ent).getHeldItemMainhand().getItem().getRegistryName().toString() + "|" +  tempItem.getMetadata(((EntityPlayer) ent).getHeldItemMainhand());
				sender.sendMessage(new TextComponentTranslation(retString));
			}
		}
		if(args.length == 1){
			if(args[0].equals("-c")){
				if(ent instanceof EntityPlayer){
					Item tempItem = ((EntityPlayer) ent).getHeldItemMainhand().getItem();
					String retString = ((EntityPlayer) ent).getHeldItemMainhand().getItem().getRegistryName().toString() + "|" +  tempItem.getMetadata(((EntityPlayer) ent).getHeldItemMainhand());
					sender.sendMessage(new TextComponentTranslation(retString));
					StringSelection selection = new StringSelection(retString);
					Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
					clipboard.setContents(selection, selection);
				}
			}
			else if(args[0].equals("nbt")){
				if(ent instanceof EntityPlayer){
					String retString ="";
					try{
						Item tempItem = ((EntityPlayer) ent).getHeldItemMainhand().getItem();
						NBTTagCompound compound = ((EntityPlayer) ent).getHeldItemMainhand().getTagCompound();
						retString = ((EntityPlayer) ent).getHeldItemMainhand().getItem().getRegistryName().toString() + "|" +  tempItem.getMetadata(((EntityPlayer) ent).getHeldItemMainhand()) + "|" + compound.toString();
					}
					catch(Exception E){
						retString = "No nbt data found for this item.";
					}
					sender.sendMessage(new TextComponentTranslation(retString));
				}
			}
		}
		else if(args.length == 2){
			if(args[0].equals("nbt") && args[1].equals("-c")){
				if(ent instanceof EntityPlayer){
					String retString ="";
					try{
						Item tempItem = ((EntityPlayer) ent).getHeldItemMainhand().getItem();
						NBTTagCompound compound = ((EntityPlayer) ent).getHeldItemMainhand().getTagCompound();
						retString = ((EntityPlayer) ent).getHeldItemMainhand().getItem().getRegistryName().toString() + "|" +  tempItem.getMetadata(((EntityPlayer) ent).getHeldItemMainhand()) + "|" + compound.toString();
						StringSelection selection = new StringSelection(retString);
						Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
						clipboard.setContents(selection, selection);
					}
					catch(Exception E){
						retString = "No nbt data found for this item.";
					}
					sender.sendMessage(new TextComponentTranslation(retString));
				}
			}
		}
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return true ;
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			BlockPos targetPos) {
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return false;
	}


}
