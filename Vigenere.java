/* 
 * Author: LonLEA
 * Date：2022-6-5
 */
public class Vigenere{
    public static String encrypt(String data,String key,int mode){
        String result = "";
        data = data.replace(" ", "").toUpperCase();
        key = key.replace(" ", "").toUpperCase();
        if(mode == 0){
            for(int i = 0,j = 0;i < data.length();i++){
                result += (char)((data.charAt(i) + key.charAt(j) - 2 * 'A') % 26 + 'A');
                j = ++j % key.length();
            }
            return result;
        }else{
            for(int i = 0,j = 0;i < data.length();i++){
                result += ((char) ('Z' - (25 - (data.charAt(i) - key.charAt(j))) % 26));
                j = ++j % key.length();
            }
            return result;
        }
    }

    public static void main(String[] args) {
        String cypher = encrypt("what are you doing", "this is a key", 0);
        System.out.println("密文" + cypher);
        String plain = encrypt(cypher, "this is a key", 1);
        System.out.println("明文" + plain);
    }
}