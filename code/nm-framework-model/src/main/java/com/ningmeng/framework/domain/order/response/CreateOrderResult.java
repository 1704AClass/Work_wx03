package com.ningmeng.framework.domain.order.response;

import com.ningmeng.framework.domain.order.NmOrders;
import com.ningmeng.framework.model.response.ResponseResult;
import com.ningmeng.framework.model.response.ResultCode;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CreateOrderResult extends ResponseResult {
    private NmOrders xcOrders;
    public CreateOrderResult(ResultCode resultCode, NmOrders xcOrders) {
        super(resultCode);
        this.xcOrders = xcOrders;
    }


}
