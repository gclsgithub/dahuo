package com.hytc.webmanage.common.exception;


import com.hytc.webmanage.common.io.FwBaseOut;
import lombok.Getter;

public class FwWebBusinessException extends RuntimeException{
    @Getter
    final private FwBaseOut out;

    public  FwWebBusinessException(FwBaseOut out){this.out = out;}
}
