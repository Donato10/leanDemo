package org.lean.transform.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageDTO<T> {
    int totalPages;
    List<T> content;
    int currentPage;
    long totalElements;
}
