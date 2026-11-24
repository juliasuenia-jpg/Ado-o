package sistema.adocao.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import sistema.adocao.service.ArmazenamentoService;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final ArmazenamentoService armazenamento;

    public WebConfig(ArmazenamentoService armazenamento) {
        this.armazenamento = armazenamento;
    }

    /** Serve as fotos enviadas em /uploads/arquivo.jpg */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(armazenamento.getRaiz().toUri().toString());
    }
}
