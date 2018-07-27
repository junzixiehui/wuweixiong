package com.junzixiehui.wwx.web.common;

import com.junzixiehui.application.api.Req;
import com.junzixiehui.application.api.Resp;
import com.junzixiehui.wwx.infrastructure.tunnel.example.TestExampleDao;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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


	@Resource
	private TestExampleDao testExampleDao;


	@ApiOperation(value="测试1", notes="test")
	@ApiImplicitParam(name = "req", value = "test", required = false, dataType = "Req")
	@RequestMapping(value="/test", method=RequestMethod.GET)
	public Resp postUser() {

		testExampleDao.findAllList();

		return Resp.success();
	}


}
