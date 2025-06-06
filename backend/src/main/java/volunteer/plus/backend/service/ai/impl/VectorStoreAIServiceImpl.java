package volunteer.plus.backend.service.ai.impl;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentReader;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.redis.RedisVectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import volunteer.plus.backend.domain.dto.stats.WarStatsRangeResponseDTO;
import volunteer.plus.backend.domain.enums.AIProvider;
import volunteer.plus.backend.exceptions.ApiException;
import volunteer.plus.backend.exceptions.ErrorCode;
import volunteer.plus.backend.service.ai.VectorStoreAIService;
import volunteer.plus.backend.service.military.WarStatsService;
import volunteer.plus.backend.util.JacksonUtil;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
@ConditionalOnProperty(value = "spring.redis.disabled", havingValue = "false")
public class VectorStoreAIServiceImpl implements VectorStoreAIService {
    public static final String PDF = ".pdf";
    public static final String DEFAULT_QUERY_PATTERN = "List all data you have";

    private final boolean allowAIPromptPreUpload;
    private final TextSplitter textSplitter;
    private final Map<AIProvider, RedisVectorStore> redisVectorStoreMap;
    private final ResourcePatternResolver resourcePatternResolver;
    private final WarStatsService warStatsService;

    public VectorStoreAIServiceImpl(final TextSplitter textSplitter,
                                    final ResourcePatternResolver resourcePatternResolver,
                                    final @Value("${spring.ai.allow.prompt.pre-upload}") boolean allowAIPromptPreUpload,
                                    final @Qualifier("redisVectorStoreMap") Map<AIProvider, RedisVectorStore> redisVectorStoreMap,
                                    final WarStatsService warStatsService) {
        this.textSplitter = textSplitter;
        this.resourcePatternResolver = resourcePatternResolver;
        this.allowAIPromptPreUpload = allowAIPromptPreUpload;
        this.redisVectorStoreMap = redisVectorStoreMap;
        this.warStatsService = warStatsService;
    }


    @SneakyThrows
    @EventListener(ApplicationReadyEvent.class)
    public void inject() {
        if (!allowAIPromptPreUpload) {
            return;
        }

        log.info("Starting data injecting process...");

        // Pattern to match all files in the prompts directory
        final Resource[] promptResources = resourcePatternResolver.getResources("classpath:/prompts/*");

        // Loop through all resources and process them
        for (final Resource resource : promptResources) {
            // Check if resource exists and is readable
            if (resource.exists() && resource.isReadable()) {
                Arrays.stream(AIProvider.values()).forEach(provider -> injectFileToVectorStore(provider, resource));
            }
        }

        log.info("Finished data injecting process...");
    }

    @Override
    public List<Document> getData(final AIProvider aiProvider,
                                  final int topK,
                                  final String query) {
        final SearchRequest searchRequest = SearchRequest.builder()
                .topK(topK)
                .query(query != null ? query : DEFAULT_QUERY_PATTERN)
                .build();

        return redisVectorStoreMap.get(aiProvider).similaritySearch(searchRequest);
    }

    @SneakyThrows
    @Override
    public void injectData(final AIProvider aiProvider,
                           final MultipartFile multipartFile) {
        if (multipartFile == null) {
            throw new ApiException(ErrorCode.EMPTY_FILE);
        }
        injectFileToVectorStore(aiProvider, multipartFile.getResource());
    }

    @Override
    public void injectWarStatisticsData(final AIProvider aiProvider,
                                        final LocalDate dateFrom,
                                        final LocalDate dateTo) {
        log.info("Started getting war statistics range from {} to {}", dateFrom, dateTo);
        final WarStatsRangeResponseDTO responseDTO = warStatsService.getWarStatsRange(dateFrom, dateTo);

        final List<Document> documents = responseDTO
                .getData()
                .getRecords()
                .stream()
                .map(data -> new Document(JacksonUtil.serialize(data), Map.of("source", "russianwarship.rip")))
                .toList();

        redisVectorStoreMap.get(aiProvider).write(documents);

        log.info("Finished injecting war statistics to vector store");
    }

    @Override
    public void deleteData(final AIProvider aiProvider,
                           final List<String> ids) {
        log.info("Deleting vectors in storage {} with ids: {}", aiProvider, ids);
        final RedisVectorStore redisStore = redisVectorStoreMap.get(aiProvider);
        redisStore.delete(ids);
    }

    private void injectFileToVectorStore(final AIProvider aiProvider,
                                         final Resource resource) {
        if (resource == null) {
            throw new ApiException(ErrorCode.EMPTY_FILE);
        }

        log.info("Injecting a file: {} to vector store {}", resource.getFilename(), aiProvider);

        final DocumentReader documentReader;
        if (resource.getFilename().contains(PDF)) {
            documentReader = new PagePdfDocumentReader(resource);
        } else {
            documentReader = new TikaDocumentReader(resource);
        }

        final List<Document> documents = textSplitter.apply(documentReader.get());

        redisVectorStoreMap.get(aiProvider).write(documents);

        log.info("Finished injecting a file: {} to vector store", resource.getFilename());
    }
}
