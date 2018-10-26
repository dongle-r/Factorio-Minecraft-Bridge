package com.dongle.TCRChest;

import com.dongle.FMCBridge.FMCBridge;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;

public class TCRGui extends GuiContainer{
	public static final int WIDTH = 176;
	public static final int HEIGHT = 166;
	
	private static final ResourceLocation background = new ResourceLocation(FMCBridge.MODID, "textures/gui/tcrgui.png");
	
    public TCRGui(TCREntity tileEntity, TCR container) {
        super(container);

        xSize = WIDTH;
        ySize = HEIGHT;
    }

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}
}
