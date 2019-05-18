
package cn.wang.chinesechess.core;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;

import cn.wang.chinesechess.ChessUtil;
import cn.wang.chinesechess.config.NAME;

/**
 * 棋子工具类，主要是一些查找算法。
 * 
 * 使用了享元模式
 * 
 * @author wanghualiang
 */
public final class PieceUtil implements NAME {

	private PieceUtil() {

	}

	private static HashMap<String, ImageIcon> imageIcons;

	static {
		imageIcons = new HashMap<String, ImageIcon>();
		String[] imgNames = { "bing.png", "hongju.png", "hongma.png",
				"hongxiang.png", "hongshi.png", "shuai.png", "hongpao.png",
				"zu.png", "heiju.png", "heima.png", "heixiang.png",
				"heishi.png", "heipao.png", "jiang.png" };

		int size = imgNames.length;
		for (int index = 0; index < size; index++) {
			ImageIcon imageIcon = ChessUtil.getImageIcon("piece/"
					+ imgNames[index]);
			imageIcons.put(imgNames[index], imageIcon);
		}

	}

	public static ImageIcon getImageIcon(String imgName) {
		return imageIcons.get(imgName);
	}

	/**
	 * 根据棋子id生成并返回棋子
	 * 
	 * 简单工厂模式
	 * 
	 * @param id
	 *            棋子的id
	 * 
	 * @return 生成的棋子
	 */
	public static ChessPiece createPiece(PieceId id) {
		if (id == null) {
			return null;
		}

		int pieceWidth = PIECE_WIDTH - 4;
		int pieceHeight = PIECE_HEIGHT - 4;
		switch (id) {
		case HONGJU1:
		case HONGJU2:
			return new ChessPiece(id, RED_NAME, PieceCategory.JU, pieceWidth,
					pieceHeight);

		case HONGMA1:
		case HONGMA2:
			return new ChessPiece(id, RED_NAME, PieceCategory.MA, pieceWidth,
					pieceHeight);

		case HONGPAO1:
		case HONGPAO2:
			return new ChessPiece(id, RED_NAME, PieceCategory.PAO, pieceWidth,
					pieceHeight);

		case HONGXIANG1:
		case HONGXIANG2:

			return new ChessPiece(id, RED_NAME, PieceCategory.HONGXIANG,
					pieceWidth, pieceHeight);

		case HONGSHI1:
		case HONGSHI2:
			return new ChessPiece(id, RED_NAME, PieceCategory.HONGSHI,
					pieceWidth, pieceHeight);

		case SHUAI:
			return new ChessPiece(id, RED_NAME, PieceCategory.SHUAI,
					pieceWidth, pieceHeight);

		case BING1:
		case BING2:
		case BING3:
		case BING4:
		case BING5:
			return new ChessPiece(id, RED_NAME, PieceCategory.BING, pieceWidth,
					pieceHeight);

		case JIANG:
			return new ChessPiece(id, BLACK_NAME, PieceCategory.JIANG,
					pieceWidth, pieceHeight);

		case HEISHI1:
		case HEISHI2:
			return new ChessPiece(id, BLACK_NAME, PieceCategory.HEISHI,
					pieceWidth, pieceHeight);

		case HEIJU1:
		case HEIJU2:
			return new ChessPiece(id, BLACK_NAME, PieceCategory.JU, pieceWidth,
					pieceHeight);

		case HEIPAO1:
		case HEIPAO2:
			return new ChessPiece(id, BLACK_NAME, PieceCategory.PAO,
					pieceWidth, pieceHeight);

		case HEIXIANG1:
		case HEIXIANG2:
			return new ChessPiece(id, BLACK_NAME, PieceCategory.HEIXIANG,
					pieceWidth, pieceHeight);

		case HEIMA1:
		case HEIMA2:
			return new ChessPiece(id, BLACK_NAME, PieceCategory.MA, pieceWidth,
					pieceHeight);

		case ZU1:
		case ZU2:
		case ZU3:
		case ZU4:
		case ZU5:
			return new ChessPiece(id, BLACK_NAME, PieceCategory.ZU, pieceWidth,
					pieceHeight);

		default:
			break;
		}
		return null;
	}

	/**
	 * 棋子类别类型的转换：String转换为枚举类型的
	 * 
	 * @param name
	 *            棋子类别，String类型
	 * @return 返回棋子的类别，枚举类型
	 */
	public static PieceCategory getPieceCategory(String name) {
		PieceCategory pc = null;

		if (name.equals(帥)) {
			pc = PieceCategory.SHUAI;
		} else if (name.equals(車) || name.equals("车")) {
			pc = PieceCategory.JU;
		} else if (name.equals(馬) || name.equals("马")) {
			pc = PieceCategory.MA;
		} else if (name.equals(炮)) {
			pc = PieceCategory.PAO;
		} else if (name.equals(相)) {
			pc = PieceCategory.HONGXIANG;
		} else if (name.equals(仕)) {
			pc = PieceCategory.HONGSHI;
		} else if (name.equals(士)) {
			pc = PieceCategory.HEISHI;
		} else if (name.equals(象)) {
			pc = PieceCategory.HEIXIANG;
		} else if (name.equals(將)) {
			pc = PieceCategory.JIANG;
		} else if (name.equals(兵)) {
			pc = PieceCategory.BING;
		} else if (name.equals(卒)) {
			pc = PieceCategory.ZU;
		}

		return pc;
	}

