package de.awi.catalog.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.awi.catalog.models.Checklist;
import de.awi.catalog.models.Device;
import de.traviadan.lib.gui.GuiFactory;
import de.traviadan.lib.gui.WindowClosedListener;
import de.traviadan.lib.gui.WindowFrame;
import de.traviadan.lib.gui.WindowOpenedListener;
import de.traviadan.lib.helper.Check;
import de.traviadan.lib.helper.Log;

public class ChecklistView extends WindowFrame {
	public static final int TXT_TESTNR = 0;
	public static final int TXT_DESCRIPTION = 1;
	public static final int TXT_TESTDATE = 2;
	public static final int TXT_EQUIPMENT = 3;
	public static final int TXT_TESTTYPE = 4;
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
	
	private Icon imgChecked;
	private Icon imgUnchecked;
	private Icon imgDivider;
	private List<JTextField> txtFields = new ArrayList<>();
	private int txtFieldsCount = 10;
	private List<JCheckBox> chkBoxes = new ArrayList<>();
	private int chkBoxesCount = 13;
	private static final long serialVersionUID = 1L;
	private Container parent;
	private Device device;
	private Checklist checklist;
	
	public ChecklistView(Container parent, Device d) {
		super();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.parent = parent;
		this.device = d;
		this.checklist = new Checklist();
		//setUndecorated(true);
		imgChecked = imgChecked();
		imgUnchecked = imgUnchecked();
		imgDivider = imgDivider();
		initContent();
	}
	
	public Checklist getChecklist() {
		return this.checklist;
	}
	
	public void setChecklist(Checklist c) {
		this.checklist = c;
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
		
		for (int i=0; i < txtFieldsCount; i++) {
			txtFields.add(new JTextField());
			txtFields.get(i).setName(""+i);
		}
		addTextfieldListener();
		setTextFields();
		for (int i=0; i < chkBoxesCount; i++) {
			chkBoxes.add(new JCheckBox(imgUnchecked, false));
			chkBoxes.get(i).setSelectedIcon(imgChecked);
			chkBoxes.get(i).setName(""+i);
		}
		addCheckBoxListener();
		setCheckBoxes();
		
		GuiFactory.placeComponent(contentPanel, gbl, "Test-Nr.:", txtFields.get(TXT_TESTNR), 0, 0);
		GuiFactory.placeComponent(contentPanel, gbl, "Testdatum:", txtFields.get(TXT_TESTDATE), 2, 0);
		GuiFactory.placeComponent(contentPanel, gbl, "Ident-Nr.:", new JLabel(device.getUnitnr()), 0, 1);
		GuiFactory.placeComponent(contentPanel, gbl, "Name:", new JLabel(device.getName()), 2, 1);
		GuiFactory.placeComponent(contentPanel, gbl, "Beschreibung:", txtFields.get(TXT_DESCRIPTION),
				new int[] { GridBagConstraints.NONE, GridBagConstraints.HORIZONTAL },
				new int[] { GridBagConstraints.EAST, GridBagConstraints.WEST },
				new int[] { 1, 3 },	new int[] { 1, 1 },
				new float[][] { {0.5f, 0.0f}, {1.0f, 0.0f} },
				new int[][] { {10, 20}, {10, 3} },
				new Insets[] { new Insets(10, 0, 30, 0), new Insets(10, 0, 30, 0) }, 0, 2);
		GuiFactory.placeComponent(contentPanel, gbl, "Testequipment:", txtFields.get(TXT_EQUIPMENT), 0, 3);
		GuiFactory.placeComponent(contentPanel, gbl, "Testart:", txtFields.get(TXT_TESTTYPE), 2, 3);
		GuiFactory.placeComponent(contentPanel, gbl, "Tester:", txtFields.get(TXT_CHECKER), 0, 4);
		
		GuiFactory.placeComponent(contentPanel, gbl, "Prüfergebnis:", chkBoxes.get(CHK_RESULT), 2, 4);
		GuiFactory.placeComponent(contentPanel, gbl, "Kommentar:", txtFields.get(TXT_COMMENT),
				new int[] { GridBagConstraints.NONE, GridBagConstraints.HORIZONTAL },
				new int[] { GridBagConstraints.EAST, GridBagConstraints.WEST },
				new int[] { 1, 3 },	new int[] { 1, 1 },
				new float[][] { {0.5f, 0.0f}, {1.0f, 0.0f} },
				new int[][] { {10, 20}, {10, 3} },
				new Insets[] { new Insets(10, 0, 40, 0), new Insets(10, 0, 40, 0) }, 0, 5);
		GuiFactory.placeSingleComponent(contentPanel, gbl, new JLabel(imgDivider),
				GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST,
				4, 1, new float[] {1.0f, 1.0f},	new int[] {0, 0}, new Insets(10, 0, 40, 0),	0, 6);
		GuiFactory.placeComponent(contentPanel, gbl, "Gehäuse:", chkBoxes.get(CHK_HOUSING), 0, 7);
		GuiFactory.placeComponent(contentPanel, gbl, "Stecker:", chkBoxes.get(CHK_PLUG), 2, 7);
		GuiFactory.placeComponent(contentPanel, gbl, "Anschlusskabel:", chkBoxes.get(CHK_CABLE), 0, 8);
		GuiFactory.placeComponent(contentPanel, gbl, "Schalter:", chkBoxes.get(CHK_SWITCH), 2, 8);
		GuiFactory.placeComponent(contentPanel, gbl, "Steckdosen:", chkBoxes.get(CHK_SOCKET), 0, 9);
		GuiFactory.placeComponent(contentPanel, gbl, "Aufbau:", chkBoxes.get(CHK_SETTING), 2, 9);
		GuiFactory.placeComponent(contentPanel, gbl, "Zustand:", chkBoxes.get(CHK_FITNESS), 0, 10);
		GuiFactory.placeComponent(contentPanel, gbl, "Zubehör:", chkBoxes.get(CHK_PERIPHERALS), 2, 10);
		GuiFactory.placeComponent(contentPanel, gbl, "Sichtprüfung:", chkBoxes.get(CHK_GENERAL), 0, 11);
		GuiFactory.placeComponent(contentPanel, gbl, "Schutzleiterwiderstand:", chkBoxes.get(CHK_PROTECTIVE_GROUND), 0, 12);
		GuiFactory.placeComponent(contentPanel, gbl, "Isolationswiderstand:", chkBoxes.get(CHK_INSULATION), 2, 12);
		GuiFactory.placeComponent(contentPanel, gbl, "Messwert (mOhm):", txtFields.get(TXT_PROTECTIVE_GROUND_VOLTAGE), 0, 13);
		GuiFactory.placeComponent(contentPanel, gbl, "Messwert (MOhm):", txtFields.get(TXT_INSULATION_VOLTAGE), 2, 13);
		GuiFactory.placeComponent(contentPanel, gbl, "Ableitstrom:", chkBoxes.get(CHK_CURRENT), 0, 14);
		GuiFactory.placeComponent(contentPanel, gbl, "Messwert (mA):", txtFields.get(TXT_CURRENT_AMPERE), 2, 14);
	}
	
