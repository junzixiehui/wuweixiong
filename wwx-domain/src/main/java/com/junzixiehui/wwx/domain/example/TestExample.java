package com.junzixiehui.wwx.domain.example;

import com.junzixiehui.zorm.annotation.Column;
import com.junzixiehui.zorm.annotation.Table;
import com.junzixiehui.zorm.entity.LongIdEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>Description: </p>
 * @author: by qulibin
 * @date: 2018/7/27  下午5:15
 * @version: 1.0
 */
@Setter
@Getter
@ToString
@Table("t_test")
public class TestExample extends LongIdEntity {


	@Column(value = "name")
	private String name;
}
