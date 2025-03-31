package com.tourism.booking.dto.page;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageReponse<T> {
    List<T> content;
    PageCustom<T> page;

    public PageReponse(Page<T> page) {
        this.content = page.getContent();
        this.page = new PageCustom<T>(page);
    }
}
