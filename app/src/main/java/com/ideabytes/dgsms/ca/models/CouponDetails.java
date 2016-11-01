package com.ideabytes.dgsms.ca.models;

/**
 * Created by sairam on 7/10/16.
 */

public class CouponDetails {


    private static CouponDetails couponDetails;
    private String discountPercentage = "";
    private String couponCode = "";
    private String productId = "dgmobi1";
    public void setProductId(String productId) {
        this.productId = productId;
    }
    public String getProductId() {
        return productId;
    }
    public String getCouponCode() {
        return couponCode;
    }

    public String getDiscountPercentage() {
        return discountPercentage;
    }
    public void setDiscountPercentage(String discountPercentage) {
        this.discountPercentage = discountPercentage;
    }
    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }
    public static CouponDetails getInstance() {
        if (couponDetails == null) {
            couponDetails = new CouponDetails();
        }
        return couponDetails;
    }
}
