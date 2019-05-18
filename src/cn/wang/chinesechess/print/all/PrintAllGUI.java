
package cn.wang.chinesechess.print.all;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.border.TitledBorder;

import cn.wang.chinesechess.ChessGUI;
import cn.wang.chinesechess.ChessUtil;
import cn.wang.chinesechess.config.NAME;
import cn.wang.chinesechess.config.PropertyReader;
import cn.wang.chinesechess.core.ChessManual;
import cn.wang.chinesechess.core.ChessPiece;
import cn.wang.chinesechess.core.ChessRule;
import cn.wang.chinesechess.core.ManualItem;
import cn.wang.chinesechess.core.MoveStep;
import cn.wang.chinesechess.core.PieceUtil;
import cn.wang.chinesechess.save.GameRecord;
import cn.wang.chinesechess.save.ISaveManual;
import cn.wang.chinesechess.save.SaveAsDialog;
import cn.wang.chinesechess.save.SaveDialog;

/**
 * 全局打谱主界面
 * 
 * @author wanghualiang
 */
public class PrintAllGUI extends JFrame implements ActionListener, ISaveManual,
		NAME {

	private static final long serialVersionUID = 266L;
	/**private JMenu 菜单栏*/
	private JMenu fileMenu, manualMenu, settingMenu, helpMenu;
	/**private JMenuItem 文件菜单选项*/
	private JMenuItem newManual, saveManual, saveManualAs, exitGame;
	/**private JMenuItem 棋谱菜单选项*/
	private JMenuItem reprintManual, inputManual, undoManual;
	/**private JMenuItem 帮助菜单选项*/
	private JMenuItem setting, helpContent, aboutGame, welcome;
	/**private JMenuItem 设置菜单选项*/
	private JCheckBoxMenuItem bgSound,gmSound;
	/**private JButton 棋谱演示按钮*/
	private JButton prev, next, auto, first, last;
	/**private JButton 功能按钮*/
	private JButton newButton, save, saveAs, reprint, inputManualButton, undo, set;
	/**private ArrayList<ManualItem> 棋谱记录*/
	private ArrayList<ManualItem> records;
	
	/**PrintAllBoard board 棋盘*/
	private PrintAllBoard board;
	/**ChessManual chessManual 棋谱*/
	private ChessManual chessManual;
	/**JPanel gameStatusPanel 游戏状态面板*/
	private JPanel gameStatusPanel;
	/**private JPanel 存放棋谱演示按钮的工具栏*/
	private JPanel manualTools;
	
	/**boolean 棋盘是否可以下棋，默认可以*/
	protected boolean canPaly = true;
	/**private JLabel 游戏状态*/ 
	private JLabel gameStatusContent, gameStatusIcon;
	/**private JPanel 存放功能按钮的工具栏*/
	private JPanel toolBar = new JPanel();

	/**public int 当前的索引 */
	public int curIndex = -1;
	/**private JScrollPane 棋谱显示滚动面板 */
	private JScrollPane manualScroll;
	/**public int 棋谱自动演示的时间间隔 */
	private int time = 1000;
	/**private JList 棋谱列表 */
	private JList manual;
	/**private Vector 棋谱对应的文字描述 */
	private Vector descs;

	/**Cursor handCursor 按钮共用的手形图标*/
	private Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);

	/**
	 * 构造函数
	 */
	public PrintAllGUI() {

		board = new PrintAllBoard(this);
		board.initChess();

		chessManual = board.chessManual;
		manualScroll = chessManual.manualScroll;
		manual = chessManual.manualList;
		descs = chessManual.descs;
		records = chessManual.getManualItems();
		initMenuBar();
		initButtons();
		initPanels();
		handleKeyEvent();
		ChessUtil.setLoop(true);
		ChessUtil.setStart(true);

		setSize(670, 660);
		setTitle("中国象棋---全局打谱");
		setResizable(false);
		setIconImage(ChessUtil.getAppIcon());
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		addWindowListener(new WindowAdapter() {
			// 响应默认的退出事件
			public void windowClosing(WindowEvent e) {
				handleExitGame();
			}

		});
	}

	/**
	 * 初始化菜单
	 * 
	 */
	private void initMenuBar() {
		// 构造菜单
		JMenuBar bar = new JMenuBar();

		// 构造文件菜单
		fileMenu = new JMenu("文件(G)");

		newManual = new JMenuItem("新建", ChessUtil.getImageIcon("newManual.gif"));
		saveManual = new JMenuItem("保存", ChessUtil.getImageIcon("save.gif"));
		saveManualAs = new JMenuItem("另存", ChessUtil.getImageIcon("saveas.gif"));
		exitGame = new JMenuItem("退出", ChessUtil.getImageIcon("exit.gif"));
		// 向文件菜单中增加菜单项
		fileMenu.add(newManual);
		fileMenu.add(saveManual);
		fileMenu.add(saveManualAs);
		fileMenu.add(exitGame);

		// 构造棋谱菜单
		manualMenu = new JMenu("棋谱(M)");
		reprintManual = new JMenuItem("重新打谱",ChessUtil.getImageIcon("reprint.gif"));
		inputManual = new JMenuItem("输入棋谱",ChessUtil.getImageIcon("inputManual.gif"));
		undoManual = new JMenuItem("悔棋", ChessUtil.getImageIcon("undo.gif"));
		// 向棋谱菜单中添加菜单项
		manualMenu.add(reprintManual);
		manualMenu.add(inputManual);
		manualMenu.add(undoManual);
		
		// 构造设置菜单
		settingMenu = new JMenu("设置(S)");
		bgSound = new JCheckBoxMenuItem("背景音乐");
		gmSound = new JCheckBoxMenuItem("游戏音效");
		bgSound.setSelected(true);
		gmSound.setSelected(true);
		// 向设置菜单中添加菜单项
		settingMenu.add(bgSound);
		settingMenu.add(gmSound);

		// 构造帮助菜单
		helpMenu = new JMenu("帮助(H)");
		welcome = new JMenuItem("欢迎", ChessUtil.getImageIcon("welcome.gif"));
		helpContent = new JMenuItem("帮助内容", ChessUtil.getImageIcon("help.gif"));
		aboutGame = new JMenuItem("关于游戏", ChessUtil.getImageIcon("info.gif"));
		// 向帮助菜单中添加菜单项
		helpMenu.add(welcome);
		helpMenu.add(helpContent);
		helpMenu.add(aboutGame);
		// 向菜单条中添加菜单
		bar.add(fileMenu);
		bar.add(manualMenu);
		bar.add(settingMenu);
		bar.add(helpMenu);

		setJMenuBar(bar);

		// 设置快捷键
		newManual.setAccelerator(KeyStroke.getKeyStroke('N',InputEvent.CTRL_DOWN_MASK));
		saveManual.setAccelerator(KeyStroke.getKeyStroke('S',InputEvent.CTRL_DOWN_MASK));
		saveManualAs.setAccelerator(KeyStroke.getKeyStroke('U',InputEvent.CTRL_DOWN_MASK));
		exitGame.setAccelerator(KeyStroke.getKeyStroke('E',InputEvent.CTRL_DOWN_MASK));

		reprintManual.setAccelerator(KeyStroke.getKeyStroke('P',InputEvent.CTRL_DOWN_MASK));
		inputManual.setAccelerator(KeyStroke.getKeyStroke('I',InputEvent.CTRL_DOWN_MASK));
		undoManual.setAccelerator(KeyStroke.getKeyStroke('U',InputEvent.CTRL_DOWN_MASK));
		
		welcome.setAccelerator(KeyStroke.getKeyStroke('W',InputEvent.CTRL_DOWN_MASK));
		helpContent.setAccelerator(KeyStroke.getKeyStroke('H',InputEvent.CTRL_DOWN_MASK));
		aboutGame.setAccelerator(KeyStroke.getKeyStroke('A',InputEvent.CTRL_DOWN_MASK));

		bgSound.setAccelerator(KeyStroke.getKeyStroke('B',InputEvent.CTRL_DOWN_MASK));
		gmSound.setAccelerator(KeyStroke.getKeyStroke('T',InputEvent.CTRL_DOWN_MASK));

		// 设置助记符
		fileMenu.setMnemonic(KeyEvent.VK_G);
		manualMenu.setMnemonic(KeyEvent.VK_M);
		settingMenu.setMnemonic(KeyEvent.VK_S);
		helpMenu.setMnemonic(KeyEvent.VK_H);

		// 注册监听器
		newManual.addActionListener(this);
		saveManual.addActionListener(this);
		saveManualAs.addActionListener(this);
		exitGame.addActionListener(this);

		inputManual.addActionListener(this);
		reprintManual.addActionListener(this);
		undoManual.addActionListener(this);

		gmSound.addActionListener(this);
		bgSound.addActionListener(this);

		welcome.addActionListener(this);
		helpContent.addActionListener(this);
		aboutGame.addActionListener(this);

	}

	/**
	 * 初始化面板
	 * 
	 */
	private void initPanels() {
		// 构造右边的面板
		JPanel rightPanel = new JPanel(new BorderLayout());
		// 棋谱描述面板
		JPanel recordsPanel = new JPanel(new BorderLayout());

		TitledBorder recordsBorder = new TitledBorder(
				PropertyReader.get("CHESS_MESSAGE_TOOLTIP"));
		recordsPanel.setBorder(recordsBorder);
		recordsPanel.setPreferredSize(new Dimension(240, 330));
		recordsPanel.add(BorderLayout.CENTER, chessManual);

		// 工具栏
		manualTools = new JPanel(new FlowLayout(FlowLayout.CENTER));
		// manualTools.setPreferredSize(new Dimension(220, 20));
		manualTools.add(first);
		manualTools.add(prev);
		manualTools.add(auto);
		manualTools.add(next);
		manualTools.add(last);

		recordsPanel.add(BorderLayout.SOUTH, manualTools);
		rightPanel.add(BorderLayout.CENTER, recordsPanel);

		JSplitPane splitH = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, board,
				rightPanel);
		splitH.setDividerSize(5);
		splitH.setDividerLocation(450);

		// 游戏状态面板
		gameStatusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		gameStatusPanel.setPreferredSize(new Dimension(660, 80));
		// gameStatusPanel.setBackground(new Color(122, 146, 170));
		gameStatusIcon = new JLabel(ChessUtil.getImageIcon("hongshuai.gif"));
		// 游戏状态栏
		gameStatusContent = new JLabel("红方先走棋");
		gameStatusContent.setFont(new Font("宋体", Font.PLAIN, 16));

		TitledBorder gameStatusBorder = new TitledBorder("游戏状态");
		gameStatusBorder.setTitleColor(Color.RED);
		gameStatusBorder.setTitleFont(new Font("宋体", Font.PLAIN, 16));
		gameStatusPanel.setToolTipText("游戏状态");
		gameStatusPanel.setBorder(gameStatusBorder);
		gameStatusPanel.add(gameStatusIcon);
		gameStatusPanel.add(gameStatusContent);

		initToolBar();

		add(BorderLayout.NORTH, toolBar);
		add(BorderLayout.CENTER, splitH);
		add(BorderLayout.SOUTH, gameStatusPanel);

	}

	/**
	 * 初始化工具条
	 * 
	 */
	private void initToolBar() {
		FlowLayout flowLayout = new FlowLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		toolBar.setLayout(flowLayout);

		toolBar.add(newButton);
		toolBar.add(save);
		toolBar.add(saveAs);

		toolBar.add(reprint);
		toolBar.add(undo);
		toolBar.add(inputManualButton);

		toolBar.add(set);

	}

	/**
	 * 初始化按钮
	 * 
	 */
	private void initButtons(){
		Dimension iconSize = new Dimension(16, 16);
		prev = new JButton(ChessUtil.getImageIcon("prev.gif"));
		prev.addActionListener(this);
		prev.setToolTipText("上一步");
		prev.setCursor(new Cursor(Cursor.HAND_CURSOR));
		prev.setPreferredSize(iconSize);

		next = new JButton(ChessUtil.getImageIcon("next.gif"));
		next.addActionListener(this);
		next.setToolTipText("下一步");
		next.setCursor(new Cursor(Cursor.HAND_CURSOR));
		next.setPreferredSize(iconSize);

		first = new JButton(ChessUtil.getImageIcon("first.gif"));
		first.addActionListener(this);
		first.setToolTipText("第一步");
		first.setCursor(new Cursor(Cursor.HAND_CURSOR));
		first.setPreferredSize(iconSize);

		last = new JButton(ChessUtil.getImageIcon("last.gif"));
		last.addActionListener(this);
		last.setToolTipText("最后一步");
		last.setCursor(new Cursor(Cursor.HAND_CURSOR));
		last.setPreferredSize(iconSize);

		auto = new JButton(ChessUtil.getImageIcon("auto.gif"));
		auto.addActionListener(this);
		auto.setToolTipText("自动演示");
		auto.setPreferredSize(iconSize);
		auto.setCursor(new Cursor(Cursor.HAND_CURSOR));

		Insets insets = new Insets(1, 1, 1, 1);
		
		newButton = new JButton(ChessUtil.getImageIcon("newManual.gif"));
		newButton.setToolTipText("新建棋谱");
		newButton.addActionListener(this);
		newButton.setCursor(handCursor);
		newButton.setMargin(insets);
		
		reprint = new JButton(ChessUtil.getImageIcon("reprint.gif"));
		reprint.setToolTipText("重新打谱");
		reprint.addActionListener(this);
		// reprint.setPreferredSize(dimension);
		reprint.setCursor(handCursor);
		reprint.setMargin(insets);

		save = new JButton(ChessUtil.getImageIcon("save.gif"));
		save.addActionListener(this);
		save.setToolTipText("保存棋谱");
		// save.setPreferredSize(dimension);
		save.setCursor(handCursor);
		save.setMargin(insets);
		save.setOpaque(true);

		saveAs = new JButton(ChessUtil.getImageIcon("saveas.gif"));
		saveAs.addActionListener(this);
		saveAs.setToolTipText("另存棋谱");
		// saveAs.setPreferredSize(dimension);
		saveAs.setCursor(handCursor);
		saveAs.setMargin(insets);

		undo = new JButton(ChessUtil.getImageIcon("undo.gif"));
		undo.addActionListener(this);
		undo.setToolTipText("悔棋");
		undo.setCursor(handCursor);
		undo.setMargin(insets);
		
		inputManualButton = new JButton(ChessUtil.getImageIcon("inputManual.gif"));
		inputManualButton.setToolTipText("输入棋谱");
		inputManualButton.addActionListener(this);
		inputManualButton.setCursor(handCursor);
		inputManualButton.setMargin(insets);

		set = new JButton(ChessUtil.getImageIcon("welcome.gif"));
		set.setToolTipText("设置");
		set.addActionListener(this);
		set.setCursor(handCursor);
		set.setMargin(insets);

		

	}

	/**
	 * 响应事件
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();

		// 下一步
		if (source == next) {
			next();
		} else if (source == prev) {
			prev();
		} else if (source == first) {
			first();
		} else if (source == last) {
			last();
		} else if (source == auto) {
			auto();
		} else if (source == helpContent) {
			ChessUtil.showHelpDialog();
		} else if (source == welcome) {
			ChessUtil.showWelcomeDialog();
		} else if (source == aboutGame) {
			ChessUtil.showAboutDialog();
		}

		// 重新打谱
		else if (source == reprint || source == reprintManual) {
			int result = JOptionPane.showConfirmDialog(this, "您需要保存当前的棋谱吗？",
					"您需要保存当前的棋谱吗？", JOptionPane.YES_NO_OPTION);

			if (result == JOptionPane.YES_OPTION) {
				SaveDialog dialog = new SaveDialog(this);
				dialog.setVisible(true);
			}
			int size = chessManual.getManualItems().size();
			for (int index = 0; index < size; index++) {
				undo();
			}
		}

		// 悔棋
		else if (source == undo || source == undoManual) {
			last();
			undo();

		} else if (source == save || source == saveManual) {
			SaveDialog dialog = new SaveDialog(this);
			dialog.setVisible(true);

		} else if (source == saveAs || source == saveManualAs) {
			SaveAsDialog dialog = new SaveAsDialog(this);
			dialog.setVisible(true);
		} else if (source == exitGame) {
			handleExitGame();

		} else if (source == inputManualButton || source == inputManual) {
			Icon icon = null;
			if(board.getPlayerName().equals(RED_NAME)){
				 icon = ChessUtil.getImageIcon("hongshuai.gif");
			}else{
				icon = ChessUtil.getImageIcon("heijiang.gif");
			}
			
			String manual = (String) JOptionPane.showInputDialog(this, "请输入棋谱", "请输入棋谱",
					JOptionPane.PLAIN_MESSAGE,icon,null,null);
			if (manual != null) {
				manual = manual.trim();// 去掉前导空白和尾部空白
			}
			movePieceByManual(manual);

		} else if (source == newButton || source == newManual) {
			PrintAllGUI newPrint = new PrintAllGUI();
			newPrint.setVisible(true);
		} else if (source == bgSound){
			ChessUtil.setLoop(bgSound.isSelected());
		} else if (source == gmSound){
			ChessUtil.setStart(gmSound.isSelected());
		}

	}

	/**
	 * 悔棋，后退一步
	 */
	private void undo() {
		canPaly = true;
		boolean flag = chessManual.removeManualItem();
		if (flag) {
			if (board.getPlayerName().equals(RED_NAME)) {
				board.changeSide();
				updateGameStatus(2, "轮到黑方走喽！");
			} else {
				board.changeSide();
				updateGameStatus(1, "轮到红方走喽！");
			}
			curIndex--;
		}
	}

	/**
	 * 根据棋谱，如馬八进七，移动棋子
	 * 
	 * @param manual
	 */
	private void movePieceByManual(String manual) {
		board.setNeedWink(false);
		if(!canPaly){
			JOptionPane.showConfirmDialog(this, "游戏已经结束", "提示",
					JOptionPane.YES_OPTION);
			return;
		}
		if (manual != null && manual.length() == 4) {
			ChessPiece movePiece = board.getMovePiece(manual);
			if (movePiece == null || !movePiece.getName().equals(board.getPlayerName())) {
				return;
			}
			//ChessPiece removedPiece = board.getRemovedPiece(manual);
			Point pStart = board.getStartPosition(manual);
			Point pEnd = board.getEndPosition(manual);

			int startX = (int) pStart.getX();
			int startY = (int) pStart.getY();
			int endX = (int) pEnd.getX();
			int endY = (int) pEnd.getY();
			System.out.println(movePiece.getCategory() + " :" + startX + startY
					+ endX + endY);
			boolean rule = ChessRule.allRule(movePiece, startX, startY, endX,
					endY, board.chessPoints, null);
			System.out.println("能否移动棋子：" + rule);
			if (rule) {
				//添加一条棋谱信息，要在移动之前添加，否则被吃掉的棋子无法记录
				board.addChessRecord(movePiece, startX, startY, endX, endY);
				//移动棋子
				board.movePiece(movePiece, startX, startY, endX, endY);
				// 打谱类当前索引+1
				curIndex++;

				// 播放棋子被吃声音
				ChessUtil.playSound("eat.wav");

				validate();
				repaint();

				// 更换己方名字，实现一人轮流走红黑2方的棋子
				board.changeSide();

			}
		}
	}

	private void auto() {
		first();
		DemoThread auto = new DemoThread();
		auto.start();
	}

	/**
	 * 移动到第一步
	 * 
	 */
	private void first() {
		while (curIndex != -1) {
			prev();
		}
	}

	/**
	 * 演示index指定的一步
	 * 
	 * @param index
	 */
	private void step(int index) {
		if (index < 0) {
			return;
		}

		ManualItem moveRecord = records.get(index);
		MoveStep step = moveRecord.getMoveStep();
		Point pStart = step.start;
		Point pEnd = step.end;
		int startI = pStart.x;
		int startJ = pStart.y;
		int endI = pEnd.x;
		int endJ = pEnd.y;

		ChessPiece piece = board.chessPoints[startI][startJ].getPiece();
		board.movePiece(piece, startI, startJ, endI, endJ);

		// 演示时不能移动棋子的
		repaint();
		validate();
	}

	/**
	 * 上一步
	 * 
	 */

	private void prev() {
		if (curIndex == -1) {
			return;
		}

		int size = records.size();

		// 更新棋盘界面
		ManualItem record = new ManualItem();
		MoveStep moveStep;

		ChessPiece eatedPiece;
		int startI = 0;
		int startJ = 0;
		int endI = 0;
		int endJ = 0;

		if (size > 0 && curIndex < size && curIndex >= 0) {
			// 获得指定的元素
			record = records.get(curIndex);
			eatedPiece = PieceUtil.createPiece(record.getEatedPieceId());

			moveStep = record.getMoveStep();
			startI = moveStep.start.x;
			startJ = moveStep.start.y;
			endI = moveStep.end.x;
			endJ = moveStep.end.y;

			// 上一步没有吃棋子
			if (eatedPiece == null) {
				System.out.println("没吃棋子");

				ChessPiece piece = board.chessPoints[endI][endJ].getPiece();
				board.chessPoints[startI][startJ].setPiece(piece, board);
				board.chessPoints[endI][endJ].setHasPiece(false);

			}
			// 上一步吃了棋子
			else {
				ChessPiece piece = board.chessPoints[endI][endJ].getPiece();
				board.chessPoints[startI][startJ].setPiece(piece, board);
				board.chessPoints[endI][endJ].setPiece(eatedPiece, board);

			}
		}

		// 后退时，画标记与正常移动时不同
		if (curIndex >= 1) {
			record = (ManualItem) records.get(curIndex - 1);
			moveStep = record.getMoveStep();
			startI = moveStep.start.x;
			startJ = moveStep.start.y;
			endI = moveStep.end.x;
			endJ = moveStep.end.y;
		}

		// 如果移动，应该画2条提示框
		board.setMoveFlag(true);
		board.movePoints[0] = new Point(endI, endJ);
		board.movePoints[1] = new Point(startI, startJ);

		curIndex--;
		scrollToView();

		repaint();
		validate();
	}

	/**
	 * 把棋谱列表滚动到当前行
	 */
	private void scrollToView() {
		if (curIndex >= 0 && curIndex < descs.size()) {
			// 选中当前行，提示玩家
			System.out.println("应该选中第N行：" + curIndex);
			manual.setSelectedIndex(curIndex);

			int lastIndex = curIndex;
			Rectangle rect = manual.getCellBounds(lastIndex, lastIndex);
			manualScroll.getViewport().scrollRectToVisible(rect);

		}
		if (curIndex == -1) {
			// 退回到没有任何棋子移动的状态时，不用画移动标记
			board.setMoveFlag(false);
			/*
			 * 当curIndex == -1时，应该不选中任何一样,
			 * 而manual.setSelectedIndex(-1)不能时列表框不选中任何一行
			 * manual.setListData(descs);可以恢复到默认状态不选中任何一行
			 */
			System.out.println("没有选中任何一行：" + curIndex);
			manual.setListData(descs);
		}
	}

	/**
	 * 响应键盘事件
	 * 
	 */
	private void handleKeyEvent() {
		Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
			public void eventDispatched(AWTEvent event) {
				if (((KeyEvent) event).getID() == KeyEvent.KEY_PRESSED) {
					int code = ((KeyEvent) event).getKeyCode();
					if (code == KeyEvent.VK_F1) {
						ChessUtil.showHelpDialog();
					} else if (code == KeyEvent.VK_DOWN) {
						next();
						System.out.println("next");
					} else if (code == KeyEvent.VK_UP) {
						prev();
						System.out.println("prev");
					} else if (code == KeyEvent.VK_HOME) {
						first();
						System.out.println("first");
					} else if (code == KeyEvent.VK_END) {
						last();
						System.out.println("last");
					} else if (code == KeyEvent.VK_ENTER) {
						auto();
					}

				}
			}
		}, AWTEvent.KEY_EVENT_MASK);

	}

	/**
	 * 处理关闭事件
	 * 
	 */
	private void handleExitGame() {
		int result = JOptionPane.showConfirmDialog(this, "您确定要退出么？", "确认退出",
				JOptionPane.YES_NO_OPTION);
		if (result == JOptionPane.YES_OPTION) {
			if (board.getWinkThread() != null) {
				board.getWinkThread().interrupt();
				System.out.println("关闭中...");
			}
			dispose();
		}
	}

	/**
	 * 更新游戏状态
	 * 
	 * @param state
	 *            图标标识
	 * @param content
	 *            游戏状态文字描述
	 */
	protected void updateGameStatus(int state, String content) {

		switch (state) {
		case 1:
			gameStatusIcon.setIcon(ChessUtil.getImageIcon("hongshuai.gif"));
			gameStatusIcon.setToolTipText("轮到红方走喽！");
			break;
		case 2:
			gameStatusIcon.setIcon(ChessUtil.getImageIcon("heijiang.gif"));
			gameStatusIcon.setToolTipText("轮到黑方走喽！");
			break;
		default:
			break;
		}

		if (content != null && !content.equals("")) {
			gameStatusContent.setText(content);
		}
	}

	private class DemoThread extends Thread {

		public DemoThread() {
			System.out.println("刚刚写的新线程构造完成了");
		}

		public void run() {
			System.out.println("刚刚写的新线程运行了");
			int size = records.size();

			while (curIndex < size - 1) {
				try {
					Thread.sleep(time);

					next();
					if (curIndex >= size - 1) {
						this.join(100);
						break;
					}
				} catch (InterruptedException ie) {
					break;
				}
			}
		}

	}

	/**
	 * 移动到最后一步
	 * 
	 */
	protected  void last() {
		while (curIndex != descs.size() - 1) {
			next();
		}
	}

	/**
	 * 演示当前步的下一步
	 * 
	 */
	protected void next() {
		if (curIndex == descs.size() - 1) {
			return;
		}
		curIndex++;
		if (curIndex < records.size()) {
			step(curIndex);
		}
		scrollToView();
	}
	
	/**
	 * 提示游戏已经结束
	 * @param canPaly 标记游戏是否结束 true未结束 ；false结束
	 * @param category 被将军的棋子种类，帅或将
	 */
	protected void gameOver(boolean canPaly,PieceCategory category){
		if(!canPaly){
			this.canPaly = canPaly;
			if(category.equals(PieceCategory.JIANG)){
				JOptionPane.showConfirmDialog(this, "游戏结束,红方获胜", "提示",
						JOptionPane.YES_OPTION);
			}else{
				JOptionPane.showConfirmDialog(this, "游戏结束,黑方获胜", "提示",
						JOptionPane.YES_OPTION);
			}			
		}
	}

	/**
	 * 全局打谱测试程序入口
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		PrintAllGUI printManual = new PrintAllGUI();
		printManual.setVisible(true);

	}

	/**
	 * （非 Javadoc）
	 */
	@Override
	public ArrayList<String> getSavePaths() {
		ArrayList<String> paths = new ArrayList<String>();
		String path = "src/manuals/whole/";
		String path2 = "src/manuals/whole/";

		paths.add(path);
		paths.add(path2);
		return paths;
	}

	@Override
	public GameRecord getGameRecord() {
		GameRecord gameRecord = new GameRecord(ManualType.PRINT_WHOLE,
				ChessUtil.getDateAndTime(), "",
				board.chessManual.getManualItems(), board.chessManual.descs,
				null, PALYER_FIRST);
		return gameRecord;
	}

}
