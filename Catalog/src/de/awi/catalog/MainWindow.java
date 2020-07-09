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
import javax.swing.DefaultListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.awi.catalog.gui.ChecklistSplitPane;
import de.awi.catalog.gui.DeviceSplitPane;
import de.awi.catalog.gui.DeviceTable;
import de.awi.catalog.gui.PartSplitPane;
import de.awi.catalog.gui.PartTable;
import de.awi.catalog.gui.StorageLocationSplitPane;
import de.awi.catalog.gui.StorageLocationTable;
import de.awi.catalog.gui.StorageSplitPane;
import de.awi.catalog.gui.StorageUnitSplitPane;
import de.awi.catalog.gui.StorageUnitTable;
import de.awi.catalog.models.ChecklistModel;
import de.awi.catalog.models.Device;
import de.awi.catalog.models.DeviceModel;
import de.awi.catalog.models.PartModel;
import de.awi.catalog.models.PartStorageUnitsModel;
import de.awi.catalog.models.StorageLocationModel;
import de.awi.catalog.models.StorageModel;
import de.awi.catalog.models.StorageUnitModel;
import de.traviadan.lib.db.Db;
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
	private PartModel partModel;
	private StorageUnitModel storageUnitModel;
	private StorageLocationModel storageLocationModel;
	private StorageModel storageModel;
	private PartStorageUnitsModel partStorageUnitsModel;
	private ChecklistModel checklistModel;
	
	private PartSplitPane partPane;
	
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
		partModel = new PartModel();
		partModel.createDbTable(db);
		storageUnitModel = new StorageUnitModel();
		storageUnitModel.createDbTable(db);
		storageModel = new StorageModel();
		storageModel.createDbTable(db);
		storageLocationModel = new StorageLocationModel();
		storageLocationModel.createDbTable(db);
		partStorageUnitsModel = new PartStorageUnitsModel();
		partStorageUnitsModel.createDbTable(db);
		checklistModel = new ChecklistModel();
		checklistModel.createDbTable(db);
		
		init();
	}
	
	@Override
	protected void opened() {
		partPane.setDividers();
	}
	
	private void init() {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	    // set layout
	    setLayout(new BorderLayout(5, 5));
	    // Size of the window
	    setSize(900, 700);
	    // Locate Window in the middle of the screen
	    setLocationRelativeTo(null);

	    initMenuBar();
	    initContent();
	    
	    log.msg(this.loadFileToString());
	    log.msg(props.getProperty("Version"));
	    log.msg(props.getProperty("Hash"));
	}

	private void initContent() {
		JTabbedPane tabbedPane = new JTabbedPane();
		
		partPane = new PartSplitPane(db);
		
		DeviceSplitPane devicePane = new DeviceSplitPane(db);

		ChecklistSplitPane checklistPane = new ChecklistSplitPane(db);
		
		StorageUnitSplitPane storageUnitPane = new StorageUnitSplitPane(db);
		DeviceTable.class.cast(devicePane.getTable()).addStockpilingListener(storageUnitPane);
		PartTable.class.cast(partPane.getTable()).addStockpilingListener(storageUnitPane);

		StorageLocationSplitPane storagelocationPane = new StorageLocationSplitPane(db);
		StorageUnitTable.class.cast(storageUnitPane.getTable()).addStockpilingListener(storagelocationPane);
		
		StorageSplitPane storagePane = new StorageSplitPane(db);
		StorageLocationTable.class.cast(storagelocationPane.getTable()).addStockpilingListener(storagePane);

		storageUnitPane.addListSelectionListener(devicePane.getTable().getListSelectionListener());
		storageUnitPane.addListSelectionListener(partPane.getTable().getListSelectionListener());
		devicePane.addListSelectionListener(checklistPane.getListSelectionListener());
		devicePane.addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					if (e.getSource() instanceof DefaultListSelectionModel) {
						if (((DefaultListSelectionModel)e.getSource()).getSelectedItemsCount() > 0) {
							int row = devicePane.getTable().getSelectedRow();
							checklistPane.setDevice((Device)devicePane.getModel().getObjectAtRow(row));
						}
					}
				}
			}
		});
		
		tabbedPane.addTab("Bauteile", partPane);
		tabbedPane.addTab("Geräte", devicePane);
		tabbedPane.addTab("Prüfungen", checklistPane);
		tabbedPane.addTab("Lagereinheiten", storageUnitPane);
		tabbedPane.addTab("Lagerorte", storagelocationPane);
		tabbedPane.addTab("Lager", storagePane);
		
		add(tabbedPane);
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
