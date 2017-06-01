package math;

import java.util.ArrayList;
import java.util.List;

public class MathSelf {
	/**
	 * k 集合元素个数  n 选出子元素个数
	 */
	private static int n=8,k=3;
	public static List addElement(){
		List elements=new ArrayList();
		//添加元素并设置好元素在集合中下标
		for (int i = 0; i <n; i++) {
			elements.add(i+1);
		}
		System.out.print("{");
		for (int i = 0; i <elements.size(); i++) {
			if(i==elements.size()-1)
			System.out.print(elements.get(i));
			else
			System.out.print(elements.get(i)+",");
		}
		System.out.print("}\n");
		return elements;
	}
	
	public static void indexSort(List<Element> elements){
		if(elements.isEmpty())
			return;
		if(n<k)
			return;
		Element[] el=new Element[k];
		for (int i = 0; i <k; i++) {
			Element element=new Element();
			element.index=i;
			element.data=elements.get(i);
			el[i]=element;
		}
		printElement(el);
		if(n>k){
			deilS(elements,el, k-1);
		}
	}
	private static  void deilS(List elementss,Element elements[],int le){
		if(le==-1)
			return;
		if(le==k-1){
			//获取最后一个元素
			Element element=elements[k-1];
			while(element.index<n-1){
				element.index=element.index+1;
				element.data=elementss.get(element.index);
				printElement(elements);
			}
			deilS(elementss,elements,le-1);
		}else{
			Element element=elements[le];
			int currtIndex=n-1-(k-1-le);
			if(element.index<currtIndex){
				element.index=element.index+1;
				element.data=elementss.get(element.index);
				int nextCount=k-1-le;
				for (int i = 0; i <nextCount; i++) {
                    int nextIndex= element.index + i + 1;
                    int nextle = le + i + 1;
                    elements[nextle].index=element.index+1+i;
                    elements[nextle].data=elementss.get(nextIndex);
				}
				printElement(elements);
				if(element.index == currtIndex &&
                        le == 0){
					return;
				}
				deilS(elementss, elements, k-1);
			}else{
				deilS(elementss, elements, le-1);
			}
		}
	}
	private static void printElement(Element element[]){
		for (int i = 0; i < element.length; i++) {
			System.out.print(element[i].data);
		}
		System.out.println();
	}
	
	public static void main(String[] args) {
		List elements=addElement();
		indexSort(elements);
	}
}
/**
 * 元素
 * @author lsf53_000
 *
 */
class Element{
	public Object data;//元素数据
	public int index;//元素下标
}