	/**
	 * 棋子类别类型的转换：枚举类型转换为String类型
	 * 
	 * @param category
	 *            棋子类别，枚举g类型
	 * @return 返回棋子的类别，String类型
	 */
	public static String catetoryToZi(PieceCategory category) {
		String name = null;
		if (category != null) {
			switch (category) {
			case JU:
				name = "車";
				break;
			case MA:
				name = "馬";
				break;
			case PAO:
				name = "炮";
				break;
			case HONGSHI:
				name = "仕";
				break;
			case HEISHI:
				name = "士";
				break;
			case HONGXIANG:
				name = "相";
				break;
			case HEIXIANG:
				name = "象";
				break;
			case JIANG:
				name = "將";
				break;
			case SHUAI:
				name = "帥";
				break;
			case BING:
				name = "兵";
				break;
			case ZU:
				name = "卒";
				break;
			}
		}

		return name;
	}

	public static ChessPiece searchPieceById(PieceId id,
			ChessPoint[][] chessPoints) {
		ChessPiece temp = null;
		for (int i = 1; i <= 9; i++) {
			for (int j = 1; j <= 10; j++) {
				if (chessPoints[i][j] == null) {
					continue;
				}
				temp = chessPoints[i][j].getPiece();
				if ((temp != null) && (temp.getId() == id)) {
					return temp;
				}
			}
		}
		return temp;

	}

	public static ArrayList<ChessPiece> searchPieceByName(String name,
			ChessPoint[][] chessPoints) {
		ArrayList<ChessPiece> pieces = new ArrayList<ChessPiece>();
		ChessPiece temp;
		for (int i = 1; i <= 9; i++) {
			for (int j = 1; j <= 10; j++) {
				temp = chessPoints[i][j].getPiece();
				if ((temp != null) && (temp.getName().equals(name))) {
					pieces.add(temp);
				}
			}
		}
		return pieces;

	}

	/**
	 * 根据棋子的种类（兵、卒、马、炮和車)获取棋子的位置）
	 * 
	 * @param category
	 *            棋子的种类
	 * @return
	 */
	public static Point[] getPositionByCategory(String playerName,
			PieceCategory category, ChessPoint[][] chessPoints) {

		Point[] points = { new Point(), new Point(), new Point(), new Point(),
				new Point() };
		int num = 0;
		for (int i = 1; i <= 9; i++) {
			for (int j = 1; j <= 10; j++) {
				ChessPiece piece = null;
				if (chessPoints[i][j].hasPiece()) {
					piece = chessPoints[i][j].getPiece();
					// 有棋子
					if (piece != null) {
						boolean flag = piece.getName().equals(playerName);

						if (flag) {
							PieceCategory category2 = piece.getCategory();
							if (category2.equals(category)) {

								Point position = piece.getPosition();
								if (position != null) {
									/*
									 * points[num] = (new Point((int) position
									 * .getX(), (int) position.getY()));
									 */
									points[num] = (new Point(i, j));
									num++;
								}

							}
						}
					}
				}
			}
		}

		return points;
	}

	/**
	 * 获取己方帅或将的位置
	 * 
	 * @param category
	 * @return
	 */
	public static Point getLeaderPosition(PieceCategory category,
			ChessPoint[][] chessPoints) {
		Point point = null;

		for (int i = 4; i <= 6; i++) {
			for (int j = 8; j <= 10; j++) {
				ChessPiece piece = chessPoints[i][j].getPiece();
				if (piece != null) {
					if (piece.getCategory().equals(category)) {
						/*
						 * Point position = piece.getPosition(); if (position !=
						 * null) { point = new Point((int) position.getX(),
						 * (int) position.getY()); return point; }
						 */
						point = new Point(i, j);
					}
				}
			}
		}

		for (int i = 4; i <= 6; i++) {
			for (int j = 1; j <= 3; j++) {
				ChessPiece piece = chessPoints[i][j].getPiece();
				if (piece != null) {
					if (piece.getCategory().equals(category)) {
						point = new Point(i, j);
					}
				}
			}
		}

		return point;
	}

	/**
	 * 打印棋子的位置
	 * 
	 * @param chessPoints
	 */
	public static void printPieceLoations(ChessPoint[][] chessPoints) {
		System.out.println();
		for (int j = 1; j <= 10; j++) {
			for (int i = 1; i <= 9; i++) {
				ChessPiece piece = chessPoints[i][j].getPiece();
				if (piece != null) {
					System.out.print("(" + i + "," + j + ")有" + piece.getId()
							+ "\t");
				}

			}
			System.out.println();
		}

	}

}
