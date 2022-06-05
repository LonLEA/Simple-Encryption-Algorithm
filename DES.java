import java.util.Scanner;

public class DES {
    private StringBuffer plaintext;     //明文字符串64bit
    private StringBuffer ciphertext;    //密文64bit
    private StringBuffer key;           //密钥64bit
    //IP置换
    private final int[] REP_IP = {
        58, 50, 42, 34, 26, 18, 10, 2,
        60, 52, 44, 36, 28, 20, 12, 4,
        62, 54, 46, 38, 30, 22, 14, 6,
        64, 56, 48, 40, 32, 24, 16, 8,
        57, 49, 41, 33, 25, 17, 9, 1,
        59, 51, 43, 35, 27, 19, 11, 3,
        61, 53, 45, 37, 29, 21, 13, 5,
        63, 55, 47, 39, 31, 23, 15, 7
    };
    //IP逆置换
    private final int[] INVER_REP_IP = {
        40, 8, 48, 16, 56, 24, 64, 32,
        39, 7, 47, 15, 55, 23, 63, 31,
        38, 6, 46, 14, 54, 22, 62, 30,
        37, 5, 45, 13, 53, 21, 61, 29,
        36, 4, 44, 12, 52, 20, 60, 28,
        35, 3, 43, 11, 51, 19, 59, 27,
        34, 2, 42, 10, 50, 18, 58, 26,
        33, 1, 41, 9, 49, 17, 57, 25};
         // 置换选择1，56位
	private final int[] PC1={
		57,49,41,33,25,17,9,
        1,58,50,42,34,26,18,
        10,2,59,51,43,35,27,
        19,11,3,60,52,44,36,
        63,55,47,39,31,23,15,
        7,62,54,46,38,30,22,
        14,6,61,53,45,37,29,
        21,13,5,28,20,12,4};
    // 置换选择2即压缩置换 48位
    private final int[] PC2 = {
    	14, 17, 11, 24, 1, 5,
    	3, 28, 15, 6, 21,10, 
    	23, 19, 12, 4, 26, 8, 
    	16, 7, 27, 20, 13, 2, 
    	41, 52, 31, 37, 47,55, 
    	30, 40, 51, 45, 33, 48, 
    	44, 49, 39, 56, 34, 53, 
    	46, 42, 50, 36,29, 32};
        //E扩展
    private final int[] E={
    	32,1,2,3,4,5,
        4,5,6,7,8,9,
        8,9,10,11,12,13,
        12,13,14,15,16,17,
        16,17,18,19,20,21,
        20,21,22,23,24,25,
        24,25,26,27,28,29,
        28,29,30,31,32,1};
        //P置换
    private final byte[] P={
    	16,7,20,21,29,12,28,17,
        1,15,23,26,5,18,31,10,
        2,8,24,14,32,27,3,9,
        19,13,30,6,22,11,4,25};
    //移位
    private int[] LFT={1,1,2,2,2,2,2,2,1,2,2,2,2,2,2,1};
    //S盒
    private static final byte[][][] S_Box = {
        {
            { 14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7 },
            { 0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8 },
            { 4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0 },
            { 15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13 } 
        },
        { 
            { 15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10 },
            { 3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5 },
            { 0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15 },
            { 13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9 } 
        },
        { 
            { 10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8 },
            { 13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1 },
            { 13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7 },
            { 1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12 } 
        },
        { 
            { 7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15 },
            { 13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9 },
            { 10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4 },
            { 3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14 }
        },
        { 
            { 2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9 },
            { 14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6 },
            { 4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14 },
            { 11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3 } 
        },
        { 
            { 12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11 },
            { 10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8 },
            { 9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6 },
            { 4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13 } 
        },
        { 
            { 4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1 },
            { 13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6 },
            { 1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2 },
            { 6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12 } 
        },
        { 
            { 13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7 },
            { 1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2 },
            { 7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8 },
            { 2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11 } 
        }};
    public StringBuffer getPlaintext(){
        return plaintext;
    }

    public void setPlaintext(StringBuffer plaintext){
        this.plaintext = plaintext;
    }

    public StringBuffer getCiphertext(){
        return ciphertext;
    }

