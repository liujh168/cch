
package cn.wang.chinesechess.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Vector;

import cn.wang.chinesechess.ChessUtil;
import cn.wang.chinesechess.config.NAME;
import cn.wang.chinesechess.print.part.Position;
import cn.wang.chinesechess.save.GameRecord;


/**
 * 棋谱工具类
 * 
 * @author ecjtu-WangHualiang
 */
public final class ManualUtil implements NAME {
	private ManualUtil() {

	}

	/**
	 * 保存棋谱，也保存了当前的时间
	 * 
	 * @param filePath
	 *            棋谱文件的路径
	 * @param descPath
	 *            棋谱文本的存储路径
	 * @param gameRecord
	 *            游戏记录
	 * @return 保存成功返回true，失败返回false
	 */
	public static boolean saveManual(String filePath, String descPath,
			GameRecord gameRecord) {

		/*
		 * 保存棋谱--文本格式--换行 Windows下换行符是\r\n
		 */
		String allDescs = "";
		for (int i = 0; i < gameRecord.getDescs().size(); i++) {
			allDescs += gameRecord.getDescs().get(i) + "\r\n\r\n";
		}
		ChessUtil.writeStringToFile(descPath, allDescs);

		// 保存棋谱文件
		try {
			FileOutputStream outOne = new FileOutputStream(filePath);
			ObjectOutputStream outTwo = new ObjectOutputStream(outOne);

			outTwo.writeObject(gameRecord.getType());// 棋谱类型
			outTwo.writeObject(gameRecord.getDateAndTime());// 当前日期和时间
			outTwo.writeObject(gameRecord.getDesc());// 棋谱描述
			outTwo.writeObject(gameRecord.getRecords());// 棋谱记录
			outTwo.writeObject(gameRecord.getDescs());// 棋谱
			outTwo.writeObject(gameRecord.getInitLocations());// 棋子初始位置
			outTwo.writeObject(gameRecord.getPalyerFirst().toString());// 先手方 电脑or玩家

			outOne.close();
			outTwo.close();

		} catch (NotSerializableException nse) {
			nse.printStackTrace();
			return false;
		} catch (IOException ex) {
			ex.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * 从指定的文件中读取游戏记录
	 * 
	 * @param file
	 *            需要读取的文件
	 * @return 从文件中读取的对象
	 */
	@SuppressWarnings("unchecked")
	/**
	 * 
	 */
	public static GameRecord readManual(File file) {
		GameRecord record = null;
		try {
			FileInputStream inOne = new FileInputStream(file);
			ObjectInputStream inTwo = new ObjectInputStream(inOne);
			ManualType type = null;
			// 根据保存棋谱的格式读数据
			type = (ManualType) inTwo.readObject();
			String dateAndTime = (String) inTwo.readObject();
			String desc = (String) inTwo.readObject();
			ArrayList<ManualItem> records = (ArrayList<ManualItem>) inTwo.readObject();
			Vector<String> descs = (Vector<String>) inTwo.readObject();	
			ArrayList<Position> initLocations = null;
			if (type == ManualType.PRINT_PARTIAL) {
				initLocations = (ArrayList<Position>) inTwo.readObject();
			}
			String palyerFirst = (String) inTwo.readObject();
			record = new GameRecord(type, dateAndTime, desc, records, descs,
					initLocations, palyerFirst);

			// 及时关闭流
			inOne.close();
			inTwo.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return record;

	}
}
