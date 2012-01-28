package zResearch;

public class CopyingObjects {
	int var;
	
	public CopyingObjects(int test) {
		
	}
	
	public static void main(String[] args) throws CloneNotSupportedException {
		CopyingObjects ob1 = new CopyingObjects(5);
		CopyingObjects ob2 = (CopyingObjects)ob1.clone();
		
		System.out.println(ob2.var);
	}

}
