package de.awi.catalog;

import java.nio.file.Paths;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;

import javax.swing.SwingUtilities;

import de.awi.catalog.models.Device;
import de.awi.catalog.models.DeviceModel;
import de.traviadan.lib.db.Db;
import de.traviadan.lib.helper.Log;
import de.traviadan.lib.helper.SysProps;

public class Launcher {
	
	public static void main(String[] args) {
		Log log = new Log();
		log.lvl = Log.Level.Debug;
		log.out = Log.Out.System;
		log.start();
		log.msg("Open MainWindow");
		log.msg("Actual path: " + System.getProperty("user.dir"));
		log.msg("Paths.get: " + Paths.get( System.getProperty("user.dir"), "catalog.log" ));
		
		log.msg(SysProps.getComputerName());
		MainWindow win = new MainWindow("AWI Catalog", log);
		
		SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				win.setVisible(true);
			}
		});
		
		for ( Enumeration<Driver> e = DriverManager.getDrivers(); e.hasMoreElements(); )
			  System.out.println( e.nextElement().getClass().getName() );

		/*
		Db db = new Db("catalog.db");
		DeviceTable dt = new DeviceTable();
		dt.createDbTable(db);
		Device d = new Device();
		d.setId(dt.insert(db, d));
		d.setDescription("Eine tolle Beschreibung");
		dt.update(db, d);
		*/
		//dt.delete(db, d);
	}
	
}
