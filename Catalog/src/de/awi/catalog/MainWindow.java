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
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

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
		init();
	}
	
	private void init() {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	    
	    // set layout
	    setLayout(new BorderLayout(5, 5));
	    // Size of the window
	    setSize(800, 650);
	    // Locate Window in the middle of the screen
	    setLocationRelativeTo(null);
	    // Show window
	    //setVisible(true);

	    initMenuBar();
	    initPopupMenu();
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
	
	private void initPopupMenu() {
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

        addMouseListener(new MouseAdapter() {

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
		
		JPanel panel2 = new JPanel();
		panel2.setLayout(new BorderLayout(5, 5));

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		JPanel panel3 = new JPanel();
		panel3.setLayout(new BorderLayout(5, 5));
		initTableDevice(panel3);
		
		splitPane.add(panel3);
		JPanel panel4 = new JPanel();
		panel4.setLayout(new BorderLayout(5, 5));
		splitPane.add(panel4);

		tabbedPane.addTab("Tab 1", panel1);
		tabbedPane.addTab("Tab 2", panel2);
		tabbedPane.addTab("Tab 3", splitPane);
		add(tabbedPane);
	}
	
	private void initTableDevice(JComponent cmp) {
		String[][] rowData = {
				{ "Japan", "245" }, { "USA", "240" }, { "Italien", "220" },
			    { "Spanien", "217" }, {"T�rkei", "215"} ,{ "England", "214" },
			    { "Frankreich", "190" }, {"Griechenland", "185" },
			    { "Deutschland", "180" }, {"Portugal", "170" }
		};

		String[] columnNames = {
				"Land", "Durchschnittliche Fernsehdauer pro Tag in Minuten"
		};

		JTable table = new JTable(rowData, columnNames);
		cmp.add(new JScrollPane(table));
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