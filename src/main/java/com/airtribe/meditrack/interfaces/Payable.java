package com.airtribe.meditrack.interfaces;

public interface Payable {

    double calculateTotal();

    // default method so every Bill type gets this for free instead of
    // repeating the same printf everywhere
    default void printReceipt() {
        System.out.printf("Amount payable: Rs. %.2f%n", calculateTotal());
    }
}
