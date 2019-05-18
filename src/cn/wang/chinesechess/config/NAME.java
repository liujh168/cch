
package cn.wang.chinesechess.config;

/**
 * 
 * @author wanghualinag
 */
public interface NAME {

	public static final String RED_NAME = "红方";

	public static final String BLACK_NAME = "黑方";
	
	public static final String PALYER_FIRST = "玩家";
	
	public static final String COMPUTER_FIRST = "电脑";

	// 棋谱文件扩展名
	public static String EXTENSION_NAME = ".chs";

	public static String EXTENSION_NAME2 = "chs";
	/**
	 * 窗口标题
	 */

	public static int RED_TURN_GAME_STATUS = 35668;

	public static int BLACK_TURN_GAME_STATUS = 35669;
	
	/** 棋盘单元格的宽度*/
	public static int UNIT_WIDTH = 44;
	/** 棋盘单元格的高度*/
	public static int UNIT_HEIGHT = 44;
	/** 棋子的宽度*/
	public static int PIECE_WIDTH = 44;
	/** 棋子的高度*/
	public static int PIECE_HEIGHT = 44;
	/**棋子种类*/
	public static enum PieceCategory {
		JU, MA, PAO, HONGXIANG, HEIXIANG, HONGSHI, HEISHI, JIANG, SHUAI, ZU, BING
	}// 棋子的种类
	/**棋子ID*/
	public static enum PieceId {
		HONGJU1, HONGJU2, HONGMA1, HONGMA2, HONGXIANG1, HONGXIANG2, HONGSHI1, HONGSHI2, SHUAI, HONGPAO1, HONGPAO2, BING1, BING2, BING3, BING4, BING5, HEIJU1, HEIJU2, HEIMA1, HEIMA2, HEIXIANG1, HEIXIANG2, HEISHI1, HEISHI2, JIANG, HEIPAO1, HEIPAO2, ZU1, ZU2, ZU3, ZU4, ZU5;
	}
	/**
	 * 棋盘的类型
	 */
	public enum BoardType {
		printWhole, printPartial, twoman, manMachine
	}
	
	/**
	 * 棋谱类型
	 * 
	 */
	public enum ManualType {
		PRINT_PARTIAL, PRINT_WHOLE, MAN_MACHINE ,TWO_MAN
	};
	public String 帥 = "帥";

	public String 車 = "車";

	public String 馬 = "馬";

	public String 炮 = "炮";

	public String 仕 = "仕";

	public String 相 = "相";

	public String 兵 = "兵";

	public String 將 = "將";

	public String 士 = "士";

	public String 象 = "象";

	public String 卒 = "卒";

}
