package com.example.wang.zzj.model;

import java.util.LinkedList;

/**
 * Created by wang on 2/10/15. RecyclerView列表类
 */
public class RecyclerViewItemArray extends LinkedList<ItemData> {

    /**发现
     * @param type 类型的第一个数据的位置
     *             没有返回-1
     * */
    public int findFirstTypePosition(int type) {
        for (int i = 0; i < size(); i++) {
            if (type == get(i).getDataType()) {
                return i;
            }
        }

        return -1;
    }

    /**发现
     * @param type 类型的最后一个数据的位置
     *             没有返回-1
     * */
    public int findLastTypePosition(int type) {
        for (int i = size() - 1; i >= 0; i--) {
            if (type == get(i).getDataType()) {
                return i;
            }
        }
        return -1;
    }

    /**发现不是
     * @param type 类型的第一个数据的位置
     *             没有返回-1
     * */
    public int findFirstNotTypePosition(int type) {
        for (int i = 0; i < size(); i++) {
            if (type != get(i).getDataType()) {
                return i;
            }
        }

        return -1;
    }

    /**发现不是
     * @param type 类型的最后一个数据的位置
     *             没有返回-1
     * */
    public int findLastNotTypePosition(int type) {
        for (int i = size() - 1; i >= 0; i--) {
            if (type != get(i).getDataType()) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 增加到type类型的最后一个
     * */
    public int addAfterLast(int type, ItemData data) {
        int position = findLastTypePosition(type);
        add(position++, data);
        return position;
    }

    /**
     * 增加到type类型的第一个
     * */
    public int addBeforFirst(int type, ItemData data) {
        int position = findFirstTypePosition(type);
        add(position, data);
        return position;
    }

    /**
     * 移除type类型的第一个数据
     * */
    public ItemData removeFirstType(int type) {
        int position = findFirstTypePosition(type);
        if (position != -1) {
            return remove(position);
        }
        return null;
    }

    /**
     * 移除所有的type类型数据
     * */
    public int removeAllType(int type) {
        int count = 0;

        for (int i = 0; i < size(); i++) {
            ItemData item = get(i);
            if (item.getDataType() == type) {
                remove(item);
                count ++;
                i --;
            }
        }

        return count;
    }

    /**
     * 数组中是否存在type类型的项
     * @param type 要查找的type类型
     * @return 空这是true否则false
     */
    public boolean isEmptyOfType(int type) {
        return findFirstTypePosition(type) == -1;
    }

}
