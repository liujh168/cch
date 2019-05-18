
package cn.wang.chinesechess.core;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import cn.wang.chinesechess.ChessUtil;
import cn.wang.chinesechess.config.NAME;
import cn.wang.chinesechess.config.PropertyReader;
import cn.wang.chinesechess.print.part.PrintPartBoard;


/**
 * 棋谱类
 * 
 * 职责：保存棋谱、删除棋谱、获取棋谱、设置棋谱
 * 
 * @author ecjtu---WangHualiang
 */
public class ChessManual extends JPanel implements NAME {

	private static final long serialVersionUID = 1L;

	public JList manualList = null;// 棋谱列表

	public JScrollPane manualScroll = null;// 棋谱滚动条

	private ChessBoard board = null;// 棋盘

	private ChessPoint[][] chessPoints;// 棋子点二维数组

	private ArrayList<ManualItem> manualItems;// 移动记录

	public Vector<String> descs;// 棋谱对应的文字描述

	/**
	 * 构造函数，初始化界面
	 */
	public ChessManual(ChessBoard board) {
		this.board = board;
		this.chessPoints = board.getChessPoints();
		manualItems = new ArrayList<ManualItem>();
		descs = new Vector<String>();

		manualList = new JList();
		manualList.setFont(new Font("宋体", Font.PLAIN, 14));
		manualList.setToolTipText(PropertyReader.get("CHESS_MESSAGE_TOOLTIP"));
		// manual.setPreferredSize(new Dimension(160,180));
		manualList.setVisibleRowCount(12);
		manualList.setAutoscrolls(true);

		manualScroll = new JScrollPane(manualList);
		// manualScroll.setPreferredSize(new Dimension(200,210));
		setLayout(new BorderLayout());
		setSize(220, 260);
		add(manualScroll, BorderLayout.CENTER);
	}

