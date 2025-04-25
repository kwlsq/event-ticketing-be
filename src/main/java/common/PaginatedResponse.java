package common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginatedResponse<T> {
  private int page;
  private int size;
  private long totalElements;
  private int totalPages;
  private boolean hasNext;
  private boolean hasPrevious;
  private List<T> content;
}
