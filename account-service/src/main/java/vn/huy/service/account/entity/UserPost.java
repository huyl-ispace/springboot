package vn.huy.service.account.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import vn.huy.service.account.util.ConstantUtil;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Node
public class UserPost {

    @Id
    @NotBlank(message = ConstantUtil.NOT_EMPTY)
    private String id;

    private String ngayTao;

    private String ngaySua;

    private boolean daXoa;

    @NotBlank(message = ConstantUtil.NOT_EMPTY)
    private String soDienThoai;

    private String diaChi;

    private String avatar;
}
