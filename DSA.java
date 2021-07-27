package dsa;

import java.util.Arrays;

public class DSA {

	/* 기능: 2진수로 변환해주는 함수
	* 입력: ed:10진수, n:저장할 배열의 크기
	* 출력: 2진수를 저장한 배열
	*/
	public static int[] toBinary(int ed, int n) {
		int tmp;
		int[] b = new int[n]; //b[20]:2진수로 변환한 ed를 저장하는 배열
		
		for(int i=0;i<n;i++) {
			tmp = ed/2;
			b[n-1-i] = ed % 2;

			ed = tmp;
		}

		return b;
	}
	
	/* 기능: 확장된 유클리드 알고리즘, 모듈러 역을 구하는 함수
	* 입력: r1:e의 값 ,r2:pi_n의 값 (r1과 r2의 최대공약수를 구하는 과정)
	* 출력: s1:d의 값 (1 = e*s1 + pi_n*t1)
	*/
	public static int extended_euclid(int r1, int r2) {
		int q, r; //q: 몫, r:나머지
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
	
	/* 기능: 효율적인 지수승 알고리즘
	* 입력: a:밑, ed:지수, n:mod n
	* 출력: a의 ed승(mod n) 의 결과값
	*/
	public static int effective_mul(int a,int ed,int n) {
		int tmp, i = 0, c = 0, f = 1; 
		int[] b = new int[20]; //b[20]:2진수로 변환한 ed를 저장하는 배열 
				
		//ed를 2진수로 변환하여 배열 b에 저장
		do {
			tmp = ed / 2;
			b[i] = ed % 2;

			ed = tmp;
			i++;
		} while (tmp != 0);

		//효율적인 연산 알고리즘 수행
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
	
	/* 기능: 검증
	* 입력: r s q HM g p y
	* 출력: 인증 실패 여부
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
			System.out.println("인증 성공");
		}
		else {
			System.out.println("인증 실패");
		}
		
	}
	
	//DSA 수행
	public static void main(String [] args) {
		int[] key_10bit = new int[10];
		int[] key_8bit = new int[8];
		int r,s,y,HM_10=0;
		int g=131, x=89, M=85, k=127, p=40193, q=157, key=815;
		
		//키값과 M을 이진수로 변환
		key_10bit = toBinary(key,10);
		key_8bit = toBinary(M,8);
		
		//SDES 메소드 호출하여 암호값 구함
		S_DES sdes=new S_DES();
		int[] HM = new int[8];
		HM = sdes.getSDES(key_10bit,key_8bit);
		
		for(int i=0;i<HM.length;i++) {
			if(HM[i]==1) {
				HM_10 += Math.pow(2, HM.length-i-1);
			}
		}
		
		//서명 생성
		r=effective_mul(g,k,p)%q;
		s=(extended_euclid(k,q)*(HM_10+x*r))%q;
		// Signature = (r,s)
				
		//공개키
		y=effective_mul(g,x,p);
		
		System.out.println("H(M)="+HM_10+"r= "+r+"\ts= "+s+"\ty= "+y);
		
		//검증
		verification(r, s, q, HM_10, g, p, y);
	}
}
