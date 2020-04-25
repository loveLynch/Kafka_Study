package com.lynch.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by lynch on 2020-04-16.
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {
    private String name;

    private String address;
}
