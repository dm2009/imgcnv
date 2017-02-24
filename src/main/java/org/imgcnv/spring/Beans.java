package org.imgcnv.spring;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.imgcnv.service.concurrent.IdGenerator;
import org.imgcnv.service.concurrent.ImageConsumer;
import org.imgcnv.service.concurrent.ImageConsumerStarter;
import org.imgcnv.service.concurrent.ImageObject;
import org.imgcnv.service.concurrent.ImageProducer;
import org.imgcnv.service.concurrent.JobMapConfig;
import org.imgcnv.service.concurrent.QueueConfig;
import org.imgcnv.service.concurrent.download.DownloadService;
import org.imgcnv.service.concurrent.download.DownloadServiceImpl;
import org.imgcnv.service.concurrent.resize.ResizeBufferedImageService;
import org.imgcnv.service.concurrent.resize.ResizeBufferedImageServiceScalrImpl;
import org.imgcnv.utils.Consts;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Class for configured Spring Bean for application.
 *
 * @author Dmitry_Slepchenkov
 *
 */
@Configuration
@ComponentScan
public class Beans {

    /**
     *
     * @return BlockingQueue<ImageObject>.
     */
    @Bean
    public BlockingQueue<ImageObject> blockingQueue() {
        return new LinkedBlockingQueue<ImageObject>(Consts.DOWNLOAD_THREADS);
    }

    /**
     *
     * @return QueueConfig.
     */
    @Bean
    public QueueConfig queueConfig() {
        return new QueueConfig();
    }

    /**
     *
     * @return IdGenerator.
     */
    @Bean
    public IdGenerator idGenerator() {
        return new IdGenerator();
    }

    /**
     *
     * @return ResizeBufferedImageService.
     */
    @Bean
    public ResizeBufferedImageService resizeService() {
        return new ResizeBufferedImageServiceScalrImpl();
    }

    /**
     *
     * @return DownloadService.
     */
    @Bean
    public DownloadService downloadService() {
        return new DownloadServiceImpl();
    }

    /**
     *
     * @return JobMapConfig.
     */
    @Bean
    public JobMapConfig jobMapConfig() {
        return new JobMapConfig();
    }

    /**
     *
     * @return ImageProducer.
     */
    @Bean
    public ImageProducer imageProducer() {
        ImageProducer bean = new ImageProducer();
        bean.setDownloadService(downloadService());
        bean.setIdGenerator(idGenerator());
        bean.setItemQueue(queueConfig());
        bean.setJobMap(jobMapConfig());
        return bean;
    }

    /**
     *
     * @return ImageConsumer.
     */
    @Bean
    public ImageConsumer imageConsumer() {
        ImageConsumer bean = new ImageConsumer();
        bean.setItemQueue(queueConfig());
        bean.setJobMap(jobMapConfig());
        bean.setResizeService(resizeService());
        return bean;
    }

    /**
     *
     * @return ImageConsumerStarter.
     */
    @Bean
    public ImageConsumerStarter imageConsumerStarter() {
        return new ImageConsumerStarter(imageConsumer());
    }
}
