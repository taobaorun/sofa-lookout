/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alipay.lookout.remote.step;

import com.alipay.lookout.api.ManualClock;
import com.alipay.lookout.api.NoopRegistry;
import com.alipay.lookout.api.Registry;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author xiangfeng.xzc
 * @date 2018/7/30
 */
public class LookoutCounterTest {
    @Test
    public void test() {
        Registry r = NoopRegistry.INSTANCE;
        ManualClock clock = new ManualClock();
        LookoutCounter counter = new LookoutCounter(r.createId("foo"), clock, 10L);
        assertThat(counter.count()).isEqualTo(0);
        counter.inc();
        counter.inc();
        counter.inc();
        clock.setWallTime(11L);
        assertThat(counter.count()).isEqualTo(3L);

        for (int i = 0; i < 6; i++) {
            counter.inc();
        }

        clock.setWallTime(21L);
        assertThat(counter.count()).isEqualTo(6L);

        // 修改step
        counter.setStep(20L);
        // 丢失上次采样的数据
        assertThat(counter.count()).isEqualTo(0L);

        for (int i = 0; i < 6; i++) {
            counter.inc();
        }
        clock.setWallTime(31L);
        assertThat(counter.count()).isEqualTo(0L);

        clock.setWallTime(40L);
        assertThat(counter.count()).isEqualTo(6L);
    }
}