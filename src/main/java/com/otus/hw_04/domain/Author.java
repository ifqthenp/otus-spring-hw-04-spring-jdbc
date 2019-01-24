package com.otus.hw_04.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Author {

    private long id;
    private String firstName;
    private String lastName;

    public Author(final String firstName, final String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

}
