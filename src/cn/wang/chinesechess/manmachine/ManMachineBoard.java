
package cn.wang.chinesechess.manmachine;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextArea;

import cn.wang.chinesechess.ChessUtil;
import cn.wang.chinesechess.ColorUtil;
import cn.wang.chinesechess.core.ChessBoard;
import cn.wang.chinesechess.core.ChessPiece;
import cn.wang.chinesechess.core.ChessRule;

/**
 * 
 * 人机对弈用的棋盘
 * 
 * @author wanghualiang
 */
public class ManMachineBoard extends ChessBoard {

	private static final long serialVersionUID = 1L;

	public JList manualList;

	public JLabel gameStatusContent;

	public JTextArea msgArea;

	public ManMachineGUI boardOwner;// 打谱时，棋盘的拥有者
	
	public ManMachineBoard() {
		
	}
	public ManMachineBoard(ManMachineGUI board) {
		boardOwner = board;
	}

	/**
	 * 初始化红方、蓝方棋子的宽度，高度和前景色
	 * 
	 */
	public void initChess(String palyerFirst) {
		playerName = RED_NAME;
		this.palyerFirst = palyerFirst;
		if (palyerFirst.equals(PALYER_FIRST)) {
			// 玩家先走棋
			if (gameStatusContent != null) {
				gameStatusContent.setText("  " + "玩家先走棋哟！");
			}

			chessPoints[1][10].setPiece(红車1, this);
			红車1.setPosition(new Point(1, 10));
			chessPoints[2][10].setPiece(红馬1, this);
			红馬1.setPosition(new Point(2, 10));
			chessPoints[3][10].setPiece(红相1, this);
			红相1.setPosition(new Point(3, 10));
			chessPoints[4][10].setPiece(红仕1, this);
			红仕1.setPosition(new Point(4, 10));
			chessPoints[5][10].setPiece(红帥, this);
			红帥.setPosition(new Point(5, 10));
			chessPoints[6][10].setPiece(红仕2, this);
			红仕2.setPosition(new Point(6, 10));
			chessPoints[7][10].setPiece(红相2, this);
			红相2.setPosition(new Point(7, 10));
			chessPoints[8][10].setPiece(红馬2, this);
			红馬2.setPosition(new Point(8, 10));
			chessPoints[9][10].setPiece(红車2, this);
			红車2.setPosition(new Point(9, 10));
			chessPoints[2][8].setPiece(红炮1, this);
			红炮1.setPosition(new Point(2, 8));
			chessPoints[8][8].setPiece(红炮2, this);
			红炮2.setPosition(new Point(8, 8));
			chessPoints[1][7].setPiece(红兵1, this);
			红兵1.setPosition(new Point(1, 7));
			chessPoints[3][7].setPiece(红兵2, this);
			红兵2.setPosition(new Point(3, 7));
			chessPoints[5][7].setPiece(红兵3, this);
			红兵3.setPosition(new Point(5, 7));
			chessPoints[7][7].setPiece(红兵4, this);
			红兵4.setPosition(new Point(7, 7));
			chessPoints[9][7].setPiece(红兵5, this);
			红兵5.setPosition(new Point(9, 7));

			chessPoints[1][1].setPiece(黑車1, this);
			黑車1.setPosition(new Point(1, 1));
			chessPoints[2][1].setPiece(黑馬1, this);
			黑馬1.setPosition(new Point(2, 1));
			chessPoints[3][1].setPiece(黑象1, this);
			黑象1.setPosition(new Point(3, 1));
			chessPoints[4][1].setPiece(黑士1, this);
			黑士1.setPosition(new Point(4, 1));
			chessPoints[5][1].setPiece(黑將, this);
			黑將.setPosition(new Point(5, 1));
			chessPoints[6][1].setPiece(黑士2, this);
			黑士2.setPosition(new Point(6, 1));
			chessPoints[7][1].setPiece(黑象2, this);
			黑象2.setPosition(new Point(7, 1));
			chessPoints[8][1].setPiece(黑馬2, this);
			黑馬2.setPosition(new Point(8, 1));
			chessPoints[9][1].setPiece(黑車2, this);
			黑車2.setPosition(new Point(9, 1));
			chessPoints[2][3].setPiece(黑炮1, this);
			黑炮1.setPosition(new Point(2, 3));
			chessPoints[8][3].setPiece(黑炮2, this);
			黑炮2.setPosition(new Point(8, 3));
			chessPoints[1][4].setPiece(黑卒1, this);
			黑卒1.setPosition(new Point(1, 4));
			chessPoints[3][4].setPiece(黑卒2, this);
			黑卒2.setPosition(new Point(3, 4));
			chessPoints[5][4].setPiece(黑卒3, this);
			黑卒3.setPosition(new Point(5, 4));
			chessPoints[7][4].setPiece(黑卒4, this);
			黑卒4.setPosition(new Point(7, 4));
			chessPoints[9][4].setPiece(黑卒5, this);
			黑卒5.setPosition(new Point(9, 4));

		} else {
			if (gameStatusContent != null) {
				gameStatusContent.setText("   " + "等待电脑走棋哟！");
			}

			chessPoints[1][10].setPiece(黑車1, this);
			黑車1.setPosition(new Point(1, 10));
			chessPoints[2][10].setPiece(黑馬1, this);
			黑馬1.setPosition(new Point(2, 10));
			chessPoints[3][10].setPiece(黑象1, this);
			黑象1.setPosition(new Point(3, 10));
			chessPoints[4][10].setPiece(黑士1, this);
			黑士1.setPosition(new Point(4, 10));
			chessPoints[5][10].setPiece(黑將, this);
			黑將.setPosition(new Point(5, 10));
			chessPoints[6][10].setPiece(黑士2, this);
			黑士2.setPosition(new Point(6, 10));
			chessPoints[7][10].setPiece(黑象2, this);
			黑象2.setPosition(new Point(7, 10));
			chessPoints[8][10].setPiece(黑馬2, this);
			黑馬2.setPosition(new Point(8, 10));
			chessPoints[9][10].setPiece(黑車2, this);
			黑車2.setPosition(new Point(9, 10));
			chessPoints[2][8].setPiece(黑炮1, this);
			黑炮1.setPosition(new Point(2, 8));
			chessPoints[8][8].setPiece(黑炮2, this);
			黑炮2.setPosition(new Point(8, 8));
			chessPoints[1][7].setPiece(黑卒1, this);
			黑卒1.setPosition(new Point(1, 7));
			chessPoints[3][7].setPiece(黑卒2, this);
			黑卒2.setPosition(new Point(3, 7));
			chessPoints[5][7].setPiece(黑卒3, this);
			黑卒3.setPosition(new Point(5, 7));
			chessPoints[7][7].setPiece(黑卒4, this);
			黑卒4.setPosition(new Point(7, 7));
			chessPoints[9][7].setPiece(黑卒5, this);
			黑卒5.setPosition(new Point(9, 7));

			chessPoints[1][1].setPiece(红車1, this);
			红車1.setPosition(new Point(1, 1));
			chessPoints[2][1].setPiece(红馬1, this);
			红馬1.setPosition(new Point(2, 1));
			chessPoints[3][1].setPiece(红相1, this);
			红相1.setPosition(new Point(3, 1));
			chessPoints[4][1].setPiece(红仕1, this);
			红仕1.setPosition(new Point(4, 1));
			chessPoints[5][1].setPiece(红帥, this);
			红帥.setPosition(new Point(5, 1));
			chessPoints[6][1].setPiece(红仕2, this);
			红仕2.setPosition(new Point(6, 1));
			chessPoints[7][1].setPiece(红相2, this);
			红相2.setPosition(new Point(7, 1));
			chessPoints[8][1].setPiece(红馬2, this);
			红馬2.setPosition(new Point(8, 1));
			chessPoints[9][1].setPiece(红車2, this);
			红車2.setPosition(new Point(9, 1));
			chessPoints[2][3].setPiece(红炮1, this);
			红炮1.setPosition(new Point(2, 3));
			chessPoints[8][3].setPiece(红炮2, this);
			红炮2.setPosition(new Point(8, 3));
			chessPoints[1][4].setPiece(红兵1, this);
			红兵1.setPosition(new Point(1, 4));
			chessPoints[3][4].setPiece(红兵2, this);
			红兵2.setPosition(new Point(3, 4));
			chessPoints[5][4].setPiece(红兵3, this);
			红兵3.setPosition(new Point(5, 4));
			chessPoints[7][4].setPiece(红兵4, this);
			红兵4.setPosition(new Point(7, 4));
			chessPoints[9][4].setPiece(红兵5, this);
			红兵5.setPosition(new Point(9, 4));
		}

	}

