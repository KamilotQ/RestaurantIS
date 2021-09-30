package sample.Order;

public class Order {
        int idorder;
        String dishname;
        Double price;

        public Order(int idorder, String dishname, Double price) {
                this.idorder = idorder;
                this.dishname = dishname;
                this.price = price;
        }

        public int getIdorder() {
                return idorder;
        }

        public void setIdorder(int idorder) {
                this.idorder = idorder;
        }

        public String getDishname() {
                return dishname;
        }

        public void setDishname(String dishname) {
                this.dishname = dishname;
        }

        public Double getPrice() {
                return price;
        }

        public void setPrice(Double price) {
                this.price = price;
        }
}