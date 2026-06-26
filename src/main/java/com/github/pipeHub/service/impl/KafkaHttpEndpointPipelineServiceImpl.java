package com.github.pipeHub.service.impl;

import com.github.pipeHub.manage.PipelineInfo;
import com.github.pipeHub.manage.PipelineManager;
import com.github.pipeHub.model.HttpEndPointConfig;
import com.github.pipeHub.model.KafkaConfig;
import com.github.pipeHub.model.KafkaHttpEndpointBridge;
import com.github.pipeHub.model.KafkaHttpEndpointPipeline;
import com.github.pipeHub.service.KafkaHttpEndpointPipelineService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
public class KafkaHttpEndpointPipelineServiceImpl implements KafkaHttpEndpointPipelineService {

    @Value("${endpoint.auth-token:ll}")
    private String authToken;

    private final CloseableHttpClient httpAgent;

    private final PipelineManager pipelineManager;

    public KafkaHttpEndpointPipelineServiceImpl(
            @Qualifier("httpAgent") CloseableHttpClient httpAgent,
            PipelineManager pipelineManager) {
        this.httpAgent = httpAgent;
        this.pipelineManager = pipelineManager;
    }

    // @Override
    // public void start(String id) {
    //     return;
    // }

    @Override
    public void startAll() {
        AtomicReference<PipelineInfo> infoRef = pipelineManager.getInfoRef();
        PipelineInfo pipelineInfo = infoRef.get();
        List<KafkaHttpEndpointPipeline> data = pipelineInfo.getData();

        for (KafkaHttpEndpointPipeline pipeline : data) {
            KafkaConfig kafkaConfig = pipeline.getKafkaConfig();
            HttpEndPointConfig httpEndPointConfig = pipeline.getHttpEndPointConfig();
            httpEndPointConfig.setAuthValue(authToken);

            KafkaHttpEndpointBridge bridge = KafkaHttpEndpointBridge.establish(kafkaConfig, httpEndPointConfig);
            try {
                bridge.openChannel(httpAgent);
            }  catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}
