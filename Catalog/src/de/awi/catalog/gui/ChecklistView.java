package de.awi.catalog.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import de.awi.catalog.models.Device;
import de.traviadan.lib.gui.GuiFactory;
import de.traviadan.lib.gui.WindowClosedListener;
import de.traviadan.lib.gui.WindowFrame;
import de.traviadan.lib.helper.Log;

public class ChecklistView extends WindowFrame {
	public static final int TXT_TESTNR = 0;
	public static final int TXT_DESCRIPTION = 1;
	public static final int TXT_TESTDATE = 2;
	public static final int TXT_EQUIPMENT = 3;
	public static final int TXT_TESTYPE = 4;
	public static final int TXT_CHECKER = 5;
	public static final int TXT_COMMENT = 6;
	public static final int TXT_PROTECTIVE_GROUND_VOLTAGE = 7;
	public static final int TXT_INSULATION_VOLTAGE = 8;
	public static final int TXT_CURRENT_AMPERE = 9;
	public static final int CHK_RESULT = 0;
	public static final int CHK_HOUSING = 1;
	public static final int CHK_PLUG = 2;
	public static final int CHK_CABLE = 3;
	public static final int CHK_SWITCH = 4;
	public static final int CHK_SOCKET = 5;
	public static final int CHK_SETTING = 6;
	public static final int CHK_FITNESS = 7;
	public static final int CHK_PERIPHERALS = 8;
	public static final int CHK_GENERAL = 9;
	public static final int CHK_PROTECTIVE_GROUND = 10;
	public static final int CHK_INSULATION = 11;
	public static final int CHK_CURRENT = 12;
	
	private List<JTextField> txtFields = new ArrayList<>();
	private static final long serialVersionUID = 1L;
	private Container parent;
	private Device device;
	
	public ChecklistView(Container parent, Device d) {
		super();
		this.parent = parent;
		this.device = d;
		//setUndecorated(true);
		initContent();
	}

	private void initContent() {
		GridBagLayout gbl = new GridBagLayout();
		JPanel contentPanel = new JPanel(gbl);
		contentPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
		add(contentPanel);
		
		contentPanel.setBorder(new Border() {
			@Override
			public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {	}
			@Override
			public boolean isBorderOpaque() {return false;}
			@Override
			public Insets getBorderInsets(Component c) {return new Insets(20, 40, 20, 40);}
		});
		
		for (int i=0; i < 10; i++) {
			txtFields.add(new JTextField());
		}
		GuiFactory.placeComponent(contentPanel, gbl, "Test-Nr.:", txtFields.get(TXT_TESTNR), 0, 0);
		GuiFactory.placeComponent(contentPanel, gbl, "Testdatum:", txtFields.get(TXT_TESTDATE), 2, 0);
		GuiFactory.placeComponent(contentPanel, gbl, "Ident-Nr.:", new JLabel(device.getUnitnr()), 0, 1);
		GuiFactory.placeComponent(contentPanel, gbl, "Name:", new JLabel(device.getName()), 2, 1);
		GuiFactory.placeComponent(contentPanel, gbl, "Beschreibung:", txtFields.get(TXT_DESCRIPTION),
				new int[] { GridBagConstraints.NONE, GridBagConstraints.HORIZONTAL },
				new int[] { GridBagConstraints.EAST, GridBagConstraints.WEST },
				new int[] { 1, 3 },
				new int[] { 1, 1 },
				new float[][] { {0.5f, 0.0f}, {1.0f, 0.0f} },
				new int[][] { {10, 20}, {10, 3} },
				new Insets[] { new Insets(10, 0, 30, 0), new Insets(10, 0, 30, 0) },
				0, 2);
		GuiFactory.placeComponent(contentPanel, gbl, "Testequipment:", txtFields.get(TXT_EQUIPMENT), 0, 3);
		GuiFactory.placeComponent(contentPanel, gbl, "Testart:", txtFields.get(TXT_TESTYPE), 2, 3);
		GuiFactory.placeComponent(contentPanel, gbl, "Tester:", txtFields.get(TXT_CHECKER), 0, 4);
		
		JCheckBox cbResult = new JCheckBox(imgUnchecked(), false);
		cbResult.setSelectedIcon(imgChecked());
		GuiFactory.placeComponent(contentPanel, gbl, "Prüfergebnis:", cbResult, 2, 4);
		GuiFactory.placeComponent(contentPanel, gbl, "Kommentar:", txtFields.get(TXT_COMMENT),
				new int[] { GridBagConstraints.NONE, GridBagConstraints.HORIZONTAL },
				new int[] { GridBagConstraints.EAST, GridBagConstraints.WEST },
				new int[] { 1, 3 },
				new int[] { 1, 1 },
				new float[][] { {0.5f, 0.0f}, {1.0f, 0.0f} },
				new int[][] { {10, 20}, {10, 3} },
				new Insets[] { new Insets(10, 0, 40, 0), new Insets(10, 0, 40, 0) },
				0, 5);
		
	}
	
	private Icon imgUnchecked() {
		BufferedImage img = null;
        try {
			img = ImageIO.read(getClass().getResource("/de/awi/catalog/gui/resources/unchecked.png"));
		} catch (IOException e) {
			System.out.println("Unchecked Icon nicht gefunden.");
		}
        ImageIcon icon = new ImageIcon(img);
        return icon;
	}
	
	private Icon imgChecked() {
		BufferedImage img = null;
        try {
			img = ImageIO.read(getClass().getResource("/de/awi/catalog/gui/resources/checked.png"));
		} catch (IOException e) {
			System.out.println("Checked Icon nicht gefunden.");
		}
        ImageIcon icon = new ImageIcon(img);
        return icon;
	}

	@Override
	protected void initFrame() {
		setSize(600, 400);
		setLocationRelativeTo(parent);
	    addWindowListener(new WindowClosedListener());
	}

	@Override
	protected void closed() {
		// TODO Auto-generated method stub
		super.closed();
	}
	
	
}
