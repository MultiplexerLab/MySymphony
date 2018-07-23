package harmony.app.ModelClass;

import java.io.Serializable;

public class SubscriptionConfig implements Serializable {
    private int id;
    private String subscriptionFor;
    private String itemType;
    private String itemName;
    private String itemCategory;
    private String itemSubcategory;
    private String weeklyPrice;
    private String monthlyPrice;
    private String yearlyPrice;
    private String hasPromotion;
    private String weeklyPromoPrice;
    private String monthlyPromoPrice;
    private String yearlyPromoPrice;
    private String promoStartDate;
    private String promoEndedDate;
    private String status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubscriptionFor() {
        return subscriptionFor;
    }

    public void setSubscriptionFor(String subscriptionFor) {
        this.subscriptionFor = subscriptionFor;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public String getItemSubcategory() {
        return itemSubcategory;
    }

    public void setItemSubcategory(String itemSubcategory) {
        this.itemSubcategory = itemSubcategory;
    }

    public String getWeeklyPrice() {
        return weeklyPrice;
    }

    public void setWeeklyPrice(String weeklyPrice) {
        this.weeklyPrice = weeklyPrice;
    }

    public String getMonthlyPrice() {
        return monthlyPrice;
    }

    public void setMonthlyPrice(String monthlyPrice) {
        this.monthlyPrice = monthlyPrice;
    }

    public String getYearlyPrice() {
        return yearlyPrice;
    }

    public void setYearlyPrice(String yearlyPrice) {
        this.yearlyPrice = yearlyPrice;
    }

    public String getHasPromotion() {
        return hasPromotion;
    }

    public void setHasPromotion(String hasPromotion) {
        this.hasPromotion = hasPromotion;
    }

    public String getWeeklyPromoPrice() {
        return weeklyPromoPrice;
    }

    public void setWeeklyPromoPrice(String weeklyPromoPrice) {
        this.weeklyPromoPrice = weeklyPromoPrice;
    }

    public String getMonthlyPromoPrice() {
        return monthlyPromoPrice;
    }

    public void setMonthlyPromoPrice(String monthlyPromoPrice) {
        this.monthlyPromoPrice = monthlyPromoPrice;
    }

    public String getYearlyPromoPrice() {
        return yearlyPromoPrice;
    }

    public void setYearlyPromoPrice(String yearlyPromoPrice) {
        this.yearlyPromoPrice = yearlyPromoPrice;
    }

    public String getPromoStartDate() {
        return promoStartDate;
    }

    public void setPromoStartDate(String promoStartDate) {
        this.promoStartDate = promoStartDate;
    }

    public String getPromoEndedDate() {
        return promoEndedDate;
    }

    public void setPromoEndedDate(String promoEndedDate) {
        this.promoEndedDate = promoEndedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