	/**
	 * 记录移动棋子和被吃棋子的信息，以及移动信息
	 */
	public void addManualItem(ManualItem manualItem,String palyerFirst) {
		manualItems.add(manualItem);
		ChessPiece piece = PieceUtil.searchPieceById(manualItem
				.getMovePieceId(), chessPoints);
		MoveStep moveStep = manualItem.getMoveStep();
		Point pStart = moveStep.getStart();
		Point pEnd = moveStep.getEnd();
		String name = piece.getName();
		PieceCategory category = piece.getCategory();
		int index = 1;
		if (name.equals(RED_NAME)) {
			index = manualItems.size() / 2 + 1;
		} else {
			index = manualItems.size() / 2;
		}	
		if (board instanceof PrintPartBoard) {// 只有残局打谱，黑方先走时，才需要修改
			PrintPartBoard board2 = (PrintPartBoard) (board);
			if (board2.getPalyerFirst().equals(BLACK_NAME)) {
				if (name.equals(BLACK_NAME)) {
					index = manualItems.size() / 2 + 1;
				} else {
					index = manualItems.size() / 2;
				}
			}
		}
		int startX = (int) pStart.getX();int startY = (int) pStart.getY();
		int endX = (int) pEnd.getX();int endY = (int) pEnd.getY();
		int count = 0;String desc = name;
		// 棋谱描述4个字要对齐的，不能因为索引而没有对齐
		if (index < 10) {
			desc += "  " + index + ": ";
		} else {
			desc += " " + index + ": ";
		}
		for (int j = 1; j <= 10; j++){//与选中的棋子在同一条线上的同类棋子一共有多少个
			ChessPiece tempPiece = chessPoints[startX][j].getPiece();
			if (tempPiece!=null&&tempPiece.getCategory().equals(category)
					&&tempPiece.getName().equals(name)){
				count++;
			}
		}
		if(count==1){//只有选中的棋子
			desc += PieceUtil.catetoryToZi(category);//第一个字
			if (name.equals(RED_NAME)) {//第二个字
				desc += ChessUtil.numToZi(10 - startX);
			} else {
				desc += (" " + startX);
			}
			if (name.equals(RED_NAME)) {//第三个字
				if (startY == endY) {
					desc += "平";
				} else if (startY > endY) {
					if(palyerFirst == null || palyerFirst.equals(PALYER_FIRST)){
						desc += "进";
					}else{
						desc += "退";
					}
				} else {
					if(palyerFirst == null || palyerFirst.equals(PALYER_FIRST)){
						desc += "退";
					}else{
						desc += "进";
					}
				}
			} else if (name.equals(BLACK_NAME)) {
				if (startY == endY) {
					desc += "平";
				} else if (startY > endY) {
					if(palyerFirst == null || palyerFirst.equals(PALYER_FIRST)){
						desc += "退";
					}else{
						desc += "进";
					}
				} else {
					if(palyerFirst == null || palyerFirst.equals(PALYER_FIRST)){
						desc += "进";
					}else{
						desc += "退";
					}
				}
			}
			if (startX == endX) {// 第4个字
				if (name.equals(RED_NAME)) {
					desc += ChessUtil.numToZi(Math.abs(startY - endY));
				} else {
					desc += " " + Math.abs(startY - endY);
				}
			} else {
				if (name.equals(RED_NAME)) {
					desc += ChessUtil.numToZi(10 - endX);
				} else {
					desc += " " + endX;
				}
			}
		}
		else if(count==2){//两个相同种类的棋子
			int num = 0;
			for (int j = 1; j <= 10; j++) {
				if (j == startY) {break;}
				ChessPiece tempPiece = chessPoints[startX][j].getPiece();
				if(tempPiece!=null && tempPiece.getCategory().equals(category)
						&& tempPiece.getName().equals(name)){num++;}
			}
			if(piece.getName().equals(RED_NAME)){//选中的棋子为红色
				if(num==0){//玩家先手，此时选中的棋子在前
					if(palyerFirst == null || palyerFirst.equals(PALYER_FIRST)){
						desc += "前";
					}else{
						desc += "后";
					}
				}else{//玩家先手，此时选中的棋子在后
					if(palyerFirst == null || palyerFirst.equals(PALYER_FIRST)){
						desc += "后";
					}else{
						desc += "前";
					}
				}
				desc += PieceUtil.catetoryToZi(category);
				if (startY == endY) {
					desc += "平";
				} else if (startY > endY) {
					if(palyerFirst == null || palyerFirst.equals(PALYER_FIRST)){
						desc += "进";
					}else{
						desc += "退";
					}
				} else {
					if(palyerFirst == null || palyerFirst.equals(PALYER_FIRST)){
						desc += "退";
					}else{
						desc += "进";
					}
				}
				if(startX == endX){
					desc += ChessUtil.numToZi(Math.abs(startY - endY));
				}else{
					desc += ChessUtil.numToZi(10 - endX);
				}
			}else{//选中的棋子为黑色
				if(num==0){//玩家先手，此时选中的棋子在后
					if(palyerFirst == null || palyerFirst.equals(PALYER_FIRST)){
						desc += "后";
					}else{
						desc += "前";
					}
				}else{//玩家先手，此时选中的棋子在前
					if(palyerFirst == null || palyerFirst.equals(PALYER_FIRST)){
						desc += "前";
					}else{
						desc += "后";
					}
				}
				desc += PieceUtil.catetoryToZi(category);
				if (startY == endY) {
					desc += "平";
				} else if (startY > endY) {
					if(palyerFirst == null || palyerFirst.equals(PALYER_FIRST)){
						desc += "退";
					}else{
						desc += "进";
					}
				} else {
					if(palyerFirst == null || palyerFirst.equals(PALYER_FIRST)){
						desc += "进";
					}else{
						desc += "退";
					}
				}
				if(startX == endX){
					desc += " " + Math.abs(startY - endY);
				}else{
					desc += " " + endX;
				}
			}
		
		}
		else if(count > 2){//三个及以上相同种类的棋子，为兵或卒
			System.out.println("三个及以上相同种类的棋子，为兵或卒  234");
			int num = 0;
			for(int j = 1; j <= 10; j++){
				if (j == startY) {break;}
				ChessPiece tempPiece = chessPoints[startX][j].getPiece();
				if(tempPiece!=null && tempPiece.getCategory().equals(category)
						&& tempPiece.getName().equals(name)){num++;}
			}
			num++;
			if(category.equals(PieceCategory.BING)){
				if(palyerFirst == null || palyerFirst.equals(PALYER_FIRST)){//玩家先手
					if(num == 1){desc += "前兵";}
					else if(num == 2){desc += "二兵";}
					else if(num == 3){desc += "三兵";}
					else if(num == 4){desc += "四兵";}
					else if(num == 5){desc += "后兵";}
				}else{
					if(num == 1){desc += "后兵";}
					else if(num == 2){desc += "四兵";}
					else if(num == 3){desc += "三兵";}
					else if(num == 4){desc += "二兵";}
					else if(num == 5){desc += "前兵";}
				}
				if (startY == endY) {
					desc += "平";
				} else if (startY > endY) {
					if(palyerFirst == null || palyerFirst.equals(PALYER_FIRST)){
						desc += "进";
					}else{
						desc += "退";
					}
				} else {
					if(palyerFirst == null || palyerFirst.equals(PALYER_FIRST)){
						desc += "退";
					}else{
						desc += "进";
					}
				}
				if(startX == endX){
					desc += ChessUtil.numToZi(Math.abs(startY - endY));
				}else{
					desc += ChessUtil.numToZi(10 - endX);
				}
			}else{
				if(palyerFirst == null || palyerFirst.equals(PALYER_FIRST)){//玩家先手
					if(num == 1){desc += "后卒";}
					else if(num == 2){desc += "4卒";}
					else if(num == 3){desc += "3卒";}
					else if(num == 4){desc += "2卒";}
					else if(num == 5){desc += "前卒";}
				}else{
					if(num == 1){desc += "前卒";}
					else if(num == 2){desc += "2卒";}
					else if(num == 3){desc += "3卒";}
					else if(num == 4){desc += "4卒";}
					else if(num == 5){desc += "后卒";}
				}
				if (startY == endY) {
					desc += "平";
				} else if (startY > endY) {
					if(palyerFirst == null || palyerFirst.equals(PALYER_FIRST)){
						desc += "退";
					}else{
						desc += "进";
					}	
				} else {
					if(palyerFirst == null || palyerFirst.equals(PALYER_FIRST)){
						desc += "进";
					}else{
						desc += "退";
					}
				}
				if(startX == endX){
					desc += " " + Math.abs(startY - endY);
				}else{
					desc += " " + endX;
				}
			}
		}
		descs.add(desc);// 保存棋局描述
		manualList.setListData(descs);
		scrollToView();// 棋谱列表框滚动到最下面
	}

