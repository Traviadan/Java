package de.awi.catalog.models;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import de.traviadan.lib.db.DbFieldGetter;
import de.traviadan.lib.db.DbTableModel;

public class DeviceTable extends DbTableModel {
	private static final long serialVersionUID = 1L;
	
	private List<Device> data = new ArrayList<>();
	
	public DeviceTable() {
		super(Device.class);
		System.out.println(tableName);
		Iterator<String> itColumns = columnNames.listIterator();
		Iterator<Class<?>> itTypes = columnTypes.listIterator();
		while(itColumns.hasNext()) {
			System.out.print(String.format("%s (%s), ", itColumns.next(), itTypes.next()));
		}
		System.out.println();
	}
	
	public void populate(List<Device> devices) {
		for(Device d: devices) {
			
		}
	}
	
	private void getDeviceAttributes(Device device) {
		Class<?> c = Device.class;
		for (Method m: c.getMethods()) {
			if (m.isAnnotationPresent(DbFieldGetter.class)) {
				String name = m.getAnnotation(DbFieldGetter.class).name();
				columnNames.add(name);
				/*
				try {
					Object o = m.invoke(device);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				*/
			}
		}
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}
}
