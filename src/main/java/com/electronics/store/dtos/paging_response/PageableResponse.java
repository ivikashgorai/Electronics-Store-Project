package com.electronics.store.dtos.paging_response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
// this class will be used to send response for get all user
public class PageableResponse<T> { // T can be any entity or object or datatype
    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean isLastPage;
}

