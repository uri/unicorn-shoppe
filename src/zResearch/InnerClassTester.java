package zResearch;

import zResearch.OutterClass.InnerClass;

public class InnerClassTester {
	public static void main(String[] args) {
		
		OutterClass out = new OutterClass();
		InnerClass inner = out.new InnerClass();
	}
}
