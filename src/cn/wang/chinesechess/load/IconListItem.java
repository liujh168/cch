
package cn.wang.chinesechess.load;

import javax.swing.Icon;

/**
 * 装载列表项
 * 
 * @author wanghualiang
 */
public class IconListItem {
	// 列表项的图标
	private Icon icon;

	// 列表项的文本内容
	private String text;

	/**
	 * @param icon
	 *            列表项的图标
	 * @param text
	 *            列表项的文本
	 */
	public IconListItem(Icon icon, String text) {
		this.icon = icon;
		this.text = text;
	}

	/**
	 * @return 列表项的图标
	 */
	public Icon getIcon() {
		return icon;
	}

	/**
	 * @return 列表项的文本
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param icon
	 *            列表项的图标
	 */
	public void setIcon(Icon icon) {
		this.icon = icon;
	}

	/**
	 * @param text
	 *            列表项的文本内容
	 */
	public void setText(String text) {
		this.text = text;
	}
}