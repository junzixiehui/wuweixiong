package com.junzixiehui.wwx.web.common;

import com.junzixiehui.wwx.web.APIs;
import com.junzixiehui.application.api.Resp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>Description: </p>
 * @author: by qulibin
 * @date: 2018/7/27  下午3:10
 * @version: 1.0
 */
@RestController
@Slf4j
@Api(value = "监控",tags = {"监控健康"}, description = "描述信息")
public class HealthyController {



	@ApiOperation(value="监控健康")
	@RequestMapping(value=APIs.HEALTH, method=RequestMethod.GET)
	public Resp healthy() {
		return Resp.success();
	}


}
