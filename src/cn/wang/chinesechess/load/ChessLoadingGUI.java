
package cn.wang.chinesechess.load;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import cn.wang.chinesechess.ChessUtil;
import cn.wang.chinesechess.config.NAME;
import cn.wang.chinesechess.config.PropertyReader;
import cn.wang.chinesechess.core.ManualUtil;
import cn.wang.chinesechess.save.GameRecord;



/**
 * 装载列表类 显示最近的棋谱信息
 * 
 * @author wanghualinag
 */
public class ChessLoadingGUI extends JFrame implements ActionListener, NAME {
	private static final long serialVersionUID = 121L;

	/** 装载游戏、退出游戏*/
	private JButton load, exit;

	/** 棋谱列表框*/
	private JList manualList;

	/** 列表框使用的模型*/
	private DefaultListModel listModel = new DefaultListModel();

	/** 棋谱记录的集合*/
	private ArrayList<GameRecord> chessRecords = new ArrayList<GameRecord>();

	/** 棋谱描述文本域*/
	private JTextArea descArea;

	/** 当前目录*/
	private File curFile;

	/** 顶层目录*/
	private File topFile;

	/**
	 * 构造函数
	 */
	public ChessLoadingGUI() {
		initList();// 读入棋谱文件
		initButtons();// 初始化按钮
		initPanels();// 初始化界面
		showGUI();// //显示界面
	}

