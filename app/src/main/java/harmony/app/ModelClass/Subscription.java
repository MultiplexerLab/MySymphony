package harmony.app.ModelClass;

import java.sql.Date;

public class Subscription {

    private String userId;
    private String deviceid;
    private String subscribedFor;
    private String itemType;
    private String itemName;
    private String itemCategory;
    private String itemSubCategory;
    private String itemPrice;
    private String subscriptionType;
    private String createdDate;
    private String updatedDate;
    private String startDate;
    private String endDate;
    private String paymentID;
    private String paymentMethod;

    public Subscription(String subscribedFor, String itemType, String itemName, String itemCategory, String itemSubCategory, String itemPrice, String subscriptionType, String paymentID, String paymentMethod) {
        this.subscribedFor = subscribedFor;
        this.itemType = itemType;
        this.itemName = itemName;
        this.itemCategory = itemCategory;
        this.itemSubCategory = itemSubCategory;
        this.itemPrice = itemPrice;
        this.subscriptionType = subscriptionType;
        this.paymentID = paymentID;
        this.paymentMethod = paymentMethod;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getSubscribedFor() {
        return subscribedFor;
    }

    public void setSubscribedFor(String subscribedFor) {
        this.subscribedFor = subscribedFor;
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

    public String getItemSubCategory() {
        return itemSubCategory;
    }

    public void setItemSubCategory(String itemSubCategory) {
        this.itemSubCategory = itemSubCategory;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(String subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(String paymentID) {
        this.paymentID = paymentID;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
