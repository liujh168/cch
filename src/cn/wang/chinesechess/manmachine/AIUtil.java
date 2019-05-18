
package cn.wang.chinesechess.manmachine;

import java.awt.Point;
import java.util.ArrayList;

import cn.wang.chinesechess.config.NAME;
import cn.wang.chinesechess.core.ChessPiece;
import cn.wang.chinesechess.core.ChessPoint;
import cn.wang.chinesechess.core.ChessRule;
import cn.wang.chinesechess.core.ManualItem;
import cn.wang.chinesechess.core.MoveStep;


/**
 * 人机对弈生成走法
 * 
 * @author wanghualiang
 */
public final class AIUtil implements NAME, AIConstants {
	private static int X = 9;
	private static int Y = 10;
	private AIUtil() {
	}

	/**
	 * 产生一个棋子的所有走法
	 * 
	 * @param piece 
	 * @param startX 
	 * @param startY
	 * @param chessPoints
	 * @param palyFirst
	 * @return 一个棋子的所有走法
	 */
	public static ArrayList<Point> generateChessMoveByPiece(ChessPiece piece,
			int startX, int startY, ChessPoint[][] chessPoints, String palyFirst) {
		ArrayList<Point> points = new ArrayList<Point>();
		/*
		 * 使用2重循环，代码最简介 如果为了提高效率，可以根据棋子的类别，分别编写相应的算法，工作量很大
		 */
		boolean rule = false;
		for (int i = 1; i <= 9; i++) {
			for (int j = 1; j <= 10; j++) {
				rule = ChessRule.allRule(piece, startX, startY, i, j,
						chessPoints, palyFirst);
				if (rule) {
					points.add(new Point(i, j));
				}
			}

		}
		return points;
	}

	/**
	 * 产生一个局面的全部走法
	 * 
	 * @param color
	 *            走棋一方，红方或黑方
	 * @param chessPoints
	 *            棋子点2维数组
	 * @param palyFirst
	 * 			  走棋先手方
	 * @return 一个局面的全部走法
	 */
	public static ArrayList<ManualItem> generateAllChessMove(String color,
			ChessPoint[][] chessPoints, String palyFirst) {
		ArrayList<ManualItem> chessMoves = new ArrayList<ManualItem>();
		ArrayList<PiecePosition> lists = getPiecePositionByColoar(color,
				chessPoints);

		int size = lists.size();
		// System.out.println("棋子数：" + size);

		for (int index = 0; index < size; index++) {
			PiecePosition record = lists.get(index);
			ChessPiece piece = record.piece;
			Point pos = record.pos;
			/*
			 * System.out.print("棋子：" + piece.getId() + "(" + pos.getX() + "," +
			 * pos.getY() + ")");
			 */
			ArrayList<Point> points = AIUtil.generateChessMoveByPiece(piece,
					pos.x, pos.y, chessPoints, palyFirst);

			int size2 = points.size();
			int endX = 0;
			int endY = 0;
			Point temp = null;
			// System.out.println("可行的走法有：" + size2 + "个");
			for (int j = 0; j < size2; j++) {
				temp = points.get(j);
				MoveStep moveStep = new MoveStep(pos, temp);
				endX = (int) temp.getX();
				endY = (int) temp.getY();
				ManualItem chessMove = new ManualItem();
				chessMove.setMoveStep(moveStep);
				if(chessPoints[endX][endY]!=null&&
						chessPoints[endX][endY].getPiece()!=null){
					chessMove.setEatedPieceId(chessPoints[endX][endY].getPiece().getId());
				}
				chessMoves.add(chessMove);
			}
		}

		return chessMoves;
	}
	/**
	 * 根据棋子名称得到棋盘上的棋子和位置
	 * @param coloar 
	 * @param chessPoints
	 * @return
	 */
	public static ArrayList<PiecePosition> getPiecePositionByColoar(String coloar,
			ChessPoint[][] chessPoints) {
		ArrayList<PiecePosition> lists = new ArrayList<PiecePosition>();

		for (int i = 1; i <= X; i++) {
			for (int j = 1; j <= Y; j++) {
				ChessPiece piece = chessPoints[i][j].getPiece();
				
				if (piece != null && piece.getName().equals(coloar)) {
					PiecePosition record = new PiecePosition();
					record.piece = piece;
					record.pos = new Point(i, j);
					lists.add(record);
				}
			}
		}
		return lists;
	}
	/**
	 * 得到走棋方所有可用棋子的信息
	 * @param side 走棋方 红方或黑方
	 * @param chessPoints 棋子的二维数组
	 * @return
	 */
	public static ArrayList<ChessPiece> getPieceBySide(String side,
			ChessPoint[][] chessPoints) {
		ArrayList<ChessPiece> lists = new ArrayList<ChessPiece>();

		for (int i = 1; i <= X; i++) {
			for (int j = 1; j <= Y; j++) {
				ChessPiece piece = chessPoints[i][j].getPiece();
				if (piece != null && piece.getName().equals(side)) {
					lists.add(piece);
				}
			}
		}
		return lists;
	}