	/**
	 * 设置界面的属性(大小，标题，位置，图标等)，然后显示。
	 */
	private void showGUI() {
		this.setSize(680, 650);
		this.setTitle("中国象棋---棋谱列表 ");
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setIconImage(ChessUtil.getAppIcon());
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		// 响应默认的退出事件
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				handleExitGame();
			}
		});

	}

	/**
	 * 读入棋谱文件
	 * 
	 */
	@SuppressWarnings("unchecked")
	private void initList() {
		manualList = new JList();
		// 安装我们自订的cellRenderer
		manualList.setCellRenderer(new IconListItemRenderer());
		manualList.setModel(listModel);

		curFile = new File("src/manuals");
		topFile = curFile;
		updateListData();

	}

	/**
	 * 初始化面板
	 */
	private void initPanels() {
		JPanel leftPanel = new JPanel();
		TitledBorder manualBorder = new TitledBorder("棋谱列表");
		leftPanel.setBorder(manualBorder);

		manualList.setFont(new Font("宋体", Font.PLAIN, 16));
		manualList.setToolTipText(PropertyReader.get("CHESS_MESSAGE_TOOLTIP"));
		manualList.setVisibleRowCount(10);
		manualList.setAutoscrolls(true);

		// 列表框响应单击和双击事件
		manualList.addMouseListener(new MouseAdapter() {
			// 列表项的名字
			String name = "";

			// 列表项是否为目录
			boolean isDir = false;

			// 列表项是否为文件
			boolean isFile = false;

			public void mouseClicked(MouseEvent e) {
				int count = e.getClickCount();

				System.out.println("当前路径为" + curFile.getAbsolutePath());
				// 单击事件
				if (count == 1) {
					int index = manualList.getSelectedIndex();
					if (index != -1) {
						IconListItem item = (IconListItem) manualList
								.getSelectedValue();
						if (item != null) {
							name = item.getText();
						}
						System.out.println("当前选项的名字为:" + name + "\n");
					}

					File dirFile = new File(curFile, name);
					System.out.println("存在么？" + dirFile.exists());
					if (dirFile.exists()) {

						if (dirFile.isDirectory()) {
							isDir = true;
							isFile = false;
							descArea.setText("这是一个目录");
							System.out.println("单击了一个目录" + name);
						}
					} else {
						dirFile = new File(curFile, name + EXTENSION_NAME);
						if (dirFile.isFile()) {
							isFile = true;
							isDir = false;
							GameRecord chessRecord = chessRecords.get(index);
							descArea.setText(chessRecord.getDateAndTime()
									+ "\n" + chessRecord.getDesc());

							System.out.println("单击了一个文件" + name);
						} else {
							isFile = false;
							isDir = false;
							System.out.println("单击了" + name);
							descArea.setText("");

						}

					}

					if (isDir) {
						System.out.println("单击了目录文件");
					} else if (isFile) {
						System.out.println("单击了文件");

					} else if (name.equals("返回上一层")) {
						System.out.println("单击了返回上一层");
					}
				}
				// 双击事件
				if (count == 2) {
					int index = manualList.getSelectedIndex();
					if (index != -1) {
						IconListItem item = (IconListItem) manualList
								.getSelectedValue();
						if (item != null) {
							name = item.getText();
						}
						System.out.println("当前选项的名字为:" + name + "\n");
					}

					File dirFile = new File(curFile, name);
					System.out.println("存在么？" + dirFile.exists());
					if (dirFile.exists()) {

						if (dirFile.isDirectory()) {
							isDir = true;
							isFile = false;

							curFile = dirFile;
							System.out.println("当前选项是一个目录" + name);
						}
					} else {
						dirFile = new File(curFile, name + EXTENSION_NAME);
						if (dirFile.isFile()) {
							isFile = true;
							isDir = false;

							System.out.println("当前选项是一个文件" + name);
						} else {
							isFile = false;
							isDir = false;
							System.out.println("当前选项不是文件，不是目录" + name);

						}

					}
					// 根据列表项来决定双击事件如何处理
					if (isDir) {
						System.out.println("即将更新目录及文件");
						updateListData();
					} else if (isFile) {
						System.out.println("即将装载游戏");
						load();
					} else if (name.equals("返回上一层")) {
						String p = curFile.getParent();
						System.out.println("返回到目录:" + p);
						chessRecords.clear();
						curFile = curFile.getParentFile();
						updateListData();
					}
				}
			}
		});

		JScrollPane listScroll = new JScrollPane(manualList);
		listScroll.setPreferredSize(new Dimension(400, 480));
		leftPanel.add(listScroll);

		// 右边的面板
		JPanel rightPanel = new JPanel(new BorderLayout());

		// 棋谱描述面板
		JPanel descPanel = new JPanel();
		TitledBorder descBorder = new TitledBorder("棋谱描述");
		descPanel.setBorder(descBorder);

		descArea = new JTextArea("这里将显示棋谱的描述信息");
		descArea.setPreferredSize(new Dimension(170, 400));
		descPanel.add(descArea);
		rightPanel.add(BorderLayout.CENTER, descPanel);

		JPanel controlPanel = new JPanel(new FlowLayout());
		controlPanel.add(load);
		controlPanel.add(exit);

		rightPanel.add(BorderLayout.SOUTH, controlPanel);

		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				leftPanel, rightPanel);
		split.setDividerSize(5);
		split.setDividerLocation(450);
		add(split, BorderLayout.CENTER);
	}

	/**
	 * 初始化按钮
	 */
	private void initButtons() {
		int width = ChessUtil.getImageIcon("load.png").getIconWidth();
		int height = ChessUtil.getImageIcon("load.png").getIconHeight();

		// 装载棋谱按钮
		load = new JButton(ChessUtil.getImageIcon("load.png"));
		load.setPreferredSize(new Dimension(width, height));
		load.setToolTipText(PropertyReader.get("LOAD_GAME_JBUTTON_TOOLTIP"));
		load.setCursor(new Cursor(Cursor.HAND_CURSOR));
		load.addActionListener(this);

		// 退出按钮
		exit = new JButton(ChessUtil.getImageIcon("exit.png"));
		exit.setPreferredSize(new Dimension(width, height));
		exit.setToolTipText("退出游戏");
		exit.addActionListener(this);
		exit.setCursor(new Cursor(Cursor.HAND_CURSOR));

	}

	/**
	 * 事件响应
	 */
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();

		// 装载游戏
		if (source == load) {
			load();
		}
		// 退出
		else if (source == exit) {
			handleExitGame();
		}

	}

	/**
	 * 装载棋谱并演示
	 * 
	 */
	private void load() {
		GameRecord chessRecord;

		if (manualList != null) {
			int index = manualList.getSelectedIndex();
			if (index != -1) {
				chessRecord = chessRecords.get(index);
				if (chessRecord == null) {
					JOptionPane.showMessageDialog(this, "选中的文件并不是棋谱文件！");
					return;
				}
			} else {
				JOptionPane.showMessageDialog(this, "没有选中棋谱！");
				return;
			}

			ChessShowGUI demo = new ChessShowGUI(chessRecord);
			demo.setVisible(true);
			dispose();
			validate();
			repaint();
		}

	}

	private void handleExitGame() {
		dispose();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ChessLoadingGUI loading = new ChessLoadingGUI();
		loading.setVisible(true);
	}

	/**
	 * 更新列表框中的选项
	 */
	@SuppressWarnings("unchecked")
	private void updateListData() {
		// 删除已有的列表项
		listModel.removeAllElements();

		// 获取当前目录下的文件
		File[] listFile = curFile.listFiles(new FileFilter() {
			// 只选择当前目录下的扩展名为EXTENSION_ANME文件
			public boolean accept(File file) {
				return file.getName().endsWith(EXTENSION_NAME);
			}
		});

		File[] listDir = curFile.listFiles(new FileFilter() {
			// 只选择当前目录下的目录文件
			@Override
			public boolean accept(File file) {
				return file.isDirectory();
			}
		});

		// 如果不是顶层目录，需要添加返回上一层目录的选项
		if (!curFile.equals(topFile)) {
			// 返回上一层选项
			IconListItem item = new IconListItem(ChessUtil
					.getImageIcon("open.gif"), "返回上一层");
			listModel.addElement(item);

			// 更换了目录，需要清空chessRecords里的原来目录里的所有文件
			chessRecords.clear();
			chessRecords.add(null);
		}

		// 添加目录列表的所有名字
		if (listDir != null) {
			for (int i = 0; i < listDir.length; i++) {
				File oneFile = listDir[i];
				if (oneFile.exists() && oneFile.canRead()) {
					String name = oneFile.getName();
					IconListItem item = new IconListItem(ChessUtil
							.getImageIcon("open.gif"), name);
					listModel.addElement(item);
					chessRecords.add(null);
				}
			}

		} else {
			System.out.println("没有目录");
		}

		// 添加文件列表的所有名字(不带扩展名)
		if (listFile != null) {
			for (int i = 0; i < listFile.length; i++) {
				File oneFile = listFile[i];
				if (oneFile.exists() && oneFile.canRead()) {
					String name = oneFile.getName();
					int index = name.indexOf('.');
					if (index != -1) {
						// 文件不需要显示扩展名
						name = name.substring(0, index);
					}

					// 读取当前目录下的棋谱文件
					try {
						GameRecord chessRecord = ManualUtil.readManual(oneFile);
						if (chessRecord != null) {
							chessRecords.add(chessRecord);
							IconListItem item = null;
							if(chessRecord.getPalyerFirst() == null || chessRecord.getPalyerFirst().equals(PALYER_FIRST)){
								 item = new IconListItem(ChessUtil
										.getImageIcon("hongshuai.gif"), name);
							}else{
								 item = new IconListItem(ChessUtil
										.getImageIcon("heijiang.gif"), name);
							}
							/*// 根据棋谱类型显示不同的图标
							IconListItem item;
							if (chessRecord.getFlag() == ManualType.NETWORK_RED) {
								item = new IconListItem(ChessUtil
										.getImageIcon("heijiang.gif"), name);
							} else {
								item = new IconListItem(ChessUtil
										.getImageIcon("hongshuai.gif"), name);
							}*/
							// 列表模型添加一项
							listModel.addElement(item);
						}

					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}

		} else {
			System.out.println("没有文件");
		}
		// 打印棋谱记录，测试是否正确
		printManualRecord();

	}

	/**
	 * 打印棋谱文件记录，检测是否和列表中的信息一致
	 */
	private void printManualRecord() {
		printXing();
		int size = chessRecords.size();
		for (int i = 0; i < size; i++) {
			GameRecord record = chessRecords.get(i);
			System.out.print("第" + i + "个记录是否为空：" + (record == null) + "\t");
			if (record == null) {
				break;
			}
			System.out.print("保存日期：" + record.getDateAndTime() + "\t");
			System.out.print("棋谱类型：" + record.getType() + "\t");
			System.out.print("棋局的描述信息：" + record.getDesc() + "\t");
		}
	}

	private void printXing() {
		System.out.println("**************************");
	}
}
