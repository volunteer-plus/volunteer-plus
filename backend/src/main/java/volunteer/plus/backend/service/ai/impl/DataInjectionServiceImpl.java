package volunteer.plus.backend.service.ai.impl;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentReader;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import volunteer.plus.backend.exceptions.ApiException;
import volunteer.plus.backend.exceptions.ErrorCode;
import volunteer.plus.backend.service.ai.DataInjectionService;

import java.util.List;


@Slf4j
@Service
public class DataInjectionServiceImpl implements DataInjectionService {
    public static final String PDF = ".pdf";

    private final boolean allowAIPromptPreUpload;
    private final TextSplitter textSplitter;
    private final VectorStore openAiVectorStore;
    private final ResourcePatternResolver resourcePatternResolver;

    public DataInjectionServiceImpl(final TextSplitter textSplitter,
                                    final @Qualifier("openAiVectorStore") VectorStore openAiVectorStore,
                                    final ResourcePatternResolver resourcePatternResolver,
                                    final @Value("${spring.ai.allow.prompt.pre-upload}") boolean allowAIPromptPreUpload) {
        this.textSplitter = textSplitter;
        this.openAiVectorStore = openAiVectorStore;
        this.resourcePatternResolver = resourcePatternResolver;
        this.allowAIPromptPreUpload = allowAIPromptPreUpload;
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
                injectFile(resource);
            }
        }

        log.info("Finished data injecting process...");
    }

    @SneakyThrows
    @Override
    public void injectData(final MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.getOriginalFilename() == null) {
            throw new ApiException(ErrorCode.EMPTY_FILE);
        }
        injectFile(multipartFile.getResource());
    }

    private void injectFile(final Resource resource) {
        if (resource == null || resource.getFilename() == null) {
            throw new ApiException(ErrorCode.EMPTY_FILE);
        }

        log.info("Injecting a file: {} to vector store", resource.getFilename());

        final DocumentReader documentReader;
        if (resource.getFilename().contains(PDF)) {
            documentReader = new PagePdfDocumentReader(resource);
        } else {
            documentReader = new TikaDocumentReader(resource);
        }

        final List<Document> documents = textSplitter.apply(documentReader.get());

        openAiVectorStore.write(documents);

        log.info("Finished injecting a file: {} to vector store", resource.getFilename());
    }
}
