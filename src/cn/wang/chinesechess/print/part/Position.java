
package cn.wang.chinesechess.print.part;

import java.awt.Point;
import java.io.Serializable;

import cn.wang.chinesechess.config.NAME;

/**
 * 残局打谱保存棋谱时，需要记录棋子的初始位置
 * 
 * @author wanghualiang
 */
public class Position implements Serializable,NAME {

	private static final long serialVersionUID = 1L;

	private PieceId id;//棋子的id

	private Point point;//棋子的坐标

	public PieceId getId() {
		return id;
	}

	public void setId(PieceId id) {
		this.id = id;
	}

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}

}
