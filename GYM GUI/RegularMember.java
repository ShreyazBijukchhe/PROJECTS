public class RegularMember extends GymMember {
    private final int attendanceLimit = 30;
    private boolean isEligibleForUpgrade;
    private String removalReason;
    private String referralSource;
    private String plan;
    private double price;

    public RegularMember(int id, String name, String location, String phone, String email, String gender, String DOB, String membershipStartDate, String referralSource) {
        super(id, name, location, phone, email, gender, DOB, membershipStartDate);
        this.isEligibleForUpgrade = false;
        this.removalReason = "";
        this.referralSource = referralSource;
        this.plan = "basic";
        this.price = 6500.0;
    }

    // Getters
    public int getAttendanceLimit() { return attendanceLimit; }
    public boolean isEligibleForUpgrade() { return isEligibleForUpgrade; }
    public String getRemovalReason() { return removalReason; }
    public String getReferralSource() { return referralSource; }
    public String getPlan() { return plan; }
    public double getPrice() { return price; }

    // Setters
    private void setEligibleForUpgrade(boolean eligible) { this.isEligibleForUpgrade = eligible; }
    private void setRemovalReason(String removalReason) { this.removalReason = removalReason; }
    private void setReferralSource(String referralSource) { this.referralSource = referralSource; }
    private void setPlan(String plan) { this.plan = plan; }
    private void setPrice(double price) { this.price = price; }

    @Override
    public void markAttendance() {
        if (isActiveStatus()) {
            setAttendance(getAttendance() + 1);
            setLoyaltyPoints(getLoyaltyPoints() + 5.0);
            if (getAttendance() >= attendanceLimit) {
                setEligibleForUpgrade(true);
            }
        }
    }

    public double getPlanPrice(String plan) {
        switch (plan.toLowerCase()) {
            case "basic":
                return 6500.0;
            case "standard":
                return 12500.0;
            case "deluxe":
                return 18500.0;
            default:
                return -1.0;
        }
    }

    public String upgradePlan(String newPlan) {
        if (!isEligibleForUpgrade) {
            return "Member is not eligible for plan upgrade. Attendance must be >= " + attendanceLimit + ".";
        }
        if (newPlan.equalsIgnoreCase(plan)) {
            return "Member is already subscribed to the " + plan + " plan.";
        }
        double newPrice = getPlanPrice(newPlan);
        if (newPrice == -1.0) {
            return "Invalid plan: " + newPlan + ". Valid plans are basic, standard, deluxe.";
        }
        setPlan(newPlan.toLowerCase());
        setPrice(newPrice);
        setLoyaltyPoints(getLoyaltyPoints() + 10.0);
        return "Plan upgraded to " + newPlan + " successfully. New price: " + newPrice;
    }

    public void revertRegularMember(String removalReason) {
        resetMember();
        setEligibleForUpgrade(false);
        setRemovalReason(removalReason);
        setPlan("basic");
        setPrice(6500.0);
    }

    @Override
    public void display() {
        super.display();
        System.out.println("Plan: " + plan);
        System.out.println("Price: " + price);
        if (!removalReason.isEmpty()) {
            System.out.println("Removal Reason: " + removalReason);
        }
    }
}