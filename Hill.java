import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


/* 
 * Author:LonLEA
 * Date:2020-6-3
 */
public class Hill{
    private static Map<Character,Integer> charMap = new HashMap<>();
    private static Map<Integer,Character> rev_charMap = new HashMap<>();
    static{
        char[] letter = new char[]{'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T',
        'U','V','W','X','Y','Z','#'};
        for(int i = 0;i <= 26;i++){
            charMap.put(letter[i], i);
        }
        for(int i = 0;i <= 26;i++){
            rev_charMap.put(i, letter[i]);
        }
    }

    public static Matrix StringtoMatrix(String data,Matrix key){
        //数据去除空格转大写
        data = data.replaceAll(" ", "").toUpperCase();
        while(data.length() % key.row != 0){
            data += "#";
        }
        System.out.println(data);
        //数据处理为对应数字的数组
        int[] dataArray = new int[data.length()];
        System.out.print("数据对应数组:");
        for(int i = 0; i< data.length();i++){
            dataArray[i] = Hill.charMap.get(data.charAt(i));
            System.out.print(dataArray[i]+" ");
        } 
        System.out.println();
        //数据转换为对应矩阵
        Matrix dataMatrix = new Matrix(key.col,dataArray.length/key.col);
        dataMatrix.initdata(dataArray);
        System.out.println("数据对应矩阵：");
        dataMatrix.display();
        return dataMatrix;
    }

    public static String MatrixtoString(Matrix m,Matrix key){
        //matrix转化为数组
        int[] datares = new int[m.col*m.row];
        for(int i = 0; i< m.col;i++){
            for(int j = 0; j < m.row;j++){
                datares[j + i * m.row] = m.matrix[j][i] % 26;
            } 
        } 
        //数组转换为对应密文
        String ciphertext = "";
        for(int i = 0; i< datares.length;i++){
            ciphertext += Hill.rev_charMap.get(datares[i]); 
        } 
        return ciphertext;
    }

    public static void main(String[] args) {
        //密钥
        Matrix key = new Matrix(3);
        key.init(new int[]{6,24,1,13,16,10,20,17,15});
        System.out.println("密钥：");
        key.display();
        Matrix rev_key = new Matrix(3);
        rev_key.init(new int[]{8,5,10,21,8,21,21,12,8});
        System.out.println("密钥的逆矩阵");
        rev_key.display();
        //读取要加密的数据
        Scanner sc = new Scanner(System.in);
        System.out.print("请输入加密数据：");
        String plaintext =sc.nextLine();
        Matrix plainMatrix = StringtoMatrix(plaintext,key);
        
        Matrix cipherMatrix = key.plus(plainMatrix);
        String ciphertext = MatrixtoString(cipherMatrix, key);
        System.out.println("加密后数据："+ciphertext);

        System.out.print("请输入解密数据：");
        String ciphertext1 = sc.nextLine();
        Matrix cipherMatrix1 = StringtoMatrix(ciphertext1,rev_key);
        
        Matrix plainMatrix1 = rev_key.plus(cipherMatrix1);
        String plaintext1 = MatrixtoString(plainMatrix1, rev_key);
        System.out.println(plaintext1);
        /* 
        data.init(new int[]{0,2,19,0,2,19});
        data.display();
        key.plus(data).display(); */
    }


}

class Matrix{
    int row;
    int col;
    int[][] matrix;

    Matrix(int n){
        this.row = n;
        this.col = n;
        this.matrix = new int[row][col];
    }

    Matrix(int row,int col){
        this.row = row;
        this.col = col;
        this.matrix = new int[row][col];
    }

    public void init(int[] data){
        for(int i=0;i<this.row;i++){
            for(int j=0;j<this.col;j++){
                this.matrix[i][j] = data[i*this.col+j];
            }
        }
    }
    public void initdata(int[] data){
        for(int i=0;i<this.col;i++){
            for(int j=0;j<this.row;j++){
                this.matrix[j][i] = data[i*this.row+j];
            }
        }
    }

    public Matrix plus(Matrix m){
        if(this.col != m.row){
            System.out.println("矩阵不能相乘");
            return null;
        }
        Matrix res = new Matrix(this.row,m.col);
        for(int i=0;i<this.row;i++){
            for(int j=0;j<m.col;j++){
                for(int k = 0;k < m.row; k++){
                    res.matrix[i][j] += (this.matrix[i][k] * m.matrix[k][j]); 
                }
            }
        }
        res.row = res.matrix.length;
        res.col = res.matrix[0].length;
        return res;
    }

    public void display(){
        for(int i = 0;i < this.row;i++){
            System.out.print("[");
            for(int j = 0;j < this.col;j++){
                System.out.print(this.matrix[i][j]);
                if(j != this.col -1){
                    System.out.print(",");
                }
            }
            System.out.print("]");
            System.out.println();
        }
    }
}