    public void setCiphertext(StringBuffer ciphertext){
        this.ciphertext = ciphertext;
    } 

    public StringBuffer getKey(){
        return key;
    }

    public void setKey(StringBuffer key){
        this.key = key;
    }

    //StringBuffer转二进制
    public StringBuffer stringBufferToBinary(StringBuffer s){
        StringBuffer res = new StringBuffer();
        StringBuffer stmp = new StringBuffer();
        for(int i = 0;i<s.length();i++){
            stmp = new StringBuffer(Integer.toBinaryString(s.charAt(i)));
            while(stmp.length() < 8){
                stmp.insert(0, 0);
            }
            res.append(stmp);
        } 
        while(res.length()<64){
            res.insert(0, 0);
        }
        return res;
    }

    //二进制转换成字符64->8
    //int column = Integer.parsInt(Sinput.substring(1,5),1)
    public StringBuffer BinaryToStringBuffer(StringBuffer s){
        StringBuffer res = new StringBuffer();
        for(int i = 0;i < 8;i++){
            int t = Integer.parseInt(s.substring(i*8,(i+1)*8),2);
            res.append((char)t);
        }
        return res;
    }

    //初始置换IP
    public StringBuffer IP(StringBuffer s){
        StringBuffer res = new StringBuffer();
        for(int i = 0; i< REP_IP.length;i++){
            res.append(s.charAt(REP_IP[i]-1));
        }
        return res;
    }

    //终止置换IP
    public StringBuffer Final(StringBuffer s){
        StringBuffer res = new StringBuffer();
        for(int i = 0; i< INVER_REP_IP.length;i++){
            res.append(s.charAt(INVER_REP_IP[i]-1));
        }
        return res;
    }

    //P置换
    public StringBuffer P(StringBuffer s){
        StringBuffer res = new StringBuffer();
        for(int i = 0;i < P.length;i++){
            res.append(s.charAt(P[i]-1));
        }
        return res;
    }

    //E扩展置换
    public StringBuffer Extent(StringBuffer s){
        StringBuffer res = new StringBuffer();
        for(int i = 0;i < E.length;i++){
            res.append(s.charAt(E[i]-1));
        }
        return res;
    }

    //子密钥生成
    public StringBuffer[] getSubkey(){
        StringBuffer keyBinary = new StringBuffer(stringBufferToBinary(key));
        StringBuffer subkey[] = new StringBuffer[16];
        StringBuffer C0 = new StringBuffer();
        StringBuffer D0 = new StringBuffer();
        //PC1置换
        for(int i = 0;i < PC1.length/2;i++){
            C0.append(keyBinary.charAt(PC1[i]-1));
            D0.append(keyBinary.charAt(PC1[i+28]-1));
        }
        for(int i = 0;i < 16;i++){
            char temp;
            temp = C0.charAt(0);
            C0.deleteCharAt(0);
            C0.append(temp);
            temp = D0.charAt(0);
            D0.deleteCharAt(0);
            D0.append(temp);
            if(LFT[i] == 2){
                temp = C0.charAt(0);
                C0.deleteCharAt(0);
                C0.append(temp);
                temp = D0.charAt(0);
                D0.deleteCharAt(0);
                D0.append(temp);
            }
            //C0D0左右合并
            StringBuffer C0D0 = new StringBuffer(C0.toString()+D0.toString());
            //PC2置换
            StringBuffer C0D0temp = new StringBuffer();
            for(int j = 0; j < PC2.length;j++){
                C0D0temp.append(C0D0.charAt(PC2[j]-1));
            }
            subkey[i] = C0D0temp;
        }
        return subkey;
    }

    //F函数
    public StringBuffer F(StringBuffer R,StringBuffer subkey){
        StringBuffer res = new StringBuffer();
        //E盒拓展
        res = Extent(R);
        //R和subkey异或运算
        for(int i = 0 ; i< 48;i++){
            res.replace(i, i + 1, (res.charAt(i) == subkey.charAt(i) ? "0" : "1"));
        }
        //S盒压缩
        StringBuffer sBox = new StringBuffer();
        for(int i = 0;i < 8;i++){
            String Sinput = res.substring(i*6,(i + 1)*6);
            int r = Integer.parseInt(Character.toString(Sinput.charAt(0))+Sinput.charAt(5), 2);
            int c = Integer.parseInt(Sinput.substring(1,5),2);
            StringBuffer Soutput = new StringBuffer(Integer.toBinaryString(S_Box[i][r][c]));
            while(Soutput.length() < 8){
                Soutput.insert(0, 0);
            }
            sBox.append(Soutput);
        }
        sBox = P(sBox);
        return sBox;
    }

