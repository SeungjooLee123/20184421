#include<stdio.h>

/* ���: Ȯ��� ��Ŭ���� �˰���, ��ⷯ ���� ���ϴ� �Լ�
* �Է�: r1:e�� �� ,r2:pi_n�� �� (r1�� r2�� �ִ������� ���ϴ� ����)
* ���: s1:d�� �� (1 = e*s1 + pi_n*t1)
*/
int extended_euclid(int r1, int r2) {
	int q, r; //q: ��, r:������
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

/* ���: ȿ������ ������ �˰���
* �Է�: a:��, ed:����, n:mod n
* ���: a�� ed��(mod n) �� �����
*/
int effective_mul(a, ed, n) {
	int tmp, b[20], i = 0, c = 0, f = 1; //b[20]:2������ ��ȯ�� ed�� �����ϴ� �迭 

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

/* ���: RSA�˰��� ����*/
int main(void) {
	int data = 65;
	int p = 29, q = 31, e = 419;
	int n, pi_n, d;
	int C, M;

	//Ű����
	n = p * q;
	pi_n = (p - 1) * (q - 1);

	d = extended_euclid(e, pi_n);
	//����Ű (KU = {e, n}), ����Ű(KR = {d, n})

	//��ȣȭ
	C = effective_mul(data, e, n);
	//��ȣȭ
	M = effective_mul(C, d, n);

	printf("������ = %d\t��ȣ�� = %d\t��ȣ�� = %d\n", data, C, M);

	return 0;
}