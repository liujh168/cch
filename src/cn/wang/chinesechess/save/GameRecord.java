
package cn.wang.chinesechess.save;

import java.util.ArrayList;
import java.util.Vector;

import cn.wang.chinesechess.config.NAME;
import cn.wang.chinesechess.core.ManualItem;
import cn.wang.chinesechess.print.part.Position;


/**
 * 游戏记录类
 * 
 * @author wanghualiang
 */
public class GameRecord implements NAME{
	
	/**棋局的描述信息*/
	private String desc; 
	/**移动记录*/
	private ArrayList<ManualItem> records;
	/**棋谱类型*/
	private ManualType type;
	/**日期和时间*/
	private String dateAndTime; 
	/**棋谱描述*/
	private Vector<String> descs; 
	/**棋子的初始位置*/
	private ArrayList<Position> initLocations = null;
	/**下棋先手private String*/
	private String palyerFirst = null;
	

	public GameRecord(ManualType type, String dateAndTime, String desc,
			ArrayList<ManualItem> records, Vector<String> descs,
			ArrayList<Position> initLocations, String palyerFisrt) {

		this.type = type;
		this.dateAndTime = dateAndTime;
		this.records = records;
		this.descs = descs;
		this.desc = desc;
		this.initLocations = initLocations;
		this.palyerFirst = palyerFisrt;
	}

	public String getDateAndTime() {
		return dateAndTime;
	}

	public String getDesc() {
		return desc;
	}

	public Vector<String> getDescs() {
		return descs;
	}

	public ManualType getType() {
		return type;
	}

	public ArrayList<Position> getInitLocations() {
		return initLocations;
	}

	public ArrayList<ManualItem> getRecords() {
		return records;
	}

	public void setDateAndTime(String dateAndTime) {
		this.dateAndTime = dateAndTime;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void setDescs(Vector<String> descs) {
		this.descs = descs;
	}

	public void setType(ManualType type) {
		this.type = type;
	}

	public void setInitLocations(ArrayList<Position> initLocations) {
		this.initLocations = initLocations;
	}

	public void setRecords(ArrayList<ManualItem> records) {
		this.records = records;
	}

	public String getPalyerFirst() {
		return palyerFirst;
	}

	public void setPalyerFirst(String palyerFirst) {
		this.palyerFirst = palyerFirst;
	}
}