	/**
	 * 获得所有的棋子移动记录
	 * 
	 * @return 返回棋子移动记录的集合
	 */
	public ArrayList<ManualItem> getManualItems() {
		return manualItems;
	}

	public void setManualItems(ArrayList<ManualItem> manualItems) {
		this.manualItems = manualItems;
	}

	/**
	 * 悔棋，画标记与正常移动时不同
	 * 
	 */
	public boolean removeManualItem() {
		int size = manualItems.size();
		if (size <= 0) {return false;}
		descs.remove(size - 1);// 更新列表信息
		manualList.setListData(descs);
		ManualItem record = new ManualItem();
		MoveStep moveStep;ChessPiece eatedPiece;
		int startI = 0;int startJ = 0;
		int endI = 0;int endJ = 0;
		if (size > 0) {
			record = (ManualItem) manualItems.remove(manualItems.size() - 1);
			eatedPiece = PieceUtil.createPiece(record.getEatedPieceId());
			moveStep = record.getMoveStep();
			startI = moveStep.start.x;startJ = moveStep.start.y;
			endI = moveStep.end.x;endJ = moveStep.end.y;
			ChessPiece piece = chessPoints[endI][endJ].getPiece();
			chessPoints[startI][startJ].setPiece(piece, board);
			if (eatedPiece == null) { // 上一步没有吃棋子
				chessPoints[endI][endJ].setHasPiece(false);
			} else {// 上一步吃了棋子
				chessPoints[endI][endJ].setPiece(eatedPiece, board);
				eatedPiece.addMouseListener(board.getMouseAdapter());
			}
		}
		if (descs.size() >= 1) {
			record = (ManualItem) manualItems.get(descs.size() - 1);
			moveStep = record.getMoveStep();
			startI = moveStep.start.x;startJ = moveStep.start.y;
			endI = moveStep.end.x;endJ = moveStep.end.y;
		}
		Point start = new Point(endI, endJ);Point end = new Point(startI, startJ);
		if (descs.size() > 0) {
			board.setMoveFlag(true);board.setMoveFlagPoints(start, end);
		} else {
			board.setMoveFlag(false);
		}
		scrollToView();
		board.validate();
		board.repaint();
		return true;
	}

	private void scrollToView() {
		int lastIndex = descs.size();
		// 选中最后一行，提示玩家
		manualList.setSelectedIndex(lastIndex - 1);

		Rectangle rect = manualList.getCellBounds(lastIndex - 1, lastIndex - 1);
		if (rect == null) {
			return;
		}
		System.out.println(rect == null);
		if ((manualScroll != null) && (manualScroll.getViewport() != null)
				&& (rect != null)) {
			// 如果rect==null，则出现空指针异常
			manualScroll.getViewport().scrollRectToVisible(rect);
		}
	}
}
