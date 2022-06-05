import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* 
 * Author: LonLEA
 * Date:2022-6-4
 */
public class Playfair{
    private String key = "cafebaby";

    //生成5*5的密码表
    public char[][] getCipherTable(){
        //5*5的密码表
        char[][] cipherTable = new char[5][5];
        this.key = this.key.replace(" ","").toUpperCase();//去除空格转换为大写字母
        char[] keyArray = this.key.toCharArray(); 
        //所有字母，用于判断字母是否已经存在于密码表（i和j当作一个字母）
        List<Character> letterList = new ArrayList<>(
            Arrays.asList('A','B','C','D','E','F','G','H','I','K','L',
                                'M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'));
        int k = 0;
        for(int i = 0;i < 5;i++){
            for(int j = 0;j < 5;j++){
                if(k < keyArray.length){
                    //如果字母List中存在，则密码表中不存在，添加至密码表，并从字母List中删除
                    if(letterList.contains(keyArray[k])){
                        cipherTable[j][i] = keyArray[k];
                        letterList.remove((Character)keyArray[k]);
                    }else{
                        j--;
                    }
                    k++;
                }else{
                    //将剩余字母按顺序加入密码表中
                    if(!letterList.isEmpty()){
                        cipherTable[j][i] = (char)letterList.get(0);
                        letterList.remove(0);
                    }
                }   
            }
        }
        return cipherTable;
    }

    //整理明文
    public List<Character> settlePlain(String text){
        //去除空格并转为大写
        text = text.replace(" ", "").toUpperCase();
        char[] charArray = text.toCharArray();
        List<Character> charList = new ArrayList<>();
        for(int i = 0; i < charArray.length;){
            if(i == charArray.length - 1){
                charList.add(charArray[i]);
                charList.add('X');
                i++;
                return charList;
            }else if(charArray[i] == charArray[i+1]){
                charList.add(charArray[i]);
                charList.add('X');
                i++;
            }else{
                charList.add(charArray[i]);
                charList.add(charArray[i+1]);
                i = i + 2;
            }
        }
        return charList;
    }

    public String Encrypt(List<Character> charList,char[][] cipherTable,int mode){
        //mode == 0 加密
        if(mode == 0){
            String cipherText = "";
            int r1 = 0,r2 = 0,c1 = 0,c2 = 0;    //存放行列
            for(int i = 0;i < charList.size()-1;i = i + 2){
                for(int j = 0;j < 5;j++){
                    for(int k = 0;k < 5;k++){
                        if(charList.get(i) == (Character)cipherTable[j][k]){
                            r1 = j;
                            c1 = k;
                        }
                        if(charList.get(i+1) == (Character)cipherTable[j][k]){
                            r2 = j;
                            c2 = k;
                        }
                    }
                }
                if(r1 == r2){
                    cipherText += String.valueOf(cipherTable[r1][(c1+1)%5]) + String.valueOf(cipherTable[r2][(c2+1)%5]);
                }else if(c1 == c2){
                    cipherText += String.valueOf(cipherTable[(r1+1)%5][c1]) + String.valueOf(cipherTable[(r2+1)%5][c2]);
                }else{
                    cipherText += String.valueOf(cipherTable[r1][c2]) + String.valueOf(cipherTable[r2][c1]);
                }
            }
            return cipherText;
        }else{
            String plainText = "";
            int r1 = 0,r2 = 0,c1 = 0,c2 = 0;    //存放行列
            for(int i = 0;i < charList.size()-1;i = i + 2){
                for(int j = 0;j < 5;j++){
                    for(int k = 0;k < 5;k++){
                        if(charList.get(i) == (Character)cipherTable[j][k]){
                            r1 = j;
                            c1 = k;
                        }
                        if(charList.get(i+1) == (Character)cipherTable[j][k]){
                            r2 = j;
                            c2 = k;
                        }
                    }
                }
                if(r1 == r2){
                    plainText += String.valueOf(cipherTable[r1][(c1+4)%5]) + String.valueOf(cipherTable[r2][(c2+4)%5]);
                }else if(c1 == c2){
                    plainText += String.valueOf(cipherTable[(r1+4)%5][c1]) + String.valueOf(cipherTable[(r2+4)%5][c2]);
                }else{
                    plainText += String.valueOf(cipherTable[r1][c2]) + String.valueOf(cipherTable[r2][c1]);
                }
            }
            return plainText;
        }
    }

    public static void main(String[] args) {
        Playfair playfair = new Playfair();
        System.out.println("密文："+playfair.key);
        char[][] cipherTable = playfair.getCipherTable(); //获取密码表
        System.out.println("密码表如下");
        for(int i = 0;i < 5;i++){
            System.out.print("[");
            for(int j = 0;j < 5;j++){
                System.out.print(cipherTable[i][j]+" ");
            }
            System.out.println("]");
        }
        List<Character> plainList = playfair.settlePlain("whats the matter");
        System.out.println("明文："+plainList);
        String cipherText = playfair.Encrypt(plainList, cipherTable, 0);
        System.out.println(cipherText);

        List<Character> ciperList = playfair.settlePlain(cipherText);
        System.out.println("要解密的密文："+ciperList);
        String plain = playfair.Encrypt(ciperList, cipherTable, 1);
        System.out.println("解密得到明文："+plain);
    }
}