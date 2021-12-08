package com.htec.account.model.response;

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
    private Integer totalPages;
    private Integer currentPage;
    private Integer usersCount;
}
