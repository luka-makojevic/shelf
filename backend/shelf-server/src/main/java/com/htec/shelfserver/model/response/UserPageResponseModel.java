package com.htec.shelfserver.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserPageResponseModel<T> {
    private List<T> users;
    private Integer total;
    private Integer currentPage;
}
