package truetyper;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class FontLoader{

    public static TrueTypeFont loadSystemFont(String name, float defSize, boolean antialias){
         return loadSystemFont(name, defSize, antialias, Font.TRUETYPE_FONT);

    }

    public static TrueTypeFont loadSystemFont(String name, float defSize, boolean antialias, int type){
         Font font;
         TrueTypeFont out = null;
         try {
                 font = new Font(name, type, (int)defSize);
                 font = font.deriveFont(defSize);
                 out = new TrueTypeFont(font, antialias);
         } catch (Exception e) {
                 e.printStackTrace();
         }
         return out;
    }

    public static TrueTypeFont createFont(ResourceLocation res, float defSize, boolean antialias){
         return createFont(res, defSize, antialias, Font.TRUETYPE_FONT);
    }

    public static TrueTypeFont createFont(ResourceLocation res, float defSize, boolean antialias, int type){
         Font font;
         TrueTypeFont out = null;
         try {
                 font = Font.createFont(type, Minecraft.getMinecraft().getResourceManager().getResource(res).getInputStream());
                 font = font.deriveFont(defSize);
                 out = new TrueTypeFont(font, antialias);
         } catch (Exception e) {
                 e.printStackTrace();
         }
         return out;
    }

}