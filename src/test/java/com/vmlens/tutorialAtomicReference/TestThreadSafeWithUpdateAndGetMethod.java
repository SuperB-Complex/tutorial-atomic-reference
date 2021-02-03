package com.vmlens.tutorialAtomicReference;

/**
 * author: JinjinMa
 * date: Feb 3 2020
 */

import com.vmlens.api.AllInterleavings;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.UnaryOperator;

import static org.junit.Assert.assertTrue;

public class TestThreadSafeWithUpdateAndGetMethod {
    @Test
    public void test() throws InterruptedException {
        try (AllInterleavings allInterleavings = new AllInterleavings("TestThreadSafe");) {
            while (allInterleavings.hasNext()) {
                final AtomicReference<State> object = new AtomicReference<> (new State());
                UnaryOperator<State> operator = state -> state.update();
                Thread thread1 = new Thread(() -> object.updateAndGet(operator));
                Thread thread2 = new Thread(() -> object.updateAndGet(operator));
                thread1.start();
                thread2.start();
                thread1.join();
                thread2.join();
                assertTrue(object.get().isAccessedByMultipleThreads());
            }
        }
    }
}
