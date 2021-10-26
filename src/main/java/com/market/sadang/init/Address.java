package com.market.sadang.init;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Address {
    private String city;
    private String street1;
    private String street2;
    private String zipcode;

    @Builder
    public Address(String city, String street1, String street2, String zipcode){
        this.city = city;
        this.street1 = street1;
        this.street2 = street2;
        this.zipcode = zipcode;
    }

}

