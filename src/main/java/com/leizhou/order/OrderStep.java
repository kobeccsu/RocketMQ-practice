package com.leizhou.order;

import java.util.ArrayList;
import java.util.List;

public class OrderStep {
    private long orderId;
    private String desc;

    public OrderStep(long orderId, String desc) {
        this.orderId = orderId;
        this.desc = desc;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "OrderStep{" +
                "orderId=" + orderId +
                ", desc='" + desc + '\'' +
                '}';
    }

    public static List<OrderStep> buildOrders(){
        List<OrderStep> orderList = new ArrayList<>();
        OrderStep order = new OrderStep(1039, "创建");
        orderList.add(order);

        order = new OrderStep(1039, "付款");
        orderList.add(order);

        order = new OrderStep(1039, "推送");
        orderList.add(order);

        order = new OrderStep(1039, "完结");
        orderList.add(order);

        order = new OrderStep(1065, "创建");
        orderList.add(order);

        order = new OrderStep(1065, "付款");
        orderList.add(order);

        order = new OrderStep(1066, "创建");
        orderList.add(order);

        order = new OrderStep(1066, "付款");
        orderList.add(order);

        return orderList;
    }
}
