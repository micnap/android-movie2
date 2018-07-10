package com.mickeywilliamson.mickey.popularmovies2;

/**
 * Reviews POJO class.
 */
class Review {

    // Review properties.
    private String author;
    private String review;


    // Constructors.
    public Review() {}

    public Review(String author, String review) {
        this.author = author;
        this.review = review;
    }

    // Setters and getters.
    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getReview() {
        return review;
    }
}
