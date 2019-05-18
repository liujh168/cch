
package cn.wang.chinesechess.core;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;

import javax.swing.JPanel;

import cn.wang.chinesechess.ChessUtil;
import cn.wang.chinesechess.ColorUtil;
import cn.wang.chinesechess.config.NAME;
import cn.wang.chinesechess.config.PropertyReader;



/**
 * 
 * 中国象棋__抽象棋盘类
 * 
 * 用到了模版方法设计模式
 * 
 * 子类如果需要更改竖线标记的画法，需要覆盖drawShuXianFlag方法
 * 
 * 子类如果需要绘制棋盘的背景图片，需要覆盖getBackgroundImage方法。
 * 
 * 子类必须实现getMouseAdapter、getBoardType、getBackgroundColor方法
 * 
 * 
 * 职责：绘制棋盘
 * 
 * @author wanghualiang
 */
public abstract class ChessBoard extends JPanel implements NAME, Runnable {

	private static final long serialVersionUID = 1L;

	// 棋盘的水平方向可容纳棋子的点的个数
	public static final int X = 9;

	// 棋盘的垂直方向可容纳棋子的点的个数
	public static final int Y = 10;

	/** ArrayList<Point> 卒、兵和炮的位置的标记*/
	protected ArrayList<Point> sidePoints = new ArrayList<Point>(14);

	/**ArrayList<Point> 当前棋子的可选走法*/
	protected ArrayList<Point> tipPoints = new ArrayList<Point>();

	/**ChessPoint 棋子点，共90个，横9*纵10*/
	public ChessPoint chessPoints[][];

	/**boolean 移动棋子时是否需要提示*/
	protected boolean moveFlag = false;
	
	/**boolean 棋盘是否可以下棋，默认可以*/
	protected boolean canPaly = true;

	/**Point[] 移动棋子提示的2个点，可以简化为MoveStep*/
	public Point[] movePoints = new Point[] { new Point(), new Point() };

	// 红方16个棋子
	public ChessPiece 红車1, 红車2, 红馬1, 红馬2, 红相1, 红相2, 红帥, 红仕1, 红仕2, 红兵1, 红兵2,
			红兵3, 红兵4, 红兵5, 红炮1, 红炮2;

	// 黑方16个棋子
	public ChessPiece 黑車1, 黑車2, 黑馬1, 黑馬2, 黑象1, 黑象2, 黑將, 黑士1, 黑士2, 黑卒1, 黑卒2,
			黑卒3, 黑卒4, 黑卒5, 黑炮1, 黑炮2;

