package de.awi.catalog.models;

import de.traviadan.lib.db.DbFieldGetter;
import de.traviadan.lib.db.DbFieldSetter;
import de.traviadan.lib.db.DbTableName;

@DbTableName(name="checklists")
public class Checklist {
	public static final String ID = "checklistid";
	public static final String TESTNR = "testnr";
	public static final String DESCRIPTION = "description";
	public static final String TESTDATE = "testdate";
	public static final String EQUIPMENT = "equipment";
	public static final String TESTTYPE = "testtype";
	public static final String CHECKER = "checker";
	public static final String RESULT = "result";
	public static final String COMMENT = "comment";
	public static final String HOUSING = "housing";
	public static final String PLUG = "plug";
	public static final String CABLE = "cable";
	public static final String SWITCH = "switch";
	public static final String SOCKET = "socket";
	public static final String SETTING = "setting";
	public static final String FITNESS = "fitness";
	public static final String PERIPHERALS = "peripherals";
	public static final String GENERAL = "general";
	public static final String PROTECTIVE_GROUND = "protectiveground";
	public static final String PROTECTIVE_GROUND_VOLTAGE = "protectivegroundvoltage";
	public static final String INSULATION = "insulation";
	public static final String INSULATION_VOLTAGE = "insulationvoltage";
	public static final String CURRENT = "current";
	public static final String CURRENT_AMPERE = "currentampere";
	
	private int id;
	private String testnr;
	private String description;
	private String testtype;
	private String testdate;
	private String equipment;
	private String checker;
	private String result;
	private String comment;
	private int housingResult;
	private int plugResult;
	private int cableResult;
	private int switchResult;
	private int socketResult;
	private int settingResult;
	private int fitnessResult;
	private int peripheralsResult;
	private int generalResult;
	private int protectiveGroundResult;
	private double protectiveGroundVoltage;
	private int insulationResult;
	private double insulationVoltage;
	private int currentResult;
	private double currentAmpere;
	
