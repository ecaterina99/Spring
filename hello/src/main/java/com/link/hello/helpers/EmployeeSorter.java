package com.link.hello.helpers;

import org.springframework.data.domain.Sort;

import java.util.List;

public class EmployeeSorter extends Sort {
    public EmployeeSorter(List<Order> orders) {
        super(orders);
    }
}