	private void visibleOrNot() {
		if (winkPiece != null) {
			winkPiece.setVisible(true);
		}

	}

	/**
	 * 红黑方交替
	 * 
	 */
	public void changeSide() {
		if (playerName.equals(RED_NAME)) {
			playerName = BLACK_NAME;
			boardOwner.updateGameStatus(2, "轮到黑方走喽！");

		} else {
			playerName = RED_NAME;
			boardOwner.updateGameStatus(1, "轮到红方走喽！");
		}
	}

	private class ManMachineMouseAdapter extends MouseAdapter {
		ManMachineBoard board;

		public ManMachineMouseAdapter(ManMachineBoard board) {
			this.board = board;
		}

		/**
		 * 鼠标移动到棋盘或棋子上面
		 */
		public void mouseEntered(MouseEvent e) {
			ChessPiece piece = null;
			// 轮到己方走棋，移动到己方棋子上面
			if (e.getSource() instanceof ChessPiece) {
				piece = (ChessPiece) e.getSource();
				if (piece.getName().equals(playerName)) {
					piece.setCursor(handCursor);
				} else {
					piece.setCursor(defaultCursor);
				}
			}

		}

		/**
		 * 按下鼠标
		 */
		public void mousePressed(MouseEvent e) {
			if(boardOwner.isLock||!canPaly){
				return;
			}
			// 第一次选中棋子
			if (!isSelected) {
				ChessPiece piece = null;
				Rectangle rect = null;

				// 第一次点击了棋盘，忽略不计
				if (e.getSource() == this) {
					isSelected = false;
					return;
				}

				// 第一次点击了棋子
				if (e.getSource() instanceof ChessPiece) {
					piece = (ChessPiece) e.getSource();
					// 第一次点击下棋方的棋子
					if (piece.getName().equals(playerName)) {
						System.out.println("第一次选中下棋方的棋子 282");
						isSelected = true;
						ChessUtil.playSound("select.wav");
						winkPiece = piece;
						needWink = true;
						Point p = getPiecePoint(piece);
						//得到棋子坐标
						startI = p.x;
						startJ = p.y;
						//可走提示
						board.initTipPoints(piece, startI, startJ);
					}else{
						return;
					}
				}
			}

			// 第二次点击
			else {
				boolean canMove = false;
				int endI = 0, endJ = 0;
				// 第二次点击了棋盘
				if (e.getSource() == board) {
					// 绝对坐标转化为gamePoints[][]的坐标
					double x1 = e.getX();
					double y1 = e.getY();
					for (int i = 1; i <= X; i++) {
						for (int j = 1; j <= Y; j++) {
							double x0 = chessPoints[i][j].getX();
							double y0 = chessPoints[i][j].getY();

							if ((Math.abs(x0 - x1) <= PIECE_WIDTH / 2)
									&& (Math.abs(y0 - y1) <= PIECE_HEIGHT / 2)) {
								// 终点坐标
								endI = i;
								endJ = j;
								break;
							}
						}
					}
					System.out.println("第2次点击棋盘时,终点坐标为：(" + endI + "," + endJ
							+ ") 330");

					if (endI == 0 || endJ == 0) {
						// 第2次点击时,没有点击到棋盘范围内
						return;
					}else{
						canMove = ChessRule.allRule(winkPiece, startI, startJ, endI,
								endJ, chessPoints, palyerFirst);
						if(canMove){
							System.out.println("移动棋子 339");
							//添加一条棋谱信息，要在移动之前添加，否则被吃掉的棋子无法记录
							board.addChessRecord(winkPiece, startI,
									startJ, endI, endJ);
							// 棋子在棋盘中移动
							movePiece(winkPiece, startI, startJ, endI,
									endJ);
							//在棋盘上，初始位置去除棋子
							chessPoints[startI][startJ].setHasPiece(false);
							//目标位置加载棋子
							chessPoints[endI][endJ].setPiece(winkPiece, board);
							needWink = false;
							winkPiece.setVisible(true);
							isSelected = false;
							ChessUtil.playSound("eat.wav");
							// 修改当前索引
							boardOwner.curIndex++;
							changeSide();
							boardOwner.computerThink();
						}else{
							System.out.println("第2次点击时,不符合棋子移动规则！351");
						}
					}
				}

				// 第二次点击了棋子
				else if (e.getSource() instanceof ChessPiece) {
					System.out.println("第二次点击了棋子 358");
					ChessPiece piece2 = (ChessPiece) e.getSource();
					Point point = getPiecePoint(piece2);
					endI = point.x;
					endJ = point.y;
					// 第二次点击了本方棋子
					if (piece2.getName().equals(playerName)) {
						needWink = true;
						winkPiece.setVisible(true);
						winkPiece = piece2;
						startI = endI;
						startJ = endJ;
						isSelected = true;
						ChessUtil.playSound("select.wav");
						//初始化棋子可走提示
						board.initTipPoints(piece2, startI, startJ);
					}else{//第二次点击了不同方的棋子
						canMove = ChessRule.allRule(winkPiece, startI, startJ, endI,
								endJ, chessPoints, palyerFirst);
						if (canMove) {
							ChessPiece pieceRemoved = chessPoints[endI][endJ].getPiece();
							//添加一条棋谱信息，要在移动之前添加，否则被吃掉的棋子无法记录
							board.addChessRecord(winkPiece, startI,
									startJ, endI, endJ);
							// 棋子在棋盘中移动
							movePiece(winkPiece, startI, startJ, endI,
									endJ);
							needWink = false;
							winkPiece.setVisible(true);
							isSelected = false;
							// 播放棋子被吃声音
							ChessUtil.playSound("eat.wav");
							// 打谱类当前索引+1
							boardOwner.curIndex++;
							changeSide();
							if(pieceRemoved.getCategory().equals(PieceCategory.JIANG)){
								canPaly = false;
								boardOwner.gameOver(canPaly,PieceCategory.JIANG);
								ChessUtil.playSound("gamewin.wav");
								return;
							}else if(pieceRemoved.getCategory().equals(PieceCategory.SHUAI)){
								canPaly = false;
								boardOwner.gameOver(canPaly,PieceCategory.SHUAI);
								ChessUtil.playSound("gamewin.wav");
								return;
							}
							boardOwner.computerThink();
						} else {
							return;
						}
					}
				}
			}
			validateAll();
		}

	}
	public void validateAll() {
		System.out.println("刷新！");
		validate();
		repaint();
		boardOwner.validate();
	}
	

	@Override
	protected Color getBackgroundColor() {
		return ColorUtil.getManMachineBgcolor();
	}

	@Override
	protected void drawShuXianFlag(Graphics2D g2) {
		// 显示横坐标
		for (int i = 1; i <= X; i++) {
			g2.drawString("" + i, i * UNIT_WIDTH - 4, UNIT_HEIGHT / 2 - 4);
		}

		for (int i = 1; i <= X; i++) {
			g2.drawString("" + ChessUtil.numToZi(10 - i), i * UNIT_WIDTH - 4,
					10 * UNIT_HEIGHT + 34);
		}
	}

	@Override
	protected MouseAdapter getMouseAdapter() {
		return new ManMachineMouseAdapter(this);
	}

	@Override
	protected BoardType getBoardType() {
		return  BoardType.printWhole;
	}

}
