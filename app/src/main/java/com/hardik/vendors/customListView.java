package com.hardik.vendors;
public class customListView {

//    public class NumbersView {

    // the resource ID for the imageView
//        private int ivNumbersImageId;

    // TextView 1
    private String product;

    // TextView 1
    private String price;

    // create constructor to set the values for all the parameters of the each single view

    public customListView( String product, String price) {
//            ivNumbersImageId = NumbersImageId;
        this.product = product;
        this.price = price;
    }

    public String getProduct()
    {
        return product;
    }
    public String getPrice()
    {
        return price;
    }


    // getter method for returning the ID of the imageview
//        public int getP() {
//            return ivNumbersImageId;
//        }
//
//        // getter method for returning the ID of the TextView 1
//        public String getNumberInDigit() {
//            return mNumberInDigit;
//        }
//
//        // getter method for returning the ID of the TextView 2
//        public String getNumbersInText() {
//            return mNumbersInText;
//        }
//    }
}