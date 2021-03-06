package com.htec.filesystem.model.response;

import com.htec.filesystem.dto.BreadCrumbDTO;
import com.htec.filesystem.dto.ShelfItemDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShelfContentResponseModel {
    private List<BreadCrumbDTO> breadCrumbs;
    private List<ShelfItemDTO> shelfItems;
}
