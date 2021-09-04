package com.mhj.olivia.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.mhj.olivia.entity.OliviaData;

@FeignClient(value = "mhj-olivia")
public interface OnlineOliviaClient {

	@PostMapping("/v1/olivia-data")
	public ResponseEntity<Void> incluir(@RequestBody OliviaData dto);

}
