package djnd.happy.farm.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
@Getter
@Setter
public class ResultPaginationDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    Object result;
    Meta meta;
    @Getter
    @Setter
    public static class Meta{
    private int page;
    private int pageSize;
    private long total;
    private int pages;
    }

}
