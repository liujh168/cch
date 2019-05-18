
package cn.wang.chinesechess;

import java.awt.Color;
/**
 * 
 * @author wanghualiang
 *
 */
public final class ColorUtil {

	private ColorUtil() {

	}

	// ÆåÅÌµÄ±³¾°É«
	public static Color DEFAULT_BGCOLOR = new Color(232, 220, 184);
	
	public static Color getNetworkBgcolor(){
		return DEFAULT_BGCOLOR;
	}
	
	public static Color getPrintWholeBgcolor(){
		return DEFAULT_BGCOLOR;
	}
	
	public static Color getPrintPartialBgcolor(){
		return DEFAULT_BGCOLOR;
	}
	
	public static Color getManMachineBgcolor(){
		return DEFAULT_BGCOLOR;
	}
	
	public static Color getEmpressBgcolor(){
		return DEFAULT_BGCOLOR;
	}
	
	public static Color getHorseMazeBgcolor(){
		return DEFAULT_BGCOLOR;
	}

	public static Color getDefaultBgcolor() {
		return DEFAULT_BGCOLOR;
	}
}
