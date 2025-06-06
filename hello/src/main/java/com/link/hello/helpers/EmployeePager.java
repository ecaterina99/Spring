package com.link.hello.helpers;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class EmployeePager extends PageRequest {
    public EmployeePager(int pageNumber, int pageSize, Sort sort) {
        super(pageNumber, pageSize, sort);
    }
}
