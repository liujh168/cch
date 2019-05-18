
package cn.wang.chinesechess.core;

import java.awt.Dimension;
import java.awt.Point;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import cn.wang.chinesechess.config.NAME;

/**
 * 棋子
 * 
 * @author wanghualiang
 */
public class ChessPiece extends JLabel implements Cloneable,NAME{

	private static final long serialVersionUID = 263L;

	/**PieceCategory 棋子的类别*/
	private PieceCategory category;
	/**String 黑方或红方*/
	private String colour;
	/**int 宽度*/
	private int width;
	/**int 高度*/
	private int height;
	/**PieceId 棋子的id*/
	private PieceId id;
	/**Point 棋子的坐标 1<=x<=9,1<=y<=10*/
	private Point position;

	public ChessPiece() {

	}

	/** 构造函数，初始化棋子的信息 */
	public ChessPiece(PieceId id, String name, PieceCategory category,
			int width, int height) {
		this.id = id;
		this.category = category;
		ImageIcon imageIcon = PieceUtil.getImageIcon("jiang.png");
		this.width = imageIcon.getIconWidth();
		this.height = imageIcon.getIconHeight();
		this.colour = name;

		this.setPreferredSize(new Dimension(width, height));

		switch (id) {
		case HONGJU1:
		case HONGJU2:
			setIcon(PieceUtil.getImageIcon("hongju.png"));
			break;

		case HONGMA1:
		case HONGMA2:
			setIcon(PieceUtil.getImageIcon("hongma.png"));
			break;
		case HONGPAO1:
		case HONGPAO2:
			setIcon(PieceUtil.getImageIcon("hongpao.png"));
			break;

		case HONGXIANG1:
		case HONGXIANG2:
			setIcon(PieceUtil.getImageIcon("hongxiang.png"));
			break;

		case HONGSHI1:
		case HONGSHI2:
			setIcon(PieceUtil.getImageIcon("hongshi.png"));
			break;

		case SHUAI:
			setIcon(PieceUtil.getImageIcon("shuai.png"));
			break;

		case BING1:
		case BING2:
		case BING3:
		case BING4:
		case BING5:
			setIcon(PieceUtil.getImageIcon("bing.png"));
			break;

		case JIANG:
			setIcon(PieceUtil.getImageIcon("jiang.png"));
			break;

		case HEISHI1:
		case HEISHI2:
			setIcon(PieceUtil.getImageIcon("heishi.png"));
			break;

		case HEIJU1:
		case HEIJU2:
			setIcon(PieceUtil.getImageIcon("heiju.png"));
			break;

		case HEIPAO1:
		case HEIPAO2:
			setIcon(PieceUtil.getImageIcon("heipao.png"));
			break;

		case HEIXIANG1:
		case HEIXIANG2:
			setIcon(PieceUtil.getImageIcon("heixiang.png"));
			break;

		case HEIMA1:
		case HEIMA2:
			setIcon(PieceUtil.getImageIcon("heima.png"));
			break;

		case ZU1:
		case ZU2:
		case ZU3:
		case ZU4:
		case ZU5:
			setIcon(PieceUtil.getImageIcon("zu.png"));
			break;
		}

		validate();
		repaint();
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	/**
	 * 棋子的种类(車、兵、炮等)
	 * 
	 * @return 棋子的种类(車、兵、炮等)
	 */
	public PieceCategory getCategory() {
		return category;
	}

	public PieceId getId() {
		return id;
	}

	public String getName() {
		return colour;
	}

	public void setId(PieceId id) {
		this.id = id;
	}

	public void setName(String name) {
		this.colour = name;
	}

	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
