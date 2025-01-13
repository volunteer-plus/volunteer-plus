package volunteer.plus.backend.service.ai.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.reader.pdf.ParagraphPdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import volunteer.plus.backend.service.ai.DataInjectionService;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataInjectionServiceImpl implements DataInjectionService {
    private final VectorStore vectorStore;

    @Value("classpath:/prompts/article_thebeatoct2024.pdf")
    private Resource promptPDF;


    @EventListener(ApplicationReadyEvent.class)
    public void inject() {
        log.info("Starting data injecting process...");
        final var pdfReader = new ParagraphPdfDocumentReader(promptPDF);
        final var textSplitter = new TokenTextSplitter();
        vectorStore.accept(textSplitter.apply(pdfReader.get()));
        log.info("Finished data injecting process...");
    }
}
