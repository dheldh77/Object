package Chapter02;

public class Reservation {
    private Customer customer;
    private Screnening screnening;
    private Money fee;
    private int audienceCount;

    public Reservation(Customer customer, Screnening screnening, Money fee, int audienceCount) {
        this.customer = customer;
        this.screnening = screnening;
        this.fee = fee;
        this.audienceCount = audienceCount;
    }
}