	@DbFieldGetter(name=ID, title="Id", visibility=false, constraint="PRIMARY KEY")
	public int getId() {
		return id;
	}
	@DbFieldSetter(name=ID)
	public void setId(int id) {
		this.id = id;
	}
	@DbFieldGetter(name=DESCRIPTION, title="Beschreibung")
	public String getDescription() {
		return description;
	}
	@DbFieldSetter(name=DESCRIPTION)
	public void setDescription(String description) {
		this.description = description;
	}
	@DbFieldGetter(name=TESTNR, title="Test-Nr.")
	public String getTestnr() {
		return testnr;
	}
	@DbFieldSetter(name=TESTNR)
	public void setTestnr(String testnr) {
		this.testnr = testnr;
	}
	@DbFieldGetter(name=TESTTYPE, title="Testart")
	public String getTesttype() {
		return testtype;
	}
	@DbFieldSetter(name=TESTTYPE)
	public void setTesttype(String testtype) {
		this.testtype = testtype;
	}
	@DbFieldGetter(name=TESTDATE, title="Prüfdatum")
	public String getTestdate() {
		return testdate;
	}
	@DbFieldSetter(name=TESTDATE)
	public void setTestdate(String testdate) {
		this.testdate = testdate;
	}
	@DbFieldGetter(name=EQUIPMENT, title="Prüfgerät")
	public String getEquipment() {
		return equipment;
	}
	@DbFieldSetter(name=EQUIPMENT)
	public void setEquipment(String equipment) {
		this.equipment = equipment;
	}
	@DbFieldGetter(name=CHECKER, title="Prüfer")
	public String getChecker() {
		return checker;
	}
	@DbFieldSetter(name=CHECKER)
	public void setChecker(String checker) {
		this.checker = checker;
	}
	@DbFieldGetter(name=RESULT, title="Ergebnis")
	public String getResult() {
		return result;
	}
	@DbFieldSetter(name=RESULT)
	public void setResult(String result) {
		this.result = result;
	}
	@DbFieldGetter(name=COMMENT, title="Kommentar")
	public String getComment() {
		return comment;
	}
	@DbFieldSetter(name=COMMENT)
	public void setComment(String comment) {
		this.comment = comment;
	}
	@DbFieldGetter(name=HOUSING, visibility=false, title="Gehäuse")
	public int getHousingResult() {
		return housingResult;
	}
	@DbFieldSetter(name=HOUSING)
	public void setHousingResult(int housingResult) {
		this.housingResult = housingResult;
	}
	@DbFieldGetter(name=PLUG, visibility=false, title="Stecker")
	public int getPlugResult() {
		return plugResult;
	}
	@DbFieldSetter(name=PLUG)
	public void setPlugResult(int plugResult) {
		this.plugResult = plugResult;
	}
	@DbFieldGetter(name=CABLE, visibility=false, title="Verkabelung")
	public int getCableResult() {
		return cableResult;
	}
	@DbFieldSetter(name=CABLE)
	public void setCableResult(int cableResult) {
		this.cableResult = cableResult;
	}
	@DbFieldGetter(name=SWITCH, visibility=false, title="Schalter")
	public int getSwitchResult() {
		return switchResult;
	}
	@DbFieldSetter(name=SWITCH)
	public void setSwitchResult(int switchResult) {
		this.switchResult = switchResult;
	}
	@DbFieldGetter(name=SOCKET, visibility=false, title="Buchsen")
	public int getSocketResult() {
		return socketResult;
	}
	@DbFieldSetter(name=SOCKET)
	public void setSocketResult(int socketResult) {
		this.socketResult = socketResult;
	}
	@DbFieldGetter(name=SETTING, visibility=false, title="Aufbau")
	public int getSettingResult() {
		return settingResult;
	}
	@DbFieldSetter(name=SETTING)
	public void setSettingResult(int settingResult) {
		this.settingResult = settingResult;
	}
	@DbFieldGetter(name=FITNESS, visibility=false, title="Zustand")
	public int getFitnessResult() {
		return fitnessResult;
	}
	@DbFieldSetter(name=FITNESS)
	public void setFitnessResult(int fitnessResult) {
		this.fitnessResult = fitnessResult;
	}
	@DbFieldGetter(name=PERIPHERALS, visibility=false, title="Zubehör")
	public int getPeripheralsResult() {
		return peripheralsResult;
	}
	@DbFieldSetter(name=PERIPHERALS)
	public void setPeripheralsResult(int peripheralsResult) {
		this.peripheralsResult = peripheralsResult;
	}
	@DbFieldGetter(name=GENERAL, visibility=false, title="Gesamtzustand")
	public int getGeneralResult() {
		return generalResult;
	}
	@DbFieldSetter(name=GENERAL)
	public void setGeneralResult(int generalResult) {
		this.generalResult = generalResult;
	}
	@DbFieldGetter(name=PROTECTIVE_GROUND, visibility=false, title="Schutzleiterwiderstand")
	public int getProtectiveGroundResult() {
		return protectiveGroundResult;
	}
	@DbFieldSetter(name=PROTECTIVE_GROUND)
	public void setProtectiveGroundResult(int protectiveGroundResult) {
		this.protectiveGroundResult = protectiveGroundResult;
	}
	@DbFieldGetter(name=PROTECTIVE_GROUND_VOLTAGE, visibility=false, title="V")
	public double getProtectiveGroundVoltage() {
		return protectiveGroundVoltage;
	}
	@DbFieldSetter(name=PROTECTIVE_GROUND_VOLTAGE)
	public void setProtectiveGroundVoltage(double protectiveGroundVoltage) {
		this.protectiveGroundVoltage = protectiveGroundVoltage;
	}
	@DbFieldGetter(name=INSULATION, visibility=false, title="Isolationswiderstand")
	public int getInsulationResult() {
		return insulationResult;
	}
	@DbFieldSetter(name=INSULATION)
	public void setInsulationResult(int insulationResult) {
		this.insulationResult = insulationResult;
	}
	@DbFieldGetter(name=INSULATION_VOLTAGE, visibility=false, title="V")
	public double getInsulationVoltage() {
		return insulationVoltage;
	}
	@DbFieldSetter(name=INSULATION_VOLTAGE)
	public void setInsulationVoltage(double insulationVoltage) {
		this.insulationVoltage = insulationVoltage;
	}
	@DbFieldGetter(name=CURRENT, visibility=false, title="Ableitstrom")
	public int getCurrentResult() {
		return currentResult;
	}
	@DbFieldSetter(name=CURRENT)
	public void setCurrentResult(int currentResult) {
		this.currentResult = currentResult;
	}
	@DbFieldGetter(name=CURRENT_AMPERE, visibility=false, title="mA")
	public double getCurrentAmpere() {
		return currentAmpere;
	}
	@DbFieldSetter(name=CURRENT_AMPERE)
	public void setCurrentAmpere(double currentAmpere) {
		this.currentAmpere = currentAmpere;
	}

	@Override
	public int hashCode() {
		final int prime = 12;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Checklist other = (Checklist) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
