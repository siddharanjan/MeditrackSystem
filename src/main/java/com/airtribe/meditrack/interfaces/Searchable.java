package com.airtribe.meditrack.interfaces;

public interface Searchable {

    // free text match, each class decides what counts as a match for itself
    boolean matches(String keyword);
}
