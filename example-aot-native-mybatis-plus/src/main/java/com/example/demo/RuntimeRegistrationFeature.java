package com.example.demo;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.core.toolkit.support.SerializedLambda;
import com.example.demo.rest.MpController;
import org.graalvm.nativeimage.hosted.Feature;
import org.graalvm.nativeimage.hosted.RuntimeSerialization;

/**
 * @author apple
 */
public class RuntimeRegistrationFeature implements Feature {
    @Override
    public void duringSetup(DuringSetupAccess access) {
        System.out.println("duringSetup register MpController.");
        RuntimeSerialization.registerLambdaCapturingClass(MpController.class);
        RuntimeSerialization.register(SerializedLambda.class, SFunction.class);
    }
}