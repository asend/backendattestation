package com.fonctionpublique.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class OperationNonPermittedException extends RuntimeException {
    
    private final String errorMsg;

    private final String operatingId;

    private final String source;

    private final String dependency;
}
