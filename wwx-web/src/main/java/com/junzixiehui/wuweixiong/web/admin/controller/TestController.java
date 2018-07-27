package com.junzixiehui.wuweixiong.web.admin.controller;

import com.junziziehui.application.api.Req;
import com.junziziehui.application.api.Resp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
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
@Api(value = "测试任务管理",tags = {"测试任务"}, description = "描述信息")
public class TestController {



	@ApiOperation(value="测试1", notes="test")
	@ApiImplicitParam(name = "req", value = "test", required = true, dataType = "Req")
	@RequestMapping(value="/test", method=RequestMethod.POST)
	public Resp postUser(@RequestBody Req req) {
		return Resp.success();
	}


}