	/**
	 * 对当前局面进行估值
	 * @param playerName 走棋方，红方或黑方
	 * @param chessPoints
	 * @param palyFirst 先手方，用于判断棋局布局，上黑下红，或上红下黑
	 * @return 当前局面，电脑方相对于玩家的优势
	 */
	public static int evaluate(String playerName, ChessPoint[][] chessPoints, String palyFirst) {
		int rValue = 0;
		int bValue = 0;
		for (int i = 1; i <= X; i++) {
			for (int j = 1; j <= Y; j++) {
				ChessPiece piece = chessPoints[i][j].getPiece();
				if (piece != null) {
					int index = pieceIdToIndex(piece.getId());
					String color = piece.getName();
					int value = getPositionValue(piece, i, j,palyFirst) + pieceValues[index];
					if (color.equals(RED_NAME)) {
						rValue += value;
					} else {
						bValue += value;
					}
				}
			}
		}
		int result = 0;
		if (palyFirst.equals(COMPUTER_FIRST)){//电脑先手，红方为电脑方，红方相对黑方优势
			result = rValue - bValue + getFlexibleValueAll(chessPoints, palyFirst);
		} else {//电脑后手，黑方为电脑方，黑方相对红方优势
			result = bValue - rValue + getFlexibleValueAll(chessPoints, palyFirst);
		}
		return result;
	}

	/**
	 * 对当前局面进行估值
	 * 
	 * @param chessPoints
	 * @return 当前局面，红方相对黑方的优势
	 */
	public static int evaluate(String playerName, ChessPoint[][] chessPoints) {
		int wValue = 0;
		int bValue = 0;
		for (int i = 1; i <= X; i++) {
			for (int j = 1; j <= Y; j++) {
				ChessPiece piece = chessPoints[i][j].getPiece();
				if (piece != null) {
					String color = piece.getName();
					int pieceIdToIndex = pieceIdToIndex(piece.getId());
					if (color.equals(RED_NAME)) {

						wValue += pieceValues[pieceIdToIndex];
					} else {
						bValue += pieceIdToIndex;
					}
				}
			}
		}
		int result = 0;
		if (playerName.equals(RED_NAME)) {
			result = wValue - bValue;
		} else {
			result = bValue - wValue;
		}
		return result;

	}

	/**
	 * 將棋子的id转换成 棋子值（位置值、灵活性值）数组的索引
	 * 
	 * @param id
	 *            棋子的id
	 * @return 数组的索引
	 */
	public static int getPieceValue(PieceId id) {
		int index = pieceIdToIndex(id);
		if (index < 0 || index > 6) {
			return -1;
		}
		return pieceValues[index];
	}