	private void addTextfieldListener() {
		for (int i=0; i < txtFieldsCount; i++) {
			txtFields.get(i).addFocusListener(new FocusListener() {
				@Override public void focusLost(FocusEvent e) {
					if (!e.isTemporary()) {
						updateChecklist((JTextField)e.getComponent());
					}
				}
				@Override public void focusGained(FocusEvent e) {}
			});
		}
	}
	
	private void addCheckBoxListener() {
		for (int i=0; i < chkBoxesCount; i++) {
			chkBoxes.get(i).addActionListener(new ActionListener() {
				@Override public void actionPerformed(ActionEvent e) {
					updateChecklist((JCheckBox)e.getSource());
				}
			});
		}
	}
	
	private void setTextFields() {
		if (checklist == null) return;
		txtFields.get(TXT_TESTNR).setText(checklist.getTestnr());
		txtFields.get(TXT_DESCRIPTION).setText(checklist.getDescription());
		txtFields.get(TXT_TESTDATE).setText(checklist.getTestdate());
		txtFields.get(TXT_EQUIPMENT).setText(checklist.getEquipment());
		txtFields.get(TXT_TESTTYPE).setText(checklist.getTesttype());
		txtFields.get(TXT_CHECKER).setText(checklist.getChecker());
		txtFields.get(TXT_COMMENT).setText(checklist.getComment());
		txtFields.get(TXT_PROTECTIVE_GROUND_VOLTAGE).setText(""+checklist.getProtectiveGroundVoltage());
		txtFields.get(TXT_INSULATION_VOLTAGE).setText(""+checklist.getInsulationVoltage());
		txtFields.get(TXT_CURRENT_AMPERE).setText(""+checklist.getCurrentAmpere());
	}
	
