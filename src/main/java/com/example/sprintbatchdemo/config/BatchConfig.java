package com.example.sprintbatchdemo.config;

import com.example.sprintbatchdemo.dto.Person;
import com.example.sprintbatchdemo.item.MyItemProcessor;
import com.example.sprintbatchdemo.listener.MyListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @description: 不需要数据库做持久化的配置
 * @author: l00427576
 * @create: 2019-05-20 17:04
 **/
@Configuration
@Slf4j
@EnableBatchProcessing
public class BatchConfig {

//    @Bean
//    public PlatformTransactionManager transactionManager() {
//        return new ResourcelessTransactionManager();
//    }

//    @Bean
//    public ResourcelessTransactionManager transactionManager() {
//        return new ResourcelessTransactionManager();
//    }

//    @Bean
//    public MapJobRepositoryFactoryBean mapJobRepositoryFactory(PlatformTransactionManager transactionManager) throws Exception {
//        MapJobRepositoryFactoryBean factory = new MapJobRepositoryFactoryBean(transactionManager);
//        factory.afterPropertiesSet();
//        return factory;
//    }
//
//    @Bean
//    public JobRepository jobRepository(MapJobRepositoryFactoryBean repositoryFactory) throws Exception {
//        return repositoryFactory.getObject();
//    }

//    @Bean
//    public JobExplorer jobExplorer(MapJobRepositoryFactoryBean repositoryFactory) {
//        return new SimpleJobExplorer(repositoryFactory.getJobInstanceDao(), repositoryFactory.getJobExecutionDao(),
//                repositoryFactory.getStepExecutionDao(), repositoryFactory.getExecutionContextDao());
//    }

//    @Bean
//    public SimpleJobLauncher jobLauncher(JobRepository jobRepository) throws Exception {
//        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
//        jobLauncher.setJobRepository(jobRepository);
//        jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
//        return jobLauncher;
//    }

    @Bean
    public ItemReader<Person> reader() throws Exception {
        //使用FlatFileItemReader读取文件。
        FlatFileItemReader<Person> reader = new FlatFileItemReader<>();
        //使用FlatFileItemReader的setResource方法设置csv文件的路径。
        reader.setResource(new ClassPathResource("people.txt"));
        //在此处对cvs文件的数据和领域模型类做对应映射。
        reader.setLineMapper(new LineMapper<Person>() {

            @Override
            public Person mapLine(String line, int lineNumber) throws Exception {

                // 逗号分割每一行数据
                String[] args = line.split(",");
                Person person = new Person();
                person.setName(args[0]);
                person.setId(Integer.parseInt(args[1]));

                log.info("read {}",person.getName());
//                try {
//                    Thread.sleep(3000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }

                return person;
            }
        });
        return reader;
    }

    @Bean
    public ItemProcessor<Person,Person> processor(){
        return new MyItemProcessor();
    }

    @Bean
    public ItemWriter<Person> writer(){
        FlatFileItemWriter<Person> writer = new FlatFileItemWriter<>();
        writer.setResource(new ClassPathResource("people-output.txt"));
        writer.setLineAggregator(new LineAggregator<Person>() {
            @Override
            public String aggregate(Person item) {
                log.info("write {}",item.getName());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                StringBuffer sb = new StringBuffer();
                sb.append(item.getId());
                sb.append(",");
                sb.append(item.getName());
                return sb.toString();
            }
        });
        return writer;
    }

    @Bean
    public Step step1(StepBuilderFactory stepBuilderFactory,ItemReader itemReader,ItemProcessor itemProcessor,
            ItemWriter itemWriter, TaskExecutor taskExecutor){

        return stepBuilderFactory
                .get("step1")
                .<Person,Person>chunk(5)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                //.taskExecutor(taskExecutor)
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor(){
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(3);
        return threadPoolTaskExecutor;
    }

    @Bean
    public Job importJob(JobBuilderFactory jobs ,Step step1){
        return jobs.get("importJob")
                .incrementer(new RunIdIncrementer())
                .flow(step1)
                .end()
                .listener(myListener())
                .build();

    }

    @Bean
    public MyListener myListener(){
        return new MyListener();
    }

}
