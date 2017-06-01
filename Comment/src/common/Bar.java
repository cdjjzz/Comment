package common;

import java.util.ArrayList;
import java.util.List;

public class Bar {
	static class OOMObject{
		public byte[] placeholder=new byte[64*1024];
	}
	public static void fillHeap(int num) throws InterruptedException{
		List<OOMObject> list=new ArrayList<OOMObject>();
		for(int i=0;i<num;i++){
			Thread.sleep(50);
			list.add(new OOMObject());
		}
	}
	public static void main(String[] args) {
		try {
			fillHeap(1000);
			System.gc();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
