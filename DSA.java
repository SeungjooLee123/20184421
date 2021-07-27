package dsa;

import java.util.Arrays;

public class DSA {

	/* ���: 2������ ��ȯ���ִ� �Լ�
	* �Է�: ed:10����, n:������ �迭�� ũ��
	* ���: 2������ ������ �迭
	*/
	public static int[] toBinary(int ed, int n) {
		int tmp;
		int[] b = new int[n]; //b[20]:2������ ��ȯ�� ed�� �����ϴ� �迭
		
		for(int i=0;i<n;i++) {
			tmp = ed/2;
			b[n-1-i] = ed % 2;

			ed = tmp;
		}

		return b;
	}
	
	/* ���: Ȯ��� ��Ŭ���� �˰���, ��ⷯ ���� ���ϴ� �Լ�
	* �Է�: r1:e�� �� ,r2:pi_n�� �� (r1�� r2�� �ִ������� ���ϴ� ����)
	* ���: s1:d�� �� (1 = e*s1 + pi_n*t1)
	*/
	public static int extended_euclid(int r1, int r2) {
		int q, r; //q: ��, r:������
		int s1 = 1, s2 = 0, s, t1 = 0, t2 = 1, t;

		do {
			q = r1 / r2;
			r = r1 % r2;
			s = s1 - q * s2;
			t = t1 - q * t2;
			
			r1 = r2;
			r2 = r;
			s1 = s2;
			s2 = s;
			t1 = t2;
			t2 = t;
		} while (r2 != 0);

		return s1;
	}
	
	/* ���: ȿ������ ������ �˰���
	* �Է�: a:��, ed:����, n:mod n
	* ���: a�� ed��(mod n) �� �����
	*/
	public static int effective_mul(int a,int ed,int n) {
		int tmp, i = 0, c = 0, f = 1; 
		int[] b = new int[20]; //b[20]:2������ ��ȯ�� ed�� �����ϴ� �迭 
				
		//ed�� 2������ ��ȯ�Ͽ� �迭 b�� ����
		do {
			tmp = ed / 2;
			b[i] = ed % 2;

			ed = tmp;
			i++;
		} while (tmp != 0);

		//ȿ������ ���� �˰��� ����
		for (int j = i - 1; j >= 0; j--) {
			c = 2 * c;
			f = (f * f) % n;

			if (b[j] == 1) {
				c = c + 1;
				f = (f * a) % n;
			}
		}

		return f;
	}
	
	/* ���: ����
	* �Է�: r s q HM g p y
	* ���: ���� ���� ����
	*/
	public static void verification(int r, int s, int q, int HM, int g, int p, int y) {
		int w,u1,u2,v;
		
		w=extended_euclid(s,q);
		if(w<0) {
			w = w+q;
		}
		
		u1=(HM*w)%q;
		u2=(r*w)%q;
		v=(effective_mul(g,u1,p)*effective_mul(y,u2,p))%q;
		
		System.out.println("w= "+w+"\tu1= "+u1+"\tu2= "+u2+"\tv= "+v);
		
		if(v==r) {
			System.out.println("���� ����");
		}
		else {
			System.out.println("���� ����");
		}
		
	}
	
	//DSA ����
	public static void main(String [] args) {
		int[] key_10bit = new int[10];
		int[] key_8bit = new int[8];
		int r,s,y,HM_10=0;
		int g=131, x=89, M=85, k=127, p=40193, q=157, key=815;
		
		//Ű���� M�� �������� ��ȯ
		key_10bit = toBinary(key,10);
		key_8bit = toBinary(M,8);
		
		//SDES �޼ҵ� ȣ���Ͽ� ��ȣ�� ����
		S_DES sdes=new S_DES();
		int[] HM = new int[8];
		HM = sdes.getSDES(key_10bit,key_8bit);
		
		for(int i=0;i<HM.length;i++) {
			if(HM[i]==1) {
				HM_10 += Math.pow(2, HM.length-i-1);
			}
		}
		
		//���� ����
		r=effective_mul(g,k,p)%q;
		s=(extended_euclid(k,q)*(HM_10+x*r))%q;
		// Signature = (r,s)
				
		//����Ű
		y=effective_mul(g,x,p);
		
		System.out.println("H(M)="+HM_10+"r= "+r+"\ts= "+s+"\ty= "+y);
		
		//����
		verification(r, s, q, HM_10, g, p, y);
	}
}
