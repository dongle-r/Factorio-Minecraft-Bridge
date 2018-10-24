package com.dongle.TCSChest;

import com.dongle.FMCBridge.FMCBridge;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;

public class TCSGui extends GuiContainer{

	public static final int WIDTH = 180;
	public static final int HEIGHT = 152;
	
	private static final ResourceLocation background = new ResourceLocation(FMCBridge.MODID, "textures/gui/tcsgui.png");
	
    public TCSGui(TCSEntity tileEntity, TCS container) {
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
