package de.awi.catalog;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;

import de.awi.catalog.gui.DeviceSplitPane;
import de.awi.catalog.gui.StorageLocationSplitPane;
import de.awi.catalog.gui.StorageSplitPane;
import de.awi.catalog.gui.StorageUnitSplitPane;
import de.awi.catalog.models.DeviceModel;
import de.awi.catalog.models.PartModel;
import de.awi.catalog.models.StorageLocationModel;
import de.awi.catalog.models.StorageModel;
import de.awi.catalog.models.StorageUnitModel;
import de.traviadan.lib.db.Db;
import de.traviadan.lib.gui.GuiFactory;
import de.traviadan.lib.gui.WindowFrame;
import de.traviadan.lib.helper.AppProps;
import de.traviadan.lib.helper.Log;

import java.io.InputStreamReader;

public class MainWindow extends WindowFrame{

	private static final long serialVersionUID = 1L;
	private JMenuBar menuBar;
	private JPopupMenu popupMenu;
	private AppProps props;
	private Log log;
	private Db db;
	private DeviceModel deviceModel;
	private PartModel electronicPartModel;
	private StorageUnitModel storageUnitModel;
	private StorageLocationModel storageLocationModel;
	private StorageModel storageModel;
	
	public MainWindow() {
		super();
		init();
	}
	
	public MainWindow(String title, Log logger) {
		super(title);
		if (logger != null) {
			log = logger;
		} else {
			log = new Log();
			log.lvl = Log.Level.Failure;
			log.out = Log.Out.System;
			log.start();
		}
		props = AppProps.getInstance();
		if (props.getProperty("Unbekannt") == null) {
			log.msg(String.format("Property \"%s\" konnte nicht geladen werden.", "Unbekannt"), Log.Level.Warning);
		}
		log.msg("Initialisierung");
		db = new Db("catalog.db");
		deviceModel = new DeviceModel();
		deviceModel.createDbTable(db);
		electronicPartModel = new PartModel();
		electronicPartModel.createDbTable(db);
		storageUnitModel = new StorageUnitModel();
		storageUnitModel.createDbTable(db);
		storageModel = new StorageModel();
		storageModel.createDbTable(db);
		storageLocationModel = new StorageLocationModel();
		storageLocationModel.createDbTable(db);
		
		init();
	}
	
	private void init() {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	    
	    // set layout
	    setLayout(new BorderLayout(5, 5));
	    // Size of the window
	    setSize(900, 700);
	    // Locate Window in the middle of the screen
	    setLocationRelativeTo(null);
	    // Show window
	    //setVisible(true);

	    initMenuBar();
	    initContent();
	    
	    log.msg(this.loadFileToString());
	    log.msg(props.getProperty("Version"));
	    log.msg(props.getProperty("Hash"));
	}

	private void createFile(String name) {
		try {
			Files.createFile(Paths.get( System.getProperty("user.dir"), "Testfile.tst" ));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String loadFileToString() {
		InputStream input = getClass().getResourceAsStream("/de/awi/catalog/resources/test.txt");
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		StringBuilder sb = new StringBuilder();
		String line;
		try {
			while((line = reader.readLine()) != null) {
				sb.append(line).append("\n");
			}
			reader.close();
		} catch (IOException e) {
			log.msg("I/O-exception:" + e.getMessage(), Log.Level.Failure);
		}
		return sb.toString();
	}
	
	private void initMenuBar() {
        menuBar = new JMenuBar();
        BufferedImage img = null;
        try {
			img = ImageIO.read(getClass().getResource("/de/awi/catalog/resources/exit.png"));
		} catch (IOException e) {
			log.msg("Exit image resource not found: " + e, Log.Level.Failure);
		}
        ImageIcon exitIcon = new ImageIcon(img);

        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        JMenuItem eMenuItem = new JMenuItem("Exit", exitIcon);
        eMenuItem.setMnemonic(KeyEvent.VK_E);
        eMenuItem.setToolTipText("Exit application");
        eMenuItem.addActionListener((event) -> close(0));

        fileMenu.add(eMenuItem);
        menuBar.add(fileMenu);

        setJMenuBar(menuBar);
    }
	
	private void addPopupMenu(JComponent cmp) {
        popupMenu = new JPopupMenu();

        JMenuItem maximizeMenuItem = new JMenuItem("Maximize");
        maximizeMenuItem.addActionListener((e) -> {
            if (getExtendedState() != JFrame.MAXIMIZED_BOTH) {
                setExtendedState(JFrame.MAXIMIZED_BOTH);
                maximizeMenuItem.setEnabled(false);
            }
        });

        popupMenu.add(maximizeMenuItem);

        JMenuItem quitMenuItem = new JMenuItem("Quit");
        quitMenuItem.addActionListener((e) -> System.exit(0));

        popupMenu.add(quitMenuItem);

        cmp.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (getExtendedState() != JFrame.MAXIMIZED_BOTH) {
                    maximizeMenuItem.setEnabled(true);
                }

                if (e.getButton() == MouseEvent.BUTTON3) {
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }
	
	private void initContent() {
		JTabbedPane tabbedPane = new JTabbedPane();
		JPanel panel1 = new JPanel();
		panel1.setLayout(new BorderLayout(5, 5));
		panel1.add(GuiFactory.getButton("Test", null), BorderLayout.PAGE_START);
		
		DeviceSplitPane devicePane = new DeviceSplitPane(db);
		StorageUnitSplitPane storageUnitPane = new StorageUnitSplitPane(db);
		DeviceTable.class.cast(devicePane.getTable()).addStockpilingListener(storageUnitPane);
		StorageLocationSplitPane storagelocationPane = new StorageLocationSplitPane(db);
		StorageUnitTable.class.cast(storageUnitPane.getTable()).addStockpilingListener(storagelocationPane);
		StorageSplitPane storagePane = new StorageSplitPane(db);
		StorageLocationTable.class.cast(storagelocationPane.getTable()).addStockpilingListener(storagePane);
		
		tabbedPane.addTab("Geräte", devicePane);
		tabbedPane.addTab("Lagereinheiten", storageUnitPane);
		tabbedPane.addTab("Lagerorte", storagelocationPane);
		tabbedPane.addTab("Lager", storagePane);
		tabbedPane.addTab("Tab", panel1);
		
		add(tabbedPane);
	}
	
	
	
	private void showOptionDialog() {
	    final JCheckBox check = new JCheckBox("Tick me");
        final Object[] options = {'e', 2, 3.14, 4, 5, "TURTLES!", check};
        int x = JOptionPane.showOptionDialog(
        		this,
        		"So many options using Object[]",
                "Don't forget to Tick it!",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.ERROR_MESSAGE,
                null, options, options[0]);

        if (check.isSelected() && x != -1) {
            System.out.println("Your choice was " + options[x]);
        } else {
            System.out.println(":( no choice");
        }
	}
}
