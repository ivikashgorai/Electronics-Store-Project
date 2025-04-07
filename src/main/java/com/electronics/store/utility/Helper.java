package com.electronics.store.utility;

import com.electronics.store.dtos.paging_response.PageableResponse;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public class Helper {

    //general method for every body
    public static <U,V>PageableResponse<V> getPagableResponse(Page<U> page, Class<V> type){ // U,V is dataType or Object
        List<U> entity =  page.getContent();
        List<V> allUserDto = new ArrayList<>();

        for(U u:entity){
            allUserDto.add(new ModelMapper().map(u,type));
        }


        PageableResponse<V> response = PageableResponse.<V>builder()
                .content(allUserDto)
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .isLastPage(page.isLast())
                .build();

        return response;
    }
}
