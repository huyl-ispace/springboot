package vn.huy.service.account.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import vn.huy.service.account.dto.ResponseDto;
import vn.huy.service.account.entity.UserPost;
import vn.huy.service.account.repository.UserPostRepository;
import vn.huy.service.account.service.UserPostService;
import vn.huy.service.account.util.ConstantUtil;
import vn.huy.service.account.util.DateTimeUtil;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserPostServiceImpl implements UserPostService {

    private final UserPostRepository userPostRepository;

    @Override
    public ResponseDto saveUser(UserPost dto, boolean update, Principal principal) {
        try {
            Optional<UserPost> optional = userPostRepository.findById(dto.getId());
            if (!update && optional.isPresent()) {
                return new ResponseDto(HttpStatus.BAD_REQUEST.value(),
                        String.format(ConstantUtil.ErrorMessage.EXISTED.getValue(), "id"), null);
            }
            if (update && optional.isEmpty()) {
                return new ResponseDto(HttpStatus.BAD_REQUEST.value(),
                        String.format(ConstantUtil.ErrorMessage.NOT_EXISTED.getValue(), "id"), null);
            }
            if (update) {
                dto.setDaXoa(optional.get().isDaXoa());
                dto.setNgayTao(optional.get().getNgayTao());
                dto.setNgaySua(DateTimeUtil.formatDateTime(LocalDateTime.now()));
            } else {
                dto.setDaXoa(false);
                dto.setNgayTao(DateTimeUtil.formatDateTime(LocalDateTime.now()));
            }
            userPostRepository.save(dto);
            return new ResponseDto(HttpStatus.OK.value(), ConstantUtil.ErrorMessage.SUCCESS.getValue(), dto);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    ConstantUtil.ErrorMessage.INTERNAL_SEVER_ERROR.getValue(), null);
        }
    }
}