	/**
	 * 根据棋子的位置，获取棋子的位置值
	 * 
	 * @param piece
	 * @param x
	 *            数学意义上的横坐标
	 * @param y
	 *            数学意义上的纵坐标
	 * @return 获取棋子的位置值
	 */
	public static int getPositionValue(ChessPiece piece, int x, int y,String palyerFirst) {
		String name = piece.getName();
		int redOrBlack = -1;
		if(palyerFirst.equals(PALYER_FIRST)){
			if (name.equals(RED_NAME)) {
				redOrBlack = 0;
			} else {
				redOrBlack = 1;
			}
		}else{
			if (name.equals(RED_NAME)) {
				redOrBlack = 1;
			} else {
				redOrBlack = 0;
			}
		}
		int index = pieceIdToIndex(piece.getId());
		// System.out.println(redOrBlack+" "+index+" "+piece.getId()+"
		// getPositionValue"+"x="+x+"y="+y);
		// 此处需要注意，数学上的坐标和数组的坐标不一致
		return positionValues[redOrBlack][index][y-1][x-1];
	}

	/**
	 * 获取一个局面的灵活性分值
	 * @param chessPoints 棋子点数组
	 * @param palyFirst 走棋先手方,电脑或玩家
	 * @return棋子的灵活性分值
	 */
	public static int getFlexibleValueAll(ChessPoint[][] chessPoints, String palyFirst) {
		int rValue = 0;
		int bValue = 0;
		for (int i = 1; i <= X; i++) {
			for (int j = 1; j <= Y; j++) {
				ChessPiece piece = chessPoints[i][j].getPiece();
				if (piece != null) {
					if(piece.getName().equals(RED_NAME)){
						rValue += getFlexibleValue(piece,i,j,chessPoints,palyFirst);
					}else{
						bValue += getFlexibleValue(piece,i,j,chessPoints,palyFirst);
					}
				}
			}
		}
		if (PALYER_FIRST.equals(COMPUTER_FIRST)){//电脑先手，为红方，红方相对黑方优势
			return rValue - bValue;
		} else {//电脑后手，为黑方，黑方相对红方优势
			return bValue - rValue;
		}
	}
	/**
	 * 获取一个棋子的灵活性分值
	 * @param piece 棋子
	 * @param startX 棋子起始位置横坐标
	 * @param startY 棋子起始位置纵坐标
	 * @param chessPoints 棋子点数组
	 * @param palyFirst 走棋先手方,电脑或玩家
	 * @return棋子的灵活性分值
	 */
	public static int getFlexibleValue(ChessPiece piece, int startX, int startY, 
			ChessPoint[][] chessPoints, String palyFirst) {
		int redValue = 0;
		int blackValue = 0;
		int num = 0;
		int index = pieceIdToIndex(piece.getId());
		for (int i = 1; i <= X; i++) {
			for (int j = 1; j <= Y; j++) {
				num = 0;
				boolean canMove = ChessRule.allRule(piece, startX, startY, i,
						j, chessPoints, palyFirst);
				if(canMove){
					num++;
				}
			}
		}
		return flexibleValues[index] + num;
	}

	/**
	 * 將棋子的id转换成索引
	 * 
	 * @param id
	 * @return 將棋子的id转换成索引
	 */
	public static int pieceIdToIndex(PieceId id) {
		int index = 0;

		switch (id) {

		case SHUAI:

		case JIANG:
			index = 0;
			break;

		case HONGJU1:
		case HONGJU2:

		case HEIJU1:
		case HEIJU2:
			index = 1;
			break;

		case HONGPAO1:
		case HONGPAO2:

		case HEIPAO1:
		case HEIPAO2:
			index = 2;
			break;

		case HONGSHI1:
		case HONGSHI2:

		case HONGMA1:
		case HONGMA2:

		case HEIMA1:
		case HEIMA2:
			index = 3;
			break;

		case HONGXIANG1:
		case HONGXIANG2:

		case HEIXIANG1:
		case HEIXIANG2:
			index = 4;
			break;

		case HEISHI1:
		case HEISHI2:
			index = 5;
			break;

		case BING1:
		case BING2:
		case BING3:
		case BING4:
		case BING5:

		case ZU1:
		case ZU2:
		case ZU3:
		case ZU4:
		case ZU5:

			index = 6;
			break;
		}
		return index;

	}

}
