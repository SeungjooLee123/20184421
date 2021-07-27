import java.util.Arrays;


public class dev {

	/* 기능: 비트 배열 arr을 n만큼 왼쪽으로 shift하는 함수
	 * 입력: arr:비트 배열, n:n만큼 shift
	 * 출력: arr:n만큼 left shift된 새로운 비트 배열*/
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
	
	/* 기능: 배열 arr을 문자표 Text에 맞게 정리하는 함수
	 * 입력: Text:순열 작업을 위한 Text, arr:비트배열
	 * 출력: tmp_nbit:arr을 Text로 순열작업을한 결과배열
	 */
	public static int[] setText(int[] Text, int[] arr) {
		int[] tmp_nbit = new int[Text.length];
		
		for(int i=0; i<Text.length; i++) {
			tmp_nbit[i] = arr[Text[i]-1];
		}
		
		return tmp_nbit;
	}
	
	/* S-DES 키(k1, k2)생성
	 * 입력: key_10bit:키의 값
	 * 출력: resultKey:k1+k2
	 */
	public static int[] creatKey(int[] key_10bit) {
		
		int[] P10 = {3, 5, 2, 7, 4, 10, 1, 9, 8, 6};
		int[] P8 = {6, 3, 7, 4, 8, 5, 10, 9};
		
		int[] tmpK1 = new int[8];
		int[] tmpK2 = new int[8];
		
		int[] Lbit = new int[5];
		int[] Rbit = new int[5];
		
		int[] tmp_10bit = new int[10];
		
		tmp_10bit = setText(P10, key_10bit);//P10의 결과
		
		Lbit = Arrays.copyOfRange(tmp_10bit, 0, 5);
		Rbit = Arrays.copyOfRange(tmp_10bit, 5, 10);
		
		Lbit = leftShift(Lbit, 1);
		Rbit = leftShift(Rbit, 1);
		
		System.arraycopy(Lbit, 0, tmp_10bit, 0, Lbit.length);
		System.arraycopy(Rbit, 0, tmp_10bit, Lbit.length, Rbit.length);
		
		tmpK1 = setText(P8, tmp_10bit);//P8의 결과, K1

		Lbit = leftShift(Lbit, 2);
		Rbit = leftShift(Rbit, 2);
		
		System.arraycopy(Lbit, 0, tmp_10bit, 0, Lbit.length);
		System.arraycopy(Rbit, 0, tmp_10bit, Lbit.length, Rbit.length);
		
		tmpK2 = setText(P8, tmp_10bit);//P8의 결과, K2
		
		int[] resultKey = new int[tmpK1.length + tmpK2.length];
		System.arraycopy(tmpK1, 0, resultKey, 0, tmpK1.length);
		System.arraycopy(tmpK2, 0, resultKey, tmpK1.length, tmpK2.length);
		
		return resultKey;
	}
	
	/* 기능: XOR연산을 해주는 함수
	 * 입력: arr1:비트배열1, arr2:비트배열2
	 * 츨력: tmp:arr1과 arr2 비트배열을 XOR 연산한 결과
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
	
	/* 기능: S0, S1 구하는 함수
	 * 입력: sBox:S박스, arr:S박스의 행,열 번호를 알기위한 4비트배열
	 * 출력: tmp:S박스의 결과비트(2bit)
	 */
	public static int[] S_Box(int[][] sBox, int[] arr) {
		int[] tmp = new int[2];
		int r = arr[0]*2 + arr[3];
		int c = arr[1]*2 + arr[2];
		
		tmp[0] = sBox[r][c]/2;
		tmp[1] = sBox[r][c]%2;
		
		return tmp;
	}
	
	
	/* 기능: 함수f_k:순열 및 치환 함수의 조합으로 구성
	 * 입력: key_8bit:8비트배열, K:S_DES키
	 * 출력: resultbit:f_k에 의해 변경된 왼쪽 4비트와 오른쪽 4비트
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
		
		tmp_8bit = setText(EP,Rbit); //EP의 결과
		
		tmp_8bit = XOR(tmp_8bit,K);
		tmpLbit = Arrays.copyOfRange(tmp_8bit, 0, 4);
		tmpRbit = Arrays.copyOfRange(tmp_8bit, 4, 8);
		
		S0 = S_Box(S0_Box,tmpLbit);
		S1 = S_Box(S1_Box,tmpRbit);
		
		System.arraycopy(S0, 0, tmp_4bit, 0, S0.length);
		System.arraycopy(S1, 0, tmp_4bit, S0.length, S1.length);
		
		tmp_4bit = setText(P4,tmp_4bit); //P4의 결과
		tmp_4bit = XOR(tmp_4bit,Lbit);
		
		//2개의 값을 리턴할 수 없기 때문에 하나의 배열로 합쳐서 넘겨줌
		System.arraycopy(tmp_4bit, 0, resultbit, 0, tmp_4bit.length);
		System.arraycopy(Rbit, 0, resultbit, tmp_4bit.length, Rbit.length);
		
		return resultbit;
	}
	
	/* 기능: 왼쪽 4비트와 오른쪽 4비트 교환
	 * 입력: arr:8비트배열
	 * 출력: arr:왼쪽 4비트와 오른쪽 4비트 교환된 8비트배열
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
	
	/* 기능: 암호화/복호화 함수
	 * 입력: key_8bit:평문(8bit), K1:S_DES 키, K2:S_DES 키, IP:우선 순열 작업을 수행하기 위한 IP, IP_1:역순열 작업을 수행하기 위한 IP_1
 	 * 출력: tmpResult:암호화/복호화 결과
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
	
	//S_DES 키 생성과 S_DES 암호/복호 알고리즘 수행
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
		
		System.out.println("평문 = " + Arrays.toString(key_8bit));
		for(int i=0;i<key_8bit.length;i++) {
			if(key_8bit[i]==1) {
				HM_10 += Math.pow(2, key_8bit.length-i-1);
			}
		}
		System.out.println(HM_10);
		
		//암호화 키 생성
		tmpKey = creatKey(key_10bit);
		
		K1 = Arrays.copyOfRange(tmpKey, 0, 8);
		K2 = Arrays.copyOfRange(tmpKey, 8, 16);
		
		//암호화
		tmpResult = ED_coding(key_8bit,K1,K2,IP,IP_1);

		System.out.println("암호화 = " + Arrays.toString(tmpResult));
		
		//복호화 키 생성
		tmpKey = creatKey(key_10bit);
		
		K1 = Arrays.copyOfRange(tmpKey, 0, 8);
		K2 = Arrays.copyOfRange(tmpKey, 8, 16);
		
		//복호화
		tmpResult = ED_coding(tmpResult,K2,K1,IP,IP_1);
		
		System.out.println("복호화 = " + Arrays.toString(tmpResult));
	}

}
