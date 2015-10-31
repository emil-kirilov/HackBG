import java.util.ArrayList;

 class HierarchyUnit {
	String key;
	ArrayList<String> values = new ArrayList<String>();
	 
	void setKey(String name) {
		key = name;
	}
	
	String getKey() {
		return key;
	}
	
	void addValue(String name) {
		values.add(name);
	}
	
	String getValueAt(int position) {
		return values.get(position);
	}
	
	
	void getValues() {
		for(int i = 0; i < values.size(); i++)
		{
			System.out.println(values.get(i));
		}
	}
	
	HierarchyUnit() {}
	
	 HierarchyUnit(String name) {
		key = name;
	}
}
