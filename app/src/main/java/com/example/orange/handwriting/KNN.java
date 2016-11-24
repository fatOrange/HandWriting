package com.example.orange.handwriting;
import java.util.*;

/**
 * Created by orange on 16/11/23.
 */
public class KNN {
    /**
     * 设置优先级队列的比较函数，距离越大，优先级越高
     */
    private Comparator<KNNNode> comparator = new Comparator<KNNNode>() {
        public int compare(KNNNode o1, KNNNode o2) {
            if (o1.getDistance() >= o2.getDistance()) {
                return 1;
            } else {
                return 0;
            }
        }
    };
    /**
     * 获取K个不同的随机数
     * @param k 随机数的个数
     * @param max 随机数最大的范围
     * @return 生成的随机数数组
     */
    public List<Integer> getRandKNum(int k, int max) {
        List<Integer> rand = new ArrayList<Integer>(k);
        for (int i = 0; i < k; i++) {
            int temp = (int) (Math.random() * max);
            if (!rand.contains(temp)) {
                rand.add(temp);
            } else {
                i--;
            }
        }
        return rand;
    }
    /**
     * 计算测试元组与训练元组之前的距离
     * @param d1 测试元组
     * @param d2 训练元组
     * @return 距离值
     */
    public double calDistance(List<Integer> d1, List<Integer> d2) {
        double distance = 0.00;
        for (int i = 0; i < d1.size(); i++) {
            distance += (d1.get(i) - d2.get(i)) * (d1.get(i) - d2.get(i));
        }
        return distance;
    }
    /**
     * 执行KNN算法，获取测试元组的类别
     * @param datas 训练数据集
     * @param testData 测试元组
     * @param k 设定的K值
     * @return 测试元组的类别
     */
    public String knn(List<List<Integer>> datas, List<Integer> testData, int k) {
        PriorityQueue<KNNNode> pq = new PriorityQueue<KNNNode>(k, comparator);
        List<Integer> randNum = getRandKNum(k, datas.size());
        for (int i = 0; i < k; i++) {
            int index = randNum.get(i);
            List<Integer> currData = datas.get(index);
            String c = currData.get(currData.size() - 1).toString();
            KNNNode node = new KNNNode(index, calDistance(testData, currData), c);
            pq.add(node);
        }
        for (int i = 0; i < datas.size(); i++) {
            List<Integer> t = datas.get(i);
            double distance = calDistance(testData, t);
            KNNNode top = pq.peek();
            if (top.getDistance() > distance) {
                pq.remove();
                pq.add(new KNNNode(i, distance, t.get(t.size() - 1).toString()));
            }
        }

        return getMostClass(pq);
    }
    /**
     * 获取所得到的k个最近邻元组的多数类
     * @param pq 存储k个最近近邻元组的优先级队列
     * @return 多数类的名称
     */
    private String getMostClass(PriorityQueue<KNNNode> pq) {
        Map<String, Integer> classCount = new HashMap<String, Integer>();
        for (int i = 0; i < pq.size(); i++) {
            KNNNode node = pq.remove();
            String c = node.getC();
            if (classCount.containsKey(c)) {
                classCount.put(c, classCount.get(c) + 1);
            } else {
                classCount.put(c, 1);
            }
        }
        int maxIndex = -1;
        int maxCount = 0;
        Object[] classes = classCount.keySet().toArray();
        for (int i = 0; i < classes.length; i++) {
            if (classCount.get(classes[i]) > maxCount) {
                maxIndex = i;
                maxCount = classCount.get(classes[i]);
            }
        }
        return classes[maxIndex].toString();
    }
}