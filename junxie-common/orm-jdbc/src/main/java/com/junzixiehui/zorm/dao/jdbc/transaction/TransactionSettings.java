package com.junzixiehui.zorm.dao.jdbc.transaction;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TransactionSettings {
    @Builder.Default
    private IsolationLevelEnum isolationLevel = IsolationLevelEnum.DEFAULT;
    @Builder.Default
    private int timeout = -1;
}
