package org.example.lv_be.module.billing.application.interfaces.in;

import org.example.lv_be.module.billing.application.dto.request.MomoIpnCallbackRequest;

public interface IHandleMomoCallbackUseCase {
    void execute(MomoIpnCallbackRequest callbackRequest);
}
