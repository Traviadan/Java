package de.awi.catalog.models;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Vector;
import de.traviadan.lib.db.DataFieldGetter;
import javax.swing.table.DefaultTableModel;

public class DeviceTable extends DefaultTableModel {
	private static final long serialVersionUID = 1L;
	
	private Vector<String> columnNames = new Vector<>();
	private Vector<Vector<Object>> data = new Vector<Vector<Object>>();
	
	public DeviceTable() {
		super();
		getDeviceAttributes(new Device());
	}
	
	public void populate(List<Device> devices) {
		for(Device d: devices) {
			
		}
	}
	
	private void getDeviceAttributes(Device device) {
		Class<?> c = Device.class;
		for (Method m: c.getMethods()) {
			if (m.isAnnotationPresent(DataFieldGetter.class)) {
				System.out.println(m.getName());
				System.out.println(m.getReturnType().getName());
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
			}
		}
	}
}
