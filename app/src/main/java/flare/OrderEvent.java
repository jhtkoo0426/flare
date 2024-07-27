package flare;


public class OrderEvent {

    private Double orderPrice;

    public OrderEvent() { }

    public void setOrderPrice(Double price) {
        orderPrice = price;
    }

    public Double getOrderPrice() {
        return orderPrice;
    }

}
