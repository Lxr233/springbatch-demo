package com.example.sprintbatchdemo.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

/**
 * @description:
 * @author: l00427576
 * @create: 2019-05-21 10:17
 **/

@Slf4j
public class MyListener implements JobExecutionListener {
    long startTime;
    long endTime;

    //监听器实现JobExecutionListener接口，并重写其beforeJob、afterJob方法即可。
    @Override
    public void beforeJob(JobExecution jobExecution) {
        startTime = System.currentTimeMillis();
        log.info("任务处理开始");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        endTime = System.currentTimeMillis();
        log.info("任务处理结束");
        log.info("耗时:" + (endTime - startTime) + "ms");
    }
}
