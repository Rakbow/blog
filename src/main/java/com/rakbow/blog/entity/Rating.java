package com.rakbow.blog.entity;

/**
 * @Project_name: blog
 * @Author: Rakbow
 * @Create: 2022-08-13 21:04
 * @Description:
 */
public class Rating {

    private int itemId;//商品类型
    private int productionId;//对应商品id
    private double AverageRating = 0;//平均评分
    private int RatingOneNum = 0;//一星评分数
    private int RatingTwoNum = 0;//二星评分数
    private int RatingThreeNum = 0;//三星评分数
    private int RatingFourNum = 0;//四星评分数
    private int RatingFiveNum = 0;//五星评分数
    private int RatingNum = 0;//总评分人数

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getProductionId() {
        return productionId;
    }

    public void setProductionId(int productionId) {
        this.productionId = productionId;
    }

    public double getAverageRating() {
        return AverageRating;
    }

    public void setAverageRating(double averageRating) {
        AverageRating = averageRating;
    }

    public int getRatingOneNum() {
        return RatingOneNum;
    }

    public void setRatingOneNum(int ratingOneNum) {
        RatingOneNum = ratingOneNum;
    }

    public int getRatingTwoNum() {
        return RatingTwoNum;
    }

    public void setRatingTwoNum(int ratingTwoNum) {
        RatingTwoNum = ratingTwoNum;
    }

    public int getRatingThreeNum() {
        return RatingThreeNum;
    }

    public void setRatingThreeNum(int ratingThreeNum) {
        RatingThreeNum = ratingThreeNum;
    }

    public int getRatingFourNum() {
        return RatingFourNum;
    }

    public void setRatingFourNum(int ratingFourNum) {
        RatingFourNum = ratingFourNum;
    }

    public int getRatingFiveNum() {
        return RatingFiveNum;
    }

    public void setRatingFiveNum(int ratingFiveNum) {
        RatingFiveNum = ratingFiveNum;
    }

    public int getRatingNum() {
        return RatingNum;
    }

    public void setRatingNum(int ratingNum) {
        RatingNum = ratingNum;
    }

    @Override
    public String toString() {
        return "AlbumRating{" +
                "itemId=" + itemId +
                ", productionId=" + productionId +
                ", AverageRating=" + AverageRating +
                ", RatingOneNum=" + RatingOneNum +
                ", RatingTwoNum=" + RatingTwoNum +
                ", RatingThreeNum=" + RatingThreeNum +
                ", RatingFourNum=" + RatingFourNum +
                ", RatingFiveNum=" + RatingFiveNum +
                ", RatingNum=" + RatingNum +
                '}';
    }
}
