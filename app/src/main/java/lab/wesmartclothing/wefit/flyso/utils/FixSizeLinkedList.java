package lab.wesmartclothing.wefit.flyso.utils;

import java.util.LinkedList;

public class FixSizeLinkedList<T> extends LinkedList<T> {

    private int capacity;

    public FixSizeLinkedList(int capacity) {
        super();
        this.capacity = capacity;
    }

    @Override
    public void add(int index, T element) {
        super.add(index, element); // 先让其更改数量；
        if (size() > capacity) {
            super.removeFirst();
        }
    }

    @Override
    public boolean add(T t) {
        if (size() + 1 > capacity) {
            super.removeFirst();
        }
        return super.add(t);
    }

    /**
     * 判断是否饱和
     *
     * @return
     */
    public boolean isSaturated() {
        return size() == capacity;
    }


    public static void main(String[] args) {
        FixSizeLinkedList<String> list = new FixSizeLinkedList<>(4);
        list.add(0, "12345");
        list.add(0, "1234");
        list.add(0, "123");
        list.add(0, "12");
        list.add(0, "1");

        for (String s : list) {
            System.out.println(s);
        }

    }
}