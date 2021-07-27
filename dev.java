import java.util.Arrays;


public class dev {

	/* ���: ��Ʈ �迭 arr�� n��ŭ �������� shift�ϴ� �Լ�
	 * �Է�: arr:��Ʈ �迭, n:n��ŭ shift
	 * ���: arr:n��ŭ left shift�� ���ο� ��Ʈ �迭*/
	public static int[] leftShift(int[] arr, int n) { 
		int[] tmp = new int[n];
		
		for (int i=0; i<n; i++) {
			tmp[i] = arr[i];
		}
		
		for(int i=n; i<arr.length; i++) {
			arr[i-n] = arr[i];
		}
		
		for (int i=0; i<n; i++) {
			arr[arr.length-n+i] = tmp[i];
		}
		
		return arr;
	}
	
	/* ���: �迭 arr�� ����ǥ Text�� �°� �����ϴ� �Լ�
	 * �Է�: Text:���� �۾��� ���� Text, arr:��Ʈ�迭
	 * ���: tmp_nbit:arr�� Text�� �����۾����� ����迭
	 */
	public static int[] setText(int[] Text, int[] arr) {
		int[] tmp_nbit = new int[Text.length];
		
		for(int i=0; i<Text.length; i++) {
			tmp_nbit[i] = arr[Text[i]-1];
		}
		
		return tmp_nbit;
	}
	
	/* S-DES Ű(k1, k2)����
	 * �Է�: key_10bit:Ű�� ��
	 * ���: resultKey:k1+k2
	 */
	public static int[] creatKey(int[] key_10bit) {
		
		int[] P10 = {3, 5, 2, 7, 4, 10, 1, 9, 8, 6};
		int[] P8 = {6, 3, 7, 4, 8, 5, 10, 9};
		
		int[] tmpK1 = new int[8];
		int[] tmpK2 = new int[8];
		
		int[] Lbit = new int[5];
		int[] Rbit = new int[5];
		
		int[] tmp_10bit = new int[10];
		
		tmp_10bit = setText(P10, key_10bit);//P10�� ���
		
		Lbit = Arrays.copyOfRange(tmp_10bit, 0, 5);
		Rbit = Arrays.copyOfRange(tmp_10bit, 5, 10);
		
		Lbit = leftShift(Lbit, 1);
		Rbit = leftShift(Rbit, 1);
		
		System.arraycopy(Lbit, 0, tmp_10bit, 0, Lbit.length);
		System.arraycopy(Rbit, 0, tmp_10bit, Lbit.length, Rbit.length);
		
		tmpK1 = setText(P8, tmp_10bit);//P8�� ���, K1

		Lbit = leftShift(Lbit, 2);
		Rbit = leftShift(Rbit, 2);
		
		System.arraycopy(Lbit, 0, tmp_10bit, 0, Lbit.length);
		System.arraycopy(Rbit, 0, tmp_10bit, Lbit.length, Rbit.length);
		
		tmpK2 = setText(P8, tmp_10bit);//P8�� ���, K2
		
		int[] resultKey = new int[tmpK1.length + tmpK2.length];
		System.arraycopy(tmpK1, 0, resultKey, 0, tmpK1.length);
		System.arraycopy(tmpK2, 0, resultKey, tmpK1.length, tmpK2.length);
		
		return resultKey;
	}
	
	/* ���: XOR������ ���ִ� �Լ�
	 * �Է�: arr1:��Ʈ�迭1, arr2:��Ʈ�迭2
	 * ����: tmp:arr1�� arr2 ��Ʈ�迭�� XOR ������ ���
	 */
	public static int[] XOR(int[] arr1, int[] arr2) {
		int[] tmp = new int[arr1.length];
		
		for(int i=0; i<tmp.length; i++) {
			if(arr1[i] == arr2[i])
				tmp[i] = 0;
			else
				tmp[i] = 1;
		}
		
		return tmp;
	}
	
