package com.sevenpay.merchantportal.report_service.model.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Page {
   private Integer totalPages;
   private Integer totalElements;
   private Integer size;
   private Integer number;
}
