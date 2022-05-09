package format;

import constant.ConfigConsts;

import java.io.*;
import java.util.Arrays;

/**
 * @author: ming
 * @date: 2022/4/13 15:27
 */
public class ConversionFormat {

    /**
     * 将包含连接信息的文件转换成邻接矩阵。
     *
     * @param deviceNums 图中的节点数
     * @return 邻接矩阵
     */
    public int[][] fileToAdjacencyMatrix(int deviceNums) {

        int[][] adjacencyMatrix = new int[deviceNums][deviceNums];

        String path = ConversionFormat.class.getClassLoader()
                .getResource(ConfigConsts.TOPO_FILE_NAME).getPath();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            int count = 0;
            String temp;
            while ((temp = br.readLine()) != null) {
                if (count > 0) {
                    String[] s = temp.split(" ");
                    int deviceId1 = Integer.parseInt(s[0]);
                    int deviceId2 = Integer.parseInt(s[1]);
                    adjacencyMatrix[deviceId1][deviceId2] = 1;
                    adjacencyMatrix[deviceId2][deviceId1] = 1;
                }
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return adjacencyMatrix;
    }


    public static void main(String[] args) {
        int[][] adjacencyMatrix = new ConversionFormat().fileToAdjacencyMatrix(ConfigConsts.NODE_NUM);
        // 输出是一行不好看
//        System.out.println(Arrays.deepToString(adjacencyMatrix));

        System.out.print("[");
        for (int i = 0; i < ConfigConsts.NODE_NUM; i++) {
            if (i != ConfigConsts.NODE_NUM - 1) {
                System.out.println(Arrays.toString(adjacencyMatrix[i]) + ",");
            } else {
                System.out.println(Arrays.toString(adjacencyMatrix[i]));
            }
        }
        System.out.println("]");


//        int count = 0;
//        for (int i = 0; i < 44; i++) {
//
//            if (adjacencyMatrix[26][i] == 1){
//                System.out.print(i + " ");
//                count++;
//            }
//        }
//        System.out.println("邻居数量: "+count);


    }


}
