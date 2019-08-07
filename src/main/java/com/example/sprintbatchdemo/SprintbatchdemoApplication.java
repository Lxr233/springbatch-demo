package com.example.sprintbatchdemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.util.Date;

@Slf4j
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class SprintbatchdemoApplication implements CommandLineRunner {

    @Autowired
    private Job job;

    @Autowired
    private JobLauncher jobLauncher;

    public static void main(String[] args) {
        SpringApplication.run(SprintbatchdemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .addDate("date", new Date())
                .addString("tableName", "mySpringBatchJob")
                .toJobParameters();
        JobExecution jobExecution = jobLauncher.run(job, jobParameters);
        ExitStatus es = jobExecution.getExitStatus();
        if (es.getExitCode().equals(ExitStatus.COMPLETED.getExitCode())) {
            log.info("任务正常完成");
        } else {
            log.info("任务失败，exitCode=" + es.getExitCode());
        }
    }
}
