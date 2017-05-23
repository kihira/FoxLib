package truetyper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import java.nio.FloatBuffer;
import java.util.regex.Pattern;

public class FontHelper {

    private static final String formatEscape = "\u00A7";
    private static final Pattern formattingCodePattern = Pattern.compile("(?i)" + String.valueOf('\u00a7') + "[0-9A-FK-OR]");


    public static void drawString(String s, float x, float y, TrueTypeFont font, float scaleX, float scaleY, float... rgba){
        drawString(s,x,y,font,scaleX,scaleY,0,rgba);

    }

    public static void drawCenteredString(String s, float x, float y, TrueTypeFont font, float scaleX, float scaleY, float ... rgba) {
        drawString(s, x - (font.getWidth(s.replaceAll(formattingCodePattern.toString(), "")) / (4F / scaleX)), y, font, scaleX, scaleY, rgba);
    }

    public static void drawString(String s, float x, float y, TrueTypeFont font, float scaleX, float scaleY, float rotationZ, float... rgba){
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution sr = new ScaledResolution(mc);

        if(mc.gameSettings.hideGUI){
            return;
        }
        float amt = 2F / sr.getScaleFactor();
        if(sr.getScaleFactor() == 1){
            amt = 2;
        }

        FloatBuffer matrixData = BufferUtils.createFloatBuffer(16);
        GlStateManager.getFloat(GL11.GL_MODELVIEW_MATRIX, matrixData);
        FontHelper.set2DMode(matrixData);
        GlStateManager.pushMatrix();
        y = mc.displayHeight-(y*sr.getScaleFactor())-(((font.getLineHeight()/amt)));
        float tx = (x*sr.getScaleFactor())+(font.getWidth(s)/2);
        float tranx = tx+2;
        float trany = y+(font.getLineHeight()/2);
        GlStateManager.translate(tranx,trany,0);
        GlStateManager.rotate(rotationZ, 0f, 0f, 1f);
        GlStateManager.translate(-tranx,-trany,0);


        GlStateManager.enableBlend();
        if(s.contains(formatEscape)){
            String[] pars = s.split(formatEscape);
            float totalOffset = 0;
            for(int i = 0; i < pars.length; i++){
                String par = pars[i];
                    float[] c = rgba;
                    if(i > 0){
                        c = Formatter.getFormatted(par.charAt(0));
                        par = par.substring(1, par.length());
                    }
                    font.drawString((x*sr.getScaleFactor()+totalOffset), y, par, scaleX/amt, scaleY/amt, c);
                    totalOffset += font.getWidth(par);
            }
        }else{
            font.drawString((x*sr.getScaleFactor()), y, s, scaleX/amt, scaleY/amt, rgba);
        }
        GlStateManager.popMatrix();
        GlStateManager.disableBlend();
        FontHelper.set3DMode();
    }

    private static void set2DMode(FloatBuffer matrixData) {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution sr = new ScaledResolution(mc);
        mc.entityRenderer.setupOverlayRendering();
        GlStateManager.matrixMode(GL11.GL_PROJECTION);
        GlStateManager.pushMatrix();
        //GlStateManager.glLoadMatrix(matrixData);

        GlStateManager.loadIdentity();
        GlStateManager.ortho(0, mc.displayWidth, 0, mc.displayHeight, -1, 1);
        GlStateManager.matrixMode(GL11.GL_MODELVIEW);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();

        Matrix4f matrix = new Matrix4f();
        matrix.load(matrixData);
        GlStateManager.translate(matrix.m30*sr.getScaleFactor(),-matrix.m31*sr.getScaleFactor(), 0f);

    }

    private static void set3DMode() {
        GlStateManager.matrixMode(GL11.GL_MODELVIEW);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(GL11.GL_PROJECTION);
        GlStateManager.popMatrix();
        Minecraft mc = Minecraft.getMinecraft();
        mc.entityRenderer.setupOverlayRendering();
    }

}
