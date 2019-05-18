
package cn.wang.chinesechess.save;

import java.util.ArrayList;

/**
 * 保存棋谱接口,保存棋谱的界面需要实现该接口
 * 
 * @author wanghualiang
 */
public interface ISaveManual {
	/**
	 * 获得棋谱的游戏记录信息
	 * 
	 * @return 游戏记录
	 */
	public GameRecord getGameRecord();

	/**
	 * 获得棋谱的保存路径
	 * 
	 * @return 保存路径列表
	 */
	public ArrayList<String> getSavePaths();
}
