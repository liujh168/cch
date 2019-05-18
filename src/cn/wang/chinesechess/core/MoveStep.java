
package cn.wang.chinesechess.core;

import java.awt.Point;
import java.io.Serializable;

/**
 * 移动一步
 * 
 * @author wanghualiang
 */
public class MoveStep implements Serializable {

	private static final long serialVersionUID = 260L;

	// 移动的起点
	public Point start;

	// 移动的终点
	public Point end;

	public MoveStep(Point start, Point end) {
		this.start = start;
		this.end = end;
	}

	public MoveStep() {
	}

	public Point getEnd() {
		return end;
	}

	public void setEnd(Point end) {
		this.end = end;
	}

	public Point getStart() {
		return start;
	}

	public void setStart(Point start) {
		this.start = start;
	}
}
