
package cn.wang.chinesechess.load;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import cn.wang.chinesechess.ChessUtil;
import cn.wang.chinesechess.HelpDialog;
import cn.wang.chinesechess.config.NAME;
import cn.wang.chinesechess.config.PropertyReader;
import cn.wang.chinesechess.config.NAME.ManualType;
import cn.wang.chinesechess.core.ChessPiece;
import cn.wang.chinesechess.core.ManualItem;
import cn.wang.chinesechess.core.ManualUtil;
import cn.wang.chinesechess.core.MoveStep;
import cn.wang.chinesechess.core.PieceUtil;
import cn.wang.chinesechess.print.part.Position;
import cn.wang.chinesechess.save.GameRecord;
import cn.wang.chinesechess.save.ISaveManual;
import cn.wang.chinesechess.save.SaveAsDialog;
import cn.wang.chinesechess.save.SaveDialog;

/**
 * 演示--根据棋谱演示棋局
 * 
 * @author wanghualiang
 */
public class ChessShowGUI extends JFrame implements ActionListener, NAME,
		ISaveManual {

	private static final long serialVersionUID = 266L;

	private JMenu fileMenu, manualMenu, settingMenu, helpMenu;

	private JMenuItem openManual, saveManual, saveManualAs, exitGame,
			openMainGUI;

	private JMenuItem firstMenuItem, prevMenuItem, autoMenuItem, nextMenuItem,
			lastMenuItem, manualListMenuItem;

	private JMenuItem setting, helpContent, aboutGame, welcome;

	private JCheckBoxMenuItem bgSound,gmSound;

	/** 上一步、下一步、自动演示、第0步、最后一步*/
	private JButton prev, next, auto, first, last;

	/** 读入、保存、另存、设置、退出、帮助*/
	private JButton read, save, saveAs, set, manualList;

	/** 显示棋谱描述的文本域*/
	private JTextArea area;
	
	/**工具栏*/
	private JPanel toolBar = new JPanel();

	/** 当前索引*/
	private int curIndex = -1;

	/** 棋盘*/
	private LoadBoard board;

	/** 显示棋谱的列表框*/
	private JList manual;

	/** 容纳列表框的滚动面板*/
	private JScrollPane manualScroll;

	/** 自动演示的时间时间间隔*/
	private int time = 1000;
	/**游戏记录对象*/
	private GameRecord gameRecord;

	/**
	 * @param gameRecord
	 *            游戏记录对象
	 */
	public ChessShowGUI(GameRecord gameRecord) {
		this.gameRecord = gameRecord;
		board = new LoadBoard();
		initMenuBar();
		initButtons();
		initPanels();
		initPieces();
		initGui();
		
	}

	/**
	 * 设置界面的属性：如大小、标题、图标等
	 */
	private void initGui() {
		setSize(660, 620);
		setTitle("中国象棋---演示棋谱");
		setIconImage(ChessUtil.getAppIcon());
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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

		openManual = new JMenuItem("读入", ChessUtil.getImageIcon("open.gif"));
		saveManual = new JMenuItem("保存", ChessUtil.getImageIcon("save.gif"));
		saveManualAs = new JMenuItem("另存", ChessUtil.getImageIcon("saveas.gif"));
		exitGame = new JMenuItem("退出", ChessUtil.getImageIcon("exit.gif"));
		/*openMainGUI = new JMenuItem("主界面", ChessUtil
				.getImageIcon("mainGUI.gif"));*/
		// 向文件菜单中增加菜单项
		fileMenu.add(openManual);
		fileMenu.add(saveManual);
		fileMenu.add(saveManualAs);
		fileMenu.add(exitGame);
		//fileMenu.add(openMainGUI);

		// 构造棋谱菜单
		manualMenu = new JMenu("棋谱(M)");
		firstMenuItem = new JMenuItem("第一步", ChessUtil
				.getImageIcon("first.gif"));
		prevMenuItem = new JMenuItem("上一步", ChessUtil.getImageIcon("prev.gif"));
		autoMenuItem = new JMenuItem("自动演示", ChessUtil.getImageIcon("auto.gif"));
		nextMenuItem = new JMenuItem("下一步", ChessUtil.getImageIcon("next.gif"));
		lastMenuItem = new JMenuItem("最后一步", ChessUtil.getImageIcon("last.gif"));
		manualListMenuItem = new JMenuItem("棋谱列表", ChessUtil
				.getImageIcon("manualList.gif"));

		// 向棋谱菜单中添加菜单项
		manualMenu.add(firstMenuItem);

		manualMenu.add(prevMenuItem);
		manualMenu.add(autoMenuItem);
		manualMenu.add(nextMenuItem);
		manualMenu.add(lastMenuItem);
		manualMenu.add(manualListMenuItem);

		// 构造帮助菜单
		helpMenu = new JMenu("帮助(H)");
		welcome = new JMenuItem("欢迎", ChessUtil.getImageIcon("welcome.gif"));
		helpContent = new JMenuItem("帮助内容", ChessUtil.getImageIcon("help.gif"));
		aboutGame = new JMenuItem("关于游戏", ChessUtil.getImageIcon("info.gif"));

		helpMenu.add(welcome);
		helpMenu.add(helpContent);
		helpMenu.add(aboutGame);

		// 构造设置菜单
		settingMenu = new JMenu("设置(S)");
		bgSound = new JCheckBoxMenuItem("背景音乐");
		gmSound = new JCheckBoxMenuItem("游戏音效");
		bgSound.setSelected(true);
		gmSound.setSelected(true);
		settingMenu.add(bgSound);
		settingMenu.add(gmSound);

		// 向菜单条中添加菜单
		bar.add(fileMenu);
		bar.add(manualMenu);
		bar.add(settingMenu);
		bar.add(helpMenu);

		setJMenuBar(bar);

		// 设置快捷键
		openManual.setAccelerator(KeyStroke.getKeyStroke('N',
				InputEvent.CTRL_DOWN_MASK));
		saveManual.setAccelerator(KeyStroke.getKeyStroke('S',
				InputEvent.CTRL_DOWN_MASK));
		saveManualAs.setAccelerator(KeyStroke.getKeyStroke('U',
				InputEvent.CTRL_DOWN_MASK));

		exitGame.setAccelerator(KeyStroke.getKeyStroke('E',
				InputEvent.CTRL_DOWN_MASK));
		/*openMainGUI.setAccelerator(KeyStroke.getKeyStroke('M',
				InputEvent.CTRL_DOWN_MASK));*/

		firstMenuItem.setAccelerator(KeyStroke.getKeyStroke('Y',
				InputEvent.CTRL_DOWN_MASK));
		prevMenuItem.setAccelerator(KeyStroke.getKeyStroke('P',
				InputEvent.CTRL_DOWN_MASK));
		autoMenuItem.setAccelerator(KeyStroke.getKeyStroke('U',
				InputEvent.CTRL_DOWN_MASK));
		nextMenuItem.setAccelerator(KeyStroke.getKeyStroke('N',
				InputEvent.CTRL_DOWN_MASK));
		lastMenuItem.setAccelerator(KeyStroke.getKeyStroke('L',
				InputEvent.CTRL_DOWN_MASK));
		manualListMenuItem.setAccelerator(KeyStroke.getKeyStroke('M',
				InputEvent.CTRL_DOWN_MASK));

		welcome.setAccelerator(KeyStroke.getKeyStroke('W',
				InputEvent.CTRL_DOWN_MASK));
		helpContent.setAccelerator(KeyStroke.getKeyStroke('H',
				InputEvent.CTRL_DOWN_MASK));
		aboutGame.setAccelerator(KeyStroke.getKeyStroke('A',
				InputEvent.CTRL_DOWN_MASK));

		bgSound.setAccelerator(KeyStroke.getKeyStroke('B',
				InputEvent.CTRL_DOWN_MASK));
		gmSound.setAccelerator(KeyStroke.getKeyStroke('T',
				InputEvent.CTRL_DOWN_MASK));

		// 设置助记符
		fileMenu.setMnemonic(KeyEvent.VK_G);
		manualMenu.setMnemonic(KeyEvent.VK_M);
		settingMenu.setMnemonic(KeyEvent.VK_S);
		helpMenu.setMnemonic(KeyEvent.VK_H);

		// 注册监听器
		openManual.addActionListener(this);
		saveManual.addActionListener(this);
		saveManualAs.addActionListener(this);
		exitGame.addActionListener(this);
		//openMainGUI.addActionListener(this);
		
		firstMenuItem.addActionListener(this);
		prevMenuItem.addActionListener(this);
		nextMenuItem.addActionListener(this);
		lastMenuItem.addActionListener(this);
		autoMenuItem.addActionListener(this);
		manualListMenuItem.addActionListener(this);
	
		gmSound.addActionListener(this);
		bgSound.addActionListener(this);

		welcome.addActionListener(this);
		helpContent.addActionListener(this);
		aboutGame.addActionListener(this);

		this.setJMenuBar(bar);
	}

	/**
	 * 初始化面板
	 */
	private void initPanels() {
		/** JPanel 构造右边的面板*/
		JPanel rightPanel = new JPanel(new BorderLayout());

		/**JPanel 棋局信息面板*/
		JPanel recordsPanel = new JPanel(new BorderLayout());

		TitledBorder recordsBorder = new TitledBorder(PropertyReader
				.get("CHESS_MESSAGE_TOOLTIP"));
		recordsPanel.setBorder(recordsBorder);

		manual = new JList(gameRecord.getDescs());
		manual.setFont(new Font("宋体", Font.PLAIN, 16));
		manual.setToolTipText(PropertyReader.get("CHESS_MESSAGE_TOOLTIP"));
		manual.setVisibleRowCount(16);
		manual.setAutoscrolls(true);

		// 列表框响应单击、双击事件
		manual.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int count = e.getClickCount();
				if (count == 1 || count == 2) {
					int index = manual.getSelectedIndex();
					int n = Math.abs(index - curIndex);
					if (index > curIndex) {
						for (int i = 0; i < n; i++) {
							next();// 会修改curIndex的值
						}
					} else if (curIndex > index) {
						for (int i = 0; i < n; i++) {
							prev();// 会修改curIndex的值
						}
					}
				}
			}
		});

		manualScroll = new JScrollPane(manual);
		recordsPanel.setPreferredSize(new Dimension(240, 340));
		recordsPanel.add(BorderLayout.CENTER, manualScroll);

		// 工具面板
		JPanel manualTools = new JPanel(new FlowLayout(FlowLayout.CENTER));
		manualTools.add(first);
		manualTools.add(prev);
		manualTools.add(auto);
		manualTools.add(next);
		manualTools.add(last);
		recordsPanel.add(BorderLayout.SOUTH, manualTools);

		// 控制面板
		FlowLayout layout = new FlowLayout(FlowLayout.LEFT);
		toolBar.setLayout(layout);
		toolBar.add(read);
		toolBar.add(save);
		toolBar.add(saveAs);
		toolBar.add(set);
		toolBar.add(manualList);

		/**JPanel 棋谱描述面板*/
		JPanel descPanel = new JPanel();
		area = new JTextArea(gameRecord.getDateAndTime() + "\n"
				+ gameRecord.getDesc());

		area.setPreferredSize(new Dimension(170, 140));
		JScrollPane scroll = new JScrollPane(area);
		descPanel.setPreferredSize(new Dimension(200, 170));
		descPanel.add(scroll);
		rightPanel.add(BorderLayout.NORTH, recordsPanel);
		rightPanel.add(BorderLayout.CENTER, descPanel);

		/** JSplitPane 分割面板*/
		JSplitPane splitH = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, board,
				rightPanel);
		splitH.setDividerSize(5);
		splitH.setDividerLocation(450);

		add(BorderLayout.CENTER, splitH);
		add(BorderLayout.NORTH, toolBar);
	}

	/**
	 * 初始化按钮
	 * 
	 */
	private void initButtons() {
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

		read = new JButton(ChessUtil.getImageIcon("open.gif"));
		read.setToolTipText("读入");
		read.addActionListener(this);
		read.setCursor(new Cursor(Cursor.HAND_CURSOR));
		read.setMargin(insets);

		saveAs = new JButton(ChessUtil.getImageIcon("saveas.gif"));
		saveAs.setToolTipText("另存");
		saveAs.addActionListener(this);
		saveAs.setCursor(new Cursor(Cursor.HAND_CURSOR));
		saveAs.setMargin(insets);

		save = new JButton(ChessUtil.getImageIcon("save.gif"));
		save.setToolTipText("保存");
		save.addActionListener(this);
		save.setCursor(new Cursor(Cursor.HAND_CURSOR));
		save.setMargin(insets);

		set = new JButton(ChessUtil.getImageIcon("welcome.gif"));
		set.setToolTipText("设置");
		set.addActionListener(this);
		set.setCursor(new Cursor(Cursor.HAND_CURSOR));
		set.setMargin(insets);

		manualList = new JButton(ChessUtil.getImageIcon("manualList.gif"));
		manualList.setToolTipText("返回棋谱列表");
		manualList.addActionListener(this);
		manualList.setCursor(new Cursor(Cursor.HAND_CURSOR));
		manualList.setMargin(insets);

	}

	/**
	 * 响应事件
	 */
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();

		// 下一步
		if (source == next || source == nextMenuItem) {
			next();

		}
		// 前一步
		else if (source == prev || source == prevMenuItem) {
			prev();
		}
		// 第一步
		else if (source == first || source == firstMenuItem) {
			while (curIndex != -1) {
				prev();
			}
		}
		// 最后一步 
		else if (source == last || source == lastMenuItem) {
			while (curIndex != gameRecord.getDescs().size() - 1) {
				next();
			}
		}
		// 退出
		else if (source == exitGame) {
			dispose();
		}
		// 自动演示
		else if (source == auto || source == autoMenuItem) {
			DemoThread auto = new DemoThread();
			auto.start();
		}
		// 帮助
		else if (source == helpContent) {
			ChessUtil.showHelpDialog();
		} 
		//欢迎
		else if (source == welcome) {
			ChessUtil.showWelcomeDialog();
		}
		//关于游戏
		else if (source == aboutGame) {
			ChessUtil.showAboutDialog();
		} 
		// 设置
		else if (source == set) {

		}
		// 保存
		else if (source == save || source == saveManual) {
			SaveDialog dialog = new SaveDialog(this);
			dialog.setVisible(true);
		}

		// 另存为
		else if (source == saveAs || source == saveManualAs) {
			SaveAsDialog dialog = new SaveAsDialog(this);
			dialog.setVisible(true);
		}

		// 读入
		else if (source == read || source == openManual) {
			read();
		} else if (source == manualList || source == manualListMenuItem) {
			ChessLoadingGUI loading = new ChessLoadingGUI();
			loading.setVisible(true);
		} else if (source == bgSound){
			ChessUtil.setLoop(bgSound.isSelected());
		} else if (source == gmSound){
			ChessUtil.setStart(gmSound.isSelected());
		}

	}

	/**
	 * 读入棋谱文件
	 * 
	 */
	@SuppressWarnings("unchecked")
	private void read() {
		JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				EXTENSION_NAME2, EXTENSION_NAME2);
		fileChooser.setFileFilter(filter);

		int state = fileChooser.showOpenDialog(this);
		File openFile = fileChooser.getSelectedFile();
		if (openFile != null && state == JFileChooser.APPROVE_OPTION) {
			// 读取当前目录下的棋谱文件

			GameRecord gameRecord = ManualUtil.readManual(openFile);

			ChessShowGUI demo = new ChessShowGUI(gameRecord);

			demo.setVisible(true);
			dispose();

		}

	}

	/**
	 * 演示当前步的下一步
	 * 
	 */
	private void next() {
		if (curIndex == gameRecord.getDescs().size() - 1) {
			return;
		}
		curIndex++;
		if (curIndex < gameRecord.getRecords().size()) {
			step(curIndex);
		}
		scrollToView();
	}

	/**
	 * 演示index指定的一步
	 * 
	 * @param index
	 */
	public void step(int index) {
		ManualItem moveRecord = gameRecord.getRecords().get(index);
		MoveStep step = moveRecord.getMoveStep();
		Point pStart = step.start;
		Point pEnd = step.end;
		int startI = pStart.x;
		int startJ = pStart.y;
		int endI = pEnd.x;
		int endJ = pEnd.y;

		// 如果移动，应该画2条提示框
		board.setMoveFlag(true);
		board.movePoints[0] = new Point(endI, endJ);
		board.movePoints[1] = new Point(startI, startJ);

		ChessPiece piece = board.chessPoints[startI][startJ].getPiece();
		if (board.chessPoints[endI][endJ].hasPiece()) {
			ChessPiece pieceRemoved = board.chessPoints[endI][endJ]
					.getPiece();
			board.remove(pieceRemoved);
			board.chessPoints[endI][endJ].setPiece(piece, board);
			board.chessPoints[startI][startJ].setHasPiece(false);
		} else {
			board.chessPoints[endI][endJ].setPiece(piece, board);
			board.chessPoints[startI][startJ].setHasPiece(false);
		}
		
		board.repaint();
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

		int size = gameRecord.getRecords().size();

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
			record = (ManualItem) gameRecord.getRecords().get(curIndex);
			eatedPiece = PieceUtil.createPiece(record.getEatedPieceId());

			moveStep = record.getMoveStep();
			startI = moveStep.start.x;
			startJ = moveStep.start.y;
			endI = moveStep.end.x;
			endJ = moveStep.end.y;

			// 上一步没有吃棋子
			if (eatedPiece == null) {
				ChessPiece piece = board.chessPoints[endI][endJ].getPiece();
				board.chessPoints[startI][startJ].setPiece(piece, board);
				(board.chessPoints[endI][endJ]).setHasPiece(false);

			}
			// 上一步吃了棋子
			else {
				ChessPiece piece = board.chessPoints[endI][endJ].getPiece();
				board.chessPoints[startI][startJ].setPiece(piece, board);

				board.chessPoints[endI][endJ].setPiece(eatedPiece, board);
				(board.chessPoints[endI][endJ]).setHasPiece(true);

			}
		}

