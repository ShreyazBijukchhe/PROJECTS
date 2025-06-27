public class PremiumMember extends GymMember {
    private final double premiumCharge = 50000.0;
    private String personalTrainer;
    private boolean isFullPayment;
    private double paidAmount;
    private double discountAmount;

    public PremiumMember(int id, String name, String location, String phone, String email, String gender, String DOB, String membershipStartDate, String personalTrainer) {
        super(id, name, location, phone, email, gender, DOB, membershipStartDate);
        this.personalTrainer = personalTrainer;
        this.isFullPayment = false;
        this.paidAmount = 0.0;
        this.discountAmount = 0.0;
    }

    // Getters
    public double getPremiumCharge() { return premiumCharge; }
    public String getPersonalTrainer() { return personalTrainer; }
    public boolean isFullPayment() { return isFullPayment; }
    public double getPaidAmount() { return paidAmount; }
    public double getDiscountAmount() { return discountAmount; }

    // Setters
    private void setPersonalTrainer(String personalTrainer) { this.personalTrainer = personalTrainer; }
    private void setFullPayment(boolean isFullPayment) { this.isFullPayment = isFullPayment; }
    private void setPaidAmount(double paidAmount) { this.paidAmount = paidAmount; }
    private void setDiscountAmount(double discountAmount) { this.discountAmount = discountAmount; }

    @Override
    public void markAttendance() {
        if (isActiveStatus()) {
            setAttendance(getAttendance() + 1);
            setLoyaltyPoints(getLoyaltyPoints() + 5.0);
        }
    }

    public String payDueAmount(double paidAmount) {
        if (isFullPayment) {
            return "Payment is already full. No further payment required.";
        }
        if (this.paidAmount + paidAmount > premiumCharge) {
            return "Payment failed: Total paid amount exceeds premium charge of " + premiumCharge + ".";
        }
        this.paidAmount += paidAmount;
        isFullPayment = (this.paidAmount == premiumCharge);
        double remainingAmount = premiumCharge - this.paidAmount;
        return "Payment of " + paidAmount + " successful. Remaining amount: " + remainingAmount;
    }

    public void calculateDiscount() {
        if (isFullPayment) {
            discountAmount = 0.10 * premiumCharge;
            System.out.println("Discount calculated: 10% of premium charge = " + discountAmount);
        } else {
            discountAmount = 0.0;
            System.out.println("No discount applied: Payment is not full.");
        }
    }

    public void revertPremiumMember() {
        resetMember();
        personalTrainer = "";
        isFullPayment = false;
        paidAmount = 0.0;
        discountAmount = 0.0;
    }

    @Override
    public void display() {
        super.display();
        System.out.println("Personal Trainer: " + personalTrainer);
        System.out.println("Paid Amount: " + paidAmount);
        System.out.println("Full Payment Status: " + isFullPayment);
        double remainingAmount = premiumCharge - paidAmount;
        System.out.println("Remaining Amount: " + remainingAmount);
        if (isFullPayment) {
            System.out.println("Discount Amount: " + discountAmount);
        }
    }
}