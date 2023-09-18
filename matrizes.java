/** 
 * Subtraia os elemtos das matrizes 'matriz1' e 'matriz 2' e atribua o resultado a 'somaMatrizes' 
 * */

public class matrizes {

    public static void main(String[] args) {
        int[][] matriz1 = {{1,2,3},{4,5,6},{7,8,9}};
        int[][] matriz2 = {{1,2,3},{4,5,6},{7,8,9}};
        int[][] somaMatrizes = new int[3][3];
        
        for(int i = 0; i < matriz1.length; i++) {
            for(int j = 0; j < matriz1[i].length; j++) {
                somaMatrizes[i][j] = matriz1[i][j] - matriz2[i][j];
                System.out.print(somaMatrizes[i][j] + " ");
            }
            System.out.println();
        }
    }
}