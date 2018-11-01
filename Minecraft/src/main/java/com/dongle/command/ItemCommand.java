package com.dongle.command;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
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
		return "itemData";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/itemData";
	}

	@Override
	public List<String> getAliases() {
		List<String> aliases = Lists.<String>newArrayList();
		aliases.add("/itemData");
		return aliases;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		// TODO Auto-generated method stub
		Entity ent = sender.getCommandSenderEntity();
		if(ent instanceof EntityPlayer){
			Item tempItem = ((EntityPlayer) ent).getHeldItemMainhand().getItem();
			String retString = ((EntityPlayer) ent).getHeldItemMainhand().getItem().getRegistryName().toString() + "|" +  tempItem.getMetadata(((EntityPlayer) ent).getHeldItemMainhand());
			sender.sendMessage(new TextComponentTranslation(retString));
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