//		// 后退时，画标记与正常移动时不同
//		if (curIndex >= 1) {
//			record = (ManualItem) gameRecord.getRecords().get(curIndex - 1);
//			moveStep = record.getMoveStep();
//			startI = moveStep.start.x;
//			startJ = moveStep.start.y;
//			endI = moveStep.end.x;
//			endJ = moveStep.end.y;
//		}
//		// 如果移动，应该画2条提示框
//		board.setMoveFlag(true);
//		board.movePoints[0] = new Point((int) (board.chessPoints[endI][endJ]
//				.getX()), (int) (board.chessPoints[endI][endJ].getY()));
//		board.movePoints[1] = new Point(
//				(int) (board.chessPoints[startI][startJ].getX()),
//				(int) (board.chessPoints[startI][startJ].getY()));

		curIndex--;

		scrollToView();
		if (curIndex == -1) {
			// 退回到没有任何棋子移动的状态时，不用画移动标记
			board.setMoveFlag(false);
			/*
			 * 当curIndex == -1时，应该不选中任何一样,
			 * 而manual.setSelectedIndex(-1)不能时列表框不选中任何一行
			 * manual.setListData(descs);可以恢复到默认状态不选中任何一行
			 */
			manual.setListData(gameRecord.getDescs());
		}

		board.repaint();
	}

	private void scrollToView() {
		if (curIndex >= 0 && curIndex < gameRecord.getDescs().size()) {
			int lastIndex = curIndex;
			Rectangle rect = manual.getCellBounds(lastIndex, lastIndex);
			manualScroll.getViewport().scrollRectToVisible(rect);
			// 选中当前行，提示玩家
			manual.setSelectedIndex(curIndex);
		}

	}

	/**
	 * 根据棋谱类型初始化棋子的位置
	 * 
	 */
	private void initPieces() {
		//ManualType type = gameRecord.getFlag();
		if (gameRecord.getInitLocations()==null) {
			board.initChess(gameRecord.getPalyerFirst());
		} else{
			int size = gameRecord.getInitLocations().size();
			System.out.println("初始化时棋子个数：" + size);
			board.setPlayerName("红方");
			for (int i = 0; i < size; i++) {
				Position location = gameRecord.getInitLocations().get(i);
				if (location != null) {
					PieceId id = location.getId();
					int x = (int) location.getPoint().getX();
					int y = (int) location.getPoint().getY();

					ChessPiece piece = PieceUtil.createPiece(id);
					board.chessPoints[x][y].setPiece(piece, board);
				}

			}
		}

		board.validate();
		board.repaint();
	}

	/**
	 * 自动演示线程
	 */
	private class DemoThread extends Thread {

		/**
		 * 构造函数
		 */
		public DemoThread() {
			System.out.println("自动演示线程构造完成了");
		}

		/*
		 * （非 Javadoc）
		 * 
		 * @see java.lang.Thread#run()
		 */
		public void run() {
			System.out.println("自动演示线程即将运行");
			int size = gameRecord.getRecords().size();

			// 从当前行演示到最后一行
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

	/*
	 * （非 Javadoc）
	 * 
	 * @see com.fans.chess.common.ISaveManual#getSavePaths()
	 */

	public ArrayList<String> getSavePaths() {
		ArrayList<String> paths = new ArrayList<String>();
		String path = "";
		String descsPath = "";

		URL url = ChessUtil.class.getResource("");
		if (url == null) {
			return null;

		}
		ManualType type = gameRecord.getType();
		if (type == ManualType.PRINT_WHOLE) {
			//path = url.getPath() + "/manual/whole/";
			//descsPath = url.getPath() + "/manualdesc/whole/";
			path = "src/manuals/whole/";
			descsPath = path;
		} else if (type == ManualType.PRINT_PARTIAL) {
			path = "src/manuals/partial/";
			descsPath = path;
		} else if (type == ManualType.MAN_MACHINE) {
			path = "src/manuals/ai/";
			descsPath = path;
		} else if (type == ManualType.TWO_MAN) {
			path = "src/manuals/palyer/";
			descsPath = path;
		}
		paths.add(path);
		paths.add(descsPath);
		return paths;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see com.fans.chess.common.ISaveManual#getGameRecord()
	 */
	public GameRecord getGameRecord() {
		String palyerFirst;
		if(gameRecord.getPalyerFirst()==null){
			palyerFirst = PALYER_FIRST;
		}else{
			palyerFirst = COMPUTER_FIRST;
		}
		GameRecord gameRecord2 = new GameRecord(gameRecord.getType(),
				ChessUtil.getDateAndTime(), "",
				gameRecord.getRecords(), gameRecord.getDescs(),
				gameRecord.getInitLocations(),palyerFirst);
		return gameRecord2;
	}
}