	private void setCheckBoxes() {
		if (checklist == null) return;
		boolean state = false;
		state = (checklist.getResult() == "i.O.") ? true : false;
		chkBoxes.get(CHK_RESULT).setSelected(state);
		state = (checklist.getHousingResult() > 0) ? true : false;
		chkBoxes.get(CHK_HOUSING).setSelected(state);
		state = (checklist.getPlugResult() > 0) ? true : false;
		chkBoxes.get(CHK_PLUG).setSelected(state);
		state = (checklist.getCableResult() > 0) ? true : false;
		chkBoxes.get(CHK_CABLE).setSelected(state);
		state = (checklist.getSwitchResult() > 0) ? true : false;
		chkBoxes.get(CHK_SWITCH).setSelected(state);
		state = (checklist.getSocketResult() > 0) ? true : false;
		chkBoxes.get(CHK_SOCKET).setSelected(state);
		state = (checklist.getSettingResult() > 0) ? true : false;
		chkBoxes.get(CHK_SETTING).setSelected(state);
		state = (checklist.getFitnessResult() > 0) ? true : false;
		chkBoxes.get(CHK_FITNESS).setSelected(state);
		state = (checklist.getPeripheralsResult() > 0) ? true : false;
		chkBoxes.get(CHK_PERIPHERALS).setSelected(state);
		state = (checklist.getGeneralResult() > 0) ? true : false;
		chkBoxes.get(CHK_GENERAL).setSelected(state);
		state = (checklist.getProtectiveGroundResult() > 0) ? true : false;
		chkBoxes.get(CHK_PROTECTIVE_GROUND).setSelected(state);
		state = (checklist.getInsulationResult() > 0) ? true : false;
		chkBoxes.get(CHK_INSULATION).setSelected(state);
		state = (checklist.getCurrentResult() > 0) ? true : false;
		chkBoxes.get(CHK_CURRENT).setSelected(state);
	}
	
	private void updateChecklist(JCheckBox chkBox) {
		try {
			int idx = Integer.parseInt(chkBox.getName());
			int result = 0;
			if (chkBox.isSelected()) result = 1;
			switch (idx) {
			case CHK_RESULT:
				if (chkBox.isSelected()) checklist.setResult("i.O.");
				else checklist.setResult("nicht i.O.");
				break;
			case CHK_HOUSING:
				checklist.setHousingResult(result);
				break;
			case CHK_PLUG:
				checklist.setPlugResult(result);
				break;
			case CHK_CABLE:
				checklist.setCableResult(result);
				break;
			case CHK_SWITCH:
				checklist.setSwitchResult(result);
				break;
			case CHK_SOCKET:
				checklist.setSocketResult(result);
				break;
			case CHK_SETTING:
				checklist.setSettingResult(result);
				break;
			case CHK_FITNESS:
				checklist.setFitnessResult(result);
				break;
			case CHK_PERIPHERALS:
				checklist.setPeripheralsResult(result);
				break;
			case CHK_GENERAL:
				checklist.setGeneralResult(result);
				break;
			case CHK_PROTECTIVE_GROUND:
				checklist.setProtectiveGroundResult(result);
				break;
			case CHK_INSULATION:
				checklist.setInsulationResult(result);
				break;
			case CHK_CURRENT:
				checklist.setCurrentResult(result);
				break;
			}
		} catch (NumberFormatException e) {
			return;
		}
	}
	
	private void updateChecklist(JTextField txtField) {
		try {
			int idx = Integer.parseInt(txtField.getName());
			switch (idx) {
			case TXT_TESTNR:
				checklist.setTestnr(txtField.getText().strip());
				break;
			case TXT_DESCRIPTION:
				checklist.setDescription(txtField.getText().strip());
				break;
			case TXT_TESTDATE:
				checklist.setTestdate(txtField.getText().strip());
				break;
			case TXT_EQUIPMENT:
				checklist.setEquipment(txtField.getText().strip());
				break;
			case TXT_TESTTYPE:
				checklist.setTesttype(txtField.getText().strip());
				break;
			case TXT_CHECKER:
				checklist.setChecker(txtField.getText().strip());
				break;
			case TXT_COMMENT:
				checklist.setComment(txtField.getText().strip());
				break;
			case TXT_PROTECTIVE_GROUND_VOLTAGE:
				checklist.setProtectiveGroundVoltage(Check.forDouble(txtField));
				break;
			case TXT_INSULATION_VOLTAGE:
				checklist.setInsulationVoltage(Check.forDouble(txtField));
				break;
			case TXT_CURRENT_AMPERE:
				checklist.setCurrentAmpere(Check.forDouble(txtField));
				break;
			}
		} catch (NumberFormatException e) {
			return;
		}
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

	private Icon imgDivider() {
		BufferedImage img = null;
        try {
			img = ImageIO.read(getClass().getResource("/de/awi/catalog/gui/resources/divider.png"));
		} catch (IOException e) {
			System.out.println("Divider Icon nicht gefunden.");
		}
        ImageIcon icon = new ImageIcon(img);
        return icon;
	}

	@Override
	protected void initFrame() {
		setSize(600, 700);
		setLocationRelativeTo(parent);
		addWindowListener(new WindowClosedListener());
	    addWindowListener(new WindowOpenedListener());
	}

	@Override
	protected void opened() {
		super.opened();
	}
	@Override
	protected void closed() {
		super.closed();
	}
	
	
}
