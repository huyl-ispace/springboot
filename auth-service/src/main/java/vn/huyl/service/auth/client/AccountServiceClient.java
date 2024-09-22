package vn.huyl.service.auth.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import vn.huyl.service.auth.config.OAuthFeignConfig;

@FeignClient(value = "account-service", path = "/account")
public interface AccountServiceClient {

    @GetMapping(value = "/test")
    String test();
}