	/**Cursor 手形光标*/
	public static Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);

	/**Cursor 默认光标*/
	public static Cursor defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);

	/**ChessPiece 闪烁的棋子*/
	protected ChessPiece winkPiece;

	/**boolean 默认没有选中棋子*/
	protected boolean isSelected = false;

	/**boolean 是否需要闪烁*/
	protected boolean needWink = false;

	/**boolean 是否可以移动棋子*/
	protected boolean move = false;

	/**Thread 闪烁的线程*/
	protected Thread winkThread;

	/**int 闪烁棋子的坐标*/
	protected int startI, startJ;
	
	public ChessManual chessManual = null;

	/**protected String 战斗方的名字，红方or黑方*/
	protected String playerName;
	/**protected String 下棋先手方 玩家or电脑,默认玩家*/
	protected String palyerFirst = null;

	/**
	 * 构造函数
	 * 
	 */
	public ChessBoard() {
		super();
		this.setLayout(null);// 不可忽视
		this.setBackground(getBackgroundColor());// 设置棋盘的背景色
		this.addMouseListener(getMouseAdapter());// 鼠标适配器

		// 初始化棋子点
		chessPoints = new ChessPoint[X + 1][Y + 1];
		for (int i = 1; i <= X; i++) {
			for (int j = 1; j <= Y; j++) {
				chessPoints[i][j] = new ChessPoint(i * UNIT_WIDTH, j
						* UNIT_HEIGHT);
			}
		}
		// 为32个棋子分配空间
		init32Pieces();
		// 初始化32个棋子的工具提示
		init32PiecesTooltip();

		// 棋谱类
		chessManual = new ChessManual(this);

		// 初始化炮和兵的位置，以便画出标记
		initPBFlag();

		winkThread = new Thread(this);
		winkThread.start();
	}

	// 默认使用默认背景色
	protected Color getBackgroundColor() {
		return ColorUtil.getDefaultBgcolor();
	}
	
	/**
	 * 初始化32个棋子的工具提示
	 * 
	 */
	private void init32PiecesTooltip() {
		红車1.setToolTipText(PropertyReader.get("JU_TOOLTIP"));
		红車2.setToolTipText(PropertyReader.get("JU_TOOLTIP"));
		黑車1.setToolTipText(PropertyReader.get("JU_TOOLTIP"));
		黑車2.setToolTipText(PropertyReader.get("JU_TOOLTIP"));

		红炮1.setToolTipText(PropertyReader.get("PAO_TOOLTIP"));
		红炮2.setToolTipText(PropertyReader.get("PAO_TOOLTIP"));
		黑炮1.setToolTipText(PropertyReader.get("PAO_TOOLTIP"));
		黑炮2.setToolTipText(PropertyReader.get("PAO_TOOLTIP"));

		红馬1.setToolTipText(PropertyReader.get("MA_TOOLTIP"));
		红馬2.setToolTipText(PropertyReader.get("MA_TOOLTIP"));
		黑馬1.setToolTipText(PropertyReader.get("MA_TOOLTIP"));
		黑馬2.setToolTipText(PropertyReader.get("MA_TOOLTIP"));

		红兵1.setToolTipText(PropertyReader.get("BING_TOOLTIP"));
		红兵2.setToolTipText(PropertyReader.get("BING_TOOLTIP"));
		红兵3.setToolTipText(PropertyReader.get("BING_TOOLTIP"));
		红兵4.setToolTipText(PropertyReader.get("BING_TOOLTIP"));
		红兵5.setToolTipText(PropertyReader.get("BING_TOOLTIP"));

		黑卒1.setToolTipText(PropertyReader.get("ZU_TOOLTIP"));
		黑卒2.setToolTipText(PropertyReader.get("ZU_TOOLTIP"));
		黑卒3.setToolTipText(PropertyReader.get("ZU_TOOLTIP"));
		黑卒4.setToolTipText(PropertyReader.get("ZU_TOOLTIP"));
		黑卒5.setToolTipText(PropertyReader.get("ZU_TOOLTIP"));

		红仕1.setToolTipText(PropertyReader.get("HONGSHI_TOOLTIP"));
		红仕2.setToolTipText(PropertyReader.get("HONGSHI_TOOLTIP"));

		黑士1.setToolTipText(PropertyReader.get("HEISHI_TOOLTIP"));
		黑士2.setToolTipText(PropertyReader.get("HEISHI_TOOLTIP"));

		黑象1.setToolTipText(PropertyReader.get("HEIXIANG_TOOLTIP"));
		黑象2.setToolTipText(PropertyReader.get("HEIXIANG_TOOLTIP"));

		红相1.setToolTipText(PropertyReader.get("HONGXIANG_TOOLTIP"));

		红相2.setToolTipText(PropertyReader.get("HONGXIANG_TOOLTIP"));

		红帥.setToolTipText(PropertyReader.get("SHUAI_TOOLTIP"));
		黑將.setToolTipText(PropertyReader.get("JIANG_TOOLTIP"));

	}

	/**
	 * 无论是对战棋盘还是打谱棋盘，都需要初始化32颗棋子，
	 * 
	 * 初始化32个棋子
	 * 
	 */
	private void init32Pieces() {
		红車1 = PieceUtil.createPiece(PieceId.HONGJU1);
		红車2 = PieceUtil.createPiece(PieceId.HONGJU2);
		红馬1 = PieceUtil.createPiece(PieceId.HONGMA1);
		红馬2 = PieceUtil.createPiece(PieceId.HONGMA2);
		红炮1 = PieceUtil.createPiece(PieceId.HONGPAO1);
		红炮2 = PieceUtil.createPiece(PieceId.HONGPAO2);
		红相1 = PieceUtil.createPiece(PieceId.HONGXIANG1);
		红相2 = PieceUtil.createPiece(PieceId.HONGXIANG2);
		红仕1 = PieceUtil.createPiece(PieceId.HONGSHI1);
		红仕2 = PieceUtil.createPiece(PieceId.HONGSHI2);
		红帥 = PieceUtil.createPiece(PieceId.SHUAI);
		红兵1 = PieceUtil.createPiece(PieceId.BING1);
		红兵2 = PieceUtil.createPiece(PieceId.BING2);
		红兵3 = PieceUtil.createPiece(PieceId.BING3);
		红兵4 = PieceUtil.createPiece(PieceId.BING4);
		红兵5 = PieceUtil.createPiece(PieceId.BING5);

		黑將 = PieceUtil.createPiece(PieceId.JIANG);
		黑士1 = PieceUtil.createPiece(PieceId.HEISHI1);
		黑士2 = PieceUtil.createPiece(PieceId.HEISHI2);
		黑車1 = PieceUtil.createPiece(PieceId.HEIJU1);
		黑車2 = PieceUtil.createPiece(PieceId.HEIJU2);
		黑炮1 = PieceUtil.createPiece(PieceId.HEIPAO1);
		黑炮2 = PieceUtil.createPiece(PieceId.HEIPAO2);
		黑象1 = PieceUtil.createPiece(PieceId.HEIXIANG1);
		黑象2 = PieceUtil.createPiece(PieceId.HEIXIANG2);
		黑馬1 = PieceUtil.createPiece(PieceId.HEIMA1);
		黑馬2 = PieceUtil.createPiece(PieceId.HEIMA2);
		黑卒1 = PieceUtil.createPiece(PieceId.ZU1);
		黑卒2 = PieceUtil.createPiece(PieceId.ZU2);
		黑卒3 = PieceUtil.createPiece(PieceId.ZU3);
		黑卒4 = PieceUtil.createPiece(PieceId.ZU4);
		黑卒5 = PieceUtil.createPiece(PieceId.ZU5);

		红帥.addMouseListener(getMouseAdapter());
		红仕1.addMouseListener(getMouseAdapter());
		红仕2.addMouseListener(getMouseAdapter());
		红相1.addMouseListener(getMouseAdapter());
		红相2.addMouseListener(getMouseAdapter());
		红車1.addMouseListener(getMouseAdapter());
		红車2.addMouseListener(getMouseAdapter());
		红馬1.addMouseListener(getMouseAdapter());
		红馬2.addMouseListener(getMouseAdapter());
		红炮1.addMouseListener(getMouseAdapter());
		红炮2.addMouseListener(getMouseAdapter());
		红兵1.addMouseListener(getMouseAdapter());
		红兵2.addMouseListener(getMouseAdapter());
		红兵3.addMouseListener(getMouseAdapter());
		红兵4.addMouseListener(getMouseAdapter());
		红兵5.addMouseListener(getMouseAdapter());

		黑將.addMouseListener(getMouseAdapter());
		黑士1.addMouseListener(getMouseAdapter());
		黑士2.addMouseListener(getMouseAdapter());
		黑象1.addMouseListener(getMouseAdapter());
		黑象2.addMouseListener(getMouseAdapter());
		黑炮1.addMouseListener(getMouseAdapter());
		黑炮2.addMouseListener(getMouseAdapter());
		黑車1.addMouseListener(getMouseAdapter());
		黑車2.addMouseListener(getMouseAdapter());
		黑馬1.addMouseListener(getMouseAdapter());
		黑馬2.addMouseListener(getMouseAdapter());
		黑卒1.addMouseListener(getMouseAdapter());
		黑卒2.addMouseListener(getMouseAdapter());
		黑卒3.addMouseListener(getMouseAdapter());
		黑卒4.addMouseListener(getMouseAdapter());
		黑卒5.addMouseListener(getMouseAdapter());
	}

	/**
	 * 绘制棋盘：10条横线、9条纵线、炮兵卒14个标记、九宫格、楚河漢界
	 * 
	 * 根据需要还绘制棋子移动的标记
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawBackgroundImage(g);

		Graphics2D g2 = (Graphics2D) g;
		// 兵、卒、炮标记笔画
		BasicStroke bsFlag = new BasicStroke(2);
		// 楚河汉界、棋盘边框笔画
		BasicStroke bsLine = new BasicStroke(2);

		// 棋盘线笔画
		BasicStroke bs1 = new BasicStroke(1);

		//绘制直线
		drawLines(g2, bsLine, bs1);
		//绘制九宫格
		drawJiuGongLines(g2, bs1);
		//绘制楚河漢界
		drawChuheHanjieString(g2);
		//绘制炮和兵标记
		drawPaoBingFlag(g2, bsFlag);

		// 如果有棋子移动，画出2个提示框，每个提示框由8条线组成
		drawMoveFlag(g2);
		drawWillMoveFlag(g2);

		// 设置字体和线宽，为画坐标做准备
		BasicStroke bsOld = new BasicStroke(1);
		g2.setStroke(bsOld);
		g2.setFont(new Font("宋体", Font.PLAIN, 14));
		g2.setColor(new Color(0, 0, 0));
		//绘制竖线标记
		drawShuXianFlag(g2);
	}
	/**
	 * 绘制背景图片
	 * @param g
	 */
	private void drawBackgroundImage(Graphics g) {
		// 默认不绘制背景图片
		Image image = getBackgroundImage();
		if (image != null) {
			Dimension size = new Dimension(super.getWidth(), super.getHeight());
			g.drawImage(image, 0, 0, size.width, size.height, null);
		}

	}

	protected Image getBackgroundImage() {
		return null;
	}

	/**
	 * 默认使用
	 * 竖线标记1到9，一到九，是按照红方在下，黑方在上绘制的
	 * 如果是按照黑方在下，红方在上绘制的，可以重写drawShuXianFlag，在方法体内调用drawShuXianFlag2
	 * @param g2
	 */
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
	/**
	 * 竖线标记一到九，1到9，是按照黑方在下，红方在上绘制的
	 * @param g2
	 */
	protected void drawShuXianFlag2(Graphics2D g2) {
		// 显示横坐标
		for (int i = 1; i <= X; i++) {
			g2.drawString("" + ChessUtil.numToZi(10 - i), i * UNIT_WIDTH - 4, UNIT_HEIGHT / 2 - 4);
		}

		for (int i = 1; i <= X; i++) {
			g2.drawString("" + i, i * UNIT_WIDTH - 4,
					10 * UNIT_HEIGHT + 34);
		}
	}

	/**
	 * 如果有棋子将要移动，在走的位置处绘制标记
	 * @param g2
	 */
	private void drawWillMoveFlag(Graphics2D g2) {
		int tipSize = tipPoints.size();
		int distance = PIECE_WIDTH - 6;

		for (int i = 0; i < tipSize; i++) {
			int a = (int) tipPoints.get(i).getX();
			int b = (int) tipPoints.get(i).getY();

			// 坐标转换
			int boardX = chessPoints[a][b].getX();
			int boardY = chessPoints[a][b].getY();

			Color color;

			color = new Color(82, 72, 255);
			BasicStroke bs = new BasicStroke(2);
			g2.setStroke(bs);
			ChessPiece piece = chessPoints[a][b].getPiece();
			if (piece != null && !piece.getName().equals(playerName)) {
				if (playerName.equals(RED_NAME)) {
					// 红方吃黑方棋子所用颜色
					color = new Color(255, 0, 0);
				} else {
					// 黑方吃红方棋子所用颜色
					color = new Color(0, 0, 0);
				}
				drawMoveTips2(g2, boardX, boardY, color);
			} else {
				// 该位置没棋子
				drawMoveTips(g2, distance, boardX, boardY, color);
			}
		}
	}

	/**
	 * 移动后的标记绘制
	 * @param g2
	 */
	private void drawMoveFlag(Graphics2D g2) {
		if (moveFlag) {
			int d = PIECE_WIDTH - 6;
			int a = 0, b = 0;
			for (int i = 0; i < movePoints.length; i++) {
				// 绘制移动标记，8条短线
				a = (int) movePoints[i].getX();
				b = (int) movePoints[i].getY();
				// 坐标转换
				int boardX = chessPoints[a][b].getX();
				int boardY = chessPoints[a][b].getY();
				Color c = new Color(0, 128, 0);
				BasicStroke bs = new BasicStroke(2);
				g2.setStroke(bs);
				drawMoveTips(g2, d, boardX, boardY, c);
			}
		}
	}

	/**
	 * 画炮和兵的位置的标记
	 * @param g2
	 * @param bsFlag
	 */
	private void drawPaoBingFlag(Graphics2D g2, BasicStroke bsFlag) { 
		int size = sidePoints.size();
		double x = PIECE_WIDTH / 9;// 棋子中心点到标记直角边交点的水平距离
		double side = PIECE_WIDTH / 6;// 标记的长度
		for (int i = 0; i < size; i++) {
			double a = sidePoints.get(i).getX();
			double b = sidePoints.get(i).getY();
			g2.setStroke(bsFlag);
			if (i >= 0 && i <= 9) {
				drawPBMiddle(g2, x, side, a, b);
			} else if (i == 10 || i == 11) {
				drawPBRight(g2, x, side, a, b);
			} else if (i == 12 || i == 13) {
				drawPBLeft(g2, x, side, a, b);
			}

		}
	}

	/**
	 * 绘制楚河、汉界
	 * @param g2
	 */
	private void drawChuheHanjieString(Graphics2D g2) { 
		g2.setFont(new Font("宋体", Font.PLAIN, 32));
		g2.drawString("漢 界", chessPoints[2][5].getX(), chessPoints[2][5].getY()
				+ 2 * UNIT_HEIGHT / 3 + 2);
		g2.drawString("楚 河", chessPoints[6][5].getX(), chessPoints[2][5].getY()
				+ 2 * UNIT_HEIGHT / 3 + 2);
	}

	/**
	 * 绘制九宫格
	 * @param g2
	 * @param bs1
	 */
	private void drawJiuGongLines(Graphics2D g2, BasicStroke bs1) {
		g2.setStroke(bs1);
		g2.drawLine(chessPoints[4][1].getX(), chessPoints[4][1].getY(),
				chessPoints[6][3].getX(), chessPoints[6][3].getY());
		g2.drawLine(chessPoints[6][1].getX(), chessPoints[6][1].getY(),
				chessPoints[4][3].getX(), chessPoints[4][3].getY());
		g2.drawLine(chessPoints[4][8].getX(), chessPoints[4][8].getY(),
				chessPoints[6][Y].getX(), chessPoints[6][Y].getY());
		g2.drawLine(chessPoints[4][Y].getX(), chessPoints[4][Y].getY(),
				chessPoints[6][8].getX(), chessPoints[6][8].getY());
	}

	/**
	 * 绘制横线和纵线
	 * @param g2
	 * @param bsLine
	 * @param bs1
	 */
	private void drawLines(Graphics2D g2, BasicStroke bsLine, BasicStroke bs1) {
		// 10条横线
		for (int j = 1; j <= Y; j++) {
			if (j == 1 || j == 5 || j == 6 || j == 10) {
				g2.setStroke(bsLine);
				g2.drawLine(chessPoints[1][j].getX(), chessPoints[1][j].getY(),
						chessPoints[X][j].getX(), chessPoints[X][j].getY());
			} else {
				g2.setStroke(bs1);
				g2.drawLine(chessPoints[1][j].getX(), chessPoints[1][j].getY(),
						chessPoints[X][j].getX(), chessPoints[X][j].getY());
			}
		}

		// 9条纵线
		for (int i = 1; i <= X; i++) {
			if (i != 1 && i != X) {
				// 中间的纵线
				g2.setStroke(bs1);
				g2.drawLine(chessPoints[i][1].getX(), chessPoints[i][1].getY(),
						chessPoints[i][Y - 5].getX(),
						chessPoints[i][Y - 5].getY());
				g2.drawLine(chessPoints[i][Y - 4].getX(),
						chessPoints[i][Y - 4].getY(), chessPoints[i][Y].getX(),
						chessPoints[i][Y].getY());
			} else {
				// 两边的纵线
				g2.setStroke(bsLine);
				g2.drawLine(chessPoints[i][1].getX(), chessPoints[i][1].getY(),
						chessPoints[i][Y].getX(), chessPoints[i][Y].getY());
			}
		}
	}
	/**
	 * 对棋子画可以吃的提示标志
	 * @param g2
	 * @param boardX
	 * @param boardY
	 * @param color
	 */
	private void drawMoveTips2(Graphics2D g2, int boardX, int boardY,
			Color color) {
		BasicStroke bs = new BasicStroke(2);
		g2.setStroke(bs);
		g2.setColor(color);
		
		g2.drawLine(boardX - PIECE_WIDTH / 2, boardY - PIECE_HEIGHT / 2, boardX
				+ PIECE_WIDTH / 2, boardY + PIECE_HEIGHT / 2);
	
		g2.drawLine(boardX - PIECE_WIDTH / 2, boardY + PIECE_HEIGHT / 2, boardX
				+ PIECE_WIDTH / 2, boardY - PIECE_HEIGHT / 2);

	}

	/**
	 * 初始化一个棋子可以走的位置
	 * @param piece
	 *            将要移动的棋子
	 * @param startX
	 *            起点横坐标
	 * @param startY
	 *            起点纵坐标
	 */
	protected void initTipPoints(ChessPiece piece, int startX, int startY) {
		BoardType boardType = getBoardType();
		if (boardType == null) {
			return;
		}
		boolean rule = false;
		for (int i = 1; i <= 9; i++) {
			for (int j = 1; j <= 10; j++) {
				if (boardType == BoardType.printWhole
						|| boardType == BoardType.manMachine
						|| boardType == BoardType.twoman
						) {
					rule = ChessRule.allRule(piece, startX, startY, i, j,
							chessPoints, palyerFirst);
				} else if (boardType == BoardType.printPartial) {
					rule = ChessRule.partialRule(piece, startX, startY, i, j,
							chessPoints, palyerFirst);
				} 
				if(rule){
					tipPoints.add(new Point(i, j));
				}
			}
		}

		repaint();
		validate();
	}

	protected abstract BoardType getBoardType();

	
	/**
	 * 对棋盘上的位置绘制棋子可以走的提示标志
	 * 
	 * @param g2
	 * @param d
	 * @param a
	 * @param b
	 */
	private void drawMoveTips(Graphics2D g2, int d, int a, int b,
			Color color) {
		g2.setColor(color);
		// 左上角的2条线
		g2.drawLine((a - d / 2),  (b - d / 2),  (a - d / 2),
				(b - d / 4));
		g2.drawLine((a - d / 2),  (b - d / 2),  (a - d / 4),
				 (b - d / 2));
		// 右上角的2条线
		g2.drawLine((a + d / 2),  (b - d / 2),  (a + d / 4),
				  (b - d / 2));
		g2.drawLine( (a + d / 2),  (b - d / 2),  (a + d / 2),
				 (b - d / 4));
		// 左下角的2条线
		g2.drawLine(  (a - d / 2),   (b + d / 2),   (a - d / 2),
				 (b + d / 4));
		g2.drawLine( (a - d / 2),  (b + d / 2),  (a - d / 4),
				 (b + d / 2));
		// 右下角的2条线
		g2.drawLine( (a + d / 2),  (b + d / 2),  (a + d / 4),
				 (b + d / 2));
		g2.drawLine( (a + d / 2),  (b + d / 2),  (a + d / 2),
				 (b + d / 4));
	}

	private void drawPBLeft(Graphics2D g2, double x, double side, double a,
			double b) {
		// 左上角
		g2.drawLine((int) (a - x), (int) (b - x), (int) (a - x),
				(int) (b - x - side));
		g2.drawLine((int) (a - x), (int) (b - x), (int) (a - x - side),
				(int) (b - x));
		// 左下角
		g2.drawLine((int) (a - x), (int) (b + x), (int) (a - x),
				(int) (b + x + side));
		g2.drawLine((int) (a - x), (int) (b + x), (int) (a - x - side),
				(int) (b + x));
	}

	private void drawPBRight(Graphics2D g2, double x, double side, double a,
			double b) {
		// 右上角
		g2.drawLine((int) (a + x), (int) (b - x), (int) (a + x),
				(int) (b - x - side));
		g2.drawLine((int) (a + x), (int) (b - x), (int) (a + x + side),
				(int) (b - x));
		// 右下角
		g2.drawLine((int) (a + x), (int) (b + x), (int) (a + x),
				(int) (b + x + side));
		g2.drawLine((int) (a + x), (int) (b + x), (int) (a + x + side),
				(int) (b + x));
	}

	private void drawPBMiddle(Graphics2D g2, double x, double side, double a,
			double b) {
		// 左上角
		g2.drawLine((int) (a - x), (int) (b - x), (int) (a - x),
				(int) (b - x - side));
		g2.drawLine((int) (a - x), (int) (b - x), (int) (a - x - side),
				(int) (b - x));
		// 左下角
		g2.drawLine((int) (a - x), (int) (b + x), (int) (a - x),
				(int) (b + x + side));
		g2.drawLine((int) (a - x), (int) (b + x), (int) (a - x - side),
				(int) (b + x));

		// 右上角
		g2.drawLine((int) (a + x), (int) (b - x), (int) (a + x),
				(int) (b - x - side));
		g2.drawLine((int) (a + x), (int) (b - x), (int) (a + x + side),
				(int) (b - x));
		// 右下角
		g2.drawLine((int) (a + x), (int) (b + x), (int) (a + x),
				(int) (b + x + side));
		g2.drawLine((int) (a + x), (int) (b + x), (int) (a + x + side),
				(int) (b + x));
	}

	/**
	 * 初始化炮和兵、卒的标记的位置
	 * 
	 */
	private void initPBFlag() {
		// 4个炮的位置
		sidePoints.add(new Point(chessPoints[2][3].getX(), chessPoints[2][3]
				.getY()));
		sidePoints.add(new Point(chessPoints[8][3].getX(), chessPoints[8][3]
				.getY()));
		sidePoints.add(new Point(chessPoints[2][8].getX(), chessPoints[2][8]
				.getY()));
		sidePoints.add(new Point(chessPoints[8][8].getX(), chessPoints[8][8]
				.getY()));

		// 3个兵、3个卒
		sidePoints.add(new Point(chessPoints[3][4].getX(), chessPoints[3][4]
				.getY()));
		sidePoints.add(new Point(chessPoints[5][4].getX(), chessPoints[5][4]
				.getY()));
		sidePoints.add(new Point(chessPoints[7][4].getX(), chessPoints[7][4]
				.getY()));
		sidePoints.add(new Point(chessPoints[3][7].getX(), chessPoints[3][7]
				.getY()));
		sidePoints.add(new Point(chessPoints[5][7].getX(), chessPoints[5][7]
				.getY()));
		sidePoints.add(new Point(chessPoints[7][7].getX(), chessPoints[7][7]
				.getY()));

		// 左边：1个兵+ 1个卒；右边：1个兵+ 1个卒
		sidePoints.add(new Point(chessPoints[1][4].getX(), chessPoints[1][4]
				.getY()));
		sidePoints.add(new Point(chessPoints[1][7].getX(), chessPoints[1][7]
				.getY()));
		sidePoints.add(new Point(chessPoints[9][4].getX(), chessPoints[9][4]
				.getY()));
		sidePoints.add(new Point(chessPoints[9][7].getX(), chessPoints[9][7]
				.getY()));

	}

	/**
	 * 查找棋子的纵坐标
	 * 
	 * @param x
	 *            棋子的横坐标
	 * @param pc
	 *            棋子的类别
	 * @return 返回棋子的纵坐标，否则返回 0
	 */
	protected int getPieceYByCategory(int x, PieceCategory pc) {
		if (pc == null) {
			System.out.println("getPieceYByCategory:pc == null");
			return 0;
		}
		int y = 0;
		if (x < 1 || x > 9) {
			return y;
		}

		for (int j = 1; j <= Y; j++) {
			ChessPiece piece = chessPoints[x][j].getPiece();
			if (piece != null && pc.equals(piece.getCategory())
					&& piece.getName().equals(playerName)) {
				System.out.println(playerName + piece.getCategory() + x + ","
						+ +j);
				y = j;
				break;
			}
		}
		return y;

	}

	/**
	 * 根据棋谱，如馬八进七，获得移动的棋子
	 * 
	 * @param manual
	 *            棋谱，如馬八进七
	 * @return 移动的棋子。
	 */
	public ChessPiece getMovePiece(String manual) {
		if (manual == null || manual.length() != 4) {
			return null;
		}

		Point point = getStartPosition(manual);
		int startX = (int) point.getX();
		int startY = (int) point.getY();

		if (startX < 1 || startX > 9 || startY < 1 || startY > 10) {
			return null;
		}
		ChessPiece piece = chessPoints[startX][startY].getPiece();
		System.out.println("移动的棋子：" + piece.getName() + " "
				+ piece.getCategory());
		return piece;

	}
	
	
	/**
	 * 根据棋谱，如馬八进七，获得移动棋子的起始坐标
	 * 
	 * @param manual棋谱
	 *            ，如馬八进七
	 * @return 移动棋子的起始坐标。
	 */
	public Point getStartPosition(String manual) {
		char name = manual.charAt(0);PieceCategory pc = getPieceCategory(manual);
		if (name == '前' || name == '后' || name == '二'
				|| name == '三' || name == '四' || name == '2'
				|| name == '3' || name == '4' ) {
			ArrayList<Point> list = new ArrayList<Point>(5);
			for (int i = 1; i <= X; i++) {
				for (int j = 1; j <= Y; j++) {
					ChessPiece piece = chessPoints[i][j].getPiece();
					if (piece != null && piece.getCategory().equals(pc)
							&& piece.getName().equals(playerName)) {
						list.add(new Point(i, j));
					}
				}
			}
			if(playerName.equals(RED_NAME)){
				if(palyerFirst == null || palyerFirst.equals(PALYER_FIRST)){
					if(name == '前'){return list.get(0);}
					else if(name == '二'){return list.get(1);}
					else if(name == '三'){return list.get(2);}
					else if(name == '四'){return list.get(3);}
					else if(name == '后'){return list.get(4);}
				}else{
					if(name == '前'){return list.get(4);}
					else if(name == '二'){return list.get(3);}
					else if(name == '三'){return list.get(2);}
					else if(name == '四'){return list.get(1);}
					else if(name == '后'){return list.get(0);}
				}
			}else{
				if(palyerFirst == null || palyerFirst.equals(PALYER_FIRST)){
					if(name == '前'){return list.get(4);}
					else if(name == '2'){return list.get(3);}
					else if(name == '3'){return list.get(2);}
					else if(name == '4'){return list.get(1);}
					else if(name == '后'){return list.get(0);}
				}else{
					if(name == '前'){return list.get(0);}
					else if(name == '2'){return list.get(1);}
					else if(name == '3'){return list.get(2);}
					else if(name == '4'){return list.get(3);}
					else if(name == '后'){return list.get(4);}
				}
			}

		}
		int startX = ChessUtil.ziToNum(manual.substring(1, 2));
		if (playerName.equals(RED_NAME)) {
			startX = 10 - startX;
		}
		int startY = getPieceYByCategory(startX, pc);
		return new Point(startX, startY);
	}
	/**
	 * 根据棋谱，如馬八进七，获得移动棋子的终点坐标
	 * 
	 * @param manual
	 *            棋谱，如馬八进七
	 * @return 移动棋子的终点坐标。
	 */
	public Point getEndPosition(String manual) {
		Point pStart = getStartPosition(manual);
		String third = manual.substring(2, 3);// 第3个字
		String fourth = String.valueOf((manual.charAt(3)));// 第4个字
		PieceCategory pc = getPieceCategory(manual);
		int endX = 0;int endY = 0;
		int jbzpjs = ChessUtil.ziToNum(fourth);
		int msx = jbzpjs;
		if (playerName.equals(RED_NAME)) {
			msx = 10 - msx;
		}
		if (third.equals("进")) {
			switch (pc) {
			case JU:
			case BING:
			case ZU:
			case PAO:
			case JIANG:
			case SHUAI:
				endX = (int) pStart.getX();
				if (playerName.equals(RED_NAME)) {
					if(palyerFirst == null || palyerFirst.equals(PALYER_FIRST)){
						endY = (int) (pStart.getY() - jbzpjs);
					}else{
						endY = (int) (pStart.getY() + jbzpjs);
					}
					
				} else {
					if(palyerFirst == null || palyerFirst.equals(PALYER_FIRST)){
						endY = (int) (pStart.getY() + jbzpjs);
					}else{
						endY = (int) (pStart.getY() - jbzpjs);
					}
				}
				break;
			case MA:
				endX = msx;
				int startX = (int) pStart.getX();
				int xDistance = Math.abs(startX - endX);
				if (playerName.equals(RED_NAME)) {
					if(palyerFirst == null || palyerFirst.equals(PALYER_FIRST)){
						if (xDistance == 1) {
							endY = (int) (pStart.getY() - 2);
						} else {
							endY = (int) (pStart.getY() - 1);
						}
					}else{
						if (xDistance == 1) {
							endY = (int) (pStart.getY() + 2);
						} else {
							endY = (int) (pStart.getY() + 1);
						}
					}
					
				} else {
					if(palyerFirst == null || palyerFirst.equals(PALYER_FIRST)){
						if (xDistance == 1) {
							endY = (int) (pStart.getY() + 2);
						} else {
							endY = (int) (pStart.getY() + 1);
						}
					}else{
						if (xDistance == 1) {
							endY = (int) (pStart.getY() - 2);
						} else {
							endY = (int) (pStart.getY() - 1);
						}
					}
				}
				break;
			case HONGSHI:
				endX = msx;
				if(palyerFirst == null || palyerFirst.equals(PALYER_FIRST)){
					endY = (int) (pStart.getY() - 1);
				}else{
					endY = (int) (pStart.getY() + 1);
				}
				break;
			case HEISHI:
				endX = msx;
				if(palyerFirst == null || palyerFirst.equals(PALYER_FIRST)){
					endY = (int) (pStart.getY() + 1);
				}else{
					endY = (int) (pStart.getY() - 1);
				}
				break;
			case HEIXIANG:
				endX = msx;
				if(palyerFirst == null || palyerFirst.equals(PALYER_FIRST)){
					endY = (int) (pStart.getY() + 2);
				}else{
					endY = (int) (pStart.getY() - 2);
				}
				break;
			case HONGXIANG:
				endX = msx;
				if(palyerFirst == null || palyerFirst.equals(PALYER_FIRST)){
					endY = (int) (pStart.getY() - 2);
				}else{
					endY = (int) (pStart.getY() + 2);
				}
				break;

			}
		} else if (third.equals("退")) {
			switch (pc) {
			case JU:
			case ZU:
			case BING:
			case PAO:
			case JIANG:
			case SHUAI:
				if (playerName.equals(RED_NAME)) {
					if(palyerFirst == null || palyerFirst.equals(PALYER_FIRST)){
						endY = (int) (pStart.getY() + jbzpjs);
					}else{
						endY = (int) (pStart.getY() - jbzpjs);
					}
					
				} else {
					if(palyerFirst == null || palyerFirst.equals(PALYER_FIRST)){
						endY = (int) (pStart.getY() - jbzpjs);
					}else{
						endY = (int) (pStart.getY() + jbzpjs);
					}
				}
				break;
			case MA:
				endX = msx;
				int startX = (int) pStart.getX();
				int xDistance = Math.abs(startX - endX);
				if (playerName.equals(RED_NAME)) {
					if(palyerFirst == null || palyerFirst.equals(PALYER_FIRST)){
						if (xDistance == 1) {
							endY = (int) (pStart.getY() + 2);
						} else {
							endY = (int) (pStart.getY() + 1);
						}
					}else{
						if (xDistance == 1) {
							endY = (int) (pStart.getY() - 2);
						} else {
							endY = (int) (pStart.getY() - 1);
						}
					}
				} else {
					if(palyerFirst == null || palyerFirst.equals(PALYER_FIRST)){
						if (xDistance == 1) {
							endY = (int) (pStart.getY() - 2);
						} else {
							endY = (int) (pStart.getY() - 1);
						}
					}else{
						if (xDistance == 1) {
							endY = (int) (pStart.getY() + 2);
						} else {
							endY = (int) (pStart.getY() + 1);
						}
					}
				}
				break;
			case HONGSHI:
				endX = msx;
				if(palyerFirst == null || palyerFirst.equals(PALYER_FIRST)){
					endY = (int) (pStart.getY() + 1);
				}else{
					endY = (int) (pStart.getY() - 1);
				}
				break;
			case HEISHI:
				endX = msx;
				if(palyerFirst == null || palyerFirst.equals(PALYER_FIRST)){
					endY = (int) (pStart.getY() - 1);
				}else{
					endY = (int) (pStart.getY() + 1);
				}
				break;
			case HEIXIANG:
				endX = msx;
				if(palyerFirst == null || palyerFirst.equals(PALYER_FIRST)){
					endY = (int) (pStart.getY() - 2);
				}else{
					endY = (int) (pStart.getY() + 2);
				}
				break;
			case HONGXIANG:
				endX = msx;
				if(palyerFirst == null || palyerFirst.equals(PALYER_FIRST)){
					endY = (int) (pStart.getY() + 2);
				}else{
					endY = (int) (pStart.getY() - 2);
				}
				break;
			}
		} else if (third.equals("平")) {
			endX = msx;
			endY = (int) pStart.getY();
		}
		return new Point(endX, endY);
	}

	/**
	 * 根据棋谱，如馬八进七，获取移动棋子的类型
	 * 
	 * @param manual
	 *            棋谱，如馬八进七
	 * @return 移动棋子的类型
	 */
	private PieceCategory getPieceCategory(String manual) {
		String name = manual.substring(0, 1);
		if (name.equals("前") || name.equals("后") ||
				name.equals("二") || name.equals("三") || name.equals("四") || 
				name.equals("2") || name.equals("3") || name.equals("4")) {
			name = manual.substring(1, 2);
		}

		return PieceUtil.getPieceCategory(name);
	}
	
	/**
	 * 得到棋盘中选中棋子的坐标棋子
	 * @param piece 选中的棋子
	 * @return 如果棋子不在棋盘,返回null,在返回棋子坐标
	 */
	public Point getPiecePoint(ChessPiece piece){
		for (int i = 1; i <= X; i++) {
			for (int j = 1; j <= Y; j++) {
				ChessPiece p = chessPoints[i][j].getPiece();
				if (p != null && chessPoints[i][j].hasPiece()
						&& p.equals(piece)) {
					
					return new Point(i, j);
				}
			}
		}
		return null;
	}
	/**
	 * 将点击棋盘的坐标转化为棋盘上的坐标，即可以放棋子的坐标
	 * @param x 点击点的横坐标
	 * @param y 点击点的纵坐标
	 * @return 如果点击的点可以转化，返回坐标，否则返回null
	 */
	public Point getBoardPoint(double x,double y){
		for (int i = 1; i <= X; i++) {
			for (int j = 1; j <= Y; j++) {
				double x0 = chessPoints[i][j].getX();
				double y0 = chessPoints[i][j].getY();

				if ((Math.abs(x0 - x) <= PIECE_WIDTH / 2)
						&& (Math.abs(y0 - y) <= PIECE_HEIGHT / 2)) {
					// 终点坐标
					return new Point(i,j);
				}
			}
		}
		return null;
	}
	/**
	 * 棋子闪烁线程
	 */
	@Override
	public void run() {
		while (true) {
			try {
				if (needWink) {
					//System.out.println("needWink:"+needWink);
					winkPiece.setVisible(false);
					Thread.sleep(600);
					winkPiece.setVisible(true);
					Thread.sleep(600);
				} else {
					//不闪烁时缓解线程
					//System.out.println("needWink:"+needWink);
					Thread.sleep(400);
				}
			} catch (InterruptedException ex) {
				break;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void clearTipPoints() {
		tipPoints.clear();
	}

	public String getPlayerName() {
		return playerName;
	}

	public Thread getWinkThread() {
		return winkThread;
	}

	public ChessPoint[][] getChessPoints() {
		return chessPoints;
	}
	public void setChessPoints(ChessPoint[][] chessPoints) {
		this.chessPoints = chessPoints;
	}
	public void addPiece(ChessPiece piece) {
		if (piece == null) {
			return;
		}
		add(piece);
	}

	public void removePiece(ChessPiece piece) {
		if (piece == null) {
			return;
		}
		remove(piece);

	}

	public void setMoveFlag(boolean moveFlag) {
		this.moveFlag = moveFlag;

	}

	public void setMoveFlagPoints(Point start, Point end) {
		movePoints[0] = start;
		movePoints[1] = end;
	}

	protected abstract MouseAdapter getMouseAdapter();

	/**
	 * 移动棋子
	 * 
	 * @param movePiece
	 * @param startX
	 * @param startY
	 * @param endX
	 * @param endY
	 */
	public void movePiece(ChessPiece movePiece, int startX, int startY,
			int endX, int endY) {
		System.out.println("想要移动棋子，坐标：(" + startX + "," + startY + "),(" + endX
				+ "," + endY + ")");
		ChessPiece pieceRemoved = chessPoints[endX][endY].getPiece();
		if (pieceRemoved != null) {
			chessPoints[endX][endY].removePiece(pieceRemoved, this);
		}
		chessPoints[endX][endY].setPiece(movePiece, this);
		chessPoints[startX][startY].setHasPiece(false);
		System.out.println("1129移动：" + movePiece.getCategory());

		setMoveFlag(true);
		movePoints[0] = new Point(endX, endY);
		movePoints[1] = new Point(startX, startY);
		clearTipPoints();

		validate();
		repaint();

	}

	/**
	 * 增加一条棋谱记录
	 * 
	 * @param movePiece
	 * @param startX
	 * @param startY
	 * @param endX
	 * @param endY
	 */
	public void addChessRecord(ChessPiece movePiece, int startX, int startY,
			int endX, int endY) {

		if (movePiece == null) {
			System.out.println("movePiece == null");
			return;
		}
		ManualItem moveRecord = new ManualItem();
		ChessPiece pieceRemoved = chessPoints[endX][endY].getPiece();

		moveRecord.setMovePieceId(movePiece.getId());
		if (pieceRemoved != null) {
			moveRecord.setEatedPieceId(pieceRemoved.getId());
		} else {
			moveRecord.setEatedPieceId(null);
		}

		moveRecord.setMoveStep(new MoveStep(new Point(startX, startY),
				new Point(endX, endY)));
		chessManual.addManualItem(moveRecord,palyerFirst);

	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setNeedWink(boolean needWink) {
		this.needWink = needWink;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public String getPalyerFirst() {
		return palyerFirst;
	}

	public void setPalyerFirst(String palyerFirst) {
		this.palyerFirst = palyerFirst;
	}
}