    //16轮迭代
    public StringBuffer iteration(StringBuffer L,StringBuffer R,int mode){
        StringBuffer res = new StringBuffer();
        StringBuffer[] subkey = getSubkey();
        if(mode == 1){
            StringBuffer[] temp = getSubkey();
            for(int i = 0;i < 16;i++){
                subkey[i] = temp[15-i];
            }
        }
        for(int i = 0;i < 16;i++){
            StringBuffer Ltemp = new StringBuffer(L);
            StringBuffer Rtemp = new StringBuffer(R);
            L.replace(0, 32, R.toString());

            StringBuffer Fres = F(Rtemp,subkey[i]);
            for(int j = 0;j < 32;j++){
                R.replace(j, j+1, (Fres.charAt(j) == Ltemp.charAt(j)?"0":"1"));
            }
        }
        StringBuffer RL = new StringBuffer(R.toString()+L.toString());

        RL = Final(RL);
        return RL;
    }

    public void DesEncrypt(int mode){
        StringBuffer plain;
        StringBuffer cipher;
        if(mode == 0){
            plain = getPlaintext();
            System.out.println("明文为:" + plain);
            plain = stringBufferToBinary(plain);
            System.out.println("二进制明文："+plain);
            plain = IP(plain);//初始置换
            StringBuffer L = new StringBuffer(plain.substring(0,32)); 
            StringBuffer R = new StringBuffer(plain.substring(32,64));
            plain = iteration(L, R, mode);
            System.out.println("二进制密文：" + plain);
            setCiphertext(BinaryToStringBuffer(plain));
            System.out.println("密文"+BinaryToStringBuffer(plain));
        }else{
            cipher = getCiphertext();
            System.out.println("密文为："+cipher);
            cipher = stringBufferToBinary(cipher);
            System.out.println("二进制密文为："+cipher);
            cipher = IP(cipher);//初始置换
            StringBuffer L = new StringBuffer(cipher.substring(0,32)); 
            StringBuffer R = new StringBuffer(cipher.substring(32,64));
            cipher = iteration(L, R, mode);
            System.out.println("二进制明文:" + cipher);
            setPlaintext(BinaryToStringBuffer(cipher));
            System.out.println("明文："+BinaryToStringBuffer(cipher));
        }
    }
    public static void main(String[] args) {
        DES1 des1 = new DES1();

        Scanner sc = new Scanner(System.in);
        System.out.print("输入明文：");
        String temp = sc.next();
        des1.setPlaintext(new StringBuffer(temp));
        System.out.print("输入密钥：");
        temp = sc.next();
        des1.setKey(new StringBuffer(temp));
        des1.DesEncrypt(0);
        des1.DesEncrypt(1); 
    }
}
        //StringBuffer res = des1.stringBufferToBinary(new StringBuffer("123456"));
        //System.out.println(des1.BinaryToStringBuffer(res));
       /*  des1.setPlaintext(new StringBuffer("12345678"));
        des1.setKey(new StringBuffer("11111111"));
        StringBuffer plain = des1.stringBufferToBinary(des1.getPlaintext());
        System.out.println("二进制明文"+plain);
        plain = des1.IP(plain);//初始置换
        StringBuffer L = new StringBuffer(plain.substring(0,32)); 
        StringBuffer R = new StringBuffer(plain.substring(32,64));
        plain = des1.iteration(L, R, 0);
        System.out.println("密文"+des1.BinaryToStringBuffer(plain));
        System.out.println(plain);

        plain = des1.IP(plain);//初始置换
        L = new StringBuffer(plain.substring(0,32)); 
        R = new StringBuffer(plain.substring(32,64));
        plain = des1.iteration(L, R, 1);
        System.out.println("明文"+des1.BinaryToStringBuffer(plain));
 */