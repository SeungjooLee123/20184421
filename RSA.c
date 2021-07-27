#include<stdio.h>

/* 기능: 확장된 유클리드 알고리즘, 모듈러 역을 구하는 함수
* 입력: r1:e의 값 ,r2:pi_n의 값 (r1과 r2의 최대공약수를 구하는 과정)
* 출력: s1:d의 값 (1 = e*s1 + pi_n*t1)
*/
int extended_euclid(int r1, int r2) {
	int q, r; //q: 몫, r:나머지
	int s1 = 1, s2 = 0, s, t1 = 0, t2 = 1, t;

	do {
		q = r1 / r2;
		r = r1 % r2;
		s = s1 - q * s2;
		t = t1 - q * t2;
		
		//printf("%d\t%d\t%d\t%d\t%d\t%d\t%d\t%d\t%d\t%d\t\n", q, r1, r2, r, s1, s2, s, t1, t2, t);

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
int effective_mul(a, ed, n) {
	int tmp, b[20], i = 0, c = 0, f = 1; //b[20]:2진수로 변환한 ed를 저장하는 배열 

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

/* 기능: RSA알고리즘 수행*/
int main(void) {
	int data = 65;
	int p = 29, q = 31, e = 419;
	int n, pi_n, d;
	int C, M;

	//키생성
	n = p * q;
	pi_n = (p - 1) * (q - 1);

	d = extended_euclid(e, pi_n);
	//공개키 (KU = {e, n}), 개인키(KR = {d, n})

	//암호화
	C = effective_mul(data, e, n);
	//복호화
	M = effective_mul(C, d, n);

	printf("데이터 = %d\t암호문 = %d\t복호문 = %d\n", data, C, M);

	return 0;
}