	/* ���: S0, S1 ���ϴ� �Լ�
	 * �Է�: sBox:S�ڽ�, arr:S�ڽ��� ��,�� ��ȣ�� �˱����� 4��Ʈ�迭
	 * ���: tmp:S�ڽ��� �����Ʈ(2bit)
	 */
	public static int[] S_Box(int[][] sBox, int[] arr) {
		int[] tmp = new int[2];
		int r = arr[0]*2 + arr[3];
		int c = arr[1]*2 + arr[2];
		
		tmp[0] = sBox[r][c]/2;
		tmp[1] = sBox[r][c]%2;
		
		return tmp;
	}
	
	
	/* ���: �Լ�f_k:���� �� ġȯ �Լ��� �������� ����
	 * �Է�: key_8bit:8��Ʈ�迭, K:S_DESŰ
	 * ���: resultbit:f_k�� ���� ����� ���� 4��Ʈ�� ������ 4��Ʈ
	 */
	public static int[] f_k(int[] key_8bit, int[] K) {
		
		int[] EP = {4, 1, 2, 3, 2, 3, 4, 1};
		int[][] S0_Box = {{1,0,3,2},{3,2,1,0},{0,2,1,3},{3,1,3,2}};
		int[][] S1_Box = {{0,1,2,3},{2,0,1,3},{3,0,1,0},{2,1,0,3}};
		int[] P4 = {2, 4, 3, 1};
		
		int[] tmp_8bit = new int[8];
		int[] tmp_4bit = new int[4];
		
		int[] Lbit = new int[4];
		int[] Rbit = new int[4];
		
		int[] tmpLbit = new int[4];
		int[] tmpRbit = new int[4];
		
		int[] S0 = new int[2];
		int[] S1 = new int[2];
		
		int[] resultbit = new int[8];
		
		Lbit = Arrays.copyOfRange(key_8bit, 0, 4);
		Rbit = Arrays.copyOfRange(key_8bit, 4, 8);
		
		tmp_8bit = setText(EP,Rbit); //EP�� ���
		
		tmp_8bit = XOR(tmp_8bit,K);
		tmpLbit = Arrays.copyOfRange(tmp_8bit, 0, 4);
		tmpRbit = Arrays.copyOfRange(tmp_8bit, 4, 8);
		
		S0 = S_Box(S0_Box,tmpLbit);
		S1 = S_Box(S1_Box,tmpRbit);
		
		System.arraycopy(S0, 0, tmp_4bit, 0, S0.length);
		System.arraycopy(S1, 0, tmp_4bit, S0.length, S1.length);
		
		tmp_4bit = setText(P4,tmp_4bit); //P4�� ���
		tmp_4bit = XOR(tmp_4bit,Lbit);
		
		//2���� ���� ������ �� ���� ������ �ϳ��� �迭�� ���ļ� �Ѱ���
		System.arraycopy(tmp_4bit, 0, resultbit, 0, tmp_4bit.length);
		System.arraycopy(Rbit, 0, resultbit, tmp_4bit.length, Rbit.length);
		
		return resultbit;
	}
	
	/* ���: ���� 4��Ʈ�� ������ 4��Ʈ ��ȯ
	 * �Է�: arr:8��Ʈ�迭
	 * ���: arr:���� 4��Ʈ�� ������ 4��Ʈ ��ȯ�� 8��Ʈ�迭
	 */
	public static int[] SW(int[] arr) {
		int[] L = new int[4];
		int[] R = new int[4];
		
		L = Arrays.copyOfRange(arr, 0, 4);
		R = Arrays.copyOfRange(arr, 4, 8);
		
		System.arraycopy(R, 0, arr, 0, R.length);
		System.arraycopy(L, 0, arr, R.length, L.length);
		
		return arr;
	}
	
	/* ���: ��ȣȭ/��ȣȭ �Լ�
	 * �Է�: key_8bit:��(8bit), K1:S_DES Ű, K2:S_DES Ű, IP:�켱 ���� �۾��� �����ϱ� ���� IP, IP_1:������ �۾��� �����ϱ� ���� IP_1
 	 * ���: tmpResult:��ȣȭ/��ȣȭ ���
	 */
	public static int[] ED_coding(int[] key_8bit,int[] K1,int[] K2, int[] IP, int[] IP_1) {
		int[] tmpResult = new int[8];
		
		key_8bit = setText(IP,key_8bit);
		
		tmpResult = f_k(key_8bit,K1);
		tmpResult = SW(tmpResult);
		
		tmpResult = f_k(tmpResult,K2);
		tmpResult = setText(IP_1,tmpResult);
		
		return tmpResult;
	}
	
	//S_DES Ű ������ S_DES ��ȣ/��ȣ �˰��� ����
	public static void main(String[] args) {
		int[] key_10bit = {1,1,0,0,1,0,1,1,1,1};
		int[] key_8bit = {0,1,1,1,1,1,1,1};
		
		int[] IP = {2, 6, 3, 1, 4, 8, 5, 7};
		int[] IP_1 = {4, 1, 3, 5, 7, 2, 8, 6};
		
		int[] K1 = new int[8];
		int[] K2 = new int[8];
		int[] tmpKey = new int[K1.length + K2.length];
		
		int[] tmpResult = new int[8];
		int HM_10=0;
		
		System.out.println("�� = " + Arrays.toString(key_8bit));
		for(int i=0;i<key_8bit.length;i++) {
			if(key_8bit[i]==1) {
				HM_10 += Math.pow(2, key_8bit.length-i-1);
			}
		}
		System.out.println(HM_10);
		
		//��ȣȭ Ű ����
		tmpKey = creatKey(key_10bit);
		
		K1 = Arrays.copyOfRange(tmpKey, 0, 8);
		K2 = Arrays.copyOfRange(tmpKey, 8, 16);
		
		//��ȣȭ
		tmpResult = ED_coding(key_8bit,K1,K2,IP,IP_1);

		System.out.println("��ȣȭ = " + Arrays.toString(tmpResult));
		
		//��ȣȭ Ű ����
		tmpKey = creatKey(key_10bit);
		
		K1 = Arrays.copyOfRange(tmpKey, 0, 8);
		K2 = Arrays.copyOfRange(tmpKey, 8, 16);
		
		//��ȣȭ
		tmpResult = ED_coding(tmpResult,K2,K1,IP,IP_1);
		
		System.out.println("��ȣȭ = " + Arrays.toString(tmpResult));
	}

}
